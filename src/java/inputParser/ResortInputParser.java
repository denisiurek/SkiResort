package inputParser;

import simulation.Logger;
import simulation.SimulationConfig;
import simulation.SimulationEngine;

import java.io.InputStream;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static simulation.TimeHelper.expandAbsoluteTime;

public class ResortInputParser implements InputParser {
  private final Scanner scanner;

  public ResortInputParser(InputStream inputStream) {
    this.scanner = new Scanner(inputStream);
    scanner.useLocale(Locale.ENGLISH);
  }

  @Override
  public SimulationEngine parse(Logger logger, SimulationConfig config) {
    SimulationBuilder builder = new SimulationBuilder(config, logger);

    parseSection(builder, (s, b) ->
        b.addNode(s.nextInt(), s.nextInt(), s.nextInt(), s.hasNext("s")));
    skipBlank();

    parseSection(builder, (s, b) ->
        b.addLift(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt()));
    skipBlank();

    parseSection(builder, (s, b) ->
        b.addRoute(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextDouble(), s.nextDouble()));
    skipBlank();

    parseAthleteGroups(builder);

    return builder.build();
  }

  private void parseSection(SimulationBuilder builder, LineParser lineParser) {
    int count = Integer.parseInt(scanner.nextLine().trim());
    for (int i = 0; i < count; i++) {
      try {
        lineParser.parse(scanLine(), builder);
      } catch (NoSuchElementException e) {
        throw new IncorrectFormattingException("Incorrect formatting", i);
      }
    }
  }

  private void skipBlank() {
    scanner.nextLine();
  }

  private Scanner scanLine() {
    Scanner s = new Scanner(scanner.nextLine());
    s.useLocale(Locale.ENGLISH);
    return s;
  }

  private int parseTime(Scanner s) {
    s.useDelimiter("[:\\s]+");
    int time = expandAbsoluteTime(s.nextInt(), s.nextInt(), s.nextInt());
    s.useDelimiter("\\s+");
    return time;
  }

  private void parseAthleteGroups(SimulationBuilder builder) {
    int groupCount = Integer.parseInt(scanner.nextLine().trim());
    for (int g = 0; g < groupCount; g++) {
      try {
        Scanner l1 = scanLine(), l2 = scanLine(), l3 = scanLine();
        builder.addAthleteGroup(
            l1.nextInt(), l1.nextInt(), l1.nextDouble(), l1.nextDouble(), l1.next(), l1.hasNext("s"),
            l2.nextDouble(), l2.nextDouble(), l2.nextDouble(),
            l3.nextInt(), parseTime(l3), l3.hasNextInt() ? l3.nextInt() : 0);
      } catch (NoSuchElementException e) {
        throw new IncorrectFormattingException("Incorrect formatting in athlete group", g);
      }
    }
  }

  @FunctionalInterface
  private interface LineParser {
    void parse(Scanner lineScanner, SimulationBuilder builder);
  }
}
