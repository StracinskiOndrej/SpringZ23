package com.example.springz23.db;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserAccount, String> {

}
