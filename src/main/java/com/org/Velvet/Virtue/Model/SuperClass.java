package com.org.Velvet.Virtue.Model;

import java.util.Date;
import org.apache.el.parser.AstFalse;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class SuperClass {

	@CreatedBy
	@Column(updatable = false)
	private int createdBy;
	@CreatedDate
	@Column(updatable = false)
	private Date createdOn;
	@LastModifiedBy
	@Column(insertable = false)
	private int updateBy;
	@LastModifiedDate
	@Column(insertable = false)
	private Date updateOn;
}
