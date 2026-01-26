package com.openclassrooms.safetynet.domain;

import java.util.List;

public record FireResponseDTO(
  String stationNumber,
  List<FirePersonDTO> persons
) {}
