package com.guruvardaan.ghargharsurvey.model;

public class EmployeeModel {
    String id, EmpId, FirstName, AcName, EmailId, Password, Gender, Dob, Department, is_teamlead, team_lead, Phonenumber, Status, firebase_id, RegDate, DepartmentName;

    public EmployeeModel(String id, String empId, String firstName, String AcName, String emailId, String password, String gender, String dob, String department, String is_teamlead, String team_lead, String phonenumber, String status, String firebase_id, String regDate, String departmentName) {
        this.id = id;
        EmpId = empId;
        FirstName = firstName;
        EmailId = emailId;
        Password = password;
        Gender = gender;
        Dob = dob;
        Department = department;
        this.is_teamlead = is_teamlead;
        this.team_lead = team_lead;
        Phonenumber = phonenumber;
        Status = status;
        this.firebase_id = firebase_id;
        RegDate = regDate;
        DepartmentName = departmentName;
        this.AcName = AcName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getIs_teamlead() {
        return is_teamlead;
    }

    public void setIs_teamlead(String is_teamlead) {
        this.is_teamlead = is_teamlead;
    }

    public String getTeam_lead() {
        return team_lead;
    }

    public void setTeam_lead(String team_lead) {
        this.team_lead = team_lead;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    public String getRegDate() {
        return RegDate;
    }

    public void setRegDate(String regDate) {
        RegDate = regDate;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getAcName() {
        return AcName;
    }

    public void setAcName(String acName) {
        AcName = acName;
    }
}
