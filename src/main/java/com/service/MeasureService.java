package com.service;

import com.Exception.RequireInformationException;
import com.Exception.SQLNotFoundException;
import com.dao.MeasureDao;
import com.entity.MeasureEntity;
import com.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
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

    // 查询某账号所有数据
    public List findAllDataByAccountNumber(String accountNumber) {
        return measureDao.findByAccountNumber(accountNumber);
    }

    //通过起止时间查询某账户某时间段内的数据
    public List findByAccountNumberAndCommitTime(String accountNumber, Timestamp fromTime, Timestamp toTime) {
        return measureDao.findByAccountNumberAndTime(accountNumber, fromTime, toTime);
    }

    //通过终止时间查询某账户内的数据
    public List findByAccountNumberAndCommitTime(String accountNumber, Timestamp toTime) throws Exception {
        MeasureEntity measureEntity = measureDao.findTheFirstByAccountNumber(accountNumber);
        if (measureEntity == null) throw new SQLNotFoundException();
        return findByAccountNumberAndCommitTime(accountNumber, measureEntity.getCommitTime(), toTime);
    }

    //查询某账户从某时间点到现在的所有数据
    public List findByAccountNumberAndFromTime(String accountNumber, Timestamp fromTime) {
        return measureDao.findByAccountNumberAndTime(accountNumber, fromTime, Timestamp.valueOf(LocalDateTime.now()));
    }

    //查询某账户今日的数据
    public List findTodayDataByAccountNumber(String accountNumber) {
        Timestamp fromTime = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        Timestamp toTime = Timestamp.valueOf(LocalDateTime.now());
        return measureDao.findByAccountNumberAndTime(accountNumber, fromTime, toTime);
    }

    //查询某账户某日的数据，参数为时间戳
    public List findByAccountNumberAndDate(String accountNumber, Timestamp timestamp) {
        LocalDate localDate = timestamp.toLocalDateTime().toLocalDate();
        return findByAccountNumberAndDate(accountNumber, localDate);
    }

    //查询某账户某日的数据，参数为LocalDate
    private List findByAccountNumberAndDate(String accountNumber, LocalDate localDate) {
        Timestamp fromTime = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp toTime = Timestamp.valueOf(localDate.plusDays(1).atStartOfDay());
        return measureDao.findByAccountNumberAndTime(accountNumber, fromTime, toTime);
    }

    //查询某账户最新数据
    public MeasureEntity findTheLatestByAccountNumber(String accountNumber) {
        return measureDao.findTheLatestByAccountNumber(accountNumber);
    }

    //查询某账户一段日期中每天的最后一条数据，参数为2个时间戳
    public List<MeasureEntity> findTheLatestOfDateByAccountNumberAndDateRange(String accountNumber, Timestamp startTime, Timestamp toTime) throws Exception {
        MeasureEntity measureEntity = measureDao.findTheFirstByAccountNumber(accountNumber);
        if (measureEntity == null) throw new SQLNotFoundException();
        Timestamp firstTime = measureEntity.getCommitTime();
        if (firstTime.after(startTime)) startTime = firstTime;
        LocalDate fromLocalDate = startTime.toLocalDateTime().toLocalDate();
        LocalDate toLocalDate = toTime.toLocalDateTime().toLocalDate();
        return findTheLatestOfDateByAccountNumberAndDateRange(accountNumber, fromLocalDate, toLocalDate);
    }

    //查询某账户某个时间前一年中每天的最后一条数据，参数为2个时间戳
    public List<MeasureEntity> findTheLatestOfDateByAccountNumberAndDateRange(String accountNumber, Timestamp toTime) throws Exception {
        MeasureEntity measureEntity = measureDao.findTheFirstByAccountNumber(accountNumber);
        if (measureEntity == null) throw new SQLNotFoundException();
        LocalDate fromLocalDate = measureEntity.getCommitTime().toLocalDateTime().toLocalDate();
        LocalDate toLocalDate = toTime.toLocalDateTime().toLocalDate();
        return findTheLatestOfDateByAccountNumberAndDateRange(accountNumber, fromLocalDate, toLocalDate);
    }

    //查询某账户一段日期中每天的最后一条数据，参数为2个LocalDate
    private List<MeasureEntity> findTheLatestOfDateByAccountNumberAndDateRange(String accountNumber, LocalDate fromLocalDate, LocalDate toLocalDate) {
        System.out.println(accountNumber);
        List<MeasureEntity> list = new ArrayList<>();
        for (Long i = new Long(0); i <= toLocalDate.toEpochDay() - fromLocalDate.toEpochDay(); ++i) {
            MeasureEntity measureEntity = measureDao.findTheLatestByAccountNumberAndTime
                    (accountNumber, Timestamp.valueOf(fromLocalDate.plusDays(i).atStartOfDay()), Timestamp.valueOf(fromLocalDate.plusDays(i + 1).atStartOfDay()));
            if (measureEntity != null) {
                list.add(measureEntity);
            }
        }
        return list;
    }
    //添加数据
    public void addData(String accountNumber,Long commitTimeMills,String device,Integer step, Integer distance, Integer heart){
        MeasureEntity measureEntity=new MeasureEntity();
        measureEntity.setAccountNumber(accountNumber);
        measureEntity.setCommitTime(new Timestamp(commitTimeMills));
        measureEntity.setDevice(device);
        measureEntity.setStep(step);
        measureEntity.setDistance(distance);
        measureEntity.setHeart(heart);
        measureDao.add(measureEntity);
    }
}
