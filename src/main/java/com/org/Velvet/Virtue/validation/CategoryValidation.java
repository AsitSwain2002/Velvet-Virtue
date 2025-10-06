package com.org.Velvet.Virtue.validation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.org.Velvet.Virtue.Dto.CategoryDto;
import com.org.Velvet.Virtue.ExceptionHandler.CategoryValidationException;
import com.org.Velvet.Virtue.Repo.CategoryRepo;

@Component
public class CategoryValidation {
	@Autowired
	private CategoryRepo categoryRepo;

	public void validateCategory(CategoryDto categoryDto) throws Exception {

		if (ObjectUtils.isEmpty(categoryDto)) {
			throw new CategoryValidationException("Categort Can't be null");
		} else {
			if (categoryDto.getName().length() > 20) {
				throw new CategoryValidationException("Categort Name Too Big");
			}
			if (categoryDto.getName().length() < 3) {
				throw new CategoryValidationException("Categort Name Too Short");
			}
			if (categoryRepo.existsByName(categoryDto.getName())) {
				throw new CategoryValidationException("Category with this name already exists");
			}
		}
	}
}
