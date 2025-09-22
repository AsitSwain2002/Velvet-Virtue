package com.org.Velvet.Virtue.Model;

import java.util.Date;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class SuperClass {

	private int createdBy;
	private Date createdOn;
	private int updateBy;
	private Date updateOn;
}
