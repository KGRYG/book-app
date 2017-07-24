package com.bookstore.bookstoreapp.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@PropertySource("file:///${user.home}/Dropbox/MyProjectWithBuddy/application.properties")
public class ApplicationConfig {

    @Value("${access_key_id}")
    private String access_key_id;

    @Value("${secret_access_key}")
    private String secret_access_key;

    @Bean
    public AmazonS3 s3Client() {

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(access_key_id, secret_access_key)))
                .withRegion(Regions.US_WEST_1)
                .build();
        return s3Client;
    }
}
