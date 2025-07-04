import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
    private int statusCode;
    private String body = "";
    private Map<String, String> headers = new LinkedHashMap<>();

    public HttpResponse(int statusCode) {
        this.statusCode = statusCode;
        setHeader("Content-Type", "text/plain");
    }

    public HttpResponse(int statusCode,String contentType){
        this.statusCode = statusCode;
    }
    public HttpResponse(int statusCode, String contentType, String body) {
        this.statusCode = statusCode;
        this.body = body;
        setHeader("Content-Type", contentType);
        setHeader("Content-Length", String.valueOf(body.getBytes().length));
    }


    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setBody(String body) {
        this.body = body;
        setHeader("Content-Length", String.valueOf(body.getBytes().length));
    }

    public void send(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 " + statusCode + " " + getStatusText() + "\r\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            out.write(entry.getKey() + ": " + entry.getValue() + "\r\n");
        }
        out.write("\r\n");
        out.write(body);
        out.flush();
    }

    private String getStatusText() {
        return switch (statusCode) {
            case 200 -> "OK";
            case 404 -> "Not Found";
            case 400 -> "Bad Request";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getHeader(String key) {
        return this.headers.getOrDefault(key, null);
    }
}
