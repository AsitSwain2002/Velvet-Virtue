package com.org.Velvet.Virtue.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Velvet.Virtue.Dto.CategoryDto;
import com.org.Velvet.Virtue.ExceptionHandler.ResourceNotFoundException;
import com.org.Velvet.Virtue.Model.Category;
import com.org.Velvet.Virtue.Repo.CategoryRepo;
import com.org.Velvet.Virtue.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public boolean addCategory(CategoryDto categorydto) {
		int userId = 1;
		Category category = mapper.map(categorydto, Category.class);
		if (category.getId() != 0) {
			updateCategory(category);
		}
		category.setCreatedBy(userId);
		category.setCreatedOn(new Date());
		if (!ObjectUtils.isEmpty(categoryRepo.save(category))) {
			return true;
		}

		return false;
	}

	private void updateCategory(Category category) {
		Category dbCategory = categoryRepo.findById(category.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));
		category.setName(category.getName());
		category.setActive(category.isActive());
		category.setUpdateBy(category.getId());
		category.setUpdateOn(new Date());

	}

	@Override
	public void deleteCategory(int category_id) {
		Category dbCategory = categoryRepo.findById(category_id)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));
		dbCategory.setDeleted(true);
		categoryRepo.save(dbCategory);

	}

	@Override
	public boolean activecategory(int category_id) {
		Category dbCategory = categoryRepo.findById(category_id)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));
		dbCategory.setActive(true);
		if (!ObjectUtils.isEmpty(categoryRepo.save(dbCategory))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean removeRecycleBin(int category_id) {
		Category dbCategory = categoryRepo.findById(category_id)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));
		dbCategory.setDeleted(false);
		if (!ObjectUtils.isEmpty(categoryRepo.save(dbCategory))) {
			return true;
		}
		return false;
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> findAll = categoryRepo.findAll();
		return findAll.stream().map(e -> mapper.map(e, CategoryDto.class)).collect(Collectors.toList());
	}

}
