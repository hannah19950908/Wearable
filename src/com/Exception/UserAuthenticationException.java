package com.Exception;

import javax.naming.AuthenticationException;
import java.util.Map;

/**
 * Created by 63289 on 2017/4/25.
 */
public class UserAuthenticationException extends Exception {
    public Map map;
    public UserAuthenticationException(Map map){
        this.map=map;
    }
}
