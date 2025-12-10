package com.org.Velvet.Virtue.service;

import java.util.List;

import com.org.Velvet.Virtue.Dto.ProductsDto;

public interface WishlistService {

	public boolean addToWishList(int productId);

	public void removeWishList(int productId);

	public List<ProductsDto> allWishlistproduct();
}
