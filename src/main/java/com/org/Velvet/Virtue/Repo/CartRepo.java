package com.org.Velvet.Virtue.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Velvet.Virtue.Model.Cart;
import com.org.Velvet.Virtue.Model.Users;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {

	List<Cart> findAllByUser(Users user);

}
