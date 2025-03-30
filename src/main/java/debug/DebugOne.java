package debug;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DebugOne {

public void debug() {
    System.out.println("..start data:");

    List<JSONObject> sellOrderList = new ArrayList<JSONObject>();
    List<JSONObject> buyOrderList = new ArrayList<JSONObject>();
    String order = "";
    Random randomNumber = new Random();

    // sellOrderList
    for (int i = 0; i < 10; i++) {
        int volume = randomNumber.nextInt(10);
        int price = randomNumber.nextInt(10);
        order = "{\"order\":\"sell\",\"volume\":" + volume + ",\"price\":" + price + "}";
        sellOrderList.add(new JSONObject(order));
    }

    // buyOrderList
    for (int i = 0; i < 10; i++) {
        int volume = randomNumber.nextInt(10);
        int price = randomNumber.nextInt(10);
        order = "{\"order\":\"buy\",\"volume\":" + volume + ",\"price\":" + price + "}";
        buyOrderList.add(new JSONObject(order));
    }

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

    System.out.println("===========result============");

    for (int i = 0; i < sellOrderList.size(); i++) {
        System.out.println(sellOrderList.get(i));
    }
    System.out.println("--++--");
    for (int i = 0; i < buyOrderList.size(); i++) {
        System.out.println(buyOrderList.get(i));
    }
    System.out.println("---exeList--");
    for (int i = 0; i < exeList.size(); i++) {
        System.out.println(exeList.get(i));
    }
}

}
