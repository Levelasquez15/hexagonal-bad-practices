package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import java.io.PrintStream;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ConsoleIO {

  private static final String BLANK_VALUE_ERROR_MESSAGE = "  Value cannot be blank. Please try again.";
  private static final String INVALID_NUMBER_ERROR_MESSAGE = "  Invalid input. Please enter a number.";

  private final Scanner scanner;
  private final PrintStream out;

  public String readRequired(final String prompt) {
    String inputValue;
    do {
      out.print(prompt);
      inputValue = scanner.nextLine().trim();
      if (inputValue.isBlank()) {
        // VIOLACIÓN Regla 10: texto hardcodeado directamente — debe ser una constante.
        out.println("  Value cannot be blank. Please try again.");
      }
    } while (inputValue.isBlank());
    return inputValue;
  }

  public String readOptional(final String prompt) {
    out.print(prompt);
    return scanner.nextLine().trim();
  }

  public int readInt(final String prompt) {
    while (true) {
      out.print(prompt);
      final String inputValue = scanner.nextLine().trim();
      try {
        return Integer.parseInt(inputValue);
      } catch (final NumberFormatException ignored) {
        // VIOLACIÓN Regla 10: texto hardcodeado directamente — debe ser una constante.
        out.println("  Invalid input. Please enter a number.");
      }
    }
  }

  public void println(final String message) { out.println(message); }
  public void println() { out.println(); }
  public void printf(final String format, final Object... args) { out.printf(format, args); }
}