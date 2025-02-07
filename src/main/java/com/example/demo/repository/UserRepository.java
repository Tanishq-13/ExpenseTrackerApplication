package com.example.demo.repository;

import com.example.demo.entities.UserInfo;
//import auth
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//passing userinfo table
//if you go inside crudrepository you will find this CrudRepository<T, ID>
//and various methods eg findById(ID id) in ID i am passing long
@Repository
public interface UserRepository extends CrudRepository<UserInfo, Long> {
    public UserInfo findByUsername(String username);
}
