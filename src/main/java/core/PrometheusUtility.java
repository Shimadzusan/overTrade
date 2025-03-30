package core;

import io.prometheus.client.Counter;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component("PrometheusUtility")
public class PrometheusUtility {

    private PrometheusUtility() {
    }

    private final static Map<String,Counter> counters = new HashMap<>();

    public boolean initializePrometheusEssentials() {
        counters.put("SAMPLE-COUNTER",Counter.build()
                .name("SAMPLE COUNTER")
                .help("Records the Sample Count").register());
        return true;
    }


    public static void incrementCounter(String counterName){
        counters.get(counterName).inc();
    }
}

