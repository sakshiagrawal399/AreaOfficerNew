package com.nic.areaofficer.location;

import java.io.Serializable;

public class PersonUtilsDemo implements Serializable {

    /**
     *
     */

    private String name;

    private String district_id;




    public PersonUtilsDemo( ) {

        this.name = name;
        this.district_id = district_id;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return district_id;
    }

    public void setEmailId(String district_id) {
        this.district_id = district_id;
    }



}