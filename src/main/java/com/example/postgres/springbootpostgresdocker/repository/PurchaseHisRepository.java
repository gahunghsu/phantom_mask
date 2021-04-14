package com.example.postgres.springbootpostgresdocker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.postgres.springbootpostgresdocker.Model.PurchaseHis;


@Repository
public interface PurchaseHisRepository extends JpaRepository<PurchaseHis,Long> {
	@Query(value = "select user_name\n"
			+ "	  from (\n"
			+ "	select user_name, rank() over (order by sum(transaction_amount) desc) as idx\n"
			+ "	  from purchase_his\n"
			+ "	 where cast(transaction_date as date) between cast(:startDate as date) and cast(:endDate as date)\n"
			+ "	 group by 1\n"
			+ "	) a\n"
			+ "	where idx <= :x", nativeQuery = true)
	List<String> findTopUserByAmt( @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("x") int x );
	
	@Query(value = "select sum(qty) as qty, sum(transaction_amount) as transaction_amount from purchase_his where cast(transaction_date as date) between cast(:startDate as date) and cast(:endDate as date)", nativeQuery = true)
	List<Object[]> findTtlAmtDol( @Param("startDate") String startDate, @Param("endDate") String endDate);
}