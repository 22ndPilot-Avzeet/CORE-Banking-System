package com.ebtps.dao;

import com.ebtps.model.Transaction;
import java.util.List;

public interface TransactionDAO {
    List<Transaction> getTransactionsByAccount(String accountNumber);
    // Note: The actual insertion of a transaction happens inside the atomic TransactionService
}
