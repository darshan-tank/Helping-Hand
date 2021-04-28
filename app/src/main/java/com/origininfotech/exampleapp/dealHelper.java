package com.origininfotech.exampleapp;

public class dealHelper {
    String cname,id,vname,course,date,time,venue,status,cancel,delete,sort,number,sortc,vemail;

    dealHelper(){}

    public dealHelper(String id,String cname,String vemail,String sortc, String vname, String course, String number, String sort, String date, String time, String venue, String status, String cancel, String delete) {
        this.cname = cname;
        this.vemail = vemail;
        this.id = id;
        this.number = number;
        this.sortc=sortc;
        this.course = course;
        this.vname = vname;
        this.date = date;
        this.time = time;
        this.sort = sort;
        this.venue = venue;
        this.status = status;
        this.cancel=cancel;
        this.delete=delete;
    }

    public String getVemail() {
        return vemail;
    }

    public void setVemail(String vemail) {
        this.vemail = vemail;
    }

    public String getSortc() {
        return sortc;
    }

    public void setSortc(String sortc) {
        this.sortc = sortc;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
