package com.pfms.transaction;

import com.pfms.transaction.Model.Transactions;
import com.pfms.transaction.dto.AllInfo;
import com.pfms.transaction.dto.MultipleDaysTransactionSum;
import com.pfms.transaction.helper.CSVHelper;
import com.pfms.transaction.repository.TransactionRepository;
import com.pfms.transaction.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.doReturn;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class TransactionServiceTests {

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private CSVHelper csvHelper;

    @Autowired
    private TransactionService transactionService;

    @Test
    public void getLimitedTransactionsTest(){
        List<Transactions> transactions = Arrays.asList(
                new Transactions(3,"test@gmail.com","testwallet","testcategory2",100,LocalDate.parse("2021-09-10")),
                new Transactions(4,"test@gmail.com","testwallet2","testcategory",300,LocalDate.parse("2021-09-05"))
        );
        doReturn(transactions).when(transactionRepository).findLimitedTransactions("test@gmail.com",10);
        List<Transactions> transactionsData = transactionService.getLimitedTransactions("test@gmail.com",10);
        Assertions.assertEquals(2,transactionsData.size());
    }

    @Test
    public void addTransactionTest() throws Exception {
        Transactions transactions = new Transactions(4,"abc@gmail.com","Citi","Food",300,LocalDate.parse("2021-09-05"));
        Mockito.doNothing().when(restTemplate).put("http://WALLET-CATEGORY/wallet/abc@Gmail.com/Citi/debitamount?amount=300", null, String.class);
        Mockito.doNothing().when(restTemplate).put("http://WALLET-CATEGORY/category/abc@gmail.com/Food/creditamount?amount=300", null);
        doReturn(transactions).when(transactionRepository).save(transactions);
        Assertions.assertEquals(transactions,transactionService.addTransaction(transactions));
    }

    @Test
    public void getAllTransactionsOfAWalletTest(){
        List<Transactions> transactions = Arrays.asList(
                new Transactions(3,"test@gmail.com","testwallet","testcategory2",100,LocalDate.parse("2021-10-10")),
                new Transactions(4,"test@gmail.com","testwallet","testcategory",300,LocalDate.parse("2021-10-05"))
        );
        doReturn(transactions).when(transactionRepository).findLimitedWalletTransactions("abc@gmail.com","testwallet",2);
        Assertions.assertEquals(2,transactionService.getAllTransactionsOfAWallet("abc@gmail.com","testwallet",2).size());
    }

    @Test
    public void getAllTRansactionsOfAWalletOnWalletNotAvailable(){
        List<Transactions> transactions = Arrays.asList(
                new Transactions(3,"test@gmail.com","testwallet","testcategory2",100,LocalDate.parse("2021-10-10")),
                new Transactions(4,"test@gmail.com","testwallet","testcategory",300,LocalDate.parse("2021-10-05"))
        );
        doReturn(transactions).when(transactionRepository).findLimitedWalletTransactions("abc@gmail.com","testwallet",2);
        Assertions.assertEquals(0,transactionService.getAllTransactionsOfAWallet("abc@gmail.com","testwallet2",2).size());
    }

    @Test
    public void getEveryInfoTest(){
        Mockito.doReturn(Double.valueOf(3000)).when(restTemplate).getForObject("http://WALLET-CATEGORY/wallet/abc@gmail.com/balance", Double.class);
        Mockito.doReturn(Arrays.asList(
                new Transactions(3,"abc@gmail.com","testwallet","testcategory2",100,LocalDate.now(ZoneId.of("Asia/Kolkata"))),
                new Transactions(4,"abc@gmail.com","testwallet","testcategory",300,LocalDate.now(ZoneId.of("Asia/Kolkata")))
        )).when(transactionRepository).findAllByTransactionDateAndUserEmailOrderByTransactionIdDesc(LocalDate.now(ZoneId.of("Asia/Kolkata")),"abc@gmail.com");
        Mockito.doReturn(Arrays.asList(
                new Transactions(3,"abc@gmail.com","testwallet","testcategory2",1000,LocalDate.parse("2021-10-14")),
                new Transactions(4,"abc@gmail.com","testwallet","testcategory",300,LocalDate.parse("2021-10-14"))
        )).when(transactionRepository).findTransactionsOfAMonth(LocalDate.now(ZoneId.of("Asia/Kolkata")).getMonthValue(),LocalDate.now(ZoneId.of("Asia/Kolkata")).getYear(),"abc@gmail.com");
        Mockito.doReturn(Arrays.asList(
                new Transactions(3,"abc@gmail.com","testwallet","testcategory2",1000,LocalDate.parse("2021-10-14")),
                new Transactions(4,"abc@gmail.com","testwallet","testcategory",300,LocalDate.parse("2021-10-14"))
        )).when(transactionRepository).findAllByUserEmailOrderByTransactionIdDesc("abc@gmail.com");
        AllInfo info = transactionService.getEveryInfo("abc@gmail.com");
        Assertions.assertEquals(3000,info.getWalletsBalance());
        Assertions.assertEquals(1300,info.getTotalSpent());
        Assertions.assertEquals(1300,info.getYearlySpent());
        Assertions.assertEquals(1300,info.getMonthlySpent().getAmount());
        Assertions.assertEquals(400,info.getTodaySpent().getAmount());
    }

    @Test
    public void getAllTransactionsOfAWalletBetweenDatesTest(){
        List<Transactions> transactions= Arrays.asList(
                new Transactions(3,"test@gmail.com","testwallet","testcategory2",1000,LocalDate.parse("2021-10-12")),
                new Transactions(4,"test@gmail.com","testwallet","testcategory",300,LocalDate.parse("2021-10-13")),
                new Transactions(5,"test@gmail.com","testwallet","testcategory3",300,LocalDate.parse("2021-10-11"))
        );
        Mockito.doReturn(transactions).when(transactionRepository).findAllByUserEmailAndWalletNameAndTransactionDateBetweenOrderByTransactionDateDesc("abc@gmail.com","testwallet",LocalDate.parse("2021-10-11"),LocalDate.parse("2021-10-13"));
        Assertions.assertEquals(3,transactionService.getAllWalletTransactionsBetweenDates("abc@gmail.com","testwallet","2021-10-11","2021-10-13").size());
    }

    @Test void getAllTransactionsOfACategoryBetweenDatesTest(){
        List<Transactions> transactions= Arrays.asList(
                new Transactions(3,"test@gmail.com","testwallet","testcategory",1000,LocalDate.parse("2021-10-12")),
                new Transactions(4,"test@gmail.com","testwallet2","testcategory",300,LocalDate.parse("2021-10-13")),
                new Transactions(5,"test@gmail.com","testwallet4","testcategory",300,LocalDate.parse("2021-10-11"))
        );
        Mockito.doReturn(transactions).when(transactionRepository).findAllByUserEmailAndCategoryNameAndTransactionDateBetweenOrderByTransactionDateDesc("abc@gmail.com","testcategory",LocalDate.parse("2021-10-11"),LocalDate.parse("2021-10-13"));
        Assertions.assertEquals(3,transactionService.getAllCategoryTransactionsBetweenDates("abc@gmail.com","testcategory","2021-10-11","2021-10-13").size());
    }

    @Test
    public void getSumOfTransactionsOnADateTest(){
        List<Transactions> transactions= Arrays.asList(
                new Transactions(3,"test@gmail.com","testwallet","testcategory",1000,LocalDate.parse("2021-10-13")),
                new Transactions(4,"test@gmail.com","testwallet2","testcategory",300,LocalDate.parse("2021-10-13")),
                new Transactions(5,"test@gmail.com","testwallet4","testcategory",300,LocalDate.parse("2021-10-13"))
        );
        Mockito.doReturn(transactions).when(transactionRepository).findAllByTransactionDateAndUserEmailOrderByTransactionIdDesc(LocalDate.parse("2021-10-13"),"abc@gmail.com");
        Assertions.assertEquals(1600,transactionService.getSumOfTransactionsOnDate("2021-10-13","abc@gmail.com").getAmount());
    }

    @Test
    public void uploadFileTest() throws Exception {
        String heading = "date,wallet,category,amount";
        String data = "2021-10-13,Citi,Food,300";
        MockMultipartFile file = new MockMultipartFile("data", "filename.txt", "text/csv", (heading+"\r\n"+data).getBytes());
        Transactions transactions = new Transactions(4,"abc@gmail.com","Citi","Food",300,LocalDate.parse("2021-09-05"));
        Mockito.doNothing().when(restTemplate).put("http://WALLET-CATEGORY/wallet/abc@Gmail.com/Citi/debitamount?amount=300", null, String.class);
        Mockito.doNothing().when(restTemplate).put("http://WALLET-CATEGORY/category/abc@gmail.com/Food/creditamount?amount=300", null);
        Mockito.doReturn(transactions).when(transactionRepository).save(transactions);
        Assertions.assertEquals(200,transactionService.uploadFile(file,"abc@gmail.com").getStatusCodeValue());
    }

    @Test
    public void uploadFileTestForWrongFile(){
        MockMultipartFile file = new MockMultipartFile("data", "filename.txt", "text/plain", "Wrong File".getBytes());
        Assertions.assertEquals(400,transactionService.uploadFile(file,"abc@gmail.com").getStatusCodeValue());
    }

    @Test
    public void uploadFileTestForWrongData(){
        MockMultipartFile file = new MockMultipartFile("data", "filename.txt", "text/csv", "Wrong File\r\nkhahdgah".getBytes());
        Assertions.assertEquals(400,transactionService.uploadFile(file,"abc@gmail.com").getStatusCodeValue());
    }

    @Test
    public void getSumOfAllTransactionsBetweenDatesSum(){
        List<Transactions> transactions= Arrays.asList(
                new Transactions(3,"test@gmail.com","testwallet","testcategory",1000,LocalDate.parse("2021-10-11")),
                new Transactions(4,"test@gmail.com","testwallet2","testcategory",300,LocalDate.parse("2021-10-12")),
                new Transactions(5,"test@gmail.com","testwallet4","testcategory",300,LocalDate.parse("2021-10-13"))
        );
        Mockito.doReturn(transactions).when(transactionRepository).findAllByTransactionDateBetweenAndUserEmailOrderByTransactionIdDesc(LocalDate.parse("2021-10-11"),LocalDate.parse("2021-10-13"),"abc@gmail.com");
        MultipleDaysTransactionSum sum =transactionService.getSumOfAllTransactionsBetweenDates("2021-10-11","2021-10-13","abc@gmail.com");
        Assertions.assertEquals(1600,sum.getAmount());
    }

    @Test
    public void downloadCSVTest(){
        List<Transactions> transactions= Arrays.asList(
                new Transactions(3,"test@gmail.com","testwallet","testcategory",1000,LocalDate.parse("2021-10-11")),
                new Transactions(4,"test@gmail.com","testwallet2","testcategory",300,LocalDate.parse("2021-10-12")),
                new Transactions(5,"test@gmail.com","testwallet4","testcategory",300,LocalDate.parse("2021-10-13"))
        );
        Mockito.doReturn(transactions).when(transactionRepository).findAllByTransactionDateBetweenAndUserEmailOrderByTransactionIdDesc(LocalDate.parse("2021-10-11"),LocalDate.parse("2021-10-13"),"abc@gmail.com");
        Assertions.assertEquals(200,transactionService.dowloadCSV("abc@gmail.com","2021-10-11","2021-10-13").getStatusCodeValue());
    }




}
