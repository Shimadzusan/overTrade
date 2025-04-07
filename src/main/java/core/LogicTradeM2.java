package core;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LogicTradeM2 {
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

        // Copy buyOrderList to process trades against it
        List<JSONObject> operationalList = new ArrayList<>(buyOrderList);

        // Execute trades
        for (JSONObject buyOrder : operationalList) {
            int buyPrice = buyOrder.getInt("price");
            int buyVolume = buyOrder.getInt("volume");
            int remainingVolume = buyVolume;

            Iterator<JSONObject> sellIterator = sellOrderList.iterator();

            while (sellIterator.hasNext() && remainingVolume > 0) {
                JSONObject sellOrder = sellIterator.next();
                int sellPrice = sellOrder.getInt("price");
                int sellVolume = sellOrder.getInt("volume");

                // Check if the prices match
                if (buyPrice == sellPrice) {
                    System.out.println("Executing trade: Buy Order = " + buyOrder + ", Sell Order = " + sellOrder);

                    // Execute the order based on volume
                    if (remainingVolume >= sellVolume) {
                        // If the buy order can take the entire sell order
                        exeList.add(sellOrder);
                        remainingVolume -= sellVolume; // Reduce the remaining volume of the buy order
                        sellIterator.remove(); // Remove the sell order as it's fulfilled
                    } else {
                        // If the buy order can't take the entire sell order
                        JSONObject partiallyExecutedOrder = new JSONObject(sellOrder.toString());
                        partiallyExecutedOrder.put("volume", remainingVolume); // Update the volume to executed
                        exeList.add(partiallyExecutedOrder);
                        sellOrder.put("volume", sellVolume - remainingVolume); // Reduce the sell order volume
                        remainingVolume = 0; // Buy order is fully executed
                    }
                }
            }
        }

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

    public boolean tradeMonitoring() {
        int sizeSellOrderList = sellOrderList.size();
        int sizeBuyOrderList = buyOrderList.size();
        int sizeExeList = exeList.size();
        influx.sendDataMonitoring(sizeSellOrderList, sizeBuyOrderList, sizeExeList);

        return true;
    }
}
