package org.mick.user.repository;

import org.mick.user.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Boolean existsUserByEmail(String email);

    User findUserByEmail(String email);

}
