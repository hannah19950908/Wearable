package com.service;

import com.dao.UserDao;
import com.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by 63289 on 2017/2/25.
 */
@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean addByInformation(String accountNumber, String password, String userName, String phone, String relativeName,
                                    String relativePhone, String email){
        UserEntity userEntity=new UserEntity();
        userEntity.setAccountNumber(accountNumber);
        if(userDao.findByExample(userEntity).isEmpty()){
            userEntity.setPassword(password);
            userEntity.setUserName(userName);
            userEntity.setPhone(phone);
            userEntity.setRelativeName(relativeName);
            userEntity.setRelativePhone(relativePhone);
            userEntity.setEmail(email);
            userEntity.toString();
            userDao.add(userEntity);
            return true;
        }
        return false;
    }
    public UserEntity findByAccountNumberAndPassword(String accountNumber, String password){
        UserEntity userEntity=new UserEntity();
        userEntity.setPassword(password);
        List list=userDao.findByExample(userEntity);
        return list.isEmpty()?null:(UserEntity) list.get(0);
    }

    public UserEntity findByAccountNumber(String accountNumber){
        UserEntity userEntity=new UserEntity();
        userEntity.setAccountNumber(accountNumber);
        List list=userDao.findByExample(userEntity);
        return list.isEmpty()?null:(UserEntity) list.get(0);
    }

    public boolean updateByInformation(String accountNumber, String oldPassword, String newPassword, String userName, String phone, String relativeName,
                                    String relativePhone, String email){
        UserEntity userEntity=new UserEntity();
        userEntity.setAccountNumber(accountNumber);
        userEntity.setPassword(oldPassword);
        if(!userDao.findByExample(userEntity).isEmpty()){
            if(newPassword!=null) userEntity.setPassword(newPassword);
            if(userName!=null) userEntity.setUserName(userName);
            if(phone!=null) userEntity.setPhone(phone);
            if(relativeName!=null) userEntity.setRelativeName(relativeName);
            if(relativePhone!=null) userEntity.setRelativePhone(relativePhone);
            if(email!=null) userEntity.setEmail(email);
            userDao.update(userEntity);
            return true;
        }
        return false;
    }
    public boolean deleteByAccountNumberAndPassword(String accountNumber, String password){
        UserEntity userEntity=new UserEntity();
        userEntity.setAccountNumber(accountNumber);
        userEntity.setPassword(password);
        if(userDao.findByExample(userEntity).isEmpty()){
            return false;
        }
        userDao.delete(userEntity);
        return true;
    }
}
