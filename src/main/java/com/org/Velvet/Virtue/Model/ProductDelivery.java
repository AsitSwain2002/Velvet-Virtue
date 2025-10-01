package com.org.Velvet.Virtue.Model;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDelivery {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String orderId;
	private boolean deleted;
	private double price;
	private int quantity;
	private LocalDate orderDate;
	@ManyToOne
	private Products products;
	@ManyToOne 
	private OrderStatus orderStatus;
	@ManyToOne
	private Users users;
	@ManyToOne(cascade = CascadeType.ALL)
	private Address address;
}
