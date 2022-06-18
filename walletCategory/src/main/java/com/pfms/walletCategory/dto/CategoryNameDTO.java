package com.pfms.walletCategory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryNameDTO {

    Long id;
    String categoryName;
    Double amountSpent;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        CategoryNameDTO cDTO=(CategoryNameDTO) obj;

        return cDTO.getCategoryName()==categoryName && cDTO.getAmountSpent()==amountSpent;
    }
}
