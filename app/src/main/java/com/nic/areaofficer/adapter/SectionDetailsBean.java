package com.nic.areaofficer.adapter;

import java.io.Serializable;

/**
 * Created by Nitesh on 25-01-2018.
 */

public class SectionDetailsBean implements Serializable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getBlock_id() {
        return block_id;
    }

    public void setBlock_id(String block_id) {
        this.block_id = block_id;
    }

    public String getPanchayat_id() {
        return panchayat_id;
    }

    public void setPanchayat_id(String panchayat_id) {
        this.panchayat_id = panchayat_id;
    }

    String name,district_id,block_id,panchayat_id;
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }


    int no;
}
