package com.origin.wottopark;

public class UserModel {
    private String fullname;
    private String companyname;
    private String phone;
    private String email;
    private String password;
    private String companyId;
    private String devicename;
    private String deviceBrand;
    private String deviceModel;
    private String imeiNumber;
    private String serialNumber;

    public UserModel(String fullname, String companyname, String phone, String email, String password, String companyId,
                     String devicename, String deviceBrand, String deviceModel, String imeiNumber, String serialNumber) {
        this.fullname = fullname;
        this.companyname = companyname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.companyId = companyId;
        this.devicename = devicename;
        this.deviceBrand = deviceBrand;
        this.deviceModel = deviceModel;
        this.imeiNumber = imeiNumber;
        this.serialNumber = serialNumber;

    }

    public String getFullname() {return fullname; }
    public void setFullname(String name) { this.fullname = fullname; }

    public String getImeiNumber() {return imeiNumber; }
    public void setImeiNumber(String imeiNumber) { this.imeiNumber = imeiNumber; }


}
