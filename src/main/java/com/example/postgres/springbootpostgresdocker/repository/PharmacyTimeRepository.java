package com.example.postgres.springbootpostgresdocker.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.postgres.springbootpostgresdocker.Model.PharmacyTime;


@Repository
public interface PharmacyTimeRepository extends JpaRepository<PharmacyTime,Long> {
	@Query(value = "select distinct phar_name"
			+ "  from pharmacy_time pt"
			+ " where pt.week = to_char(cast(:dateTime as date), 'Dy')"
			+ "   and substring(:dateTime,12,5) between open and close"
			+ " order by 1;",nativeQuery = true)
	List<String> findOpenPharmacyByDateTime( @Param("dateTime") String dateTime);
	
	@Query(value = "select distinct id, phar_name, week, close, open"
			+ "  from pharmacy_time pt"
			+ " where open <= :time and close >= :time"
			+ " order by 2,3;",nativeQuery = true)
	List<PharmacyTime> findOpenPharmacyByTime( @Param("time") String time);
	
	@Query(value = "select * "
			+ "  from pharmacy_time "
			+ " where phar_name = :name"
			+ " ;",nativeQuery = true)
	List<PharmacyTime> findPharmacyByName( @Param("name") String name);
}
