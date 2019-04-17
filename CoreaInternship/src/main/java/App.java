package main.java;

import main.java.dto.EmployeeDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {

    public static void main(String[] args) {

        String fileContent = null;
        try {
            fileContent = new String(Files.readAllBytes(Paths.get("input.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        EmployeeDTO employeeDTO = Mapper.mapEmployee(fileContent);
        Processor.proccess(employeeDTO);

    }
}
