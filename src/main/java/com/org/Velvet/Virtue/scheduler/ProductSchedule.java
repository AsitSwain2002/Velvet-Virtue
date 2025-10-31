package com.org.Velvet.Virtue.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.org.Velvet.Virtue.Model.Products;
import com.org.Velvet.Virtue.Repo.ProductRepo;

@EnableScheduling
public class ProductSchedule {

	@Autowired
	private ProductRepo productRepo;

	public void productScheduling() {

		LocalDateTime minusDays = LocalDateTime.now().minusDays(28);
		List<Products> findAllDeletedAndDeletedOnBefore = productRepo.findAllByDeletedAndDeletedOnBefore(true,minusDays);
		productRepo.deleteAll(findAllDeletedAndDeletedOnBefore);
	}
}
