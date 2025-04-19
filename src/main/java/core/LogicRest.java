package core;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@CrossOrigin
public class LogicRest {
    InfluxDBLogic influx = new InfluxDBLogic();
    LogicTrade logicTrade = new LogicTrade();
    int counter = 0;
    long incomeTrafficSize = 0;
    long outcomeTrafficSize = 0;

    public LogicRest() throws IOException {
    }

        @PostMapping("/sendOrder")
    public String sendOrder(@RequestBody String requestBody) throws InterruptedException {
        System.out.println("requestBody from sendOrder: " + requestBody);
        incomeTrafficSize += requestBody.getBytes().length;
        counter++;
        if(counter > 10) {
            logicTrade.tradeProcessing();
            logicTrade.tradeMonitoring();
            logicTrade.tradeMonitoringPrometheus(incomeTrafficSize, outcomeTrafficSize);
            counter = 0;
        }

        JSONObject jsonObject = new JSONObject(requestBody);
        int volume = jsonObject.getInt("volume");
        int price = jsonObject.getInt("price");
        String result = "";

        String order = jsonObject.getString("order");
//        Thread.sleep(2000);
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
        outcomeTrafficSize += result.getBytes().length;
        return result;
    }
}
