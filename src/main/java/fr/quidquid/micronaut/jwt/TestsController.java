package fr.quidquid.micronaut.jwt;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller()
public class TestsController {

    public TestsController() {
    }

    @Get("/ping")
    public HttpResponse ping() {
        return HttpResponse.ok("pong");
    }

    @Get("/secret")
    public HttpResponse secret() {
        return HttpResponse.ok("Access granted");
    }

}