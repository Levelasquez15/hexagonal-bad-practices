package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidUserIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Tests para el Value Object UserId")
class UserIdTest {

  @ParameterizedTest
  @DisplayName("Debe crear UserId eliminando espacios en los extremos")
  @ValueSource(strings = {" user123 ", "  user123  ", "user123\t"})
  void shouldCreateUserIdWithTrimmedValue(String input) {
    // Arrange
    final String correctUserId = "user123";
    
    // Act
    final UserId userId = new UserId(input);
    
    // Assert
    assertEquals(correctUserId, userId.toString());
  }

  @Test
  @DisplayName("Debe lanzar NullPointerException cuando UserId es nulo")
  void shouldThrowNullPointerExceptionWhenUserIdIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> new UserId(null));
  }

  @ParameterizedTest
  @DisplayName("Debe lanzar InvalidUserIdException cuando UserId está vacío o tiene solo espacios")
  @ValueSource(strings = {"", "   ", "\t", "\n", "\r", "\f", "\b"})
  void shouldThrowIllegalArgumentExceptionWhenUserIdIsEmpty(String input) {
    // Act & Assert
    assertThrows(InvalidUserIdException.class, () -> new UserId(input));
  }
}
