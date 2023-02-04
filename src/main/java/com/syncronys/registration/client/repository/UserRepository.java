package com.syncronys.registration.client.repository;

import com.syncronys.registration.client.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);

    User findByUserNameAndSsn(String userName, String ssn);
}
