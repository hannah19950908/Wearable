package com.controller;

import com.Exception.SQLNotFoundException;
import com.Exception.TokenException;
import com.entity.MeasureEntity;
import com.service.MeasureService;
import com.service.TokenService;
import com.util.JSONUtil;
import com.util.ListToMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 63289 on 2017/4/19.
 */
@CrossOrigin(value = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "api", produces = "application/json;charset=UTF-8")
public class MeasureController {
    private final MeasureService measureService;
    private final TokenService tokenService;

    @Autowired
    public MeasureController(MeasureService measureService, TokenService tokenService) {
        this.measureService = measureService;
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "{token}/someDayData", method = RequestMethod.POST)
    public String getByDate(@PathVariable String token, @RequestBody(required = false) String mapString) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber = tokenService.getAccountNumber(token);
        if(accountNumber==null) throw new TokenException();
        List list;
        Long timeMills=(Long) map.get("timestamp");
        if(timeMills==null) list = measureService.findTodayDataByAccountNumber(accountNumber);
        else{
            Timestamp timestamp=new Timestamp(timeMills);
            list = measureService.findByAccountNumberAndDate(accountNumber, timestamp);
        }
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(map, list));
    }

    @RequestMapping(value = "{token}/latest", method = RequestMethod.POST)
    public String getEachDateLatestByDateRange(@PathVariable String token, @RequestBody(required = false) String mapString) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber = tokenService.getAccountNumber(token);
        if(accountNumber==null) throw new TokenException();
        Long fromTimeMills=(Long) map.get("fromTime");
        Long toTimeMills=(Long) map.get("toTime");
        List list;
        if(fromTimeMills==null||toTimeMills==null){
            list=new ArrayList();
            list.add(measureService.findTheLatestByAccountNumber(accountNumber));
        }else {
            list = measureService.findTheLatestOfDateByAccountNumberAndDateRange(accountNumber,
                    new Timestamp(fromTimeMills), new Timestamp(toTimeMills));
        }
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(map, list));
    }

    @RequestMapping(value = "{token}/data", method = RequestMethod.POST)
    public String getAllByTimeRange(@PathVariable String token, @RequestBody String mapString) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber = tokenService.getAccountNumber(token);
        if(accountNumber==null) throw new TokenException();
        Long fromTimeMills=(Long)map.get("fromTime");
        Long toTimeMills=(Long) map.get("toTime");
        List list;
        if(fromTimeMills==null){
            list = measureService.findAllDataByAccountNumber(accountNumber);
        }else if(toTimeMills==null){
            list = measureService.findByAccountNumberAndFromTime(accountNumber, new Timestamp((Long) map.get("fromTime")));
        }else{
            list = measureService.findByAccountNumberAndCommitTime(accountNumber, new Timestamp((Long) map.get("fromTime")),new Timestamp(toTimeMills));
        }
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(map, list));
    }
}
