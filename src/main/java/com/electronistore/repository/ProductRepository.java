package com.electronistore.repository;

import com.electronistore.entity.Category;
import com.electronistore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {

//    Custom Finder Method JPA Data

    Page<Product> findByProductTitleContaining(String subTitle, Pageable pageable);    //-> Search by Product name

    Page<Product> findByIsLiveTrue(Pageable pageable);    //=> Search by Live Product

    Page<Product> findByDescriptionContaining(String keywords, Pageable pageable);  //-> Search anything about product

    Page<Product> findByCategory(Category category, Pageable pageable);  //-> Search product by category

//    todo Create more method like user can search by product name and price under 1000Rs
//    Page<Product> findByDescriptionContainingAndPriceLessThanEqaul(String keyword, int price);

//    todo make method search by category

//    todo search by price


}
