package com.org.Velvet.Virtue.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Velvet.Virtue.Model.Users;

@Repository
public interface UsersRepo extends JpaRepository<Users, Integer> {

	boolean existsByEmail(String email);

	Users findByEmail(String username);

}
