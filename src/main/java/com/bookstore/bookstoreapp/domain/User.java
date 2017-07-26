package com.bookstore.bookstoreapp.domain;

import com.bookstore.bookstoreapp.domain.security.Authority;
import com.bookstore.bookstoreapp.domain.security.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by karen on 7/10/17.
 */
@Entity
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(length = 50)
    private String phone;

    private boolean enabled = true;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @OneToMany(cascade={CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE, CascadeType.REFRESH}, mappedBy = "user", orphanRemoval = true)
    private List<UserPayment> userPaymentList;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<UserShipping> userShippingList;

    @OneToOne(cascade=CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy="user", orphanRemoval = true)
    private List<Order> orderList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new Authority(role.toString()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public List<UserPayment> getUserPaymentList() {
        return userPaymentList;
    }

    public void setUserPaymentList(List<UserPayment> userPaymentList) {
        this.userPaymentList = userPaymentList;
    }

    public List<UserShipping> getUserShippingList() {
        return userShippingList;
    }

    public void setUserShippingList(List<UserShipping> userShippingList) {
        this.userShippingList = userShippingList;
    }

    public void addUserShipping(UserShipping userShipping) {
        userShippingList.add(userShipping);
        userShipping.setUser(this);
    }

    public void removeUserShipping(UserShipping userShipping) {
        userShippingList.remove(userShipping);
        userShipping.setUser(null);
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        shoppingCart.setUser(this);
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
