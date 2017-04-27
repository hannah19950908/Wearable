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

    @RequestMapping("{token}/latest")
    public String getLatest(@PathVariable String token) throws Exception {
        Map map = new HashMap();
        String accountNumber = tokenService.getAccountNumber(token);
        if (accountNumber == null) throw new TokenException();
        MeasureEntity measureEntity = measureService.findTheLatestByAccountNumber(accountNumber);
        if (measureEntity != null) {
            map.put("measure", measureEntity);
        } else throw new SQLNotFoundException();
        return JSONUtil.toJSON(map);
    }

    @RequestMapping(value = "{token}/someDayData",method = RequestMethod.GET)
    public String getToday(@PathVariable String token) throws Exception {
        String accountNumber = tokenService.getAccountNumber(token);
        if(accountNumber==null) throw new TokenException();
        List list = measureService.findTodayDataByAccountNumber(accountNumber);
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(list));
    }

    @RequestMapping(value = "{token}/someDayData", method = RequestMethod.POST)
    public String getByDate(@PathVariable String token, @RequestBody String mapString) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber = tokenService.getAccountNumber(token);
        if(accountNumber==null) throw new TokenException();
        List list = measureService.findByAccountNumberAndDate(accountNumber, new Timestamp((Long) map.get("timestamp")));
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(map, list));
    }

    @RequestMapping(value = "{token}/latestByDateRange", method = RequestMethod.POST)
    public String getEachDateLatestByDateRange(@PathVariable String token, @RequestBody String mapString) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber = tokenService.getAccountNumber(token);
        if(accountNumber==null) throw new TokenException();
        List list = measureService.findTheLatestOfDateByAccountNumberAndDateRange(accountNumber,
                new Timestamp((Long) map.get("fromTime")), new Timestamp((Long) map.get("toTime")));
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(map, list));
    }

    @RequestMapping("{token}/data")
    public String getAll(@PathVariable String token) throws Exception {
        String accountNumber = tokenService.getAccountNumber(token);
        if(accountNumber==null) throw new TokenException();
        List list = measureService.findAllDataByAccountNumber(accountNumber);
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(list));
    }

    @RequestMapping(value = "{token}/dataByTimeRange", method = RequestMethod.POST)
    public String getAllByTimeRange(@PathVariable String token, @RequestBody String mapString) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber = tokenService.getAccountNumber(token);
        if(accountNumber==null) throw new TokenException();
        Timestamp toTime=new Timestamp((Long) map.get("toTime"));
        List list;
        if(toTime==null){
            list = measureService.findByAccountNumberAndFromTime(accountNumber, new Timestamp((Long) map.get("fromTime")));
        }else{
            list = measureService.findByAccountNumberAndCommitTime(accountNumber, new Timestamp((Long) map.get("fromTime")),toTime);
        }
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(map, list));
    }
}
