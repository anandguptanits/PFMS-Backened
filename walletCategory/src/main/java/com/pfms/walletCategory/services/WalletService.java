package com.pfms.walletCategory.services;

import com.pfms.walletCategory.dto.WalletNames;
import com.pfms.walletCategory.exception.WalletCategoryException;
import com.pfms.walletCategory.model.Wallet;
import com.pfms.walletCategory.repository.WalletRepository;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class WalletService{

    @Autowired
    private WalletRepository walletRepository;

    public List<Wallet> getAll(String userEmail){
        return walletRepository.findAllByUserEmail(userEmail);
    }

    public void addWallet(Wallet wallet) throws Exception {
        try {
            if (existWithWalletNameAndUserEmail(wallet.getWalletName(), wallet.getUserEmail())) {
                throw new DuplicateKeyException("Wallet with name " + wallet.getWalletName() + " is already available");
            }
            walletRepository.save(wallet);
        }catch (DuplicateKeyException e){
            throw new DuplicateKeyException(e.getMessage());
        }
        catch (Exception e){
            throw new Exception("Please try again later");
        }
    }

    public void creditAmount(Long id,double amount) throws Exception {
        try {
            Wallet wallet = walletRepository.findById(id).get();
            if(amount<=0){
                throw new InvalidValue("Amount can't be zero or less than zero");
            }
            wallet.setBalance(wallet.getBalance() + amount);
            walletRepository.save(wallet);
        }
        catch (NullPointerException e){
            throw new NullPointerException("Wallet not found");
        }
        catch (InvalidValue e){
            throw new InvalidValue(e.getMessage());
        }
        catch (Exception e){
            throw new Exception("Please try again later");
        }
    }

    public void debitAmount(Long id,double amount) throws Exception {
        try {
            Wallet wallet = walletRepository.findById(id).get();
            if(amount<=0){
                throw new InvalidValue("Amount can't be zero or less than zero");
            }
            if(wallet.getBalance()<amount){
                throw new InvalidValue("Insufficient Balance");
            }
            wallet.setBalance(wallet.getBalance() - amount);
            walletRepository.save(wallet);
        }
        catch (NullPointerException e){
            throw new NullPointerException("Wallet not found");
        }
        catch (InvalidValue e){
            throw new InvalidValue(e.getMessage());
        }
        catch (Exception e){
            throw new Exception("Please try again later");
        }
    }

    public void creditAmount(String userEmail,String walletName,Double amount) throws Exception {
        try {
            Wallet wallet = walletRepository.findByUserEmailAndAndWalletName(userEmail, walletName);
            if(amount<=0){
                throw new InvalidValue("Amount can't be zero or less than zero");
            }
            wallet.setBalance(wallet.getBalance() + amount);
            walletRepository.save(wallet);
        }
        catch (NullPointerException e){
            throw new NullPointerException("Wallet not found");
        }
        catch (InvalidValue e){
            throw new InvalidValue(e.getMessage());
        }
        catch (Exception e){
            throw new Exception("Please try again later");
        }
    }

    public void debitAmount(String userEmail,String walletName, double amount) throws Exception {
        try {
            Wallet wallet = walletRepository.findByUserEmailAndAndWalletName(userEmail, walletName);
            if(amount<=0){
                throw new InvalidValue("Amount can't be zero or less than zero");
            }
            wallet.setBalance(wallet.getBalance() - amount);
            walletRepository.save(wallet);
        }
        catch (NullPointerException e){
            throw new NullPointerException("Wallet not found");
        }
        catch (InvalidValue e){
            throw new InvalidValue(e.getMessage());
        }
        catch (Exception e){
            throw new Exception("Please try again later");
        }
    }

    public boolean existWithWalletNameAndUserEmail(String walletName,String userEmail){
        return walletRepository.existsByWalletNameAndUserEmail(walletName,userEmail);
    }

    public double getBalanceOfAllWallets(String userEmail){
        return walletRepository.findAllByUserEmail(userEmail).stream()
                .mapToDouble(Wallet::getBalance).sum();
    }

    public List<String> getNames(String userEmail){
        return walletRepository.listAllWalletNames(userEmail);
    }

    public void deleteWallet(Long id){
        walletRepository.deleteById(id);
    }

}
