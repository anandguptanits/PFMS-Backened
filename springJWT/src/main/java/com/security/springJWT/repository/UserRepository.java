package com.security.springJWT.repository;

import com.security.springJWT.user.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserData,Long> {

    public UserData findByUserEmail(String userEmail);

    boolean existsByUserEmail(String userEmail);


}
