package com.syncronys.registration.client.service;

import com.syncronys.registration.client.dto.UserDto;
import com.syncronys.registration.client.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByUserName(String userName);

    List<UserDto> findAllUsers();
}
