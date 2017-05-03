package com.controller;

import com.Exception.RequireInformationException;
import com.Exception.TokenException;
import com.entity.MeasureEntity;
import com.service.MeasureService;
import com.service.TokenService;
import com.util.JSONUtil;
import com.util.ListToMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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

    @RequestMapping({"{token}/date",
            "{token}/date/{timeMills}"})
    public String getByDate
            (@PathVariable String token, @PathVariable(required = false) Long timeMills)
            throws Exception {
        String accountNumber = tokenService.getAccountNumber(token);
        if (accountNumber == null) throw new TokenException();
        List list;
        if (timeMills == null) list = measureService.findTodayDataByAccountNumber(accountNumber);
        else list = measureService.findByAccountNumberAndDate(accountNumber, new Timestamp(timeMills));
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(list));
    }

    @RequestMapping({"{token}/latest",
            "{token}/latest/{fromTimeMills}",
            "{token}/latest/{fromTimeMills}/",
            "{token}/latest/t{toTimeMills}",
            "{token}/latest/{fromTimeMills}/{toTimeMills}"})
    public String getEachDateLatestByDateRange
            (@PathVariable String token, @PathVariable(required = false) Long fromTimeMills,@PathVariable(required = false) Long toTimeMills)
            throws Exception {
        String accountNumber = tokenService.getAccountNumber(token);
        if (accountNumber == null) throw new TokenException();
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
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(list));
    }

    @RequestMapping({"{token}/data",
            "{token}/data/{fromTimeMills}",
            "{token}/data/{fromTimeMills}/",
            "{token}/data/t{toTimeMills}",
            "{token}/data/{fromTimeMills}/{toTimeMills}"})
    public String getAllByTimeRange
            (@PathVariable String token,@PathVariable(required = false) Long fromTimeMills,@PathVariable(required = false) Long toTimeMills)
            throws Exception {
        String accountNumber = tokenService.getAccountNumber(token);
        if (accountNumber == null) throw new TokenException();
        List list;
        if (fromTimeMills == null && toTimeMills == null) {
            list = measureService.findAllDataByAccountNumber(accountNumber);
        } else if (toTimeMills == null)
            list = measureService.findByAccountNumberAndFromTime(accountNumber, new Timestamp(fromTimeMills));
        else if (fromTimeMills == null)
            list = measureService.findByAccountNumberAndCommitTime(accountNumber, new Timestamp(0), new Timestamp(toTimeMills));
        else
            list = measureService.findByAccountNumberAndCommitTime(accountNumber, new Timestamp(fromTimeMills), new Timestamp(toTimeMills));
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(list));
    }
    @RequestMapping(value = "{token}",method = RequestMethod.POST)
    public void addData(@PathVariable String token,@RequestBody String mapString) throws Exception{
        Map map=JSONUtil.parseMap(mapString);
        String accountNumber=tokenService.getAccountNumber(token);
        Long commitTimeMills=(Long)map.get("commitTime");
        String device=(String)map.get("device");
        Integer step=(Integer)map.get("step");
        Integer distance=(Integer)map.get("distance");
        Integer heart=(Integer)map.get("heart");
        measureService.addData(accountNumber,commitTimeMills,device,step,distance,heart);
    }
}
