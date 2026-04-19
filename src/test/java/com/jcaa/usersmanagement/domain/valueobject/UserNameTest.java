package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidUserNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Tests para el Value Object UserName")
class UserNameTest {

  @ParameterizedTest
  @DisplayName("Debe validar el nombre y eliminar espacios duplicados en los extremos")
  @ValueSource(strings = {"John Arrieta", "   John Arrieta   ", "John Arrieta \t"})
  void shouldValidateUserNameMinimumLength(final String userName) {
    // Arrange
    final String correctUserName = "John Arrieta";
    
    // Act
    final UserName userNameVo = new UserName(userName);
    
    // Assert
    assertEquals(correctUserName, userNameVo.toString());
  }

  // -- Flujo con excepciones y ramas de validación ---

  @Test
  @DisplayName("Debe lanzar NullPointerException cuando el nombre es nulo")
  void shouldValidateUserNameIsNotNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> new UserName(null));
  }

  @ParameterizedTest
  @DisplayName("Debe lanzar InvalidUserNameException cuando el nombre es vacío o no tiene la longitud mínima")
  @ValueSource(
      strings = {"", "  ", "\t", "\n", "\r", "\f", "\b", "Jo", "Ty  ", "", "   Cy ", "Ed\t"})
  void shouldValidateUserNameIsNotEmptyAndMinimumLength(final String userName) {
    // Act & Assert
    assertThrows(InvalidUserNameException.class, () -> new UserName(userName));
  }
}
