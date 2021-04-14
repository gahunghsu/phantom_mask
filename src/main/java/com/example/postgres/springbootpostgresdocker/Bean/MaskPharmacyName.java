package com.example.postgres.springbootpostgresdocker.Bean;

import lombok.Data;

@Data
public class MaskPharmacyName {

	String type;

    String name;
    
    public MaskPharmacyName() {}
    
    public MaskPharmacyName(String type, String name) {
        this.type = type;
        this.name = name;
    }
}
