package com.ebtps.dao;

import com.ebtps.model.Account;

public interface ManagerDAO {
    Account getAccountByNumber(String accountNumber);
    boolean updateAccountStatus(String accountNumber, String status);
}
