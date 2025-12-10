package com.org.Velvet.Virtue.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Velvet.Virtue.Dto.ProductsDto;
import com.org.Velvet.Virtue.ExceptionHandler.ResourceNotFoundException;
import com.org.Velvet.Virtue.Model.Products;
import com.org.Velvet.Virtue.Model.Users;
import com.org.Velvet.Virtue.Model.Wishlist;
import com.org.Velvet.Virtue.Repo.ProductRepo;
import com.org.Velvet.Virtue.Repo.UsersRepo;
import com.org.Velvet.Virtue.Repo.WishlistRepo;
import com.org.Velvet.Virtue.Util.CommonUtil;
import com.org.Velvet.Virtue.service.WishlistService;

@Service
public class WishlistServiceImpl implements WishlistService {

	@Autowired
	private WishlistRepo wishlistRepo;
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private UsersRepo usersRepo;

	@Override
	public boolean addToWishList(int productId) {
		Products product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		Integer userId = CommonUtil.getLoggedUser().getId();
		Users loggedUser = usersRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user not found"));
		Wishlist wishlist = Wishlist.builder().user(loggedUser).products(product).build();
		Wishlist save = wishlistRepo.save(wishlist);
		if (!ObjectUtils.isEmpty(save)) {
			return true;
		}
		return false;
	}

	@Override
	public void removeWishList(int productId) {
		Products product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		Users loggedUser = CommonUtil.getLoggedUser();
		Wishlist wishlist = wishlistRepo.findByUserIdAndProductsId(loggedUser.getId(), product.getId());
		if (ObjectUtils.isEmpty(wishlist)) {
			throw new ResourceNotFoundException("Product not found");
		}
		wishlist.setDeleted(true);
		Wishlist save = wishlistRepo.save(wishlist);
	}

	@Override
	public List<ProductsDto> allWishlistproduct() {
		Integer userId = CommonUtil.getLoggedUser().getId();
		List<Wishlist> allwishList = wishlistRepo.findAllByUserIdAndIsDeleted(userId,false);
		List<Products> list = allwishList.stream().map(e -> e.getProducts()).toList();
		return list.stream().map(e -> mapper.map(e, ProductsDto.class)).collect(Collectors.toList());
	}

}
