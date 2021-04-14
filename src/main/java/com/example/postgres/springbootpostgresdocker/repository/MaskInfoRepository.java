package com.example.postgres.springbootpostgresdocker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.postgres.springbootpostgresdocker.Model.MaskInfo;

@Repository
public interface MaskInfoRepository extends JpaRepository<MaskInfo,Long> {	
	@Query(value = "select * "
			+ "  from mask_info "
			+ " where phar_name = :name"
			+ " order by price;",nativeQuery = true)
	List<MaskInfo> findMaskByPharmacyOrderByPrice( @Param("name") String name);
	
	@Query(value = "select * "
			+ "  from mask_info "
			+ " where phar_name = :name"
			+ " order by name;",nativeQuery = true)
	List<MaskInfo> findMaskByPharmacyOrderByName( @Param("name") String name);
	
	@Query(value = "select * "
			+ "  from mask_info "
			+ " where phar_name = :name"
			+ "  and name = :mask"
			+ " ;",nativeQuery = true)
	List<MaskInfo> findMaskByPharMaskName( @Param("name") String name, @Param("mask") String mask);
	
	@Query(value = "select * "
			+ "  from mask_info "
			+ " where name = :name"
			+ " ;",nativeQuery = true)
	List<MaskInfo> findByName( @Param("name") String name);
	
	@Query(value = "select phar_name"
			+ "  from mask_info "
			+ " where price between :lowprice and :highprice"
			+ " group by 1 having count(name) >= :qty order by 1;",nativeQuery = true)
	List<String> findPharmacyByPriceMore( @Param("lowprice") double lowprice, @Param("highprice") double highprice, @Param("qty")int qty);
	
	@Query(value = "select phar_name"
			+ "  from mask_info "
			+ " where price between :lowprice and :highprice"
			+ " group by 1 having count(name) <= :qty order by 1;",nativeQuery = true)
	List<String> findPharmacyByPriceLess( @Param("lowprice") double lowprice, @Param("highprice") double highprice,@Param("qty")int qty);
	
	@Query(value = "with TTL_NAME as (\n"
			+ "select 'PHAR' as type, phar_name as NAME FROM mask_info\n"
			+ "union all\n"
			+ "select 'MASK' as type, name as NAME FROM mask_info\n"
			+ ")\n"
			+ "SELECT distinct type, name, ts_rank_cd(to_tsvector('english', upper(NAME)), query) AS rank -- 搜尋欄位\n"
			+ "FROM TTL_NAME A, to_tsquery(upper(:condition)) query -- 搜尋文字\n"
			+ "WHERE query @@ to_tsvector('english', upper(NAME))\n"
			+ "  and type in :type \n"
			+ "ORDER BY rank DESC", nativeQuery = true)
	List<Object[]> findMaskPharmacyName( @Param("condition") String condition, @Param("type") List<String> type);
}
