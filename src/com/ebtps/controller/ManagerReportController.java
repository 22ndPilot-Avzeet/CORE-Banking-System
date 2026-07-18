package com.ebtps.controller;

import com.ebtps.dao.ReportDAO;
import com.ebtps.dao.ReportDAOImpl;

import java.util.Map;

public class ManagerReportController {

    private final ReportDAO reportDAO;

    public ManagerReportController() {
        this.reportDAO = new ReportDAOImpl();
    }

    public Map<String, String> getSystemReport() {
        return reportDAO.getManagerSystemReport();
    }
}
