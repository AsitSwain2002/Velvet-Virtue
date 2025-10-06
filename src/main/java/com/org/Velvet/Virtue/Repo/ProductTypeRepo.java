package com.org.Velvet.Virtue.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Velvet.Virtue.Model.ProductType;

@Repository
public interface ProductTypeRepo extends JpaRepository<ProductType, Integer> {

	boolean existsByType(String name);

}
