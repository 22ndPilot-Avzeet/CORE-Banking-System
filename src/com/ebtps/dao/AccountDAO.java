package com.ebtps.dao;

import com.ebtps.model.Account;
import java.util.List;

/**
 * Data Access Object Interface for Account operations.
 */
public interface AccountDAO {
    /**
     * Retrieves all accounts associated with a specific user.
     * @param userId The ID of the user.
     * @return A list of Account objects.
     */
    List<Account> getAccountsByUserId(int userId);
}
