package com.employees.request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProjectRequest {
    public Long projectId;
    public Long employeeId;
    public LocalDate dateFrom;
    public LocalDate dateTo;

    public ProjectRequest(Long employeeId, Long projectId, LocalDate dateFrom, LocalDate dateTo) {
        this.projectId = projectId;
        this.employeeId = employeeId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public static ProjectRequest fromString(String request, String dateFormat) {
        final String[] requestParts = request.trim().split(",");
        if(requestParts.length != 4) {
            throw new RuntimeException("Invalid CSV file is uploaded");
        }

        final Long employeeId = Long.parseLong(requestParts[0].startsWith("\uFEFF")
                ? requestParts[0].substring(1)
                : requestParts[0]);
        final LocalDate dateTo = requestParts[3].toLowerCase().equals("null")
                ? LocalDate.now()
                : LocalDate.parse(requestParts[3], DateTimeFormatter.ofPattern(dateFormat));

        return new ProjectRequest(employeeId,
                Long.parseLong(requestParts[1]),
                LocalDate.parse(requestParts[2], DateTimeFormatter.ofPattern(dateFormat)),
                dateTo);
    }

}
