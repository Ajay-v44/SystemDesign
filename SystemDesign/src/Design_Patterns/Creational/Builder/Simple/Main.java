package Design_Patterns.Creational.Builder.Simple;

public class Main {
    static void main() {
        // Using Builder Pattern (nested class)
        HttpRequest request = new HttpRequest.HttpRequestBuilder()
                .withUrl("https://api.example.com")
                .withMethod("POST")
                .withHeader("Content-Type", "application/json")
                .withHeader("Accept", "application/json")
                .withQueryParams("key", "12345")
                .withBody("{\"name\": \"Ajay\"}")
                .withTimeout(60)
                .build();

        request.execute(); // Guaranteed to be in a consistent state
    }
}
