package inputParser;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;

class ResortInputParserTest {

    @Test
    void parse() {
        try (FileInputStream fs = new FileInputStream(new File("src/test/resources/test0"))) {
            ResortInputParser parser = new ResortInputParser(fs);
            parser.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}