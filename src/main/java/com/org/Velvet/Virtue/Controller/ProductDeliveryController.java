package com.org.Velvet.Virtue.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.Velvet.Virtue.Dto.AddressDto;
import com.org.Velvet.Virtue.Dto.ProductDeliveryDto;
import com.org.Velvet.Virtue.Util.ResponseBuilder;
import com.org.Velvet.Virtue.service.ProductDeliveryService;

@RestController
@RequestMapping("api/v1/delivery/")
public class ProductDeliveryController {

	@Autowired
	private ProductDeliveryService deliveryService;

	@PostMapping("save-delhivery")
	public ResponseEntity<?> saveDelhivery(@RequestBody ProductDeliveryDto deliveryDto) {
		boolean saveDelivery = deliveryService.saveDelivery(deliveryDto);
		if (saveDelivery) {
			return ResponseBuilder.withOutData("Saved Successfully", HttpStatus.CREATED);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("update-delhivery-status/{deliveryId}/{statusId}")
	public ResponseEntity<?> updateStatus(@PathVariable int deliveryId, @PathVariable int statusId) {
		boolean updateStatus = deliveryService.updateStatus(deliveryId, statusId);
		if (updateStatus) {
			return ResponseBuilder.withOutData("Status Update Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("cancel-order/{deliveryId}")
	public ResponseEntity<?> cancelOrder(@PathVariable int deliveryId) {
		deliveryService.cancelOrder(deliveryId);
		return ResponseBuilder.withOutData("Order Cancelled Successfully", HttpStatus.OK);
	}

	@GetMapping("track-order/{deliveryId}")
	public ResponseEntity<?> trackOrder(@PathVariable int deliveryId) {
		ProductDeliveryDto trackDelivery = deliveryService.trackDelivery(deliveryId);
		if (!ObjectUtils.isEmpty(trackDelivery)) {
			return ResponseBuilder.withData("fetched", trackDelivery, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("delivery-history")
	public ResponseEntity<?> deliveryHistory() {
		// change later
		int userId = 1;
		List<ProductDeliveryDto> deliveryHistory = deliveryService.getDeliveryHistory(userId);
		if (!CollectionUtils.isEmpty(deliveryHistory)) {
			return ResponseBuilder.withData("fetched", deliveryHistory, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Nothing Found", HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("update-address/{deliveryId}")
	public ResponseEntity<?> updateAddress(@PathVariable int deliveryId, @RequestBody AddressDto addressDto) {
		boolean updateDeliveryAddress = deliveryService.updateDeliveryAddress(deliveryId, addressDto);
		if (updateDeliveryAddress) {
			return ResponseBuilder.withOutData("update Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
