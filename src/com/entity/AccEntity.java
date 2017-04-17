package com.entity;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by 63289 on 2017/4/17.
 */
@Entity
@Table(name = "acc", schema = "wearable", catalog = "")
public class AccEntity {
    private String acountNumber;
    private Date time;
    private int accX;
    private int accY;
    private int accZ;

    @Id
    @Column(name = "acountNumber", nullable = false, length = 10)
    public String getAcountNumber() {
        return acountNumber;
    }

    public void setAcountNumber(String acountNumber) {
        this.acountNumber = acountNumber;
    }

    @Basic
    @Column(name = "time", nullable = false)
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Basic
    @Column(name = "accX", nullable = false)
    public int getAccX() {
        return accX;
    }

    public void setAccX(int accX) {
        this.accX = accX;
    }

    @Basic
    @Column(name = "accY", nullable = false)
    public int getAccY() {
        return accY;
    }

    public void setAccY(int accY) {
        this.accY = accY;
    }

    @Basic
    @Column(name = "accZ", nullable = false)
    public int getAccZ() {
        return accZ;
    }

    public void setAccZ(int accZ) {
        this.accZ = accZ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccEntity accEntity = (AccEntity) o;

        if (accX != accEntity.accX) return false;
        if (accY != accEntity.accY) return false;
        if (accZ != accEntity.accZ) return false;
        if (acountNumber != null ? !acountNumber.equals(accEntity.acountNumber) : accEntity.acountNumber != null)
            return false;
        if (time != null ? !time.equals(accEntity.time) : accEntity.time != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = acountNumber != null ? acountNumber.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + accX;
        result = 31 * result + accY;
        result = 31 * result + accZ;
        return result;
    }
}
