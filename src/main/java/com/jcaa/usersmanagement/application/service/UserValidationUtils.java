package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.enums.UserRole;

/**
 * Clean Code - Regla 13 (evitar clases utilitarias innecesarias):
 * Esta clase "Utils" agrupa métodos que en realidad pertenecen a sus respectivos objetos
 * de dominio (UserModel, UserRole, UserStatus) o a los servicios que los usan.
 *
 * La regla dice: no crear clases Utils/Helper/Manager sin una razón sólida.
 * La lógica de negocio vive en los objetos de negocio, no en utilitarios genéricos.
 * Una clase llamada "UserValidationUtils" es señal de:
 *   - diseño pobre
 *   - lógica mal ubicada
 *   - falta de encapsulación en dominio o servicios
 *
 * Clean Code - Regla 23 (minimizar conocimiento disperso):
 * Las reglas de validación de usuario están fragmentadas aquí en vez de estar
 * centralizadas en el propio UserModel o en un servicio de dominio dedicado.
 *
 * Clean Code - Regla 12 (alta cohesión real):
 * Esta clase mezcla responsabilidades que no pertenecen al mismo concepto:
 *   - Validación de estado (isUserActive)
 *   - Validación de rol (isAdmin)
 *   - Validación de formato de email (isValidEmail)
 *   - Validación de contraseña (isValidPassword)
 *   - Verificación de permisos con parámetros mixtos (canPerformAction)
 * Sus métodos no trabajan sobre un mismo concepto o responsabilidad — son un
 * "contenedor de cosas relacionadas vagamente". Eso es exactamente baja cohesión.
 */
public class UserValidationUtils {

  // Clean Code - Regla 13: la validación de si un usuario puede hacer login
  // debería vivir en UserModel.isAllowedToLogin() o en un servicio de dominio.
  public static boolean isUserActive(final UserModel user) {
    return user.getStatus() == UserStatus.ACTIVE;
  }

  // Clean Code - Regla 13: esta regla de negocio (qué roles son administradores)
  // debería encapsularse en UserRole o en un servicio de autorización, no aquí.
  public static boolean isAdmin(final UserModel user) {
    return user.getRole() == UserRole.ADMIN;
  }

  // Clean Code - Regla 13: validación que pertenece al value object UserPassword.
  // Clean Code - Regla 18 (magic numbers): el número 8 es un magic number aquí —
  // ya tiene significado en UserPassword pero se repite sin constante.
  private static final int MIN_PASSWORD_LENGTH = 8;
  
  public static boolean isValidPassword(final String password) {
    return password != null && password.length() >= MIN_PASSWORD_LENGTH;
  }

  // Clean Code - Reglas 5, 17, 18, 20 combinadas: 
  // Encapsular en objeto, método claro, extraer condiciones grandes, evitar literales.
  public static boolean canPerformAction(final UserModel user, final int maxInactivityDays) {
    if (isInvalidIdentity(user)) {
      return false;
    }
    return hasAllowedStatus(user.getStatus()) && maxInactivityDays >= 0;
  }

  private static boolean isInvalidIdentity(final UserModel user) {
    return user == null || user.getId() == null || user.getEmail() == null;
  }

  private static boolean hasAllowedStatus(final UserStatus status) {
    return UserStatus.ACTIVE.equals(status) || UserStatus.PENDING.equals(status);
  }
}


