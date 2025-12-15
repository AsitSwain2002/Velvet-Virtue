package com.org.Velvet.Virtue.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Velvet.Virtue.Model.Review;
import com.org.Velvet.Virtue.Model.Users;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Integer> {

	Page<Review> findAllByUserAndDeleted(Users users, boolean b, Pageable of);

	Page<Review> findAllByDeletedFalse(Pageable of);

}
