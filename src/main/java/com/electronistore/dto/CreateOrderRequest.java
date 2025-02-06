package com.electronistore.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

    private String userId;

    private String cartId;

    private String orderStatus = "PENDING";

    private String paymentStatus = "NOTPAID";

    private String billingPhone;

    private  String billingAddress;

    private  String billingName;
}
