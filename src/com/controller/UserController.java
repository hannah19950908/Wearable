package com.controller;

import com.entity.UserEntity;
import com.service.UserService;
import com.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import static org.apache.commons.codec.digest.Sha2Crypt.sha256Crypt;

/**
 * Created by 63289 on 2017/2/25.
 */
@Controller
@RequestMapping("User")
@SessionAttributes({"accountNumber", "password"})
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody String mapString, ModelMap modelMap) throws Exception {
        modelMap = (ModelMap) JSONUtil.parseMap(mapString);
        String accountNumber = (String) modelMap.get("accountNumber");
        String password = sha256Crypt(((String) modelMap.get("password")).getBytes());
        UserEntity userEntity = userService.findByAccountNumberAndPassword(accountNumber, password);
        if (userEntity != null) {
            modelMap.put("status", 0);
        } else {
            modelMap.put("status", 1);
        }
        return JSONUtil.toJSON(modelMap);
    }

    @RequestMapping(value = "join", method = RequestMethod.POST)
    @ResponseBody
    public String join(@RequestBody String mapString, ModelMap modelMap) throws Exception {
        modelMap = (ModelMap) JSONUtil.parseMap(mapString);
        String accountNumber = (String) modelMap.get("accountNumber");
        //TODO
        return JSONUtil.toJSON(modelMap);
    }
}
