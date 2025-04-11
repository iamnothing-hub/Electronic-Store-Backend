package com.electronistore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.engine.internal.Cascade;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "id")
    private String categoryId;

    @Column(name = "category_title", length = 50, nullable = false)
    private String title;

    @Column(name = "category_desc")
    private String description;

    private String coverImage;

    // Mapping category to product
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="category")
    @JsonIgnore  // Prevent infinite recursion
    private List<Product> products = new ArrayList<>();

    /*
    Cascade => cascade means flow the data like waterfall(jharana). And CascadeType All means data will do ({PERSIST, MERGE, REMOVE, REFRESH, DETACH})
    Fetch => fetch means data will fetch according to condition. Here condition is Lazy means data will fetch when required.
    MappedBy => It will create a table which shows relation between category and product.
     */

    /*
    todo: Jab Ham product ko create karenge to iske liye API URI /localhost:3306/products se start hota hai.
          Category ko create karte hain to iske liye API URI /localhost:3306/categories se start hota hai.
          Jab Ham product ko add karenge aur category ko sath mein add karenge to iske liye upar wala URI failed ho jayegi.
          Iske liye ham alag se URI banayenge Like:
                                        localhost:3306/categories/{categoryId}/products/
                                        localhost:3306/categories/{categoryId}/products/{productId}

     */

}
