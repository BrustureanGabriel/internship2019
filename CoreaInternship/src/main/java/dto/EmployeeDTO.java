package main.java.dto;

import java.util.Date;
import java.util.List;

public class EmployeeDTO {

    private Date startDate;
    private Date endDate;
    private List<SuspensionDTO> suspensionDTOList;
    private String errorMessage;

    public EmployeeDTO() {
    }

    public EmployeeDTO(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<SuspensionDTO> getSuspensionDTOList() {
        return suspensionDTOList;
    }

    public void setSuspensionDTOList(List<SuspensionDTO> suspensionDTOList) {
        this.suspensionDTOList = suspensionDTOList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
