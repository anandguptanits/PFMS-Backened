package com.pfms.walletCategory.services;

import com.pfms.walletCategory.exception.WalletCategoryException;
import com.pfms.walletCategory.model.Category;
import com.pfms.walletCategory.model.Wallet;
import com.pfms.walletCategory.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;


    @Test
    void canAddWallet() throws Exception {
        Wallet wallet =new Wallet("abc@gmail.com","SBI",223D);

        when(walletRepository.existsByWalletNameAndUserEmail("SBI","abc@gmail.com")).thenReturn(true);
        assertThrows(DuplicateKeyException.class, () -> walletService.addWallet(wallet));

    }

    @Test
    void checkIfThrowsExceptionIfAmountIsCreditedNotExistWallet() {
        assertThrows(Exception.class, () -> walletService.creditAmount(1L,233D));
    }

    @Test
    void checkIfThrowsExceptionIfAmountIsDebitedNotExistWallet() {
        assertThrows(Exception.class, () -> walletService.debitAmount(1L,233D));
    }
}