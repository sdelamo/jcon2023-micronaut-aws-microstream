package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.uri.UriBuilder;

@Controller
class HomeController {

    @Get
    HttpResponse<Object> index() {
        return HttpResponse.seeOther(UriBuilder.of("/swagger-ui").path("index.html").build());
    }
}
