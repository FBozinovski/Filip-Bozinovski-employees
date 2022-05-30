package com.employees.service;

import com.employees.request.ProjectRequest;
import com.employees.response.EmployeePairResponse;
import com.employees.response.ProjectResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class FileService {

    public List<EmployeePairResponse> readFile(MultipartFile file, String dateFormat) throws IOException {
        if(!file.getContentType().equals("text/csv")) {
            throw new RuntimeException("Wrong file type is selected. Please select a .csv file");
        }
        final InputStream inputStream = file.getInputStream();
        final Map<Long, List<ProjectRequest>> projectRequests = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .map(it -> ProjectRequest.fromString(it, dateFormat))
                .collect(Collectors.groupingBy(request -> request.projectId));

        final List<ProjectResponse> projectResponses = projectRequests.keySet()
                .stream()
                .flatMap(key -> processProjectRequest(key, projectRequests.get(key)).stream())
                .collect(Collectors.toList());

        final Map<String, List<ProjectResponse>> employeePairsMap =
                projectResponses
                        .stream()
                        .collect(Collectors.groupingBy(it -> String.format("%d %d", it.firstEmployeeId, it.secondEmployeeId)));

        final List<EmployeePairResponse> employeePairResponses = employeePairsMap
                .keySet()
                .stream()
                .map(key -> mapToEmployeePairResponse(employeePairsMap.get(key)))
                .sorted(Comparator.comparing(EmployeePairResponse::getDaysWorked))
                .collect(Collectors.toList());

        final Long maxDayWorked = employeePairResponses.get(0).daysWorked;

        return employeePairResponses
                .stream()
                .filter(employeePairResponse -> employeePairResponse.getDaysWorked().equals(maxDayWorked))
                .collect(Collectors.toList());
    }

    private List<ProjectResponse> processProjectRequest(Long projectId, List<ProjectRequest> requests) {
        final List<ProjectRequest> sortedRequests = requests
                .stream()
                .sorted(Comparator.comparing(ProjectRequest::getEmployeeId))
                .collect(Collectors.toList());
        return mapToProjectResponse(projectId, sortedRequests);
    }

    private List<ProjectResponse> mapToProjectResponse(Long projectId, List<ProjectRequest> requests) {
        final List<ProjectResponse> responses = new ArrayList<>();
        for(int i = 0; i < requests.size(); i++) {
            int j = i + 1;
            final LocalDate startDateFirstEmployee = requests.get(i).getDateFrom();
            final LocalDate toDateFirstEmployee = requests.get(i).getDateTo();
            while(j < requests.size()) {
                final LocalDate startDateSecondEmployee = requests.get(j).getDateFrom();
                final LocalDate toDateSecondEmployee = requests.get(j).getDateTo();
                if((startDateFirstEmployee.isBefore(toDateSecondEmployee)) && (startDateSecondEmployee.isBefore(toDateFirstEmployee))) {
                    final LocalDate dateBegin = startDateFirstEmployee.isAfter(startDateSecondEmployee) ?
                            startDateFirstEmployee : startDateSecondEmployee;
                    final LocalDate dateEnd = toDateFirstEmployee.isBefore(toDateSecondEmployee) ?
                            toDateFirstEmployee : toDateSecondEmployee;
                    final Long days = DAYS.between(dateBegin, dateEnd);
                    responses.add(new ProjectResponse(projectId, requests.get(i).getEmployeeId(), requests.get(j).getEmployeeId(), days));
                }
                j++;
            }
        }
        return responses;
    }

    public EmployeePairResponse mapToEmployeePairResponse(List<ProjectResponse> projectResponses) {
        final Long totalDaysWorked = projectResponses.stream().mapToLong(it -> it.daysWorked).sum();
        return new EmployeePairResponse(projectResponses.get(0).firstEmployeeId,
                projectResponses.get(0).secondEmployeeId,
                totalDaysWorked,
                projectResponses);
    }

}
