package com.org.Velvet.Virtue.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.org.Velvet.Virtue.Util.ResponseBuilder;
import com.org.Velvet.Virtue.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductsController {

	@Autowired
	private ProductService productService;

	@PostMapping(value = "/save-product", consumes = { "multipart/form-data" })
	public ResponseEntity<?> saveProduct(@RequestParam String productsDto, @RequestParam List<MultipartFile> files)
			throws IOException {
		boolean product = productService.saveProduct(productsDto, files);
		if (product) {
			return ResponseBuilder.withOutData("Saved Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
