package com.openclassrooms.safetynet.repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetynet.domain.FireStation;

import jakarta.annotation.PostConstruct;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@Service
public class FireStationRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<FireStation> fireStations = new ArrayList<>();

    /**
     * Loads fire station data from data.json file during application initialization.
     * Populates the fireStations list with all fire station mappings.
     * 
     * @throws RuntimeException if data.json is not found or cannot be parsed
     */
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

    /**
     * Retrieves a fire station mapping by address.
     * 
     * @param addressToMatch the address to search for (case-insensitive)
     * @return the FireStation object if found, null otherwise
     */
    public FireStation readFireStation(String addressToMatch) {
        for (FireStation dbFireStation : fireStations) {
            String dbFireStationAddress = dbFireStation.address().toLowerCase();
            if (dbFireStationAddress.equals(addressToMatch.toLowerCase())) {
                return dbFireStation;
            }
        }
        return null;
    }

    /**
     * Creates a new fire station mapping and persists it to data.json.
     * 
     * @param newFireStation the FireStation object to create
     * @return the created FireStation object
     * @throws RuntimeException if data.json cannot be read or written
     */
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

    /**
     * Updates an existing fire station mapping with new data and persists changes to data.json.
     * 
     * @param address the address of the fire station to update (case-insensitive)
     * @param newFireStation the FireStation object containing updated information
     * @return true if the fire station was found and updated, false otherwise
     * @throws RuntimeException if data.json cannot be read or written
     */
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

    /**
     * Deletes a fire station mapping by address and persists changes to data.json.
     * 
     * @param address the address of the fire station to delete (case-insensitive)
     * @return true if the fire station was found and deleted, false otherwise
     * @throws RuntimeException if data.json cannot be read or written
     */
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

    /**
     * Helper method to remove a fire station from the in-memory list.
     * 
     * @param addressOfStationToRemove the address of the fire station to remove (case-insensitive)
     * @return true if the fire station was found and removed, false otherwise
     */
    private boolean removeFireStationFromFireStations(String addressOfStationToRemove) {
        boolean fireStationFound = false;
        Iterator<FireStation> iterator = fireStations.iterator();
        while (iterator.hasNext()) {
            FireStation dbFireStation = iterator.next();
            if (dbFireStation.address().toLowerCase().equals(addressOfStationToRemove.toLowerCase())) {
                iterator.remove();
                fireStationFound = true;
                break;
            }
        }
        return fireStationFound;
    }
}
