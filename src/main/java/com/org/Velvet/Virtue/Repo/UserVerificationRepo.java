package com.org.Velvet.Virtue.Repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Velvet.Virtue.Model.UserVerification;

@Repository
public interface UserVerificationRepo extends JpaRepository<UserVerification, Integer> {

	List<UserVerification> findAllByIsActiveAndCreatedOnBefore(boolean b, LocalDateTime minusDays);

}
