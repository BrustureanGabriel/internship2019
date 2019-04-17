package main.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import main.java.dto.EmployeeDTO;
import main.java.dto.SuspensionDTO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Processor {

    public static void proccess(EmployeeDTO employeeDTO) {
        Map<Integer, Integer> holidaysPerYear = new TreeMap<>();
        Date firstYear = Mapper.mapDate((employeeDTO.getStartDate().getYear() + 1900) + "-12-31");
        Date lastYear = Mapper.mapDate((employeeDTO.getEndDate().getYear() + 1900) + "-01-01");
        List<SuspensionDTO> result = expandSuspensionList(employeeDTO.getSuspensionDTOList());
        int mandatoryHolidays = 20;
        System.out.println(firstYear);
        for (int i = employeeDTO.getStartDate().getYear(); i <= employeeDTO.getEndDate().getYear(); i++) {
            int holidaysDays = 0;
            if (i == employeeDTO.getStartDate().getYear()) {
                if (firstYear.getYear() % 4 != 0)
                    holidaysDays += (firstYear.getTime() - employeeDTO.getStartDate().getTime()) / 86400000 * mandatoryHolidays / 365;
                else
                    holidaysDays += (firstYear.getTime() - employeeDTO.getStartDate().getTime()) / 86400000 * mandatoryHolidays / 366;
            } else if (i == employeeDTO.getEndDate().getYear()) {
                if (i % 4 != 0)
                    holidaysDays += (employeeDTO.getEndDate().getTime() - lastYear.getTime()) / 86400000 * (mandatoryHolidays + i - firstYear.getYear()) / 365;
                else
                    holidaysDays += (employeeDTO.getEndDate().getTime() - lastYear.getTime() / 86400000 * (mandatoryHolidays + i - firstYear.getYear()) / 366);
            } else if (mandatoryHolidays < 24) holidaysDays = mandatoryHolidays + (i - firstYear.getYear());
            else holidaysDays = mandatoryHolidays;
            for (int j = 0; j < result.size(); j++) {
                if (i == result.get(j).getStartDate().getYear()) {
                    if (i % 4 != 0)
                        holidaysDays = (int) (holidaysDays - ((result.get(j).getEndDate().getTime() - result.get(j).getStartDate().getTime()) / 86400000 * (mandatoryHolidays + (i - firstYear.getYear())) / 365));
                    else
                        holidaysDays = (int) (holidaysDays - ((result.get(j).getEndDate().getTime() - result.get(j).getStartDate().getTime()) / 86400000 * (mandatoryHolidays + (i - firstYear.getYear())) / 366));
                }
            }
            if (holidaysDays > 24) holidaysDays = 24;
            holidaysPerYear.put(1900 + i, holidaysDays);
        }
        for (Map.Entry<Integer, Integer> entry : holidaysPerYear.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        String message="  ";
        if (employeeDTO.getErrorMessage()==""){message+="no employee error"+"; ";}
        else message+=new EmployeeDTO(employeeDTO.getErrorMessage()).toString()+"; ";
        for (SuspensionDTO suspension:result)
        {
        if (suspension.getErrorMessage()==""){message+="no suspension error"+"; ";}
        else message+=new SuspensionDTO(suspension.getErrorMessage()).toString()+"; ";
        }
        write(message, holidaysPerYear);
    }

    private static void write(String message, Map<Integer, Integer> map) {
        try {

            JSONArray holidayRightsPerYearList = new JSONArray();
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                JSONObject holidayRightsPerYear = new JSONObject();
                holidayRightsPerYear.put("year", entry.getKey().toString());
                holidayRightsPerYear.put("holidayDays", entry.getValue().toString());
                holidayRightsPerYearList.put(holidayRightsPerYear);
            }
            JSONObject jsonString = new JSONObject().put("output", new JSONObject()
                    .put("errorMessage", message)
                    .put("holidayRightsPerYearList", holidayRightsPerYearList));
            System.out.println(jsonString);
            File file = new File("output.json");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(toPrettyFormat(jsonString.toString()));
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String toPrettyFormat(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }

    private static List<SuspensionDTO> expandSuspensionList(List<SuspensionDTO> suspensionDTOList) {
        List<SuspensionDTO> result = new ArrayList<>();
        suspensionDTOList.forEach(suspensionDTO -> {
            if (suspensionDTO.getStartDate().getYear() == suspensionDTO.getEndDate().getYear()) {
                result.add(suspensionDTO);
            } else {
                int years = suspensionDTO.getEndDate().getYear() - suspensionDTO.getStartDate().getYear();
                for (int i = 0; i <= years; i++) {
                    if (i == 0) {
                        result.add(new SuspensionDTO(suspensionDTO.getStartDate(),
                                Mapper.mapDate(suspensionDTO.getStartDate().getYear() + 1900 + "-12-" + "31")));
                    } else if (i == years) {
                        result.add(new SuspensionDTO(Mapper.mapDate(suspensionDTO.getEndDate().getYear() + 1900 + "-01-" + "01"),
                                suspensionDTO.getEndDate()));
                    } else {
                        String startDate = String.valueOf(suspensionDTO.getStartDate().getYear() + 1900 + i) + "-01-" + "01";
                        String endDate = String.valueOf(suspensionDTO.getStartDate().getYear() + 1900 + i) + "-12-" + "31";
                        result.add(new SuspensionDTO(Mapper.mapDate(startDate),
                                Mapper.mapDate(endDate)));
                    }
                }
            }
        });
        return result;
    }
}
