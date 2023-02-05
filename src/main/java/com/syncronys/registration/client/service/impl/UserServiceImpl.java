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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    public static final String MM_DD_YYYY = "MM-dd-yyyy";
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
        LocalDate dob = LocalDate.parse(userDto.getDob(), DateTimeFormatter.ofPattern(MM_DD_YYYY));
        BeanUtils.copyProperties(userDto, user);
        user.setDob(dob);
        List<ClientUser> clientUser = clientUserRepository.findByFirstNameAndLastNameAndSsnAndDob(user.getFirstName(), user.getLastName(), user.getSsn(), user.getDob());
        if (ObjectUtils.isEmpty(clientUser)) {
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
    public UserDto findByUserName(String userName) {
        User user = userRepository.findByUserName(userName);
        return convertEntityToDto(user);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    private UserDto convertEntityToDto(User user) {
        if (ObjectUtils.isEmpty(user)) {
            return null;
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setDob(user.getDob().format(DateTimeFormatter.ofPattern(MM_DD_YYYY)));
        return userDto;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

    @Override
    public void updateUser(UserDto userDto) {
        User user = userRepository.findByUserName(userDto.getUserName());
        user.setConsentFlag(userDto.getConsentFlag());
        userRepository.save(user);
    }

}
