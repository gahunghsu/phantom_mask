package com.example.postgres.springbootpostgresdocker.Model;


import lombok.Data;

@Data
public class QueryCondition {

	private String type;
	private String condition;
	
	public QueryCondition() {
        super();
    }

    public QueryCondition(String type, String condition) {
        super();
        this.type = type;
        this.condition = condition;
    }
}
