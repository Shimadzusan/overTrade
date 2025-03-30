package core;

import debug.DebugOne;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Launch {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("..start");
        SpringApplication.run(Launch.class, args);
//        new DebugOne().debug();
    }

}
