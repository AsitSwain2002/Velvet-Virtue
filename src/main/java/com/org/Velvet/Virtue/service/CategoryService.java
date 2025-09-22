package com.org.Velvet.Virtue.service;

import com.org.Velvet.Virtue.Dto.CategoryDto;

public interface CategoryService {

	boolean addCategory(CategoryDto categorydto);

	void deleteCategory(int id);

}
