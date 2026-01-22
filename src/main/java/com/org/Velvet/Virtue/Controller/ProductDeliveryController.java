package com.org.Velvet.Virtue.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.org.Velvet.Virtue.Util.CommonUtil;
import com.org.Velvet.Virtue.Util.ResponseBuilder;
import com.org.Velvet.Virtue.service.ProductDeliveryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/delivery/")
@Tag(name = "Product delhivery", description = "all operation for delhivery th eproduct")
public class ProductDeliveryController {

	@Autowired
	private ProductDeliveryService deliveryService;

	@Operation(summary = "add to delhivery - access by user", tags = { "Product delhivery" })
	@PreAuthorize("hasRole('USER')")
	@PostMapping("save-delhivery")
	public ResponseEntity<?> saveDelhivery(@RequestBody ProductDeliveryDto deliveryDto) {
		boolean saveDelivery = deliveryService.saveDelivery(deliveryDto);
		if (saveDelivery) {
			return ResponseBuilder.withOutData("Saved Successfully", HttpStatus.CREATED);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "update delhivery status - Acess by user", tags = { "Product delhivery" })
	@PreAuthorize("hasRole('USER')")
	@PostMapping("update-delhivery-status/{deliveryId}/{statusId}")
	public ResponseEntity<?> updateStatus(@PathVariable int deliveryId, @PathVariable int statusId) {
		boolean updateStatus = deliveryService.updateStatus(deliveryId, statusId);
		if (updateStatus) {
			return ResponseBuilder.withOutData("Status Update Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "cancel the order - access by user,admin,seller", tags = { "Product delhivery" })
	@PreAuthorize("hasAnyRole('USER','ADMIN','SELLER')")
	@PostMapping("cancel-order/{deliveryId}")
	public ResponseEntity<?> cancelOrder(@PathVariable int deliveryId) {
		deliveryService.cancelOrder(deliveryId);
		return ResponseBuilder.withOutData("Order Cancelled Successfully", HttpStatus.OK);
	}

	@Operation(summary = "track the order - Access by User,Admin", tags = { "Product delhivery" })
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@GetMapping("track-order/{deliveryId}")
	public ResponseEntity<?> trackOrder(@PathVariable String deliveryId) {
		ProductDeliveryDto trackDelivery = deliveryService.trackDelivery(deliveryId);
		if (!ObjectUtils.isEmpty(trackDelivery)) {
			return ResponseBuilder.withData("fetched", trackDelivery, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("No Order found", HttpStatus.OK);
		}
	}

	@Operation(summary = "See all deliverd order - Access by User,Admin", tags = { "Product delhivery" })
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@GetMapping("delivery-history")
	public ResponseEntity<?> deliveryHistory() {
		// change later
		int userId = CommonUtil.getLoggedUser().getId();
		List<ProductDeliveryDto> deliveryHistory = deliveryService.getDeliveryHistory(userId);
		if (!CollectionUtils.isEmpty(deliveryHistory)) {
			return ResponseBuilder.withData("fetched", deliveryHistory, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Nothing Found", HttpStatus.OK);
		}
	}

	@Operation(summary = "update address - Access by User", tags = { "Product delhivery" })
	@PreAuthorize("hasRole('USER')")
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
