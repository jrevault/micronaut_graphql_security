package fr.quidquid.micronaut.jwt;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

@Controller()
public class TestsController {

    public TestsController() {
    }

    @Get("/ping")
    @Secured( IS_ANONYMOUS )
    public HttpResponse ping() {
        return HttpResponse.ok("pong");
    }

    @Get("/secret")
    @Secured( IS_AUTHENTICATED )
    public HttpResponse secret() {
        return HttpResponse.ok("Access granted");
    }

}