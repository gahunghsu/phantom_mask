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
@Table(name = "mask_info")
public class MaskInfo {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long maskId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;    
    
    @Column(name = "phar_name")
    private String pharName;
    
    public MaskInfo() {
        super();
    }

    public MaskInfo(String name, BigDecimal price, String pharName) {
        super();
        this.name = name;
        this.price = price;
        this.pharName = pharName;
    }
}
