package com.cetc.spinnertest;

import android.support.annotation.NonNull;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.UUID;

public class ArmyVehicle extends LitePalSupport implements Serializable {
    // 车辆名称编号
    @Column(defaultValue = "通信车")
    private String name;
    // 北斗卡号
    @Column(unique = true)
    private int BDSimID;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public int getBDSimID() {
        return BDSimID;
    }

    public String getName() {
        return name;
    }

    public void setBDSimID(int BDSimID) {
        this.BDSimID = BDSimID;
    }

    public void setName(String name) {
        this.name = name;
    }
}
