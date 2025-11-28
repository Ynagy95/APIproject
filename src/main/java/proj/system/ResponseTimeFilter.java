package proj.system;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class ResponseTimeFilter implements Filter {
    @Override
    public Response filter(FilterableRequestSpecification req,
                           FilterableResponseSpecification res,
                           FilterContext ctx) {

        Response response = ctx.next(req, res);

        long responseTime = response.getTime();

        if (responseTime > 10000) {
            throw new AssertionError("Response time exceeded 10000ms: " + responseTime);
        }

        return response;
    }
}
