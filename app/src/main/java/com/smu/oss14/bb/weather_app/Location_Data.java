package com.smu.oss14.bb.weather_app;

public class Location_Data {

    private Double lon;
    private Double lat;
    private String[] AddrValue;
    private String Addr;
    private String LCcode;

    public Location_Data(){
        this.setLon(0.0);
        this.setLat(0.0);
        this.setAddrValue(new String[]{});
        this.setAddr(null);
        this.setLCcode(null);
    }

    public Location_Data(Double lon, Double lat, String[] addrValue, String Addr, String LCcode){
        this.setLon(lon);
        this.setLat(lat);
        this.setAddrValue(addrValue);
        this.setAddr(Addr);
        this.setLCcode(LCcode);
    }

    public Location_Data(Double lon, Double lat, String[] addrValue, String Addr){
        this.setLon(lon);
        this.setLat(lat);
        this.setAddrValue(addrValue);
        this.setAddr(Addr);
        this.setLCcode(null);
    }


    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String[] getAddrValue() {
        return AddrValue;
    }

    public void setAddrValue(String[] addrValue) {
        AddrValue = addrValue;
    }

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }

    public String getLCcode() {
        return LCcode;
    }

    public void setLCcode(String LCcode) {
        this.LCcode = LCcode;
    }
}
