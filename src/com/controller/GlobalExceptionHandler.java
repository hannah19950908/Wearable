package com.controller;

import com.Exception.UserAuthenticationException;
import com.util.JSONUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * Created by 63289 on 2017/4/25.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException{
    @ExceptionHandler(UserAuthenticationException.class)
    //@ResponseStatus(value = HttpStatus.FORBIDDEN,reason = "Wrong accountNumber or wrong password")
    @ResponseBody
    public String userAuthenticationHandler(UserAuthenticationException ex)throws Exception{
        ex.map.put("status",3);
        return JSONUtil.toJSON(ex.map);
    }
}
