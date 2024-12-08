package com.example.domitory.repos;

import com.example.domitory.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    //нельзя показывать ПАРОЛИ А ВЫВОДЕ НА СТРАНИЦУ!!!!!!

//    Optional<Users> getAll();

    Optional<Users> findByUsername(String username);

//    @Query("select p from users p where concat(p.id, '', p.username, '', p.password, '', " +
//            "p.role_id, '', p.full_name, '', p.email, '', p.phone) like %?1%")
//    List<Users> searchUsers(String keyword);
}

