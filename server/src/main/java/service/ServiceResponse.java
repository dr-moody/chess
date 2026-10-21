package service;

import java.util.HashSet;

public class ServiceResponse {
    public record Pair(String name, String value) {}
    HashSet<Pair> body = new HashSet<>();

    public ServiceResponse(String content) {
        String[] pairs = content.split(",");

        for (String pair : pairs) {
            String[] parts = pair.split(":",2);
            String key = parts[0];
            String value = parts[1];

            body.add(new Pair(key, value));
        }
    }
}
