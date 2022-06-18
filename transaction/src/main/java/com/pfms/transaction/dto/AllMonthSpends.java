package com.pfms.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AllMonthSpends {

    private Year year;
    private HashMap<Month,Double> monthlyTransactions = new HashMap<Month, Double>();

    public void addMonthTransaction(Month month,double amount){
        this.monthlyTransactions.put(month,amount);
    }


}
