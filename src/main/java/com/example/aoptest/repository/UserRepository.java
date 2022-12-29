package com.example.aoptest.repository;

import com.example.aoptest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value = "(select * from user order by count_user desc limit 4, 50) order by rand() limit 16")
    List<User> findAllRandom();

    List<User> findTop3ByOrderByCountUserDescSeqAsc();
}
