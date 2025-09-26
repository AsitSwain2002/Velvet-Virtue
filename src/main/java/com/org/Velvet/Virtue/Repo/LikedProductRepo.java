package com.org.Velvet.Virtue.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Velvet.Virtue.Model.LikedProduct;
import com.org.Velvet.Virtue.Model.Products;
import com.org.Velvet.Virtue.Model.Users;

@Repository
public interface LikedProductRepo extends JpaRepository<LikedProduct, Integer> {

	List<LikedProduct> findAllByUsersAndProducts(Users users, Products dbProducts);

	LikedProduct findByUsersAndProducts(Users users, Products dbProducts);

	List<LikedProduct> findAllByUsers(Users users);

}
