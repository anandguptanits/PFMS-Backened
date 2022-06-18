package com.pfms.walletCategory.repository;

import com.pfms.walletCategory.model.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WalletRepositoryTest {

    @Autowired
    private WalletRepository underTestWallet;

    @Test
    void whetherAbleToFindByUserEmailAndAndWalletName() {

        Wallet expected=new Wallet("abc@gmail.com","SBI",234D);
        underTestWallet.save(expected);
        Wallet actual=underTestWallet.findByUserEmailAndAndWalletName("abc@gmail.com","SBI");
        assertEquals(actual,expected);
    }

    @Test
    void whetherAbleToGetListAllWalletNames() {

        Wallet wallet1=new Wallet("abc@gmail.com","SBI",234D);
        Wallet wallet2=new Wallet("abc@gmail.com","VISA",234D);
        Wallet wallet3=new Wallet("abc@gmail.com","CITI",234D);

        List<Wallet> listWallet= Arrays.asList(wallet1,wallet2,wallet3);
        underTestWallet.saveAll(listWallet);
        List<String> expected=Arrays.asList("SBI","VISA", "CITI");

        List<String> actual=underTestWallet.listAllWalletNames("abc@gmail.com");

        assertThat(expected).hasSameElementsAs(actual);

        //assertEquals(expected,actual);

    }
}