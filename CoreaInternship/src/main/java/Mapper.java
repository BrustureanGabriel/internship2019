package main.java;

import main.java.dto.EmployeeDTO;
import main.java.dto.SuspensionDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mapper {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static EmployeeDTO mapEmployee(String fileContent) {
        try {
            EmployeeDTO result = new EmployeeDTO();
            JSONObject jsonFile = new JSONObject(fileContent);
            JSONObject employeeData = jsonFile.getJSONObject("employeeData");
            String employmentStartDate = employeeData.getString("employmentStartDate");
            String employmentEndDate = employeeData.getString("employmentEndDate");
            result.setStartDate(mapDate(employmentStartDate));
            result.setEndDate(mapDate(employmentEndDate));
            result.setSuspensionDTOList(mapSuspensions(employeeData));
            return result;
        } catch (Exception e) {
            return new EmployeeDTO(e.getMessage());
        }
    }

    private static List<SuspensionDTO> mapSuspensions(JSONObject employeeData) {
        try {
            JSONArray suspensionPeriodList = (JSONArray) employeeData.get("suspensionPeriodList");
            List<SuspensionDTO> suspensionDTOList = new ArrayList<>();
            for (int i = 0; i < suspensionPeriodList.length(); i++) {
                suspensionDTOList.add(mapSuspension(suspensionPeriodList.getJSONObject(i)));
            }
            return suspensionDTOList;
        } catch (JSONException e) {
            return null ;
        }
    }

    private static SuspensionDTO mapSuspension(JSONObject suspensionData) {
        try {
            String startDateString = suspensionData.getString("startDate");
            String endDateString = suspensionData.getString("endDate");
            Date startDate = mapDate(startDateString);
            Date endDate = mapDate(endDateString);
            return new SuspensionDTO(startDate, endDate);
        } catch (Exception e) {
            return new SuspensionDTO(e.getMessage());
        }
    }

    public static Date mapDate(String startDateString) {
        try{
            return new SimpleDateFormat(DATE_FORMAT).parse(startDateString);
        } catch (ParseException e){
            return null;
        }
    }
}
