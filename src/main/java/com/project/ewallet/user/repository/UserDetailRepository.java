package com.project.ewallet.user.repository;

import com.project.ewallet.user.entity.UserDetail;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDetailRepository extends CrudRepository<UserDetail, Long> {

    Optional<UserDetail> findByUsername(String username);
}
