package com.org.Velvet.Virtue.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.Velvet.Virtue.Dto.CategoryDto;
import com.org.Velvet.Virtue.Util.ResponseBuilder;
import com.org.Velvet.Virtue.service.CategoryService;

@RestController
@RequestMapping("api/v1/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping("/save-category")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) throws Exception {
		boolean addCategory = categoryService.addCategory(categoryDto);
		if (addCategory) {
			return ResponseBuilder.withOutData("Saved Successfully", HttpStatus.CREATED);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/active-category/{id}")
	public ResponseEntity<?> activeCategory(@PathVariable int id) {
		boolean addCategory = categoryService.activecategory(id);
		if (addCategory) {
			return ResponseBuilder.withOutData("Active Successfully", HttpStatus.CREATED);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/remove-recycle/{id}")
	public ResponseEntity<?> removeRecyclebin(@PathVariable int id) {
		boolean addCategory = categoryService.removeRecycleBin(id);
		if (addCategory) {
			return ResponseBuilder.withOutData("remove from recyclebin Successfully", HttpStatus.CREATED);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("all-category")
	public ResponseEntity<?> allCategory() {
		List<CategoryDto> allCategory = categoryService.getAllCategory();
		if (!CollectionUtils.isEmpty(allCategory)) {
			return ResponseBuilder.withData("fetched", allCategory, HttpStatus.CREATED);
		} else {
			return ResponseBuilder.withOutData("No category Found", HttpStatus.NOT_FOUND);
		}
	}
}
