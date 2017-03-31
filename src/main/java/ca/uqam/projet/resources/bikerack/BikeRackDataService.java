package ca.uqam.projet.resources.bikerack;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BikeRackDataService {

    private static final String ID_HEADER = "INV_ID";
    private static final String MARQUE_HEADER = "MARQ";
    private static final String LATITUDE_HEADER = "LAT";
    private static final String LONGITUDE_HEADER = "LONG";
    private static Map<String, Integer> headers = new HashMap<String, Integer>();

    public static List<BikeRack> fetchDataSet(String url) {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        return ParseBikeRacks(result);
    }

    private static List<BikeRack> ParseBikeRacks(String bikeRacksDocument) {
        ArrayList<BikeRack> bikeRackDataList = new ArrayList<>();

        try {
            StringReader reader = new StringReader(bikeRacksDocument);
            CSVReader csvReader = new CSVReader(reader);
            String[] nextLine;
            int currentLine = 0;
            while ((nextLine = csvReader.readNext()) != null) {
                if (currentLine == 0) {
                    FillHeaderInfo(nextLine);
                } else {
                    BikeRackData rackData = ParseBikeRackData(nextLine);

                    bikeRackDataList.add(new BikeRack(rackData.getId(),
                            rackData.getMarque(), rackData.getLongitude(), rackData.getLatitude()));
                }
                currentLine++;
            }
        } catch (IOException ex) {
            // This should not happen
            System.err.println(ex.getMessage());
        }
        return bikeRackDataList;
    }

    private static void FillHeaderInfo(String[] headerLine) {
        headers.clear();
        for (int i = 0; i < headerLine.length; i++) {
            headers.put(headerLine[i], i);
        }
    }

    private static BikeRackData ParseBikeRackData(String[] line) {
        BikeRackData rack = new BikeRackData();
        rack.setId(Integer.parseInt(line[headers.get(ID_HEADER)]));
        rack.setMarque(line[headers.get(MARQUE_HEADER)]);
        rack.setLongitude(Double.parseDouble(line[headers.get(LONGITUDE_HEADER)]));
        rack.setLatitude(Double.parseDouble(line[headers.get(LATITUDE_HEADER)]));
        return rack;
    }
}
