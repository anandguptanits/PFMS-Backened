package com.pfms.transaction.service;

import com.pfms.transaction.Model.Transactions;
import com.pfms.transaction.dto.*;
import com.pfms.transaction.exception.TransactionException;
import com.pfms.transaction.helper.CSVHelper;
import com.pfms.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Transactions> getAllTransactionsOfAUser(String userEmail){
        return transactionRepository.findAllByUserEmailOrderByTransactionIdDesc(userEmail);
    }

    public List<Transactions> getLimitedTransactions(String userEmail,int limit){
        return transactionRepository.findLimitedTransactions(userEmail,limit);
    }

    public List<Transactions> getAllTransactionsOfACategory(String userEmail,String categoryName,int limit){
        return transactionRepository.findLimitedCategoryTransactions(userEmail,categoryName,limit);
    }

    public List<Transactions> getAllTransactionsOfAWallet(String userEmail,String walletName,int limit){
        return transactionRepository.findLimitedWalletTransactions(userEmail, walletName, limit);
    }

    public List<Transactions> getAllWalletTransactionsBetweenDates(String userEmail,String walletName,String startDate,String endDate){
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end =  LocalDate.parse(endDate);
        return transactionRepository.findAllByUserEmailAndWalletNameAndTransactionDateBetweenOrderByTransactionDateDesc(userEmail,walletName,start,end);
    }

    public List<Transactions> getAllCategoryTransactionsBetweenDates(String userEmail,String categoryName,String startDate,String endDate){
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end =  LocalDate.parse(endDate);
        return transactionRepository.findAllByUserEmailAndCategoryNameAndTransactionDateBetweenOrderByTransactionDateDesc(userEmail,categoryName,start,end);
    }



    public Transactions addTransaction(Transactions transaction){
        try {
            System.out.println(transaction.getWalletName()+"  "+transaction.getAmount());
            restTemplate.put("http://WALLET-CATEGORY/wallet/" + transaction.getUserEmail() + "/" + transaction.getWalletName() + "/debitamount?amount=" + transaction.getAmount(), null, String.class);
        }
        catch (Exception e){
            System.out.println(e.getMessage()+"  at wallet");
            throw new TransactionException(e.getMessage().split(":")[2].split(",")[0].split("\"")[1]);
        }
        try {
            System.out.println(transaction.getUserEmail()+" "+transaction.getCategoryName()+" "+transaction.getAmount());
            restTemplate.put("http://WALLET-CATEGORY/category/" + transaction.getUserEmail() + "/" + transaction.getCategoryName() + "/creditamount?amount=" + transaction.getAmount(), null);
        }
        catch (Exception e){
            System.out.println(e.getMessage()+"  at category");
            restTemplate.put("http://WALLET-CATEGORY/wallet/"+transaction.getUserEmail()+"/"+transaction.getWalletName()+"/creditamount?amount="+transaction.getAmount(),null,String.class);
            throw new TransactionException(e.getMessage().split(":")[2].split(",")[0].split("\"")[1]);
        }
        transaction.setTransactionDate(LocalDate.now(ZoneId.of("Asia/Kolkata")));
        return transactionRepository.save(transaction);
    }

    public Transactions addTransactionFromFile(Transactions transaction){
        try {
            restTemplate.put("http://WALLET-CATEGORY/wallet/" + transaction.getUserEmail() + "/" + transaction.getWalletName() + "/debitamount?amount=" + transaction.getAmount(), null, String.class);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            throw new TransactionException(e.getMessage().split(":")[2].split(",")[0].split("\"")[1]);
        }
        try {
            restTemplate.put("http://WALLET-CATEGORY/category/" + transaction.getUserEmail() + "/" + transaction.getCategoryName() + "/creditamount?amount=" + transaction.getAmount(), null);
        }
        catch (Exception e){
            restTemplate.put("http://WALLET-CATEGORY/wallet/"+transaction.getUserEmail()+"/"+transaction.getWalletName()+"/creditamount?amount="+transaction.getAmount(),null,String.class);
            throw new TransactionException(e.getMessage().split(":")[2].split(",")[0].split("\"")[1]);
        }
        return transactionRepository.save(transaction);
    }

    public List<Transactions> getTransactionsBetweenDates(String startDate,String endDate,String userEmail){
        LocalDate parsedStartDate = LocalDate.parse(startDate);
        LocalDate parseEndDate = LocalDate.parse(endDate);
        return transactionRepository.findAllByTransactionDateBetweenAndUserEmailOrderByTransactionIdDesc(parsedStartDate, parseEndDate,userEmail);
    }

    public List<Transactions> getTransactionsOfADate(String date,String userEmail){
        LocalDate parseDate = LocalDate.parse(date);
        return transactionRepository.findAllByTransactionDateAndUserEmailOrderByTransactionIdDesc(parseDate,userEmail);
    }

    public MultipleDaysTransactionSum getSumOfAllTransactionsBetweenDates(String startDate,String endDate,String userEmail){
        double sumOfTransactions= getTransactionsBetweenDates(startDate, endDate,userEmail).stream()
                .mapToDouble(Transactions::getAmount)
                .sum();
        return new MultipleDaysTransactionSum(LocalDate.parse(startDate),LocalDate.parse(endDate),sumOfTransactions);
    }

    public SingleDayTransactionSum getSumOfTransactionsOnDate(String date,String userEmail){
        double sumOfTransactions= getTransactionsOfADate(date,userEmail).stream()
                .mapToDouble(Transactions::getAmount)
                .sum();

        return new SingleDayTransactionSum(LocalDate.parse(date),sumOfTransactions);
    }

    public List<Transactions> todayTransactions(String userEmail){
        return transactionRepository.findAllByTransactionDateAndUserEmailOrderByTransactionIdDesc(LocalDate.now(ZoneId.of("Asia/Kolkata")),userEmail);
    }

    public SingleDayTransactionSum todayTransactionsSum(String userEmail){
        double sumOfTransactions = todayTransactions(userEmail).stream().mapToDouble(Transactions::getAmount)
                .sum();
        return new SingleDayTransactionSum(LocalDate.now(ZoneId.of("Asia/Kolkata")),sumOfTransactions);
    }

    public List<Transactions> getMonthTransactions(int n,String userEmail){
        return transactionRepository.findTransactionsOfAMonth(n,LocalDate.now().getYear(),userEmail);
    }

    public AllMonthSpends getAllMonthSpends(String userEmail){
        AllMonthSpends allMonthSpends = new AllMonthSpends();
        for(int i=1;i<=12;i++){
            double sum= getMonthTransactions(i,userEmail).stream()
                    .mapToDouble(Transactions::getAmount).sum();
            allMonthSpends.addMonthTransaction(Month.of(i),sum);
        }
        allMonthSpends.setYear(Year.now());
        return  allMonthSpends;
    }

    public AllInfo getEveryInfo(String userEmail){
        AllInfo allInfo = new AllInfo();
        try {
            allInfo.setWalletsBalance(restTemplate.getForObject("http://WALLET-CATEGORY/wallet/" + userEmail  + "/balance", Double.class));
        }
        catch (Exception e){
            throw new TransactionException(e.getMessage().split(":")[2].split(",")[0].split("\"")[1]);
        }
        allInfo.setTodaySpent(todayTransactionsSum(userEmail));
        allInfo.setMonthlySpent(new MonthTransactions(LocalDate.now().getMonth(),getMonthTransactions(LocalDate.now().getMonth().getValue(),userEmail).stream()
                .mapToDouble(Transactions::getAmount).sum()));
        double sum=0;
        for(Map.Entry<Month,Double> entry: getAllMonthSpends(userEmail).getMonthlyTransactions().entrySet()){
            sum+=entry.getValue();
        }
        allInfo.setYearlySpent(sum);
        allInfo.setTotalSpent(getAllTransactionsOfAUser(userEmail).stream()
                .mapToDouble(Transactions::getAmount).sum());
        return allInfo;
    }

    public List<TopCategories> getTopCategories(String userEmail){
        return transactionRepository.getTopCategroy(userEmail);
    }

    public ResponseEntity<Map<String, String>> uploadFile(MultipartFile file, String userEmail){
        Map<String,String> response = new HashMap<>();
        if(CSVHelper.isCSV(file)){
            try{
                List<Transactions> transactions = CSVHelper.csvToTransactions(file.getInputStream(),userEmail);
                try {
                    transactions.stream().forEach(transactions1 -> {
                        try {
                            addTransactionFromFile(transactions1);
                        }catch (Exception e){

                        }
                    });
                }
                catch (Exception e){
                    response.put("error",e.getMessage());
                    return ResponseEntity.badRequest().body(response);
                }
            } catch (Exception e){
                response.put("error",e.getMessage());
                return ResponseEntity.badRequest().body(response);
            }
            response.put("message","File successfully uploaded");
            return ResponseEntity.ok().body(response);
        }
        else{
            response.put("error","Select a CSV file");
            return ResponseEntity.badRequest().body(response);
        }
    }

    public ResponseEntity<Resource> dowloadCSV(String userEmail,String startDate,String endDate){
        final List<Transactions> transactionsList= getTransactionsBetweenDates(startDate,endDate,userEmail);
        final InputStreamResource resource = new InputStreamResource(CSVHelper.writeDataToCsv(transactionsList));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=Transaction.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }


}
