package com.service;

import com.dao.TokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by 63289 on 2017/4/27.
 */
@Service
public class TokenService {
    private final TokenDao tokenDao;

    @Autowired
    public TokenService(TokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

    public String generateToken(String accoutNumber){
        String token= UUID.randomUUID().toString().replace("-","");
        tokenDao.put(token,accoutNumber);
        return token;
    }

    public String getAccountNumber(String token){
        return tokenDao.get(token);
    }

    public void delete(String token){
        tokenDao.delete(token);
    }
}
