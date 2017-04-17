package com.controller;

import com.entity.UserEntity;
import com.service.UserService;
import com.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.codec.digest.Sha2Crypt.sha256Crypt;

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
        Map map=JSONUtil.parseMap(mapString);
        String accountNumber = (String) map.get("accountNumber");
        String password = sha256Crypt(((String) map.get("password")).getBytes());
        map.put("password",password);
        UserEntity userEntity = userService.findByAccountNumberAndPassword(accountNumber, password);
        if (userEntity != null) {
            map.put("status", 0);
            model.addAllAttributes(map);
        } else {
            map.put("status", 1);
        }
        return JSONUtil.toJSON(map);
    }

    @RequestMapping(value = "check", method = RequestMethod.POST)
    public String check(@RequestBody String mapString) throws Exception {
        Map map=JSONUtil.parseMap(mapString);
        String accountNumber = (String) map.get("accountNumber");
        UserEntity userEntity=userService.findByAccountNumber(accountNumber);
        if(userEntity==null){
            map.put("status",0);
        }else{
            map.put("status",1);
        }
        return JSONUtil.toJSON(map);
    }

    @RequestMapping(value = "join",method=RequestMethod.POST)
    public String join(@RequestBody String mapString,Model model) throws Exception{
        Map map=JSONUtil.parseMap(mapString);
        String accountNumber = (String) map.get("accountNumber");
        UserEntity userEntity=userService.findByAccountNumber(accountNumber);
        map.put("status",1);
        if(userEntity==null){
            String password = sha256Crypt(((String) map.get("password")).getBytes());
            String userName=(String)map.get("userName");
            String phone=(String)map.get("phone");
            String relativeName=(String)map.get("relativeName");
            String relativePhone=(String)map.get("relativePhone");
            String email=(String)map.get("email");
            if(userService.addByInformation(accountNumber,password,userName,phone,relativeName,relativePhone,email)){
                map.put("status",0);
                model.addAttribute("accountNumber",accountNumber);
                model.addAttribute("password",password);
            }
        }
        return JSONUtil.toJSON(map);
    }

    @RequestMapping(value="edit",method = RequestMethod.POST)
    public String edit(@RequestBody String mapString,@ModelAttribute("accountNumber") String accountNumber,Model model) throws Exception{
        Map map=JSONUtil.parseMap(mapString);
        String oldPassword = sha256Crypt(((String) map.get("oldPassword")).getBytes());
        String password=userService.findByAccountNumber(accountNumber).getPassword();
        map.put("status",1);
        if(oldPassword==password){
            String newPassword = sha256Crypt(((String) map.get("newPassword")).getBytes());
            String userName=(String)map.get("userName");
            String phone=(String)map.get("phone");
            String relativeName=(String)map.get("relativeName");
            String relativePhone=(String)map.get("relativePhone");
            String email=(String)map.get("email");
            model.addAttribute("password",newPassword);
            if(userService.updateByInformation(accountNumber,oldPassword,newPassword,userName,phone,relativeName,relativePhone,email)){
                map.put("status",0);
            }
        }
        return JSONUtil.toJSON(map);
    }

    @RequestMapping("display")
    public String display(@ModelAttribute("accountNumber") String accountNumber) throws Exception{
        Map map=new HashMap();
        UserEntity userEntity=userService.findByAccountNumber(accountNumber);
        userEntity.setPassword(null);
        if(userEntity!=null){
            map.put("status",0);
            map.put("user",userEntity);
        }else{
            map.put("status",1);
        }
        return JSONUtil.toJSON(map);
    }

    @RequestMapping("logout")
    public String logout(Model model) throws Exception{
        Map map=new HashMap();
        model.addAttribute("acountNumber","");
        map.put("status",0);
        return JSONUtil.toJSON(map);
    }

    @RequestMapping(value = "delete",method = RequestMethod.POST)
    public String delete(String mapString, @ModelAttribute("accountNumber") String accountNumber, Model model) throws Exception{
        Map map=JSONUtil.parseMap(mapString);
        if(userService.deleteByAccountNumberAndPassword(accountNumber,(String)map.get("password"))){
            map.put("status",0);
            model.addAttribute("acountNumber","");
        }else{
            map.put("status",1);
        }
        return JSONUtil.toJSON(map);
    }
}
