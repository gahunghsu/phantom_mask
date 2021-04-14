package com.example.postgres.springbootpostgresdocker.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "pharmacy_time")
public class PharmacyTime {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
	
	@Column(name = "pharName")
    private String pharName;
	
	@Column(name = "week")
    private String week;
	
	@Column(name = "open")
    private String open;

    @Column(name = "close")
    private String close;
    
    public PharmacyTime() {
        super();
    }

    public PharmacyTime(long id, String pharName, String week, String open, String close) {
        super();
        this.id = id;
        this.pharName = pharName;
        this.week = week;
        this.open = open;
        this.close = close;
    }
    
}
