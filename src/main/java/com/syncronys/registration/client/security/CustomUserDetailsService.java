package com.syncronys.registration.client.security;

import com.syncronys.registration.client.config.AuthUser;
import com.syncronys.registration.client.entity.Role;
import com.syncronys.registration.client.entity.User;
import com.syncronys.registration.client.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ssn = request.getParameter("ssn");
        User user = userRepository.findByUserNameAndSsn(userName, ssn);
        if (user != null) {
            AuthUser authUser =  new AuthUser(user.getUserName(),
                    user.getPassword(),
                    mapRolesToAuthorities(user.getRoles()));
            BeanUtils.copyProperties(user, authUser);
            return authUser;
        } else {
            throw new UsernameNotFoundException("Invalid username or password or ssn.");
        }
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<? extends GrantedAuthority> mapRoles = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return mapRoles;
    }
}

