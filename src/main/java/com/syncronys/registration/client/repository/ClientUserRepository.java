package com.syncronys.registration.client.repository;

import com.syncronys.registration.client.entity.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {

    List<ClientUser> findByFirstNameAndLastNameAndSsnAndDob(String firstName, String lastName, String ssn, LocalDate dob);
}
