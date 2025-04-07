package core;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LogicTradeM {
    private List<JSONObject> sellOrderList = new ArrayList<>();
    private List<JSONObject> buyOrderList = new ArrayList<>();
    private List<JSONObject> exeList = new ArrayList<>();
    private InfluxDBLogic influx = new InfluxDBLogic();

    public boolean addOrderBuy(JSONObject order) {
        buyOrderList.add(order);
        return true;
    }

    public boolean addOrderSell(JSONObject order) {
        sellOrderList.add(order);
        return true;
    }

    public boolean tradeProcessing() {
        // Print sell orders
        printOrders("Sell Orders", sellOrderList);

        // Print buy orders
        printOrders("Buy Orders", buyOrderList);

        System.out.println("=== tradeProcessing ===");

        // Process trades
        List<JSONObject> operationalList = new ArrayList<>(buyOrderList);

        // Execute trades
        executeTrades(operationalList, sellOrderList, exeList);
        executeTrades(operationalList, buyOrderList, exeList);

        System.out.println("=== endTradeProcessing ===");
        return true;
    }

    private void printOrders(String label, List<JSONObject> orders) {
        System.out.println(label + ": " + orders.size());
        for (JSONObject order : orders) {
            System.out.println(order);
        }
        System.out.println("--++--");
    }

    private void executeTrades(List<JSONObject> operationalList, List<JSONObject> targetList, List<JSONObject> exeList) {
        Iterator<JSONObject> operationalIterator = operationalList.iterator();

        while (operationalIterator.hasNext()) {
            JSONObject operationalOrder = operationalIterator.next();
            int price = operationalOrder.getInt("price");
            int volume = operationalOrder.getInt("volume");

            Iterator<JSONObject> targetIterator = targetList.iterator();

            while (targetIterator.hasNext()) {
                JSONObject targetOrder = targetIterator.next();
                int targetPrice = targetOrder.getInt("price");
                int targetVolume = targetOrder.getInt("volume");

                if (price == targetPrice && volume == targetVolume) {
                    System.out.println(targetOrder);
                    exeList.add(targetOrder);
                    targetIterator.remove(); // Safe removal

                    // Break after finding the first matching order
                    break;
                }
            }
        }
    }

    public boolean tradeMonitoring() {
        int sizeSellOrderList = sellOrderList.size();
        int sizeBuyOrderList = buyOrderList.size();
        int sizeExeList = exeList.size();
        influx.sendDataMonitoring(sizeSellOrderList, sizeBuyOrderList, sizeExeList);

        return true;
    }
}
