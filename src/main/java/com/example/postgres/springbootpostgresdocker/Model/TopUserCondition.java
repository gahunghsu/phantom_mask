package com.example.postgres.springbootpostgresdocker.Model;

import lombok.Data;

@Data
public class TopUserCondition {

	private String startDate;
	private String endDate;
	private int x;
	
	public TopUserCondition() {
        super();
    }

    public TopUserCondition(String startDate, String endDate, int x) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
        this.x = x;
    }
}
