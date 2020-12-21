package com.nic.areaofficer.location;

import java.io.Serializable;

public class PersonUtilsPanchayat implements Serializable {

    /**
     *
     */

    private String name;

    private String panchayat_id;

    public String getBlock_id() {
        return block_id;
    }

    public void setBlock_id(String block_id) {
        this.block_id = block_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    private String block_id;
    private String district_id;




    public PersonUtilsPanchayat( ) {

        this.name = name;
        this.panchayat_id = panchayat_id;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return panchayat_id;
    }

    public void setEmailId(String panchayat_id) {
        this.panchayat_id = panchayat_id;
    }



}