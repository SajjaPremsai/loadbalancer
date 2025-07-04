import java.util.ArrayList;
import java.util.List;

public class Router {
    private final List<RequestHandler> routes;

    public Router() {
        this.routes = new ArrayList<>();
    }

    public HttpResponse route(HttpRequest request) {
        for (RequestHandler handler : routes) {
            HttpResponse response = handler.handleRequest(request);
            if (response != null) {
                return response;
            }
        }
        return new HttpResponse(404, "text/plain", "Route Not Found");
    }

    public void addRoute(RequestHandler handler) {
        routes.add(handler);
    }
}
