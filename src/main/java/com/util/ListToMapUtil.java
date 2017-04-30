package com.util;

import com.Exception.SQLNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 63289 on 2017/4/19.
 */
public class ListToMapUtil {
    public static Map ListToMap(List list)throws Exception{
        Map map=new HashMap();
        return ListToMap(map,list);
    }
    public static Map ListToMap(Map map,List list)throws Exception{
        if(list==null||list.isEmpty()){
            throw new SQLNotFoundException();
        }else{
            map.put("measures",list);
        }
        return map;
    }
}
