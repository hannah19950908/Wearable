package com.dao;

import com.entity.MeasureEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by 63289 on 2017/4/18.
 */
@Repository
public class MeasureDao {
    private final HibernateTemplate hibernateTemplate;

    @Autowired
    public MeasureDao(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
    @Transactional
    public void add(MeasureEntity measureEntity){
        hibernateTemplate.save(measureEntity);
    }
    public List<MeasureEntity> findByExample(MeasureEntity measureEntity){
        return hibernateTemplate.findByExample(measureEntity);
    }
    public List findByAccountNumberAndTime(String accountNumber, Timestamp fromTime, Timestamp toTime){
        return hibernateTemplate.find("from MeasureEntity measureEntity where measureEntity.accountNumber=? " +
                "and measureEntity.commitTime>=? " +
                "and measureEntity.commitTime<=? " +
                "order by measureEntity.commitTime",
                new Object[]{accountNumber,fromTime,toTime});
    }
    public List findByAccountNumberAndTimeDesc(String accountNumber, Timestamp fromTime, Timestamp toTime){
        return hibernateTemplate.find("from MeasureEntity measureEntity where measureEntity.accountNumber=? " +
                        "and measureEntity.commitTime>=? " +
                        "and measureEntity.commitTime<=? " +
                        "order by measureEntity.commitTime desc",
                new Object[]{accountNumber,fromTime,toTime});
    }
    public MeasureEntity findTheLatestMeasureByAccountNumber(String accountNumber){
        List list=hibernateTemplate.find("from MeasureEntity measureEntity where measureEntity.accountNumber=? " +
                "order by measureEntity.commitTime desc");
        return list.isEmpty()?null:(MeasureEntity)list.get(0);
    }
    @Transactional
    public void update(MeasureEntity measureEntity){
        hibernateTemplate.update(measureEntity);
    }
    @Transactional
    public void delete(MeasureEntity measureEntity){
        hibernateTemplate.delete(measureEntity);
    }
}
