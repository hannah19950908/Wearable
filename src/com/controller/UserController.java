package com.controller;

import com.entity.UserEntity;
import com.service.UserService;
import com.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.util.DigestUtil.Md5Encoder;

/**
 * Created by 63289 on 2017/2/25.
 */
@RestController
@RequestMapping("User")
@SessionAttributes("accountNumber")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@RequestBody String mapString, Model model) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber = (String) map.get("accountNumber");
        String password = Md5Encoder((String) map.get("password"));
        map.put("password", password);
        UserEntity userEntity = userService.findByAccountNumberAndPassword(accountNumber, password);
        if (userEntity == null) {
            map.put("status", 1);
        } else {
            map.put("status", 0);
            //将用户名放在session中。
            model.addAttribute("accountNumber", accountNumber);
        }
        return JSONUtil.toJSON(map);
    }

    @RequestMapping(value = "check", method = RequestMethod.POST)
    public String check(@RequestBody String mapString) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber = (String) map.get("accountNumber");
        UserEntity userEntity = userService.findByAccountNumber(accountNumber);
        if (userEntity == null) {
            map.put("status", 0);
        } else {
            map.put("status", 1);
        }
        return JSONUtil.toJSON(map);
    }

    @RequestMapping(value = "join", method = RequestMethod.POST)
    public String join(@RequestBody String mapString, Model model) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String accountNumber = (String) map.get("accountNumber");
        UserEntity userEntity = userService.findByAccountNumber(accountNumber);
        map.put("status", 1);
        if (userEntity == null) {
            System.out.println(userEntity);
            String password = Md5Encoder((String) map.get("password"));
            map.put("password", password);
            String userName = (String) map.get("userName");
            String phone = (String) map.get("phone");
            String relativeName = (String) map.get("relativeName");
            String relativePhone = (String) map.get("relativePhone");
            String email = (String) map.get("email");
            if (userService.addByInformation(accountNumber, password, userName, phone, relativeName, relativePhone, email)) {
                map.put("status", 0);
                model.addAttribute("accountNumber", accountNumber);
            } else {
                map.put("status", 2);
            }
        }
        return JSONUtil.toJSON(map);
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String edit(@RequestBody String mapString, @ModelAttribute("accountNumber") String accountNumber, Model model) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        String oldPassword = Md5Encoder((String) map.get("oldPassword"));
        String password = userService.findByAccountNumber(accountNumber).getPassword();
        map.put("status", 1);
        if (oldPassword.equals(password)) {
            String newPassword = Md5Encoder((String) map.get("newPassword"));
            String userName = (String) map.get("userName");
            String phone = (String) map.get("phone");
            String relativeName = (String) map.get("relativeName");
            String relativePhone = (String) map.get("relativePhone");
            String email = (String) map.get("email");
            if (userService.updateByInformation(accountNumber, oldPassword, newPassword, userName, phone, relativeName, relativePhone, email)) {
                map.put("status", 0);
            }
        }
        return JSONUtil.toJSON(map);
    }

    @RequestMapping("display")
    public String display(@ModelAttribute("accountNumber") String accountNumber) throws Exception {
        Map map = new HashMap();
        UserEntity userEntity = userService.findByAccountNumber(accountNumber);
        userEntity.setPassword(null);
        if (userEntity == null) {
            map.put("status", 1);
        } else {
            map.put("status", 0);
            map.put("user", userEntity);
        }
        return JSONUtil.toJSON(map);
    }
    //暂时不可用，可以做伪登出。
    @RequestMapping("logout")
    public String logout(@ModelAttribute("accountNumber")String accountNumber, ModelMap model) throws Exception {
        Map map = new HashMap();
        model.remove("accountNumber");
        if(model.get("accountNumber")==null){
            map.put("status", 0);
        }else map.put("status",1);
        return JSONUtil.toJSON(map);
    }
    //删除账户
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String delete(@RequestBody String mapString, @ModelAttribute("accountNumber") String accountNumber) throws Exception {
        Map map = JSONUtil.parseMap(mapString);
        if (userService.deleteByAccountNumberAndPassword(accountNumber, (String) map.get("password"))) {
            map.put("status", 0);
        } else {
            map.put("status", 1);
        }
        return JSONUtil.toJSON(map);
    }
}
