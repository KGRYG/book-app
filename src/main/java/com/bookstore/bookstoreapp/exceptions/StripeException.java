package com.bookstore.bookstoreapp.exceptions;


public class StripeException extends RuntimeException {

    public StripeException(Throwable e) {
        super(e);
    }

}
