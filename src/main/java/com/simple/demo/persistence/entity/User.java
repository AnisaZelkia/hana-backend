package com.simple.demo.persistence.entity;

import java.util.Collection;
import java.util.Collections;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_users")
@Setter
@Getter
@SQLRestriction("deleted_at <> null")
@NoArgsConstructor
@SQLDelete(sql = "UPDATE t_users SET deleted_at = now() WHERE id=? AND version =?")
public class User extends AuditableEntity implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="fullname", unique = true)
	private String fullname;
	@Column(name="email", unique = true)
	private String email;
	@Column(name="password", unique = true)
	private String password;
	@Enumerated(EnumType.STRING)
	private UserRole userRole;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole.name());
		return Collections.singletonList(authority);
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}

}
