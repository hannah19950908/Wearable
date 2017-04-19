package com.service;

import com.dao.MeasureDao;
import com.entity.MeasureEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 63289 on 2017/4/18.
 */
@Service
public class MeasureService {
    private final MeasureDao measureDao;

    @Autowired
    public MeasureService(MeasureDao measureDao) {
        this.measureDao = measureDao;
    }

    public List findByAccountNumber(String accountNumber) {
        MeasureEntity measureEntity = new MeasureEntity();
        measureEntity.setAccountNumber(accountNumber);
        return measureDao.findByExample(measureEntity);
    }

    public List findByAccountNumberAndCommitTime(String accountNumber, Timestamp fromTime, Timestamp toTime) {
        return measureDao.findByAccountNumberAndTime(accountNumber, fromTime, toTime);
    }

    public List findByAccountNumberAndCommitTime(String accountNumber, Timestamp fromTime) {
        return measureDao.findByAccountNumberAndTime(accountNumber, fromTime, Timestamp.valueOf(LocalDateTime.now()));
    }

    public List findByAccountNumberAndCommitTime(String accountNumber) {
        Timestamp fromTime = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        Timestamp toTime = Timestamp.valueOf(LocalDateTime.now());
        return measureDao.findByAccountNumberAndTime(accountNumber, fromTime, toTime);
    }

    public List findByAccountNumberAndDate(String accountNumber,int year, int month, int day) {
        LocalDate localDate=LocalDate.of(year,month,day);
        Timestamp fromTime=Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp toTime=Timestamp.valueOf(localDate.plusDays(1).atStartOfDay());
        return measureDao.findByAccountNumberAndTime(accountNumber,fromTime,toTime);
    }

    public MeasureEntity findTheLatestByAccountNumber(String accountNumber){
        return measureDao.findTheLatestMeasureByAccountNumber(accountNumber);
    }

    public List<MeasureEntity> findTheLatestOfDateByAccountNumberAndDateRange
            (String accountNumber,int startYear,int startMonth,int startDay, int toYear,int toMonth,int toDay){
        LocalDate fromLocalDate=LocalDate.of(startYear,startMonth,startDay);
        LocalDate toLocalDate=LocalDate.of(toYear,toMonth,toDay);
        return findTheLatestOfDateByAccountNumberAndDateRange(accountNumber,fromLocalDate,toLocalDate);
    }
    public List<MeasureEntity> findTheLatestOfDateByAccountNumberAndDateRange(String accountNumber,Timestamp startTime,Timestamp toTime){
        LocalDate fromLocalDate=startTime.toLocalDateTime().toLocalDate();
        LocalDate toLocalDate=toTime.toLocalDateTime().toLocalDate();
        return findTheLatestOfDateByAccountNumberAndDateRange(accountNumber,fromLocalDate,toLocalDate);
    }
    private List<MeasureEntity> findTheLatestOfDateByAccountNumberAndDateRange(String accountNumber,LocalDate fromLocalDate,LocalDate toLocalDate){
        List<MeasureEntity> list=new ArrayList<MeasureEntity>();
        for(LocalDate day=fromLocalDate;day!=toLocalDate.plusDays(1);day=day.plusDays(1)){
            List measureEntitys = measureDao.findByAccountNumberAndTimeDesc
                    (accountNumber,Timestamp.valueOf(day.atStartOfDay()),Timestamp.valueOf(day.plusDays(1).atStartOfDay()));
            if(measureEntitys!=null&&!measureEntitys.isEmpty()){
                list.add((MeasureEntity) measureEntitys.get(0));
            }
        }
        return list;
    }
}
