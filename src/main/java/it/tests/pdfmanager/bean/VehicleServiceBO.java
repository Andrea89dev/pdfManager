package it.tests.pdfmanager.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class VehicleServiceBO {

    private String model;
    private String vin;
    private String plate;
    private List<String> servicesList;
    private Date serviceStartDate;
    private Date serviceEndDate;
    private String duration;
    private Double price;

}
