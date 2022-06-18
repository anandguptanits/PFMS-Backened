package com.pfms.walletCategory.controller;

import com.pfms.walletCategory.dto.WalletNames;
import com.pfms.walletCategory.model.Wallet;
import com.pfms.walletCategory.services.WalletService;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/{userEmail}/getall")
    private List<Wallet> getAllWallets(@PathVariable(name = "userEmail") String userEmail){
        return walletService.getAll(userEmail);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String,String>> addWallet(@RequestBody Wallet wallet){
        Map<String,String> response = new HashMap<>();
        try {
            wallet.setWalletName(WordUtils.capitalizeFully(wallet.getWalletName()));
            walletService.addWallet(wallet);
            response.put("message","Wallet Added");
            return ResponseEntity.accepted().body(response);
        }
        catch (Exception e){
            response = new HashMap<>();
            response.put("message",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

//    @PutMapping("/{id}/creditamount")
//    public ResponseEntity<Map<String,String>> creditAmount(@PathVariable(name = "id") Long id,@RequestParam(name = "amount") double amount){
//        Map<String,String> response = new HashMap<>();
//        try {
//            walletService.creditAmount(id, amount);
//            response.put("message","Amount is credited to your wallet");
//            return ResponseEntity.ok().body(response);
//        }catch (Exception e){
//            response.put("Error",e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }

//    @PutMapping("/{id}/debitamount")
//    public ResponseEntity<Map<String,String>> debitAmount(@PathVariable(name = "id") Long id,@RequestParam(name = "amount") double amount){
//        Map<String,String> response = new HashMap<>();
//        try {
//            walletService.debitAmount(id, amount);
//            response.put("message","Amount is debited from your wallet");
//            return ResponseEntity.ok().body(response);
//        }catch (Exception e){
//            response.put("Error",e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }

    @PutMapping("/{userEmail}/{walletName}/creditamount")
    public ResponseEntity<Map<String,String>> creditAmount(@PathVariable(name = "userEmail") String userEmail,@PathVariable(name = "walletName") String walletName,@RequestParam(name = "amount") double amount){
        Map<String,String> response = new HashMap<>();
        try {
            walletService.creditAmount(userEmail,walletName,amount);
            response.put("message","Amount is credited to your wallet");
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            response.put("Error",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{userEmail}/{walletName}/debitamount")
    public ResponseEntity<Map<String,String>> debitAmount(@PathVariable(name = "userEmail") String userEmail,@PathVariable(name = "walletName") String walletName,@RequestParam(name = "amount") double amount){
        Map<String,String> response = new HashMap<>();
        try {
            walletService.debitAmount(userEmail,walletName, amount);
            response.put("message","Amount is debited from your wallet");
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            response.put("Error",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{userEmail}/balance")
    public double getBalanceOfAllWallets(@PathVariable("userEmail")String userEmail){
        return walletService.getBalanceOfAllWallets(userEmail);
    }

    @GetMapping("/{userEmail}/getnames")
    public ResponseEntity<List<String>> getNames(@PathVariable("userEmail")String userEmail){
        return ResponseEntity.ok().body(walletService.getNames(userEmail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteWallet(@PathVariable("id")Long id){
        Map<String,String> response = new HashMap<>();
        try {
            walletService.deleteWallet(id);
            response.put("message","Wallet deleted successfully");
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            response.put("error",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

}
