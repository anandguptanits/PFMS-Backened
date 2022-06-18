package com.pfms.walletCategory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "Wallets",uniqueConstraints = @UniqueConstraint(columnNames = {"wallet_name","user_email"}))
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wallet_name", nullable = false)
    private String walletName;

    @Column(name = "user_email",nullable = false)
    private String userEmail;

    @Column(name = "balance")
    private double balance;

    public Wallet setWalletName(String walletName) {
        this.walletName = walletName;
        return this;
    }

    public Wallet setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public Wallet(String userEmail,String walletName,double balance){
        this.walletName=walletName;
        this.userEmail=userEmail;
        this.balance=balance;
    }
}
