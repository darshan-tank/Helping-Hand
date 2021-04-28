package com.origininfotech.exampleapp;

public class addExamHelper {
    String coursename,examtime,examdate,venue,examyear,email,cName,delete,sort,id,sortC,sortY,sortD;

    addExamHelper(){}

    public String getSortD() {
        return sortD;
    }

    public void setSortD(String sortD) {
        this.sortD = sortD;
    }

    public String getSortY() {
        return sortY;
    }

    public void setSortY(String sortY) {
        this.sortY = sortY;
    }

    public String getSortC() {
        return sortC;
    }

    public void setSortC(String sortC) {
        this.sortC = sortC;
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

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getExamtime() {
        return examtime;
    }

    public void setExamtime(String examtime) {
        this.examtime = examtime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExamdate() {
        return examdate;
    }

    public void setExamdate(String examdate) {
        this.examdate = examdate;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getExamyear() {
        return examyear;
    }

    public void setExamyear(String examyear) {
        this.examyear = examyear;
    }

    addExamHelper(String id,String sortC,String coursename, String examdate, String examtime, String venue, String examyear,String email, String name,String sort,String delete,String sortY,String sortD){
        this.coursename = coursename;
        this.id=id;
        this.sortY=sortY;
        this.sortC = sortC;
        this.delete = delete;
        this.sort = sort;
        this.examdate = examdate;
        this.examtime = examtime;
        this.venue = venue;
        this.examyear = examyear;
        this.email = email;
        this.sortD=sortD;
        cName = name;
    }
}
