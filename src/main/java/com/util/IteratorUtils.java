package com.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 63289 on 2017/5/11.
 */
public class IteratorUtils {
    public static List toList(Iterator iterator){
        List list=new ArrayList();
        while (iterator.hasNext()){
            list.add(iterator.next());
        }
        return list;
    }
}
