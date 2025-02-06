package com.electronistore.repository;

import com.electronistore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {

//    Search Filter
    ArrayList<Category> findBytitleContaining(String keyword);

}
