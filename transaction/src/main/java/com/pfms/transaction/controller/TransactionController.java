package com.pfms.transaction.controller;

import com.pfms.transaction.Model.Transactions;
import com.pfms.transaction.dto.*;
import com.pfms.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "transaction/{userEmail}")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/all")
    public ResponseEntity<List<Transactions>> getAllTransactionsOfAUser(@PathVariable("userEmail") String userEmail){
        List<Transactions> transactions = transactionService.getAllTransactionsOfAUser(userEmail);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(transactions);
    }

    @GetMapping("")
    public ResponseEntity<List<Transactions>> getLimitedTransactions(@PathVariable("userEmail")String userEmail,@RequestParam("limit")int limit){
        List<Transactions> transactions = transactionService.getLimitedTransactions(userEmail,limit);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(transactions);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<Transactions>> getAllTransactionsOfACategory(@PathVariable("userEmail") String userEmail,@PathVariable("categoryName") String categoryName,@RequestParam("limit")int limit){
        List<Transactions> transactions = transactionService.getAllTransactionsOfACategory(userEmail, categoryName,limit);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(transactions);
    }

    @GetMapping("/wallet/{walletName}/{startDate}/{endDate}")
    public List<Transactions> getAllWalletTransactionsBetweenDates(@PathVariable("userEmail")String userEmail,@PathVariable("walletName")String walletName,@PathVariable("startDate")String startDate,@PathVariable("endDate")String endDate){
        return transactionService.getAllWalletTransactionsBetweenDates(userEmail, walletName, startDate, endDate);
    }

    @GetMapping("/category/{categoryName}/{startDate}/{endDate}")
    public List<Transactions> getAllCategoryTransactionsBetweenDates(@PathVariable("userEmail")String userEmail,@PathVariable("categoryName")String categoryName,@PathVariable("startDate")String startDate,@PathVariable("endDate")String endDate){
        return transactionService.getAllCategoryTransactionsBetweenDates(userEmail, categoryName, startDate, endDate);
    }

    @GetMapping("/wallet/{walletName}")
    public ResponseEntity<List<Transactions>> getAllTransactionsOfAWallet(@PathVariable("userEmail") String userEmail,@PathVariable("walletName") String walletName,@RequestParam("limit")int limit){
        List<Transactions> transactions = transactionService.getAllTransactionsOfAWallet(userEmail, walletName,limit);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(transactions);
    }

    @PostMapping("/addTransaction")
    public Transactions addTransaction(@PathVariable("userEmail") String userEmail,@RequestBody Transactions transaction) throws Exception {
        return transactionService.addTransaction(transaction);
    }

    @GetMapping("/get/{startDate}/{endDate}")
    public List<Transactions> getTransactionsBetweenDates(@PathVariable("userEmail") String userEmail,@PathVariable("startDate")String startDate,@PathVariable("endDate")String endDate){
        return transactionService.getTransactionsBetweenDates(startDate,endDate,userEmail);
    }

    @GetMapping("/get/{date}")
    public List<Transactions> getTransactionsOfADate(@PathVariable("userEmail") String userEmail,@PathVariable("date")String date){
        return transactionService.getTransactionsOfADate(date,userEmail);
    }

    @GetMapping("/sum/{startDate}/{endDate}")
    public MultipleDaysTransactionSum getSumOfAllTransactionsBetweenDates(@PathVariable("userEmail") String userEmail,@PathVariable("startDate")String startDate, @PathVariable("endDate")String endDate){
        return transactionService.getSumOfAllTransactionsBetweenDates(startDate,endDate,userEmail);
    }

    @GetMapping("/sum/{date}")
    public SingleDayTransactionSum getSumOfTransactionsOnDate(@PathVariable("userEmail") String userEmail,@PathVariable("date") String date){
        return transactionService.getSumOfTransactionsOnDate(date,userEmail);
    }

    @GetMapping("/today")
    public List<Transactions> todayTransactions(@PathVariable("userEmail") String userEmail){
        return transactionService.todayTransactions(userEmail);
    }

    @GetMapping("/todaysum")
    public SingleDayTransactionSum todayTransactionsSum(@PathVariable("userEmail") String userEmail){
        return transactionService.todayTransactionsSum(userEmail);
    }

    @GetMapping("/month/{month}")
    public List<Transactions> getMonthTransactions(@PathVariable("userEmail") String userEmail,@PathVariable("month") int n){
        return transactionService.getMonthTransactions(n,userEmail);
    }

    @GetMapping("/allmonths")
    public AllMonthSpends getAllMonthSpends(@PathVariable("userEmail") String userEmail){
        return transactionService.getAllMonthSpends(userEmail);
    }

    @GetMapping("/getEveryinfo")
    public AllInfo getEveryInfo(@PathVariable("userEmail") String userEmail){
        return transactionService.getEveryInfo(userEmail);
    }

    @GetMapping("/getTopCategories")
    public List<TopCategories> getTopCategories(@PathVariable("userEmail") String userEmail){
        return transactionService.getTopCategories(userEmail);
    }

    @GetMapping("/downloadCsv/{startDate}/{endDate}")
    public ResponseEntity<Resource> downloadCsv(@PathVariable("userEmail") String userEmail,
                                                @PathVariable("startDate")String startDate,
                                                @PathVariable("endDate")String endDate
                                                ){
        return transactionService.dowloadCSV(userEmail,startDate,endDate);
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file")MultipartFile file, @PathVariable("userEmail")String userEmail){
        return transactionService.uploadFile(file,userEmail);
    }

}
