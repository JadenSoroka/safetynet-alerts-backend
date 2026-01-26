package com.openclassrooms.safetynet.domain;

import java.util.List;
import java.util.Map;

public record FloodResponseDTO(
  String stationNumber,
  Map<String, List<FloodPersonDTO>> households
) {}
