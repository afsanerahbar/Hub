package edu.duke.ece351.ar694;

import edu.duke.ece351.ar694.XJson;
import java.io.IOException;
import com.google.gson.*;
import java.nio.file.*;

// XJsonTest.java - Test class for validation of edu.duke.ece351.ar694.XJson functionality
class XJsonTest {
  private final XJson processor;

  public XJsonTest() {
    this.processor = new XJson();
  }

  public static void main(String[] args) {
    XJsonTest tester = new XJsonTest();
    tester.runAllTests();
  }

  public void runAllTests() {
    System.out.println("Running edu.duke.ece351.ar694.XJson tests...");

    testStringReversal();
    testNumberPreservation();
    testBooleanPreservation();
    testNullPreservation();
    testArrayReversal();
    testObjectReversal();
    testComplexNesting();

    System.out.println("All tests passed!");
  }

  private void testStringReversal() {
    String input = "\"hello\"";
    String expected = "\"olleh\"";
    String actual = processor.processJsonString(input);
    assertEqual(expected, actual, "String reversal");
  }

  private void testNumberPreservation() {
    String input = "42";
    String expected = "42";
    String actual = processor.processJsonString(input);
    assertEqual(expected, actual, "Number preservation");
  }

  private void testBooleanPreservation() {
    String input = "true";
    String expected = "false";
    String actual = processor.processJsonString(input);
    assertEqual(expected, actual, "Boolean preservation");
  }

  private void testNullPreservation() {
    String input = "null";
    String expected = "null";
    String actual = processor.processJsonString(input);
    assertEqual(expected, actual, "Null preservation");
  }

  private void testArrayReversal() {
    String input = "[\"abc\", 123, \"def\"]";
    String expected = "[\"fed\", 123, \"cba\"]";
    String actual = processor.processJsonString(input);
    assertJsonEqual(expected, actual, "Array reversal");
  }

  private void testObjectReversal() {
    String input = "{\"name\": \"john\", \"age\": 30}";
    String expected = "{\"name\": \"nhoj\", \"age\": 30}";
    String actual = processor.processJsonString(input);
    assertJsonEqual(expected, actual, "Object reversal");
  }

  private void testComplexNesting() {
    String input = "{\"users\": [{\"name\": \"alice\", \"data\": [\"x\", \"y\"]}, {\"name\": \"bob\"}]}";
    String expected = "{\"users\": [{\"name\": \"bob\"}, {\"name\": \"ecila\", \"data\": [\"y\", \"x\"]}]}";
    String actual = processor.processJsonString(input);
    assertJsonEqual(expected, actual, "Complex nesting");
  }

  private void assertEqual(String expected, String actual, String testName) {
    if (!expected.equals(actual)) {
      throw new AssertionError(String.format(
          "%s failed:\nExpected: %s\nActual: %s", testName, expected, actual));
    }
    System.out.println("✓ " + testName);
  }

  private void assertJsonEqual(String expected, String actual, String testName) {
    Gson gson = new Gson();
    JsonElement expectedElement = JsonParser.parseString(expected);
    JsonElement actualElement = JsonParser.parseString(actual);

    if (!expectedElement.equals(actualElement)) {
      throw new AssertionError(String.format(
          "%s failed:\nExpected: %s\nActual: %s", testName, expected, actual));
    }
    System.out.println("✓ " + testName);
  }

  /**
   * Create sample test files for the assignment
   */
  public void createTestFiles() throws IOException {
    // Create Tests directory
    Path testsDir = Paths.get("Tests");
    Files.createDirectories(testsDir);

    // Test 1: Simple string reversal
    writeTestCase(1,
        "\"hello\"",
        "\"olleh\"");

    // Test 2: Array with mixed types
    writeTestCase(2,
        "[\"abc\", 42, true, null, \"xyz\"]",
        "[\"zyx\", null, true, 42, \"cba\"]");

    // Test 3: Complex nested object
    writeTestCase(3,
        "{\"items\": [{\"name\": \"first\", \"tags\": [\"a\", \"b\"]}, {\"name\": \"second\"}], \"count\": 2}",
        "{\"items\": [{\"name\": \"dnoces\"}, {\"name\": \"tsrif\", \"tags\": [\"b\", \"a\"]}], \"count\": 2}");

    System.out.println("Test files created in Tests/ directory");
  }

  private void writeTestCase(int n, String input, String expected) throws IOException {
    Path testsDir = Paths.get("Tests");

    // Write input file
    Files.writeString(testsDir.resolve(n + "-in.json"), input);

    // Write expected output file
    Files.writeString(testsDir.resolve(n + "-out.json"), expected);
  }
}
