package com.ebtps.dao;

import java.util.Map;

public interface ReportDAO {
    Map<String, Double> getCustomerExpenseAnalytics(int userId);
    Map<String, String> getManagerSystemReport();
}
