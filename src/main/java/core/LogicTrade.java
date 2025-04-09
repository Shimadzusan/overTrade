package core;

import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogicTrade {
    public List<JSONObject> sellOrderList = new ArrayList<JSONObject>();
    public List<JSONObject> buyOrderList = new ArrayList<JSONObject>();
    List<JSONObject> exeList = new ArrayList<JSONObject>();
    InfluxDBLogic influx = new InfluxDBLogic();

    HTTPServer srv = new HTTPServer(3333);

    static final Gauge incomeTrafficSize = Gauge.build()
            .name("incomeTrafficSize")
            .labelNames("incomeTrafficSize")
            .help("A gauge that holds a specific value.")
            .register();

    static final Gauge outcomeTrafficSize = Gauge.build()
            .name("outcomeTrafficSize")
            .labelNames("outcomeTrafficSize")
            .help("A gauge that holds a specific value.")
            .register();

    static final Gauge sizeSellOrderListGauge = Gauge.build()
            .name("sizeSellOrderListGauge")
            .labelNames("sizeSellOrderListGauge")
            .help("A gauge that holds a specific value.")
            .register();

    static final Gauge sizeBuyOrderListGauge = Gauge.build()
            .name("sizeBuyOrderListGauge")
            .labelNames("sizeBuyOrderListGauge")
            .help("A gauge that holds a specific value.")
            .register();

    static final Gauge sizeExeListGauge = Gauge.build()
            .name("sizeExeListGauge")
            .labelNames("sizeExeListGauge")
            .help("A gauge that holds a specific value.")
            .register();

    public LogicTrade() throws IOException {
    }

    public boolean addOrderBuy(JSONObject order) {
        buyOrderList.add(order);
        return true;
    }

    public boolean addOrderSell(JSONObject order) {
        sellOrderList.add(order);
        return true;
    }

    public boolean tradeProcessing() {
        for (int i = 0; i < sellOrderList.size(); i++) {
            System.out.println(sellOrderList.get(i));
        }
        System.out.println("sellOrderList: " + sellOrderList.size());
        System.out.println("--++--");
        for (int i = 0; i < buyOrderList.size(); i++) {
            System.out.println(buyOrderList.get(i));
        }
        System.out.println("buyOrderList: " + buyOrderList.size());

        // exequte module
        System.out.println("===tradeProcessing===");
        List<JSONObject> operationalList = buyOrderList;
        List<JSONObject> operationalListTwo = new ArrayList<>();

        //.. addition to exeList and deleting from sellOrderList
        for (int i = 0; i < operationalList.size(); i++) {
            int price = operationalList.get(i).getInt("price");
            int volume = operationalList.get(i).getInt("volume");

            for (int j = 0; j < sellOrderList.size(); j++) {
                int sell_price = sellOrderList.get(j).getInt("price");
                int sell_volume = sellOrderList.get(j).getInt("volume");
                if(price == sell_price && volume == sell_volume) {
                    System.out.println(sellOrderList.get(j));
                    exeList.add(sellOrderList.get(j));
                    operationalListTwo.add(sellOrderList.get(j));
                    sellOrderList.remove(j);
                    break;
                }
            }
        }

        //.. deleting from buyOrderList
        for (int i = 0; i < operationalListTwo.size(); i++) {
            int price = operationalListTwo.get(i).getInt("price");
            int volume = operationalListTwo.get(i).getInt("volume");

            for (int j = 0; j < buyOrderList.size(); j++) {
                int buy_price = buyOrderList.get(j).getInt("price");
                int buy_volume = buyOrderList.get(j).getInt("volume");
                if(price == buy_price && volume == buy_volume) {
                    buyOrderList.remove(j);
                    break;
                }
            }
        }
        System.out.println("===endTradeProcessing===");
        return true;
    }

    public boolean tradeMonitoring() {
        int sizeSellOrderList = sellOrderList.size();
        int sizeBuyOrderList = buyOrderList.size();
        int sizeExeList = exeList.size();
        influx.sendDataMonitoring(sizeSellOrderList, sizeBuyOrderList, sizeExeList);
        return true;
    }

    public boolean tradeMonitoring2(long incomeTraffic, long outcomeTraffic) {
        // Get the sizes of the lists
        int sizeSellOrderList = sellOrderList.size();
        int sizeBuyOrderList = buyOrderList.size();
        int sizeExeList = exeList.size();

        String sellInstance = "sellOrderList";
        sizeSellOrderListGauge.labels(sellInstance).set(sizeSellOrderList);
        String buyInstance = "buyOrderList";
        sizeBuyOrderListGauge.labels(buyInstance).set(sizeBuyOrderList);
        String exeInstance = "exeOrderList";
        sizeExeListGauge.labels(exeInstance).set(sizeExeList);

        String incomeTrafficSize2 = "incomeTrafficSize";
        incomeTrafficSize.labels(incomeTrafficSize2).set(incomeTraffic);

        String outcomeTrafficSize2 = "outcomeTrafficSize";
        outcomeTrafficSize.labels(outcomeTrafficSize2).set(outcomeTraffic);

        return true;
    }

}
