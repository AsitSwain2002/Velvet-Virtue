package com.org.Velvet.Virtue.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Velvet.Virtue.Model.Products;

@Repository
public interface ProductRepo extends JpaRepository<Products, Integer> {

	List<Products> findAllByDeletedFalse();

}
