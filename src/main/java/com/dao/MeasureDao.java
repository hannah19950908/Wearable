package com.dao;

import com.entity.MeasureEntity;
import com.util.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 63289 on 2017/4/18.
 */
@Repository
@Transactional
public class MeasureDao {
    private final HibernateTemplate hibernateTemplate;
    private final TokenDao tokenDao;

    @Autowired
    public MeasureDao(HibernateTemplate hibernateTemplate, TokenDao tokenDao) {
        this.hibernateTemplate = hibernateTemplate;
        this.tokenDao = tokenDao;
    }

    public void add(MeasureEntity measureEntity) {
        hibernateTemplate.save(measureEntity);
    }

    public void addByList(List<MeasureEntity> measureEntities) {
        for (MeasureEntity measureEntity : measureEntities) {
            hibernateTemplate.save(measureEntity);
        }
    }

    public List findByAccountNumber(String accountNumber) {
        String queryString = "from MeasureEntity where accountNumber=? order by commitTime,id";
        Object[] objects = new Object[]{accountNumber};
        return findListByAccountNumberAndQueryStringAndObjects(accountNumber, queryString, objects);
    }

    public List findByAccountNumberAndTime(String accountNumber, Timestamp fromTime, Timestamp toTime) {
        String queryString = "from MeasureEntity where accountNumber=? and commitTime between ? and ? order by commitTime";
        Object[] objects = new Object[]{accountNumber, fromTime, toTime};
        return findListByAccountNumberAndQueryStringAndObjects(accountNumber, queryString, objects);
    }

    public MeasureEntity findTheLatestByAccountNumberAndTime(String accountNumber, Timestamp fromTime, Timestamp toTime) {
        String queryString = "from MeasureEntity where commitTime in " +
                "(select max(commitTime) from MeasureEntity where accountNumber=? and commitTime between ? and ?)";
        Object[] objects = new Object[]{accountNumber, fromTime, toTime};
        return findByAccountNumberAndQueryStringAndObjects(accountNumber, queryString, objects);
    }

    public MeasureEntity findTheLatestByAccountNumber(String accountNumber) {
        String queryString = "from MeasureEntity where commitTime in (select max(commitTime) from MeasureEntity where accountNumber=?)";
        Object[] objects = new Object[]{accountNumber};
        return findByAccountNumberAndQueryStringAndObjects(accountNumber, queryString, objects);
    }

    public MeasureEntity findTheFirstByAccountNumber(String accountNumber) {
        String queryString = "from MeasureEntity where commitTime in (select min(commitTime) from MeasureEntity where accountNumber=?)";
        Object[] objects = new Object[]{accountNumber};
        return findByAccountNumberAndQueryStringAndObjects(accountNumber, queryString, objects);
    }

    private MeasureEntity findByAccountNumberAndQueryStringAndObjects(String accountNumber, String queryString, Object[] objects) {
        if (tokenDao.hasCache(accountNumber)) {
            Iterator iterator = hibernateTemplate.iterate(queryString, objects);
            return iterator.hasNext() ? (MeasureEntity) iterator.next() : null;
        }
        List list = hibernateTemplate.find(queryString, objects);
        return list.isEmpty() ? null : (MeasureEntity) list.get(0);
    }

    private List findListByAccountNumberAndQueryStringAndObjects(String accountNumber, String queryString, Object[] objects) {
        if (tokenDao.hasCache(accountNumber))
            return IteratorUtils.toList(hibernateTemplate.iterate(queryString, objects));
        return hibernateTemplate.find(queryString, objects);
    }

    public void update(MeasureEntity measureEntity) {
        hibernateTemplate.update(measureEntity);
    }

    public void delete(MeasureEntity measureEntity) {
        hibernateTemplate.delete(measureEntity);
    }
}
