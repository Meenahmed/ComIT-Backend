package com.meenah.meenahsignature.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meenah.meenahsignature.auth.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserPrincipal implements UserDetails {

    private Collection<? extends GrantedAuthority> grantedAuthorities;
    private String name;

    private String username;



    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    public UserPrincipal(Collection<? extends GrantedAuthority> grantedAuthorities,
                         String name,
                         String username,
                         String email,
                         String password) {
        this.grantedAuthorities = grantedAuthorities;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static UserPrincipal create(User user){

        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new UserPrincipal(authorities,
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword());

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
