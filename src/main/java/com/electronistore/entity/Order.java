package com.electronistore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    //PENDING, ORDERED, DELIVERED, DISPATCHED
    private String orderStatus;

    //PAID, NOT-PAID
    private String paymentStatus;

    private int orderAmount;

    private String billingPhone;

    private  String billingAddress;

    private  String billingName;

    private Date orderDate;

    private Date deliveredDate;


    //User
    @ManyToOne(fetch = FetchType.EAGER)  // Many order can be done by 1 user  <=> 1 Order can be done by Many user
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("orders")
    private  User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderItem> orderItemList = new ArrayList<>();


    // We can also use Payment Method like UPI, Credit card, Debit Card etc

}
