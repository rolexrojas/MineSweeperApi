package com.deviget.repository;

import com.deviget.domain.UserAuth;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthRepository extends CrudRepository<UserAuth, Long> {

    UserAuth findUserAuthByUsername(String Username);

}
