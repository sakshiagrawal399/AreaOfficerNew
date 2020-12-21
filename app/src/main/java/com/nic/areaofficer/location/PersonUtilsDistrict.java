package com.nic.areaofficer.location;

import java.io.Serializable;

public class PersonUtilsDistrict implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String name;

    private String emailId;


    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    private String district_id;

    private boolean isSelected;

    public PersonUtilsDistrict() {

    }

    public PersonUtilsDistrict(String name, String emailId,String district_id) {

        this.name = name;
        this.emailId = emailId;
        this.district_id = district_id;

    }

    public PersonUtilsDistrict(String name, String emailId, boolean isSelected) {

        this.name = name;
        this.emailId = emailId;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}