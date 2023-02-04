package com.syncronys.registration.client.service.impl;

import com.syncronys.registration.client.dto.UserDto;
import com.syncronys.registration.client.entity.ClientUser;
import com.syncronys.registration.client.entity.Role;
import com.syncronys.registration.client.entity.User;
import com.syncronys.registration.client.repository.ClientUserRepository;
import com.syncronys.registration.client.repository.RoleRepository;
import com.syncronys.registration.client.repository.UserRepository;
import com.syncronys.registration.client.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private ClientUserRepository clientUserRepository;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           ClientUserRepository clientUserRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.clientUserRepository = clientUserRepository;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        LocalDate dob = LocalDate.parse(userDto.getDob(), DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        BeanUtils.copyProperties(userDto, user);
        user.setDob(dob);
        List<ClientUser> clientUser = clientUserRepository.findByFirstNameAndLastNameAndSsnAndDob(user.getFirstName(), user.getLastName(), user.getSsn(), user.getDob());
        if(ObjectUtils.isEmpty(clientUser)){
            throw new IllegalArgumentException("Client Not Exists in our records.");
        }
        //encrypt the password once we integrate spring security
        //user.setPassword(userDto.getPassword());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if (role == null) {
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    private UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

    @PostConstruct
    private void initData() {
        for (int i = 0; i < 3; i++) {
            ClientUser e = new ClientUser();
            e.setId(Long.valueOf(++i));
            e.setFirstName("SSR" + e.getId());
            e.setLastName("SSR" + e.getId());
            e.setSsn("5657" + e.getId());
            e.setDob(LocalDate.parse("12-12-2022", DateTimeFormatter.ofPattern("MM-dd-yyyy")));
            clientUserRepository.save(e);
        }
    }
}
