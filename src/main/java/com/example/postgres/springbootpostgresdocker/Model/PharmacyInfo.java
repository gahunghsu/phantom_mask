package com.example.postgres.springbootpostgresdocker.Model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "pharmacy_info")
public class PharmacyInfo {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long pharmacyId;

    @Column(name = "name")
    private String name;

    @Column(name = "cashBalance")
    private BigDecimal cashBalance;
    
    @Transient
    private String openingHours;
    
    @Transient
    private List<MaskInfo> masks;
    
    public PharmacyInfo() {
        super();
    }

    public PharmacyInfo(long pharmacyId, String name, BigDecimal cashBalance) {
        super();
        this.pharmacyId = pharmacyId;
        this.name = name;
        this.cashBalance = cashBalance;
    }
}
