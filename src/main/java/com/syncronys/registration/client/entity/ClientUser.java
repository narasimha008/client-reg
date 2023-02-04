package com.syncronys.registration.client.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CLIENT_USERS")
public class ClientUser {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Client_First_Name")
    private String firstName;

    @Column(name = "Client_Last_Name")
    private String lastName;

    @Column(name = "Client_SSN")
    private String ssn;

    @Column(name = "Client_DOB")
    private LocalDate dob;

}
