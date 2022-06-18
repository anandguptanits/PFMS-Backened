package com.pfms.walletCategory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "goal",uniqueConstraints = @UniqueConstraint(columnNames = {"goal_name","user_email"}))
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long goalId;

    @Column(name = "goal_name")
    private String goalName;

    @Column(name = "user_email")
    private String userEmail;

    @OneToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private List<Category> categories = new LinkedList<>();

    private double maxSpent;

    private double amountSpent;

    public Goal(String goalName,String userEmail,double maxSpent){
        this.goalName=goalName;
        this.userEmail=userEmail;
        this.maxSpent=maxSpent;
    }

    public Goal setLimit(double limit) {
        this.maxSpent = maxSpent;
        return this;
    }

    public Goal setAmountSpent(double amountSpent) {
        this.amountSpent = amountSpent;
        return this;
    }
    public Goal setCategories(Category category){
        this.categories.add(category);
        return this;
    }

    public Goal removeCategory(Category category){
        this.categories.remove(category);
        return this;
    }
}
