package com.aldren.repository;

import com.aldren.model.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {

    Users findByUserId(String userId);

}
