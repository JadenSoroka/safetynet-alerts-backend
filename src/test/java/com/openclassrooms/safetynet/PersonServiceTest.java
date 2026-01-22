package com.openclassrooms.safetynet;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.safetynet.service.SafetyNetService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PersonServiceTest {

  @Mock
  private SafetyNetService personService;
}
