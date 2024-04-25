package com.dmytro.gameboot.repository;

import com.dmytro.gameboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.enabled = TRUE WHERE u.email = ?1")
    int enableUser(String email);


    @Modifying
    @Query("UPDATE User u SET u.balance = u.balance + :amount WHERE u.userId = :userId")
    void topUpUserBalance(@Param("amount") Double amount, @Param("userId") Long userId);
}
