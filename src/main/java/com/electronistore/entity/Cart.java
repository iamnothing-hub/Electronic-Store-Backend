package com.electronistore.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table
public class Cart {

    @Id
    private String cartId;

    private Date createdAt;

    @OneToOne
    @JoinColumn(name="user_Id")
    private User user;

    // Mapping with Cart-Item
    @OneToMany(mappedBy = "cart" ,cascade = CascadeType.ALL /*  fetch = FetchType.EAGER*/)
    @JsonManagedReference  // Prevent circular reference while serializing
    private List<CartItem> items = new ArrayList<>();



}
