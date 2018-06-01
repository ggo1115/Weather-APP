package com.smu.oss14.bb.weather_app;

public class Weatherinfo_Data {
    private String date;//날짜(0:오늘,1:내일,2:모레)
    private String hour;//시간단위
    private String temp_cur;//현재온도
    private String temp_max;//최고온도
    private String temp_min;//최저온도
    private String sky_state;//하늘상태 (1:맑음 2:구름조금 3:구름많음 4:흐림)
    private String pty;//강수상태(0:- 1:비 2:비/눈 3:눈/비 4:눈)
    private String wf;//날씨상태
    private String pop;//강수확률
    private String ws;//풍속
    private String wd;//풍향
    private String reh;//습도


    //값이 주어지지 않은 생성자
    public Weatherinfo_Data(){
        this.setDate("4");
        this.setHour("1");
        this.setTemp_cur("-999.0");
        this.setTemp_max("-999.0");
        this.setTemp_min("-999.0");
        this.setSky_state("0");
        this.setPty("5");
        this.setWf(null);
        this.setPop("0");
        this.setWs("0");
        this.setWd(null);
        this.setReh("0");
    }


    //값이 주어졌을 때 생성자
    public Weatherinfo_Data(String date, String hour, String temp_cur,
                            String temp_max, String temp_min, String sky_state,
                            String pty, String wf, String pop, String ws, String wd, String reh){
        this.setDate(date);
        this.setHour(hour);
        this.setTemp_cur(temp_cur);
        this.setTemp_max(temp_max);
        this.setTemp_min(temp_min);
        this.setSky_state(sky_state);
        this.setPty(pty);
        this.setWf(wf);
        this.setPop(pop);
        this.setWs(ws);
        this.setWd(wd);
        this.setReh(reh);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getTemp_cur() {
        return temp_cur;
    }

    public void setTemp_cur(String temp_cur) {
        this.temp_cur = temp_cur;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }

    public String getSky_state() {
        return sky_state;
    }

    public void setSky_state(String sky_state) {
        this.sky_state = sky_state;
    }

    public String getPty() {
        return pty;
    }

    public void setPty(String pty) {
        this.pty = pty;
    }

    public String getWf() {
        return wf;
    }

    public void setWf(String wf) {
        this.wf = wf;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getWs() {
        return ws;
    }

    public void setWs(String ws) {
        this.ws = ws;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getReh() {
        return reh;
    }

    public void setReh(String reh) {
        this.reh = reh;
    }
}
