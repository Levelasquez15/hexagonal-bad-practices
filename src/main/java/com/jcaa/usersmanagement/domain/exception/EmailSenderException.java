package com.jcaa.usersmanagement.domain.exception;

public final class EmailSenderException extends DomainException {

  // Clean Code - Regla 9: constructores privados forzando el uso de factorías estáticas.
  private EmailSenderException(final String message) {
    super(message);
  }

  private EmailSenderException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static EmailSenderException becauseSmtpFailed(
      final String destinationEmail, final String smtpError) {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — debe ser una constante.
    return new EmailSenderException(
        String.format("No se pudo enviar el correo a '%s'. Error SMTP: %s", destinationEmail, smtpError));
  }

  public static EmailSenderException becauseSendFailed(final Throwable cause) {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — debe ser una constante.
    return new EmailSenderException("La notificación por correo no pudo ser enviada.", cause);
  }
}
