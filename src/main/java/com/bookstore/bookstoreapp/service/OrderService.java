package com.bookstore.bookstoreapp.service;


import com.bookstore.bookstoreapp.domain.*;

public interface OrderService {
	
	Order createOrder(
            ShoppingCart shoppingCart,
            ShippingAddress shippingAddress,
            BillingAddress billingAddress,
            Payment payment,
            String shippingMethod,
            User user
    );

}
