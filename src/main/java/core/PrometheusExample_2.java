package core;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;

public class PrometheusExample_2 {
    // Create a Gauge metric
    static final Gauge valueGauge = Gauge.build()
            .name("example_gauge_value")
            .labelNames("instance")
            .help("A gauge that holds a specific value.")
            .register();

    public static void main(String[] args) {
        // Start the HTTP server on port 8080
        try {
            HTTPServer server = new HTTPServer(3333);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the gauge to the desired value (e.g., 5000000)
        String instance = "my_instance";
        setGaugeValue(instance, 500000145);

        // Log message to indicate the gauge value set
        System.out.println("Gauge set to 5000000 for instance: " + instance);
    }

    private static void setGaugeValue(String instance, double value) {
        // Set the gauge value for the given instance
        valueGauge.labels(instance).set(value);
    }
}
