package core;

import io.prometheus.client.Counter;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;

public class PrometheusExample {
    // Create a Counter metric
    static final Counter requests = Counter.build()
            .name("example_counter_total")
            .labelNames("instance")
            .help("Total number of requests.")
            .register();

    public static void main(String[] args) {
        // Start the HTTP server on port 8080
        try {
            HTTPServer server = new HTTPServer(3333);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Simulate sending a value (e.g., incrementing by 5)
        String instance = "my_instance";
        incrementCounter(instance, 6);

        // Log message to indicate the counter increment
        System.out.println("Counter incremented by 5 for instance: " + instance);
    }

    private static void incrementCounter(String instance, int value) {
        // Increment the counter by the given value
        for (int i = 0; i < value; i++) {
            requests.labels(instance).inc(); // Increment the counter by 1, for the given instance label
        }
    }
}
