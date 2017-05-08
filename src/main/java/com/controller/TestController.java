package com.controller;

import com.Exception.TokenException;
import com.service.MeasureService;
import com.service.TokenService;
import com.util.JSONUtil;
import com.util.ListToMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by 63289 on 2017/5/8.
 */
@CrossOrigin
@RequestMapping(value = "api", produces = "application/json;charset=UTF-8")
@RestController
public class TestController {
    private final TokenService tokenService;
    private final MeasureService measureService;

    @Autowired
    public TestController(TokenService tokenService, MeasureService measureService) {
        this.tokenService = tokenService;
        this.measureService = measureService;
    }

    @RequestMapping({"{token}/date/time"})
    public String getByDate(@PathVariable String token, @RequestParam(required = false) Long timeMills) throws Exception {
        String accountNumber = tokenService.getAccountNumber(token);
        if (accountNumber == null) throw new TokenException();
        List list;
        LocalTime startTime = LocalTime.now();
        if (timeMills == null) list = measureService.findTodayDataByAccountNumber(accountNumber);
        else list = measureService.findByAccountNumberAndDate(accountNumber, new Timestamp(timeMills));
        Long useTime = LocalTime.now().toNanoOfDay() - startTime.toNanoOfDay();
        Map map = new HashMap();
        map.put("costTimeInMiili", useTime / 1000000);
        if (useTime > 0) return JSONUtil.toJSON(map);
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(list));
    }
    @RequestMapping("{token}/random/{number}")
    public void generateData(@PathVariable String token, @PathVariable Integer number) throws Exception {
        String accountNumber = tokenService.getAccountNumber(token);
        Random random = new Random();
        Integer step = 0;
        Integer distance = 0;
        Integer heart = 40;
        for (int i = 0; i < number; ++i) {
            Long commitTime = System.currentTimeMillis() + i - number;
            String device = "00-00-00-00-00-00-00-00";
            if (i % 100 == 0) {
                Integer change = random.nextInt(2);
                step += change;
                distance += change / (1 + random.nextInt(1));
                heart = 40 + change * 10 + random.nextInt(20);
            } else heart = 40 + random.nextInt(20);
            measureService.addData(accountNumber, commitTime, device, step, distance, heart);
        }
    }
}
