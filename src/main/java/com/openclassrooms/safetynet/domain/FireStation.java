package com.openclassrooms.safetynet.domain;

public class FireStation {
  String address;
  String stationNumber;

  public FireStation(String address, String station) {
    this.address = address;
    stationNumber = station;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getStationNumber() {
    return stationNumber;
  }

  public void setStationNumber(String stationNumber) {
    this.stationNumber = stationNumber;
  }

}
