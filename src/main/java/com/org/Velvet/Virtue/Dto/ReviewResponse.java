package com.org.Velvet.Virtue.Dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewResponse {
	private List<ReviewDto> reviewDto;
	private int totalPage;
	private boolean isLastPage;
	private boolean isfirstPage;
	private long totalElement;
	private int pageNumber;
	private int pagesize;
}
