package com.simple.demo.persistence.entity;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EntityListeners({ AuditingEntityListener.class })
@Getter
@Setter
public abstract class AuditableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private String id;

	@Column(name = "created_at", nullable = false, updatable = false)
	@CreatedDate
	private ZonedDateTime createdAt;

	@Column(name = "created_by", nullable = false, updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_at", nullable = false)
	@LastModifiedDate
	private ZonedDateTime updatedAt;

	@Column(name = "updated_by", nullable = false)
	@LastModifiedBy
	private String updatedBy;

	@Column(name = "version")
	@Version
	private Long version;

	@Column(name = "deleted_at", nullable = true)
	private ZonedDateTime deletedAt;

}
