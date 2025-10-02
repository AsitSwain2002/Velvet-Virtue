package com.org.Velvet.Virtue.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Velvet.Virtue.Model.OrderStatus;
import com.org.Velvet.Virtue.Model.ProductDelivery;
import com.org.Velvet.Virtue.Model.Users;

@Repository
public interface ProductDeliveryRepo extends JpaRepository<ProductDelivery, Integer> {

	List<ProductDelivery> findAllByUsersAndOrderStatus(Users map, OrderStatus staus);

}
