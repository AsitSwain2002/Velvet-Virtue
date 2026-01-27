package com.org.Velvet.Virtue.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Velvet.Virtue.Model.ProductType;
import com.org.Velvet.Virtue.Model.Products;

@Repository
public interface ProductTypeRepo extends JpaRepository<ProductType, Integer> {

	boolean existsByType(String name);

	ProductType findByType(String type);

}
