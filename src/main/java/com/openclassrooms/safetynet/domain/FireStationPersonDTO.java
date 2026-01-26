package com.openclassrooms.safetynet.domain;

import java.util.List;

public record FireStationPersonDTO(
  List<CoveredPersonDTO> coveredPersons,
  int adultCount,
  int childCount
) {}
