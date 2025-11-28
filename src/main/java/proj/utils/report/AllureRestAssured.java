package proj.utils.report;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.output.WriterOutputStream;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.PrintStream;
import java.io.StringWriter;

public class AllureRestAssured {

    /**
     * Sends an HTTP request and attaches full request/response to Allure.
     *
     * @param reqSpec  RestAssured RequestSpecification
     * @param method   HTTP method (GET, POST, PUT, DELETE, etc.)
     * @param endpoint API endpoint (relative path)
     * @return Response object
     */
    public static Response logAndAttach(RequestSpecification reqSpec, String method, String endpoint) {

        StringWriter writer = new StringWriter();
        PrintStream ps = new PrintStream(new WriterOutputStream(writer), true);

        // Add logging filters
        reqSpec.filter(new RequestLoggingFilter(ps));
        reqSpec.filter(new ResponseLoggingFilter(ps));

        // Send request
        Response response = switch (method.toUpperCase()) {
            case "POST" -> reqSpec.post(endpoint);
            case "GET" -> reqSpec.get(endpoint);
            case "PUT" -> reqSpec.put(endpoint);
            case "DELETE" -> reqSpec.delete(endpoint);
            case "PATCH" -> reqSpec.patch(endpoint);
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        };

        // Extract body safely
        String responseBody = response.getBody() != null ? response.getBody().asString() : "";

        // Save everything inside TestNG test attributes
        ITestResult result = Reporter.getCurrentTestResult();
        result.setAttribute("requestResponseLog", writer.toString());
        result.setAttribute("responseBody", responseBody);
        result.setAttribute("statusCode", response.getStatusCode());

        return response;
    }

}
