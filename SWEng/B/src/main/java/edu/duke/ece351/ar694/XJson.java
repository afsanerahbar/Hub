package edu.duke.ece351.ar694;
import com.google.gson.*;
import java.util.*;
import java.util.regex.Pattern;
import com.google.gson.JsonPrimitive;


/**
 * edu.duke.ece351.ar694.XJson - JSON reversal program as specified in the assignment
 * Reads JSON from STDIN, reverses according to specified rules, outputs to STDOUT
 */
public class XJson {
  private static final Pattern VALID_STRING_PATTERN = Pattern.compile("^[a-z]{1,10}$");
  private final Gson gson;

  public XJson() {
    this.gson = new GsonBuilder()
        .serializeNulls()
        .create();
  }

  public static void main(String[] args) {
    XJson processor = new XJson();
    processor.processStdin();
  }


  /**
   * Main processing method - reads JSON from STDIN and outputs reversed JSON to STDOUT
   */
  public void processStdin() {
    try (Scanner scanner = new Scanner(System.in)) {
      StringBuilder jsonBuffer = new StringBuilder();

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        jsonBuffer.append(line).append("\n");

        // Try to parse and process complete JSON objects
        String currentJson = jsonBuffer.toString().trim();
        if (!currentJson.isEmpty()) {
          processJsonSequence(currentJson, jsonBuffer);
        }
      }

//      // Process any remaining JSON
//      String remainingJson = jsonBuffer.toString().trim();
//      if (!remainingJson.isEmpty()) {
//        processJsonSequence(remainingJson, jsonBuffer);
//      }

    } catch (Exception e) {
      System.err.println("Error processing input: " + e.getMessage());
      System.exit(1);
    }
  }

  /**
   * Process a sequence of JSON objects separated by whitespace
   */
  private void processJsonSequence(String jsonSequence, StringBuilder buffer) {
    JsonStreamParser parser = new JsonStreamParser(jsonSequence);
    StringBuilder processedPart = new StringBuilder();

    try {
      while (parser.hasNext()) {
        JsonElement element = parser.next();
        JsonElement reversed = reverseJsonElement(element);

        //Deserialization: Output the reversed JSON
        System.out.println(gson.toJson(reversed));

        // Track how much we've processed
        String elementJson = gson.toJson(element);
        processedPart.append(elementJson);
      }

      // Remove processed part from buffer
      String original = buffer.toString();
      String remaining = original.substring(processedPart.length()).trim();
      buffer.setLength(0);
      buffer.append(remaining);

    } catch (JsonSyntaxException e) {
      // Incomplete JSON - wait for more input
      // This is expected when reading streaming input
    }
  }

  /**
   * Reverse a JSON element according to the assignment rules
   */
  public JsonElement reverseJsonElement(JsonElement element) {
    if (element == null || element.isJsonNull()) {
      return JsonNull.INSTANCE;
    }

    if (element.isJsonPrimitive()) {
      return reversePrimitive(element.getAsJsonPrimitive());
    }

    if (element.isJsonArray()) {
      return reverseArray(element.getAsJsonArray());
    }

    if (element.isJsonObject()) {
      return reverseObject(element.getAsJsonObject());
    }

    return element;
  }

  /**
   * Reverse primitive values (strings get reversed, numbers/booleans stay same)
   */
  private JsonElement reversePrimitive(JsonPrimitive primitive) {
    if (primitive.isString()) {
      String str = primitive.getAsString();
      validateString(str);
      return new JsonPrimitive(reverseString(str));
    }
    else if (primitive.isBoolean()) {
      return primitive.getAsBoolean() ? new JsonPrimitive(false) : new JsonPrimitive(true);
    }
    else if (primitive.isNumber()) {
      // Negate the number - handle different number types properly
      try {
        // Check if it's a decimal number
        if (primitive.getAsString().contains(".")) {
          // It's a decimal
          double value = primitive.getAsDouble();
          return new JsonPrimitive(-value);
        } else {
          // It's an integer
          long value = primitive.getAsLong();
          return new JsonPrimitive(-value);
        }
      } catch (NumberFormatException e) {
        // Fallback to double
        double value = primitive.getAsDouble();
        return new JsonPrimitive(-value);
      }
    }

    // Numbers, booleans remain unchanged
    return primitive;
  }

  /**
   * Reverse an array: reverse the order and reverse each element
   */
  private JsonElement reverseArray(JsonArray array) {
    JsonArray reversed = new JsonArray();

    // Reverse the array order and reverse each element
    for (int i = array.size() - 1; i >= 0; i--) {
      JsonElement element = array.get(i);
      reversed.add(reverseJsonElement(element));
    }

    return reversed;
  }

  /**
   * Reverse an object: reverse each field value
   */
  private JsonElement reverseObject(JsonObject object) {
    JsonObject reversed = new JsonObject();

    for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
      String key = entry.getKey();
      JsonElement value = entry.getValue();
      reversed.add(key, reverseJsonElement(value));
    }

    return reversed;
  }

  /**
   * Reverse a string character by character
   */
  private String reverseString(String str) {
    return new StringBuilder(str).reverse().toString();
  }

  /**
   * Validate string according to assignment constraints
   */
  private void validateString(String str) {
    if (!VALID_STRING_PATTERN.matcher(str).matches()) {
      throw new IllegalArgumentException(
          "Invalid string: '" + str + "'. Must be 1-10 lowercase letters only.");
    }
  }

  /**
   * Process a single JSON string (for testing)
   */
  public String processJsonString(String jsonString) {
    try {
      JsonElement element = JsonParser.parseString(jsonString);
      JsonElement reversed = reverseJsonElement(element);
      return gson.toJson(reversed);
    } catch (JsonSyntaxException e) {
      throw new IllegalArgumentException("Invalid JSON: " + e.getMessage());
    }
  }
}
