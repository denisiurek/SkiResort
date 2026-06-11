package inputParser;

import inputParser.sections.AthleteGroupSectionParser;
import inputParser.sections.LiftSectionParser;
import inputParser.sections.NodeSectionParser;
import inputParser.sections.RouteSectionParser;
import simulation.Logger;
import simulation.SimulationConfig;
import simulation.SimulationEngine;

import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

public class ResortInputParser implements InputParser {
  private final Scanner scanner;

  public ResortInputParser(InputStream inputStream) {
    this.scanner = new Scanner(inputStream);
    scanner.useLocale(Locale.ENGLISH);
  }

  ResortInputParser() {
    this.scanner = new Scanner(System.in);
    scanner.useLocale(Locale.ENGLISH);
  }

  @Override
  public SimulationEngine parse(Logger logger, SimulationConfig config) throws IncorrectFormattingException {
    SimulationBuilder builder = new SimulationBuilder(config, logger);
    int currentLine = 1;
    try {
      String nodeHeader = scanner.nextLine();
      currentLine++;

      int nodeCount = new Scanner(nodeHeader).nextInt();
      new NodeSectionParser().parseSection(extractLines(nodeCount), builder);
      currentLine += nodeCount;

      scanner.nextLine();
      currentLine++;

      String liftHeader = scanner.nextLine();
      currentLine++;

      int liftCount = new Scanner(liftHeader).nextInt();
      new LiftSectionParser().parseSection(extractLines(liftCount), builder);
      currentLine += liftCount;

      scanner.nextLine();
      currentLine++;

      String routeHeader = scanner.nextLine();
      currentLine++;

      int routeCount = new Scanner(routeHeader).nextInt();
      new RouteSectionParser().parseSection(extractLines(routeCount), builder);
      currentLine += routeCount;

      scanner.nextLine();
      currentLine++;

      String athleteGroupHeader = scanner.nextLine();
      currentLine++;

      int athleteGroupLineCount = new Scanner(athleteGroupHeader).nextInt() * 3;
      new AthleteGroupSectionParser().parseSection(extractLines(athleteGroupLineCount), builder);

    } catch (IncorrectFormattingException e) {
      e.appendInputLineNumber(currentLine);
      throw e;
    }
    return builder.build();

  }

  private String[] extractLines(int count) {
    String[] lines = new String[count];
    for (int i = 0; i < count; i++) {
      lines[i] = scanner.nextLine();
    }
    return lines;
  }
}
