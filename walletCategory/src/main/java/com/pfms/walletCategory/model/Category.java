package com.pfms.walletCategory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "categories",uniqueConstraints = @UniqueConstraint(columnNames = {"category_name","user_email"}))
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "category_name",nullable = false)
    private String categoryName;

    @Column(name = "user_email",nullable = false)
    private String userEmail;

    @Column(name = "amount_spent")
    private double amountSpent;

//    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//    @JoinColumn(name = "goal_name",referencedColumnName = "goal_name")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Goal goal;

    public Category(String categoryName,String userEmail){
        this.categoryName=categoryName;
        this.userEmail=userEmail;
        this.amountSpent=0;
    }

    public Category setAmountSpent(double amountSpent) {
        this.amountSpent = amountSpent;
        return this;
    }

    public Category setCategoryName(String categoryName){
        this.categoryName=categoryName;
        return this;
    }
//
//    public void setGoal(Goal goal) {
//        this.goal = goal;
//    }
}
