package com.quickresolve.ModelClass;

public class ComplaintModel {

    String branch, category, description, enrollment, fullName, mobile;

    public ComplaintModel() {}   // required for Firebase

    public ComplaintModel(String branch, String category, String description,
                          String enrollment, String fullName, String mobile) {
        this.branch = branch;
        this.category = category;
        this.description = description;
        this.enrollment = enrollment;
        this.fullName = fullName;
        this.mobile = mobile;
    }

    public String getBranch() { return branch; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getEnrollment() { return enrollment; }
    public String getFullName() { return fullName; }
    public String getMobile() { return mobile; }
}

