package com.jcaa.usersmanagement.domain.exception;

public final class EmailSenderException extends DomainException {

  private static final String MSG_SMTP_FAILED = "No se pudo enviar el correo a '%s'. Error SMTP: %s";
  private static final String MSG_SEND_FAILED = "La notificación por correo no pudo ser enviada.";

  // Clean Code - Regla 9: constructores privados forzando el uso de factorías estáticas.
  private EmailSenderException(final String message) {
    super(message);
  }

  private EmailSenderException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static EmailSenderException becauseSmtpFailed(
      final String destinationEmail, final String smtpError) {
    // Clean Code - Regla 10: usar constantes en lugar de texto hardcodeado.
    return new EmailSenderException(
        String.format(MSG_SMTP_FAILED, destinationEmail, smtpError));
  }

  public static EmailSenderException becauseSendFailed(final Throwable cause) {
    // Clean Code - Regla 10: usar constantes en lugar de texto hardcodeado.
    return new EmailSenderException(MSG_SEND_FAILED, cause);
  }
}
