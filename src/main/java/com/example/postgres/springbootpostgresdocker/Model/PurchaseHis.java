package com.example.postgres.springbootpostgresdocker.Model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "purchase_his")
public class PurchaseHis {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "pharmacyName")
    private String pharmacyName;

    @Column(name = "maskName")
    private String maskName;
    
    @Column(name = "transactionAmount")
    private BigDecimal transactionAmount;
    
    @Column(name = "transactionDate")
    private String transactionDate;
    
    @Column(name = "userName")
    private String userName;
    
    @Column(name = "qty")
    private int qty;
    
    public PurchaseHis() {
        super();
    }

    public PurchaseHis(String userName, String pharmacyName, String maskName, BigDecimal transactionAmount, String transactionDate, int qty) {
        super();
        this.userName = userName;
        this.pharmacyName = pharmacyName;
        this.maskName = maskName;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.qty = qty;
    }
}
