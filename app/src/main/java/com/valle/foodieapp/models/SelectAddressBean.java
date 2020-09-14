package com.valle.foodieapp.models;

public class SelectAddressBean {

    private String address;
    private String addressType;
    private String lattitude;
    private String longitude;
    private boolean selected;

    public SelectAddressBean(String address, String addressType, String lattitude, String longitude,boolean selected) {
        this.address = address;
        this.addressType = addressType;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.selected = selected;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressType() {
        return addressType;
    }

    public String getLattitude() {
        return lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
