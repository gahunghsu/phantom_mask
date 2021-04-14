package com.example.postgres.springbootpostgresdocker.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.postgres.springbootpostgresdocker.Model.PharmacyInfo;


@Repository
public interface PharmacyInfoRepository extends JpaRepository<PharmacyInfo,Long> {

	@Query(value = "select * "
			+ "  from pharmacy_info "
			+ " where name = :name"
			+ " ;",nativeQuery = true)
	List<PharmacyInfo> findByName( @Param("name") String name);
	
	@Transactional
    @Modifying
    @Query(value = "update pharmacy_info set cash_balance = cash_balance + ?2 where name = ?1",nativeQuery = true)
    int updateCashByName(String name, BigDecimal amount);
}
