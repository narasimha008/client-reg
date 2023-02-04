package com.syncronys.registration.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    private String address;

    private String city;

    private String zipcode;

    private String state;

    private String ssn;

    private String dob;

    @NotEmpty(message = "UserName should not be empty")
    private String userName;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    private String consentFlag;
}
