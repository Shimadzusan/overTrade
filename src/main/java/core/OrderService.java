package core;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.annotation.Counted;
//import io.micrometer.core.annotation.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {

    private final AtomicInteger sellOrderCount = new AtomicInteger(0);

    private final MeterRegistry meterRegistry;

    public OrderService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    // This method will be timed and metrics will be captured
    @Timed(value = "orders.add.sell", description = "Time taken to add a sell order")
    @Counted(value = "orders.add.sell.count", description = "Count of sell orders added")
    public void addSellOrder(JSONObject order) {
        // Logic to add sell order, e.g., saving to a database or list
        sellOrderCount.incrementAndGet();
        // Your existing logic here, e.g.,
        System.out.println("Added sell order: " + order);
    }

    // Register a gauge to get the current number of sell orders
//    @Gauge(value = "orders.sell.current", description = "Current count of sell orders in the system")
//    public int currentSellOrderCount() {
//        return sellOrderCount.get();
//    }
}

