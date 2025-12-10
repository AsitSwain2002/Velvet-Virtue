package com.org.Velvet.Virtue.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Velvet.Virtue.Model.Products;
import com.org.Velvet.Virtue.Model.Users;
import com.org.Velvet.Virtue.Model.Wishlist;

@Repository
public interface WishlistRepo extends JpaRepository<Wishlist, Integer> {

	Wishlist findByUserIdAndProductsId(int userId, int productId);

	List<Wishlist> findAllByUserIdAndIsDeleted(Integer userId, boolean b);

}
