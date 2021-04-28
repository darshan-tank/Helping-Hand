package com.origininfotech.exampleapp;

public class dataHelper {
    String email,coursename,examdate,examtime,venue,cName,examyear,id;

    dataHelper() {}

    public dataHelper(String email, String coursename, String examdate, String examtime, String venue, String cname,String examyear,String id) {
        this.email = email;
        this.coursename = coursename;
        this.examyear = examyear;
        this.examdate = examdate;
        this.examtime = examtime;
        this.venue = venue;
        this.id=id;
        this.cName = cname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamyear() {
        return examyear;
    }

    public void setExamyear(String examyear) {
        this.examyear = examyear;
    }

    public String getCname() {
        return cName;
    }

    public void setCname(String cname) {
        this.cName = cname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getExamdate() {
        return examdate;
    }

    public void setExamdate(String examdate) {
        this.examdate = examdate;
    }

    public String getExamtime() {
        return examtime;
    }

    public void setExamtime(String examtime) {
        this.examtime = examtime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
