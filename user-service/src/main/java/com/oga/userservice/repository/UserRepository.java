package com.oga.userservice.repository;

import com.oga.userservice.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByUserName(String userName);
    List<UserEntity> findByIdNotLike(long id);

}
