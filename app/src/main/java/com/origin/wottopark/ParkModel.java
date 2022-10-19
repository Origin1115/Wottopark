package com.origin.wottopark;

public class ParkModel {
    private String platenumber;
    private String parkedtime;
    private String ticketnumber;
    private String inputtime;

    public ParkModel() {
    }

    public ParkModel(String platenumber, String parkedtime, String ticketnumber, String inputtime, String totalprice) {
        this.platenumber = platenumber;
        this.parkedtime = parkedtime;
        this.ticketnumber = ticketnumber;
        this.inputtime = inputtime;
        this.totalprice = totalprice;
    }

    public String getPlatenumber() {
        return platenumber;
    }

    public void setPlatenumber(String platenumber) {
        this.platenumber = platenumber;
    }

    public String getParkedtime() {
        return parkedtime;
    }

    public void setParkedtime(String parkedtime) {
        this.parkedtime = parkedtime;
    }

    public String getTicketnumber() {
        return ticketnumber;
    }

    public void setTicketnumber(String ticketnumber) {
        this.ticketnumber = ticketnumber;
    }

    public String getInputtime() {
        return inputtime;
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    private String totalprice;
}
