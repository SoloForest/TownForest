package com.ll.townforest.boundedContext.account.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String fullName;
	@Column(unique = true)
	private String email;
	@Column(unique = true)
	private String phoneNumber;

	public List<? extends GrantedAuthority> getGrantedAuthorities() {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		return grantedAuthorities;
	}

	public String getPhoneNumString() {
		if (this.phoneNumber.length() == 11) {
			return this.phoneNumber.substring(0, 3) + "-" + this.phoneNumber.substring(3, 7) + "-"
				+ this.phoneNumber.substring(7);
		} else if (this.phoneNumber.length() == 10) {
			return this.phoneNumber.substring(0, 3) + "-" + this.phoneNumber.substring(3, 6) + "-"
				+ this.phoneNumber.substring(6);
		} else {
			return this.phoneNumber;
		}
	}
}
