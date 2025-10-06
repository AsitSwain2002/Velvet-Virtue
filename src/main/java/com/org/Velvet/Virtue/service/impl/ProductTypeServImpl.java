package com.org.Velvet.Virtue.service.impl;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Velvet.Virtue.Dto.ProductTypeDto;
import com.org.Velvet.Virtue.ExceptionHandler.ProductTypeValidationException;
import com.org.Velvet.Virtue.ExceptionHandler.ResourceNotFoundException;
import com.org.Velvet.Virtue.Model.ProductType;
import com.org.Velvet.Virtue.Repo.ProductTypeRepo;
import com.org.Velvet.Virtue.service.ProductTypeService;

@Service
public class ProductTypeServImpl implements ProductTypeService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ProductTypeRepo productTypeRepo;

	@Override
	public boolean saveType(ProductTypeDto productTypeDto) {
		
		String name = productTypeDto.getType();
		if(productTypeRepo.existsByType(name)) {
			throw new ProductTypeValidationException("Product Type Already Present");
		}
		int userId = 1;
		ProductType type = mapper.map(productTypeDto, ProductType.class);
		if (type.getId() != 0) {
			updateType(type);
		}
		type.setCreatedBy(userId);
		type.setCreatedOn(new Date());
		ProductType save = productTypeRepo.save(type);
		if (!ObjectUtils.isEmpty(save)) {
			return true;
		}
		return false;
	}

	private void updateType(ProductType type) {
		int userId = 1;
		ProductType dbtype = productTypeRepo.findById(type.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Type Not Found"));
		dbtype.setUpdateBy(userId);
		dbtype.setUpdateOn(new Date());
	}

}
