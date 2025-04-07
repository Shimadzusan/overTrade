package core;

import io.prometheus.client.Counter;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component("PrometheusUtility")
public class PrometheusUtility {

    PrometheusUtility() {
        initializePrometheusEssentials();
    }

//    static final Counter my_counter_2 = Counter.build()
//            .name("my_counter_2")
//            .help("Total number of sell orders added.")
//            .register();

    private final static Map<String,Counter> counters = new HashMap<>();

    public boolean initializePrometheusEssentials() {
//        counters.put("SAMPLE-COUNTER",Counter.build()
//                .name("SAMPLE COUNTER")
//                .help("Records the Sample Count").register());

        counters.put("countBuyOrder",Counter.build()
                .name("countBuyOrder")
                .labelNames("metric_10","metric_30")
//                .labelNames("123")
                .help("Records the Sample Count").register());

        return true;
    }

    public static void incrementCounter(String counterName) {
        Counter counter = counters.get(counterName);
        if (counter != null) {
            counter.inc();
        } else {
            throw new IllegalArgumentException("Counter with name '" + counterName + "' does not exist.");
        }
    }

    public static void registerCounter(String counterName, String help) {
        Counter counter = Counter.build()
                .name(counterName)
                .help(help)
                .register();

        counters.put(counterName, counter);
    }



//    public static void incrementCounter(String counterName){
//        counters.get(counterName).inc();
//    }
}

