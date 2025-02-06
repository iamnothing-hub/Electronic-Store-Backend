package com.electronistore.dto;


import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {

    private String orderId;

    private String orderStatus = "PENDING";

    private String paymentStatus = "NOTPAID";

    private int orderAmount;

    private String billingPhone;

    private  String billingAddress;

    private  String billingName;

    private Date orderDate = new Date();

    private Date deliveredDate;

//    private User user;

    private List<OrderItemDto> orderItemList = new ArrayList<>();


    // We can also use Payment Method like UPI, Credit card, Debit Card etc
}
