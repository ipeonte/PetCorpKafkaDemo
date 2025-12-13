package com.example.demo.petoffice.rest.framework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.StringUtils;
import com.example.demo.petcorp.shared.TestUtils;

@TestPropertySource("classpath:/test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestFramework {

  private static final String BASE_FOLER =
      "src" + File.separator + "test" + File.separator + "resources" + File.separator + "tdd";

  private static final String IN_FOLDER = BASE_FOLER + File.separator + "in";

  private static final String OUT_FOLDER = BASE_FOLER + File.separator + "out";

  private static final String IN_EXT = "txt";

  private static final String OUT_EXT = "json";

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate _rest;

  @Test
  public void test() throws IOException {
    // Read in folder
    try (DirectoryStream<Path> dstream =
        Files.newDirectoryStream(Paths.get(IN_FOLDER), "*." + IN_EXT)) {
      for (Path file : dstream)
        checkTestCase(file, readOutFile(file));
    }
  }

  private String readOutFile(Path file) throws IOException {
    // Read expected text
    return Files
        .readString(Path.of(OUT_FOLDER, TestUtils.getFileNameWithoutExt(file) + "." + OUT_EXT));
  }

  private void checkTestCase(Path file, String expected) throws IOException {
    String curl = Files.readString(file);

    if (!StringUtils.hasText(curl))
      fail("Empty file " + file);

    final String ctext = "curl";
    String url = curl.substring(curl.indexOf(ctext) + ctext.length());

    if (!StringUtils.hasText(url))
      fail("Empty curl text " + url);

    url = url.trim().replace("{port}", port + "");
    String actual = _rest.getForObject(url, String.class);

    assertEquals(expected, actual, curl + " result doesn't match");
  }
}
