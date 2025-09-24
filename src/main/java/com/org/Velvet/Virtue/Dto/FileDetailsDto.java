package com.org.Velvet.Virtue.Dto;

import lombok.Data;

@Data
public class FileDetailsDto {
	private Integer id;
	private String uploadFileName;
	private String originalFileName;
	private String displayFileName;
	private String path;
	private Long size;
}
