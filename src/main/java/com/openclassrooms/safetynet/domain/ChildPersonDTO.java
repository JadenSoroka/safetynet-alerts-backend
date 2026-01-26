package com.openclassrooms.safetynet.domain;

import java.util.List;

public record ChildPersonDTO(
  String firstName,
  String lastName,
  int age,
  List<String> familyMembers
) {}
