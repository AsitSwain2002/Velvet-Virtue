package com.org.Velvet.Virtue.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Velvet.Virtue.Model.Products;

@Repository
public interface ProductRepo extends JpaRepository<Products, Integer> {

}
