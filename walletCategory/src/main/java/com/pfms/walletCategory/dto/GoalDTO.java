package com.pfms.walletCategory.dto;

import com.pfms.walletCategory.model.Goal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GoalDTO {

    private String goalName;
    private String userEmail;
    private double maxSpent;
    private double amountSpent;
    private List<String> categories;

}
