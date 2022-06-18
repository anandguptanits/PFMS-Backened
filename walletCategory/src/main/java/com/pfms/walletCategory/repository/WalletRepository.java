package com.pfms.walletCategory.repository;

import com.pfms.walletCategory.dto.WalletNames;
import com.pfms.walletCategory.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {

    boolean existsByWalletNameAndUserEmail(String walletName,String userEmail);
    Wallet findByUserEmailAndAndWalletName(String userEmail,String walletName);
    List<Wallet> findAllByUserEmail(String email);

    @Query(value = "SELECT wallet_name as walletName from Wallets where user_email=?1",nativeQuery = true)
    List<String> listAllWalletNames(String userEmail);

}
