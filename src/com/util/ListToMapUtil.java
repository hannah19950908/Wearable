package com.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 63289 on 2017/4/19.
 */
public class ListToMapUtil {
    public static Map ListToMap(List list){
        Map map=new HashMap();
        return ListToMap(map,list);
    }
    public static Map ListToMap(Map map,List list){
        if(list==null||list.isEmpty()){
            map.put("status",1);
        }else{
            map.put("status",0);
            map.put("measures",list);
        }
        return map;
    }
}
