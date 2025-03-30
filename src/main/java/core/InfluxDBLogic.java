package core;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import java.util.concurrent.TimeUnit;

public class InfluxDBLogic {
    InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086");
    String dbName = "over_trade";

    public InfluxDBLogic() {
        influxDB.setDatabase(dbName);
    }
    public void sendData(String logic_method, int value, int price) {
        // Create a data point
        Point point = Point.measurement("counter_data")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)  // Use the current timestamp
                .addField("logic_method", logic_method)
                .addField("value", value)
                .addField("price", price)
                .build();
        // Write the data point to InfluxDB
        influxDB.write(dbName, "autogen", point);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            influxDB.close();
            System.out.println("Finalizing, influxDB.close()");
        } finally {
            super.finalize();
        }
    }
}
