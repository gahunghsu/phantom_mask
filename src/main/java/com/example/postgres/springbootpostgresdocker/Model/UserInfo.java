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
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cashBalance")
    private BigDecimal cashBalance;
    
    @Transient
    private List<PurchaseHis> purchaseHistories;

    public UserInfo() {
        super();
    }

    public UserInfo(long id, String name, BigDecimal cashBalance) {
        super();
        this.id = id;
        this.name = name;
        this.cashBalance = cashBalance;
    }

    
}
