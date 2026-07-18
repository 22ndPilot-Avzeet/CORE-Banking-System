package com.ebtps.controller;

import com.ebtps.authentication.SessionManager;
import com.ebtps.dao.ReportDAO;
import com.ebtps.dao.ReportDAOImpl;

import java.util.Map;

public class CustomerAnalyticsController {

    private final ReportDAO reportDAO;

    public CustomerAnalyticsController() {
        this.reportDAO = new ReportDAOImpl();
    }

    public Map<String, Double> getExpenseAnalytics() {
        int userId = SessionManager.getInstance().getCurrentUser().getId();
        return reportDAO.getCustomerExpenseAnalytics(userId);
    }
}
