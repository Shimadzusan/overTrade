package core;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class LogicRest {
    InfluxDBLogic influx = new InfluxDBLogic();
    LogicTrade logicTrade = new LogicTrade();
    int counter = 0;

    @PostMapping("/sendOrder")
    public String sendOrder(@RequestBody String requestBody) {
        System.out.println("requestBody from sendOrder: " + requestBody);
        counter++;
        if(counter > 100) {
            logicTrade.tradeProcessing();
            counter = 0;
        }

        JSONObject jsonObject = new JSONObject(requestBody);
        int volume = jsonObject.getInt("volume");
        int price = jsonObject.getInt("price");
        String result = "";

        String order = jsonObject.getString("order");
        switch (order) {
            case "sell":
                logicTrade.addOrderSell(jsonObject);
                result = "{\"result\":\"sell ok\"}";
                influx.sendData("sell", volume, price);
                break;

            case "buy":
                logicTrade.addOrderBuy(jsonObject);
                result = "{\"result\":\"buy ok\"}";
                influx.sendData("buy", volume, price);
                break;
        }
        return result;
    }
}
