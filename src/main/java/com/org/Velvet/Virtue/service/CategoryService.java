package com.org.Velvet.Virtue.service;

import java.util.List;

import com.org.Velvet.Virtue.Dto.CategoryDto;

public interface CategoryService {

	boolean addCategory(CategoryDto categorydto) throws Exception;

	void deleteCategory(int id);

	boolean activecategory(int category_id);

	boolean removeRecycleBin(int category_id);

	List<CategoryDto> getAllCategory();

	CategoryDto findById(int id);
}
