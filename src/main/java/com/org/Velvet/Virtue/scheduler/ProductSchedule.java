package com.org.Velvet.Virtue.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.org.Velvet.Virtue.Model.Products;
import com.org.Velvet.Virtue.Repo.ProductRepo;

@EnableScheduling
public class ProductSchedule {

	@Autowired
	private ProductRepo productRepo;

	@Scheduled(cron = "0 0 0 * * SAT,SUN")
	public void productScheduling() {

		LocalDateTime minusDays = LocalDateTime.now().minusDays(28);
		List<Products> findAllDeletedAndDeletedOnBefore = productRepo.findAllByDeletedAndDeletedOnBefore(true,
				minusDays);
		productRepo.deleteAll(findAllDeletedAndDeletedOnBefore);
	}
}
