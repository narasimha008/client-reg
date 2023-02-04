package com.syncronys.registration.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Getter
@Setter
public class AuthUser extends User {

    private String firstName;
    private String lastName;

    public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}
