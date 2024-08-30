package com.guruvardaan.ghargharsurvey.model;

public class AdminCModel {
    String DepartmentID, DepartmentName, DepartmentShortName, DepartmentCode, TotalComplaints, team_solved, total_solved, unsolved;

    public AdminCModel(String departmentID, String departmentName, String departmentShortName, String departmentCode, String totalComplaints, String team_solved, String total_solved, String unsolved) {
        DepartmentID = departmentID;
        DepartmentName = departmentName;
        DepartmentShortName = departmentShortName;
        DepartmentCode = departmentCode;
        TotalComplaints = totalComplaints;
        this.team_solved = team_solved;
        this.total_solved = total_solved;
        this.unsolved = unsolved;
    }

    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getDepartmentShortName() {
        return DepartmentShortName;
    }

    public void setDepartmentShortName(String departmentShortName) {
        DepartmentShortName = departmentShortName;
    }

    public String getDepartmentCode() {
        return DepartmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        DepartmentCode = departmentCode;
    }

    public String getTotalComplaints() {
        return TotalComplaints;
    }

    public void setTotalComplaints(String totalComplaints) {
        TotalComplaints = totalComplaints;
    }

    public String getTeam_solved() {
        return team_solved;
    }

    public void setTeam_solved(String team_solved) {
        this.team_solved = team_solved;
    }

    public String getTotal_solved() {
        return total_solved;
    }

    public void setTotal_solved(String total_solved) {
        this.total_solved = total_solved;
    }

    public String getUnsolved() {
        return unsolved;
    }

    public void setUnsolved(String unsolved) {
        this.unsolved = unsolved;
    }
}
