package com.nic.areaofficer.location;

import java.io.Serializable;

public class PersonUtils implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String name;

    private String emailId;

    public String getPanchayat_code() {
        return panchayat_code;
    }

    public void setPanchayat_code(String panchayat_code) {
        this.panchayat_code = panchayat_code;
    }

    private String panchayat_code;

    public String getBlock_id() {
        return block_id;
    }

    public void setBlock_id(String block_id) {
        this.block_id = block_id;
    }

    private String block_id;

    private boolean isSelected;

    public PersonUtils() {

    }

    public PersonUtils(String name, String emailId,String panchayat_code,String block_id) {

        this.name = name;
        this.emailId = emailId;
        this.panchayat_code = panchayat_code;
        this.block_id = block_id;

    }

    public PersonUtils(String name, String emailId, boolean isSelected) {

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