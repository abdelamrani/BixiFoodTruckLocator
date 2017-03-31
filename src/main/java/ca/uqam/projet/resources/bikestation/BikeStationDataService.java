package ca.uqam.projet.resources.bikestation;

import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BikeStationDataService {

    public static List<BikeStation> fetchDataSet(String url) throws BikeStationDataParseException {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        return parseXmlDocument(result);
    }

    private static List<BikeStation> parseXmlDocument(String xmlDocument) throws BikeStationDataParseException {

        ArrayList<BikeStation> bikeStationDataList = new ArrayList<>();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = docFactory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xmlDocument.getBytes("ISO-8859-1")));
            NodeList nodeList = document.getDocumentElement().getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                BikeStationData bikeStationData = parseBikeStationElement((Element) nodeList.item(i));
                if (bikeStationData.getIsInstalled()) {
                    bikeStationDataList.add(createBikeStation(bikeStationData));
                }
            }
        } catch (IOException ex) {
            // This should never happen
            System.err.println(ex.getMessage());
        } catch (ParserConfigurationException | SAXException ex) {
            throw new BikeStationDataParseException(ex.getMessage());
        } finally {
            return bikeStationDataList;
        }
    }

    private static BikeStationData parseBikeStationElement(Element element) {

        BikeStationData bikeStationData = new BikeStationData();
        bikeStationData.setId(Integer.parseInt(element.getElementsByTagName("terminalName").item(0).getChildNodes().item(0).getNodeValue()));
        bikeStationData.setName(element.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue());
        bikeStationData.setIsInstalled(Boolean.parseBoolean(element.getElementsByTagName("installed").item(0).getChildNodes().item(0).getNodeValue()));
        bikeStationData.setIsLocked(Boolean.parseBoolean(element.getElementsByTagName("locked").item(0).getChildNodes().item(0).getNodeValue()));
        bikeStationData.setIsPublic(Boolean.parseBoolean(element.getElementsByTagName("public").item(0).getChildNodes().item(0).getNodeValue()));
        bikeStationData.setNbBikes(Integer.parseInt(element.getElementsByTagName("nbBikes").item(0).getChildNodes().item(0).getNodeValue()));
        bikeStationData.setNbEmptyDocks(Integer.parseInt(element.getElementsByTagName("nbEmptyDocks").item(0).getChildNodes().item(0).getNodeValue()));
        bikeStationData.setLastUpdateTime(Long.parseLong(element.getElementsByTagName("lastUpdateTime").item(0).getChildNodes().item(0).getNodeValue()));
        bikeStationData.setLongitude(Double.parseDouble(element.getElementsByTagName("long").item(0).getChildNodes().item(0).getNodeValue()));
        bikeStationData.setLatitude(Double.parseDouble(element.getElementsByTagName("lat").item(0).getChildNodes().item(0).getNodeValue()));
        return bikeStationData;
    }

    private static BikeStation createBikeStation(BikeStationData data) {
        return new BikeStation(data.getId(), data.getName(), data.getNbBikes(), data.getNbEmptyDocks(), data.getLongitude(), data.getLatitude());
    }
}
