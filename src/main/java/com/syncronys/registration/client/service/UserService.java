package com.syncronys.registration.client.service;

import com.syncronys.registration.client.dto.UserDto;
import com.syncronys.registration.client.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    abstract UserDto findByUserName(String userName);

    List<UserDto> findAllUsers();

    void updateUser(UserDto userDto);
}
