package com.idcard.Model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.idcard.config.RefreshTokenEntity;
import com.idcard.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="tbl_user_details")
@Data
public class UserEntity implements UserDetails{
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    @Column(unique = true)
	private String email;
	private String password;
	@Column(unique = true)
	private String mobile;
	@Enumerated(EnumType.STRING)
    private Role role;
	
	@Column(name = "createuser", length = 50)
    private String createUser;
    private Date createDate;
    @Column(name = "changeuser", length = 50)
    private String changeUser;
    private Date changeDate;
    @Column(name = "ipaddress", length = 100)
    private String ipaddress;
    @OneToOne(mappedBy = "user")
    private RefreshTokenEntity refreshEntity;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}
	@Override
	public String getUsername() {
		return email;
	}
	@Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}	
