package com.controller;

import com.Exception.TokenException;
import com.service.MeasureService;
import com.service.TokenService;
import com.util.JSONUtil;
import com.util.ListToMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
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
        if (accountNumber == null) throw new TokenException();
        Long timeMills = (Long) map.get("timestamp");
        List list;
        if (timeMills == null) list = measureService.findTodayDataByAccountNumber(accountNumber);
        else list = measureService.findByAccountNumberAndDate(accountNumber, new Timestamp(timeMills));
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(map, list));
    }

    @RequestMapping(value = "{token}/latest", method = RequestMethod.POST)
    public String getEachDateLatestByDateRange(@PathVariable String token, @RequestBody(required = false) String mapString) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber = tokenService.getAccountNumber(token);
        if (accountNumber == null) throw new TokenException();
        Long fromTimeMills = (Long) map.get("fromTime");
        Long toTimeMills = (Long) map.get("toTime");
        List list;
        if (fromTimeMills == null && toTimeMills == null) {
            list = new ArrayList();
            list.add(measureService.findTheLatestByAccountNumber(accountNumber));
        } else if (fromTimeMills == null)
            list = measureService.findTheLatestOfDateByAccountNumberAndDateRange(accountNumber, new Timestamp(toTimeMills));
        else if (toTimeMills == null)
            list = measureService.findTheLatestOfDateByAccountNumberAndDateRange(accountNumber, new Timestamp(fromTimeMills), new Timestamp(System.currentTimeMillis()));
        else
            list = measureService.findTheLatestOfDateByAccountNumberAndDateRange(accountNumber, new Timestamp(fromTimeMills), new Timestamp(toTimeMills));
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(map, list));
    }

    @RequestMapping(value = "{token}/data", method = RequestMethod.POST)
    public String getAllByTimeRange(@PathVariable String token, @RequestBody(required = false) String mapString) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber = tokenService.getAccountNumber(token);
        if (accountNumber == null) throw new TokenException();
        Long fromTimeMills = (Long) map.get("fromTime");
        Long toTimeMills = (Long) map.get("toTime");
        List list;
        if (fromTimeMills == null && toTimeMills == null) {
            list = measureService.findAllDataByAccountNumber(accountNumber);
        } else if (toTimeMills == null)
            list = measureService.findByAccountNumberAndFromTime(accountNumber, new Timestamp(fromTimeMills));
        else if (fromTimeMills == null)
            list = measureService.findByAccountNumberAndCommitTime(accountNumber, new Timestamp(0), new Timestamp(toTimeMills));
        else
            list = measureService.findByAccountNumberAndCommitTime(accountNumber, new Timestamp(fromTimeMills), new Timestamp(toTimeMills));

        return JSONUtil.toJSON(ListToMapUtil.ListToMap(map, list));
    }
}
