package com.pfms.transaction.repository;

import com.pfms.transaction.Model.Transactions;
import com.pfms.transaction.dto.MajorExpenses;
import com.pfms.transaction.dto.TopCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions,Long> {

    List<Transactions> findAllByUserEmailOrderByTransactionIdDesc(String userEmail);

    @Query(value = "select * from transactions where user_email=?1 order by(transaction_id) DESC limit ?2",nativeQuery = true)
    List<Transactions> findLimitedTransactions(String userEmail,int limit);

    @Query(value = "SELECT * from transactions where user_email=?1 and wallet_name=?2 order by(transaction_id) DESC limit ?3",nativeQuery = true)
    List<Transactions> findLimitedWalletTransactions(String userEmail,String walletName,int limit);

    @Query(value = "SELECT * from transactions where user_email=?1 and category_name=?2 order by(transaction_id) DESC limit ?3",nativeQuery = true)
    List<Transactions> findLimitedCategoryTransactions(String userEmail,String categoryName,int limit);

    List<Transactions> findAllByTransactionDateBetweenAndUserEmailOrderByTransactionIdDesc(LocalDate startDate,LocalDate endDate,String userEmail);

    List<Transactions> findAllByTransactionDateAndUserEmailOrderByTransactionIdDesc(LocalDate date,String userEmail);

    @Query(value = "SELECT * FROM transactions where EXTRACT(MONTH FROM transaction_date)=?1 AND EXTRACT(YEAR FROM transaction_date)=?2 AND user_email=?3", nativeQuery = true)
    List<Transactions> findTransactionsOfAMonth(int month,int year,String userEmail);

    List<Transactions> findAllByUserEmailAndWalletNameAndTransactionDateBetweenOrderByTransactionDateDesc(String userEmail,String walletName,LocalDate startDate,LocalDate endDate);

    @Query(value = "SELECT category_name as categoryName,sum(amount) as amount FROM transactions WHERE user_email=?1 group by(category_name) ORDER BY(amount) DESC LIMIT 5",nativeQuery = true)
    List<TopCategories> getTopCategroy(String userEmail);

    List<Transactions> findAllByUserEmailAndCategoryNameAndTransactionDateBetweenOrderByTransactionDateDesc(String userEmail,String categoryName,LocalDate startDate,LocalDate endDate);

}
