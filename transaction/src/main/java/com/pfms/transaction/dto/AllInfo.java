package com.pfms.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AllInfo {

    double walletsBalance;
    SingleDayTransactionSum todaySpent;
    MonthTransactions monthlySpent;
    double yearlySpent;
    double totalSpent;

}
