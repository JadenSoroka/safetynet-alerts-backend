package com.openclassrooms.safetynet.repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.domain.Person;

import jakarta.annotation.PostConstruct;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@Service
public class FireStationRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<FireStation> fireStations = new ArrayList<>();

    @PostConstruct
    public void loadPersons() {
        try (InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream("data.json")) {
            if (inputStream == null) {
            throw new RuntimeException("data.json not found");
            }
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode fireStationsNode = rootNode.get("firestations");
            
            // Get fire stations
            for (JsonNode fireStationNode : fireStationsNode) {
            FireStation fireStation = new FireStation(
                fireStationNode.get("address").asString(),
                fireStationNode.get("station").asString()
            );
            fireStations.add(fireStation);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load data.json", e);
        }
    }

    public FireStation findFireStationByAddress(String address) {
        FireStation currentFireStations = null;

        for (FireStation fireStation : fireStations) {
            if (fireStation.address().equals(address)) {
                currentFireStations = fireStation;
                break;
            }
        }

        return currentFireStations;
    }

    public FireStation createFireStation(FireStation newFireStation) {
        try (InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream("data.json")) {
            if (inputStream == null) {
            throw new RuntimeException("data.json not found");
            }
            JsonNode rootNode = objectMapper.readTree(inputStream);
            
            ObjectNode rootObjectNode = (ObjectNode) rootNode;

            fireStations.add(newFireStation);
            
            rootObjectNode.putPOJO("firestations", fireStations);
            objectMapper.writeValue(new File("src/main/resources/data.json"), rootObjectNode);
            return newFireStation;
        } catch (IOException e) {
        throw new RuntimeException("Failed to load data.json", e);
        }    
    }

    public boolean updateFireStation(String address, FireStation newFireStation) {
        try (InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream("data.json")) {
            if (inputStream == null) {
            throw new RuntimeException("data.json not found");
            }
            JsonNode rootNode = objectMapper.readTree(inputStream);
            
            ObjectNode rootObjectNode = (ObjectNode) rootNode;

            boolean fireStationFound = removeFireStationFromFireStations(address);
            if (!fireStationFound) {
                return false;
            }
            fireStations.add(newFireStation);
            
            rootObjectNode.putPOJO("firestations", fireStations);
            objectMapper.writeValue(new File("src/main/resources/data.json"), rootObjectNode);
            return true;
        } catch (IOException e) {
        throw new RuntimeException("Failed to load data.json", e);
        }    
    }

    public boolean deleteFireStation(String address) {
        try (InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream("data.json")) {
            if (inputStream == null) {
            throw new RuntimeException("data.json not found");
            }
            JsonNode rootNode = objectMapper.readTree(inputStream);
            
            ObjectNode rootObjectNode = (ObjectNode) rootNode;

            boolean fireStationFound = removeFireStationFromFireStations(address);
            if (!fireStationFound) {
                return false;
            }
            
            rootObjectNode.putPOJO("firestations", fireStations);
            objectMapper.writeValue(new File("src/main/resources/data.json"), rootObjectNode);
            return true;
        } catch (IOException e) {
        throw new RuntimeException("Failed to load data.json", e);
        }  
    }

    private boolean removeFireStationFromFireStations(String addressOfStationToRemove) {
        boolean fireStationFound = false;
        Iterator<FireStation> iterator = fireStations.iterator();
        while (iterator.hasNext()) {
            FireStation dbFireStation = iterator.next();
            if (dbFireStation.address().toLowerCase().equals(addressOfStationToRemove)) {
                iterator.remove();
                fireStationFound = true;
                break;
            }
        }
        return fireStationFound;
    }
}
