package service;

import java.util.HashMap;
import java.util.Map;

public class ServiceResponse {
    public Map<String, String> body = new HashMap<>();

    public ServiceResponse(String content) {
        String[] pairs = content.split(",");

        for (String pair : pairs) {
            String[] parts = pair.split(":",2);
            String key = parts[0];
            String value = parts[1];

            body.put(key, value);
        }
    }

    public String get(String value) {
        return body.get(value);
    }
}
