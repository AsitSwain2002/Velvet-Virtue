package com.org.Velvet.Virtue.Model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Products extends SuperClass {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String description;
	private Integer quantity;
	private boolean active;
	private boolean deleted;
	private double price;
	private int discount;
	private double priceAfterDiscount;
	private double rating;
	@ManyToOne
	@JoinColumn
	private Category category;
	@OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
	private List<FileDetails> fileDetails;
	@OneToOne
	private ProductType productType;
	@OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
	private List<LikedProduct> likedProducts;
	@OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
	private List<Review> reviews;

}
