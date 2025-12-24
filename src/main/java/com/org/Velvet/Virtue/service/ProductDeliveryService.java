package com.org.Velvet.Virtue.service;

import java.util.List;

import com.org.Velvet.Virtue.Dto.AddressDto;
import com.org.Velvet.Virtue.Dto.ProductDeliveryDto;

public interface ProductDeliveryService {

	boolean saveDelivery(ProductDeliveryDto deliveryDto);

	boolean updateStatus(int deliveryId, int statusId);

	void cancelOrder(int deliveryId);

	ProductDeliveryDto trackDelivery(String deliveryId);

	List<ProductDeliveryDto> getDeliveryHistory(int userId);

	boolean updateDeliveryAddress(int deliveryId, AddressDto newAddress);
}
