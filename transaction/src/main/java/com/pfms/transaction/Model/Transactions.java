package com.pfms.transaction.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.text.ParseException;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@ToString
@Table(name = "transactions")
public class Transactions {

    @Id
    @Column(name = "transaction_id",nullable = false,unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;

    private String userEmail,walletName,categoryName;
    private double amount;
    private LocalDate transactionDate;

    public Transactions(String userEmail, String walletName, String categoryName, double amount) throws ParseException {
        this.userEmail = userEmail;
        this.walletName = walletName;
        this.categoryName = categoryName;
        this.amount = amount;
    }

    public Transactions(LocalDate transactionDate,String walletName,String categoryName,double amount,String userEmail){
        this.transactionDate=transactionDate;
        this.amount=amount;
        this.categoryName=categoryName;
        this.userEmail=userEmail;
        this.walletName=walletName;
    }

    public Transactions setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public Transactions setWalletName(String walletName) {
        this.walletName = walletName;
        return this;
    }

    public Transactions setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public Transactions setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public Transactions setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }
}
