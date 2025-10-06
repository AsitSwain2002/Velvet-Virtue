package com.org.Velvet.Virtue.validation;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.org.Velvet.Virtue.Dto.ProductsDto;
import com.org.Velvet.Virtue.ExceptionHandler.ProductValidationException;

@Component
public class ProductValidation {

	public void validate(ProductsDto productDto) {
		Map<String, String> errors = new LinkedHashMap<String, String>();

		if (ObjectUtils.isEmpty(productDto)) {
			errors.put("Request Error", "Product can't be null");
		}

		String pName = productDto.getName();
		String description = productDto.getDescription();
		Integer discount = productDto.getDiscount();
		Integer quantity = productDto.getQuantity();

		// ---------------- Name Validation ----------------
		if (pName == null || pName.trim().isEmpty()) {
			errors.put("name", "Product name can't be null or empty");
		} else {
			if (pName.length() < 3) {
				errors.put("name", "Product name is too short (min 3 chars required)");
			}
			if (pName.length() > 200) {
				errors.put("name", "Product name is too long (max 200 chars allowed)");
			}
		}

		// ---------------- Description Validation ----------------
		if (description == null || description.trim().isEmpty()) {
			errors.put("description", "Description can't be null or empty");
		} else {
			if (description.length() < 10) {
				errors.put("description", "Description is too short (min 10 chars required)");
			}
			if (description.length() > 300) {
				errors.put("description", "Description is too long (max 300 chars allowed)");
			}
		}

		// ---------------- Discount Validation ----------------

		if (discount < 0) {
			errors.put("discount", "Discount can't be negative");
		}
		if (discount > 100) {
			errors.put("discount", "Discount can't be more than 100%");
		}

		// ---------------- Quantity Validation ----------------
		if (quantity == null) {
			errors.put("quantity", "Quantity can't be null");
		} else {
			if (quantity < 0) {
				errors.put("quantity", "Quantity can't be negative");
			}
			if (quantity > 10000) {
				errors.put("quantity", "Quantity exceeds maximum limit (10000)");
			}
		}

		// ---------------- Throw if Errors Exist ----------------
		if (!errors.isEmpty()) {
			throw new ProductValidationException(errors);
		}
	}
}
