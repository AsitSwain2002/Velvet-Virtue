package com.org.Velvet.Virtue.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.org.Velvet.Virtue.Dto.CategoryDto;
import com.org.Velvet.Virtue.Repo.CategoryRepo;
import com.org.Velvet.Virtue.service.CategoryService;

public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public boolean addCategory(CategoryDto categorydto) {

		
		return false;
	}

	@Override
	public void deleteCategory(int id) {
		// TODO Auto-generated method stub

	}

}
