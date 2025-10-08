package com.org.Velvet.Virtue.service;

import com.org.Velvet.Virtue.Dto.RequestDto;
import com.org.Velvet.Virtue.Dto.ResponseDto;

public interface AuthService {
	ResponseDto login(RequestDto requestDto);
}
