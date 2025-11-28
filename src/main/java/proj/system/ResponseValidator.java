package proj.system;

import io.restassured.response.Response;
import org.testng.Assert;

public class ResponseValidator {

    public static void assertJson(Response response) {
        try {
            response.jsonPath().get();  // try parsing
        } catch (Exception e) {
            Assert.fail("Expected response to be valid JSON, but parsing failed: " + e.getMessage());
        }
    }


    public static void assertNotJson(Response response) {
        boolean threwException = false;
        try {
            response.jsonPath().get();  // try parsing
        } catch (Exception e) {
            threwException = true;      // expected
        }
        if (!threwException) {
            Assert.fail("Expected response to NOT be JSON, but parsing succeeded.");
        }
    }
}
