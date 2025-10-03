package com.org.Velvet.Virtue.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Velvet.Virtue.Model.Review;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Integer> {

}
