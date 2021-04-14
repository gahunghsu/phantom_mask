package com.example.postgres.springbootpostgresdocker.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.postgres.springbootpostgresdocker.Model.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {

	@Transactional
    @Modifying
    @Query(value = "update user_info set cash_balance = cash_balance - ?2 where name = ?1",nativeQuery = true)
    int updateCashByName(String name, BigDecimal amount);
}
