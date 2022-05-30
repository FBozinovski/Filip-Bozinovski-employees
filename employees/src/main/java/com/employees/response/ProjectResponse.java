package com.employees.response;

public class ProjectResponse {
    public Long id;
    public Long firstEmployeeId;
    public Long secondEmployeeId;
    public Long daysWorked;

    public ProjectResponse(Long id, Long firstEmployeeId, Long secondEmployeeId, Long daysWorked) {
        this.id = id;
        this.firstEmployeeId = firstEmployeeId;
        this.secondEmployeeId = secondEmployeeId;
        this.daysWorked = daysWorked;
    }

}
