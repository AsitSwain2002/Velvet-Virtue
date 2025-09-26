package com.org.Velvet.Virtue.Model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String firstName;
	private String lastName;
	private String mobile;
	private String email;
	private String password;
	private String age;
	private boolean isDeleted;
	@OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
	private List<Address> address;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Roles> roles;
	@OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LikedProduct> likedProducts;

}
