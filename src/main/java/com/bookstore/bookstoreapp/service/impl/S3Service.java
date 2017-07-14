package com.bookstore.bookstoreapp.service.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.*;
import com.bookstore.bookstoreapp.domain.Book;
import com.bookstore.bookstoreapp.exceptions.S3Exception;
import com.bookstore.bookstoreapp.service.BookService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class S3Service {

    private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);

    private static final String BOOK_PICTURE_FILE_NAME = "bookPicture";
    private static final String BOOK_FOLDER_NAME = "books_images";

    @Value("${aws.s3.root.bucket.name}")
    private String bucketName;

    @Value("${image.store.tmp.folder}")
    private String tempImageStore;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private BookService bookService;

    public void deleteImageFromS3(String bookId) {
        Book book = bookService.findOne(Long.valueOf(bookId));

        if (book != null && !book.getBookImageUrl().isEmpty()) {
            AmazonS3URI amazonS3URI = new AmazonS3URI(book.getBookImageUrl());
            s3Client.deleteObject(amazonS3URI.getBucket(), amazonS3URI.getKey());
        }
    }

    public String storeBookImage(MultipartFile uploadedFile, String bookId) {

        String bookImageUrl = null;

        try {

            if (uploadedFile != null && !uploadedFile.isEmpty()) {

                byte[] bytes = uploadedFile.getBytes();

                File tmpImageStoredFolder = new File(tempImageStore + File.separatorChar + bookId);

                if (!tmpImageStoredFolder.exists()) {
                    LOG.info("Creating the temporary root for the S3 assets");
                    tmpImageStoredFolder.mkdirs();
                }

                File tmpProfileImageFile = new File(tmpImageStoredFolder.getAbsolutePath()
                        + File.separatorChar
                        + BOOK_PICTURE_FILE_NAME
                        + "."
                        + FilenameUtils.getExtension(uploadedFile.getOriginalFilename()));

                LOG.info("Temporary file will be saved to {}", tmpProfileImageFile.getAbsolutePath());

                try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(tmpProfileImageFile.getAbsolutePath())))) {
                    stream.write(bytes);
                }

                bookImageUrl = storeBookImageToS3(tmpProfileImageFile, bookId);

                tmpProfileImageFile.delete();
            }

        } catch (IOException e) {
            throw new S3Exception(e);
        }

        return bookImageUrl;
    }

    private String storeBookImageToS3(File resource, String bookId) {

        String resourceUrl = null;

        if (!resource.exists()) {
            LOG.error("The file {} does not exist. Throwing an exception", resource.getAbsolutePath());
            throw new S3Exception("The file " + resource.getAbsolutePath() + " doesn't exist");
        }

        String rootBucketUrl = ensureBucketExists(bucketName);

        if (null == rootBucketUrl) {

            LOG.error("The bucket {} does not exist and the application " +
                    "was not able to create it. The image won't be stored with the profile", rootBucketUrl);

        } else {

            AccessControlList acl = new AccessControlList();
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

            String key = BOOK_FOLDER_NAME + "/BookId_" + bookId + "/" + BOOK_PICTURE_FILE_NAME + "." + FilenameUtils.getExtension(resource.getName());

            try {
                s3Client.putObject(new PutObjectRequest(bucketName, key, resource).withAccessControlList(acl));
                resourceUrl = s3Client.getUrl(bucketName, key).toString();
            } catch (AmazonClientException e) {
                LOG.error("A client exception occurred while trying to store the profile" +
                        " image {} on S3. The profile image won't be stored", resource.getAbsolutePath(), e);
                throw new S3Exception(e);
            }
        }

        return resourceUrl;
    }

    private String ensureBucketExists(String bucketName) {
        String bucketUrl = null;
        try {
            if (!s3Client.doesBucketExist(bucketName)) s3Client.createBucket(bucketName);
            bucketUrl = s3Client.getUrl(bucketName, null) + bucketName;
        } catch (AmazonClientException ace) {
            LOG.error("An error occurred while connecting to S3. Will not execute action for bucket: {}", bucketName, ace);
            throw new S3Exception(ace);
        }
        return bucketUrl;
    }
}
