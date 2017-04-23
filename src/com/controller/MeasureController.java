package com.controller;

import com.entity.MeasureEntity;
import com.service.MeasureService;
import com.service.UserService;
import com.util.JSONUtil;
import com.util.ListToMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by 63289 on 2017/4/19.
 */
@CrossOrigin(value = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "Measure", produces = "application/json;charset=UTF-8")
@SessionAttributes("accountNumber")
public class MeasureController {
    private final MeasureService measureService;
    private final UserService userService;

    @Autowired
    public MeasureController(MeasureService measureService, UserService userService) {
        this.measureService = measureService;
        this.userService = userService;
    }

    @RequestMapping("getLatest")
    public String getLatest(@RequestBody(required = false) String mapString, ModelMap model) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber=userService.getAccountNumber(model,map);
        if(accountNumber==null){
            map.put("status",3);
            return JSONUtil.toJSON(map);
        }
        MeasureEntity measureEntity = measureService.findTheLatestByAccountNumber(accountNumber);
        if (measureEntity != null) {
            map.put("status", 0);
            map.put("measure", measureEntity);
        } else map.put("status", 1);
        return JSONUtil.toJSON(map);
    }

    @RequestMapping("getToday")
    public String getToday(@RequestBody(required = false) String mapString, ModelMap model) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber=userService.getAccountNumber(model,map);
        if(accountNumber==null){
            map.put("status",3);
            return JSONUtil.toJSON(map);
        }
        List list = measureService.findTodayDataByAccountNumber(accountNumber);
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(list));
    }

    @RequestMapping(value = "getByDate", method = RequestMethod.POST)
    public String getByDate(@RequestBody String mapString, ModelMap model) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber=userService.getAccountNumber(model,map);
        if(accountNumber==null){
            map.put("status",3);
            return JSONUtil.toJSON(map);
        }
        List list = measureService.findByAccountNumberAndDate(accountNumber, new Timestamp((Long) map.get("timestamp")));
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(map, list));
    }

    @RequestMapping(value = "getEachDateLatestByDateRange", method = RequestMethod.POST)
    public String getEachDateLatestByDateRange(@RequestBody String mapString, ModelMap model) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber=userService.getAccountNumber(model,map);
        if(accountNumber==null){
            map.put("status",3);
            return JSONUtil.toJSON(map);
        }
        List list = measureService.findTheLatestOfDateByAccountNumberAndDateRange(accountNumber,
                new Timestamp((Long) map.get("fromTime")), new Timestamp((Long) map.get("toTime")));
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(map, list));
    }

    @RequestMapping("getAll")
    public String getAll(@RequestBody(required = false) String mapString, ModelMap model) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber=userService.getAccountNumber(model,map);
        if(accountNumber==null){
            map.put("status",3);
            return JSONUtil.toJSON(map);
        }
        List list = measureService.findAllDataByAccountNumber(accountNumber);
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(map, list));
    }

    @RequestMapping(value = "getAllByTimeRange", method = RequestMethod.POST)
    public String getAllByTimeRange(@RequestBody String mapString, ModelMap model) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber=userService.getAccountNumber(model,map);
        if(accountNumber==null){
            map.put("status",3);
            return JSONUtil.toJSON(map);
        }
        List list = measureService.findByAccountNumberAndCommitTime(accountNumber,
                new Timestamp((Long) map.get("fromTime")), new Timestamp((Long) map.get("toTime")));
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(map, list));
    }

    @RequestMapping(value = "getAllByFromTime", method = RequestMethod.POST)
    public String getAllByFromTime(@RequestBody String mapString, ModelMap model) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber=userService.getAccountNumber(model,map);
        if(accountNumber==null){
            map.put("status",3);
            return JSONUtil.toJSON(map);
        }
        List list = measureService.findByAccountNumberAndFromTime(accountNumber, new Timestamp((Long) map.get("fromTime")));
        return JSONUtil.toJSON(ListToMapUtil.ListToMap(map, list));
    }
}
