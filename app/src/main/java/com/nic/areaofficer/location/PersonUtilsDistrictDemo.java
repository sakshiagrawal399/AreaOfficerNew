package com.nic.areaofficer.location;

import java.io.Serializable;

public class PersonUtilsDistrictDemo implements Serializable {

    /**
     *
     */

    private String name;

    public String getBlock_id() {
        return block_id;
    }

    public void setBlock_id(String block_id) {
        this.block_id = block_id;
    }

    private String block_id;


    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    private String district_id;




    public PersonUtilsDistrictDemo( ) {

        this.name = name;
        this.block_id = block_id;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return block_id;
    }

    public void setEmailId(String block_id) {
        this.block_id = block_id;
    }



}