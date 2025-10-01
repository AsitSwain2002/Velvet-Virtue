package com.org.Velvet.Virtue.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.Velvet.Virtue.Dto.ProductDeliveryDto;
import com.org.Velvet.Virtue.Util.ResponseBuilder;
import com.org.Velvet.Virtue.service.ProductDeliveryService;

@RestController
@RequestMapping("api/v1/delivery/")
public class ProductDeliveryController {

	@Autowired
	private ProductDeliveryService deliveryService;

	@PostMapping("save-delhivery")
	public ResponseEntity<?> saveProduct(@RequestBody ProductDeliveryDto deliveryDto) {
		boolean saveDelivery = deliveryService.saveDelivery(deliveryDto);
		if (saveDelivery) {
			return ResponseBuilder.withOutData("Saved Successfully", HttpStatus.CREATED);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
