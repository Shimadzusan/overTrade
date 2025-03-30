package core;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LogicTrade {
    public List<JSONObject> sellOrderList = new ArrayList<JSONObject>();
    public List<JSONObject> buyOrderList = new ArrayList<JSONObject>();

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
        System.out.println("--++--");
        for (int i = 0; i < buyOrderList.size(); i++) {
            System.out.println(buyOrderList.get(i));
        }
        // exequte module
        System.out.println("===tradeProcessing===");
        List<JSONObject> operationalList = buyOrderList;
        List<JSONObject> exeList = new ArrayList<JSONObject>();

        for (int i = 0; i < operationalList.size(); i++) {
            int price = operationalList.get(i).getInt("price");
            int volume = operationalList.get(i).getInt("volume");

            for (int j = 0; j < sellOrderList.size(); j++) {
                int sell_price = sellOrderList.get(j).getInt("price");
                int sell_volume = sellOrderList.get(j).getInt("volume");
                if(price == sell_price && volume == sell_volume) {
                    System.out.println(sellOrderList.get(j));
                    exeList.add(sellOrderList.get(j));
                    sellOrderList.remove(j);
                    break;
                }
            }
        }

        for (int i = 0; i < exeList.size(); i++) {
            int price = exeList.get(i).getInt("price");
            int volume = exeList.get(i).getInt("volume");

            for (int j = 0; j < buyOrderList.size(); j++) {
                int buy_price = buyOrderList.get(j).getInt("price");
                int buy_volume = buyOrderList.get(j).getInt("volume");
                if(price == buy_price && volume == buy_volume) {
//                    System.out.println(buyOrderList.get(j));
//                    exeList.add(sellOrderList.get(j));
                    buyOrderList.remove(j);
                    break;
                }
            }
        }
        return true;
    }

}
