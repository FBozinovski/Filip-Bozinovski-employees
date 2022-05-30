package com.employees.response;

import java.util.List;

public class EmployeePairResponse {
    public Long firstEmployeeId;
    public Long secondEmployeeId;
    public Long daysWorked;
    public List<ProjectResponse> projectResponseList;

    public EmployeePairResponse(Long firstEmployeeId, Long secondEmployeeId, Long daysWorked,
                                List<ProjectResponse> projectResponseList) {
        this.firstEmployeeId = firstEmployeeId;
        this.secondEmployeeId = secondEmployeeId;
        this.daysWorked = daysWorked;
        this.projectResponseList = projectResponseList;
    }

    public Long getFirstEmployeeId() {
        return firstEmployeeId;
    }

    public Long getSecondEmployeeId() {
        return secondEmployeeId;
    }

    public Long getDaysWorked() {
        return daysWorked;
    }

    public List<ProjectResponse> getProjectResponseList() {
        return projectResponseList;
    }

}
