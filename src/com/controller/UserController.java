package com.controller;

import com.Exception.TokenException;
import com.Exception.UserSetupException;
import com.entity.UserEntity;
import com.service.TokenService;
import com.service.UserService;
import com.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

import static com.util.DigestUtil.Md5Encoder;

/**
 * Created by 63289 on 2017/2/25.
 */
@CrossOrigin(value = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "api", produces = "application/json;charset=UTF-8")
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "token", method = RequestMethod.POST)
    public String login(@RequestBody String mapString) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber = (String) map.get("accountNumber");
        String password = Md5Encoder((String) map.get("password"));
        map.put("password", password);
        UserEntity userEntity = userService.findByAccountNumberAndPassword(accountNumber, password);
        if (userEntity == null) {
            throw new AuthenticationException();
        } else {
            map.put("token", tokenService.generateToken(accountNumber));
        }
        return JSONUtil.toJSON(map);
    }

    @RequestMapping(value = "user", method = RequestMethod.POST)
    public String setup(@RequestBody String mapString) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber = (String) map.get("accountNumber");
        UserEntity userEntity = userService.findByAccountNumber(accountNumber);
        if (userEntity == null) {
            System.out.println(userEntity);
            String password = Md5Encoder((String) map.get("password"));
            map.put("password", password);
            String userName = (String) map.get("userName");
            String phone = (String) map.get("phone");
            String relativeName = (String) map.get("relativeName");
            String relativePhone = (String) map.get("relativePhone");
            String email = (String) map.get("email");
            if (userService.addByInformation(accountNumber, password, userName, phone, relativeName, relativePhone, email))
                map.put("token", tokenService.generateToken(accountNumber));
            else throw new RuntimeException();
        } else throw new UserSetupException();
        return JSONUtil.toJSON(map);
    }

    @RequestMapping(value = "{token}", method = RequestMethod.GET)
    public String display(@PathVariable String token) throws Exception {
        Map map = new HashMap();
        String accountNumber = tokenService.getAccountNumber(token);
        if(accountNumber==null) throw new TokenException();
        UserEntity userEntity = userService.findByAccountNumber(accountNumber);
        userEntity.setPassword(null);
        map.put("user", userEntity);
        return JSONUtil.toJSON(map);
    }

    @RequestMapping(value = "{token}", method = RequestMethod.PUT)
    public String edit(@PathVariable String token, @RequestBody String mapString) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber = tokenService.getAccountNumber(token);
        if(accountNumber==null) throw new TokenException();
        String newPasswordNotEncoded = (String) map.get("newPassword");
        String newPassword = null;
        if (newPasswordNotEncoded != null) {
            newPassword = Md5Encoder(newPasswordNotEncoded);
        }
        String userName = (String) map.get("userName");
        String phone = (String) map.get("phone");
        String relativeName = (String) map.get("relativeName");
        String relativePhone = (String) map.get("relativePhone");
        String email = (String) map.get("email");
        if (!userService.updateByInformation(accountNumber, newPassword, userName, phone, relativeName, relativePhone, email))
            throw new RuntimeException();
        return JSONUtil.toJSON(map);
    }

    @RequestMapping(value = "{token}", method = RequestMethod.DELETE)
    public void logout(@PathVariable String token) throws Exception {
        String accountNumber = tokenService.getAccountNumber(token);
        if(accountNumber==null) throw new TokenException();
        tokenService.delete(token);
    }

    @RequestMapping(value = "{token}/user", method = RequestMethod.DELETE)
    public void delete(@PathVariable String token) throws Exception {
        String accountNumber = tokenService.getAccountNumber(token);
        if(accountNumber==null) throw new TokenException();
        tokenService.delete(token);
        if(!userService.deleteByAccountNumber(accountNumber)) throw new RuntimeException();
    }
}
