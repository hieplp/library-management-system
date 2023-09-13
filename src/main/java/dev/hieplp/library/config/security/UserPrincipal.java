package dev.hieplp.library.config.security;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Slf4j
@Data
//@EqualsAndHashCode(callSuper = true)
public class UserPrincipal implements UserDetails {

    private String userId;
    private String token;
    private Byte tokenType;
    private Set<Integer> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.debug("Get authorities of user: {}", userId);
        var authorities = new ArrayList<GrantedAuthority>();
        roles.forEach(userRole -> authorities.add(new SimpleGrantedAuthority(userRole.toString())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
