package it.tests.pdfmanager;

import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PdfFileInspector {

    public Map<String, String> getFileFields(File file){
        Map<String, String> valuesMap = new HashMap<>();

        if(ObjectUtils.isNotEmpty(file)) {
            if(file.getName().startsWith("end_contract")) {
                fillEndContract(valuesMap);
            } else if(file.getName().startsWith("start_contract")) {
                fillStartContract(valuesMap);
            }
        }

        return valuesMap;
    }

    private void fillStartContract(Map<String, String> valuesMap) {
        valuesMap.put("place", "Duomo di Milano");
        valuesMap.put("recipient", "Mario Rossi\nPiazza del Duomo\n20122 Milano");
        valuesMap.put("customername", "Mario Rossi");
        valuesMap.put("phone", "340 0000000");
        valuesMap.put("emailcustomer", "mario.rossi@test.it");
        valuesMap.put("address", "Piazza del Duomo");
        valuesMap.put("zipcode", "20122");
        valuesMap.put("city", "Milano");
        valuesMap.put("country", "Italia");
        valuesMap.put("contractnr", "Contratto test");
        valuesMap.put("services", "- Servizio myIveco#1\n- Servizio myIveco#2\n- Servizio myIveco#3\n- Servizio myIveco#4");
        valuesMap.put("payfrequency", "Decennale");
        valuesMap.put("ibancustomer", "XX00X0000000000000000000000");
        valuesMap.put("signatory", "Marco Bianchi");
        valuesMap.put("biccustomer", "00000000");
        valuesMap.put("vehiclesdata", "Dati del veicolo");
    }

    private void fillEndContract(Map<String, String> valuesMap) {
        valuesMap.put("signatory", "Mario Rossi");
    }

}
