import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private String httpVersion;
    private Map<String, String> headers = new HashMap<>();
    private String body = "";

    public static HttpRequest parse(BufferedReader in) throws IOException {
        HttpRequest request = new HttpRequest();

        String requestLine = in.readLine();
        if (requestLine == null || requestLine.isEmpty()) return null;

        String[] requestParts = requestLine.split(" ");
        if (requestParts.length != 3) return null;

        request.method = requestParts[0];
        request.path = requestParts[1];
        request.httpVersion = requestParts[2];

        String line;
        while (!(line = in.readLine()).isEmpty()) {
            String[] headerParts = line.split(":", 2);
            if (headerParts.length == 2) {
                request.headers.put(headerParts[0].trim(), headerParts[1].trim());
            }
        }

        if (request.headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(request.headers.get("Content-Length"));
            char[] bodyChars = new char[contentLength];
            in.read(bodyChars, 0, contentLength);
            request.body = new String(bodyChars);
        }

        return request;
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getHeader(String key) {
        return headers.getOrDefault(key, null);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
