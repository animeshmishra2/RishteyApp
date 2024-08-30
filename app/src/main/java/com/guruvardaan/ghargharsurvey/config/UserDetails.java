package com.guruvardaan.ghargharsurvey.config;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDetails {
    private SharedPreferences mSharedPreferences;
    public static final String userinfo = "userinfo_x";
    public static final String uid = "uid";
    public static final String name = "name";
    public static final String FIREBASE = "FIREBASE";
    private String profilef = "profilef";
    private String language = "language";
    public static final String cust_json = "cust_json";
    public static final String user_type = "user_type";
    public static final String agentId = "agentId";
    public static final String typess = "typess";

    public static final String fcm_msg = "fcm_msg";
    private String mobile_no = "mobile_no";
    private String initials = "initials";
    private String alternate_no = "alternate_no";
    private String whatsapp = "whatsapp";
    private String advisor_email = "advisor_email";
    private String flatHouseNo = "flatHouseNo";
    private String landMark = "landMark";
    private String country_id = "country_id";
    private String state_id = "state_id";
    private String city_id = "city_id";
    private String area_id = "area_id";
    private String tehsil = "tehsil";
    private String pin_code = "pin_code";
    private String village = "village";
    private String advisordob = "advisordob";
    private String acHolder = "acHolder";
    private String acNo = "acNo";
    private String IFSC = "IFSC";
    private String bank = "bank";
    private String branch = "branch";
    private String nomineeName = "nomineeName";
    private String nomineeAge = "nomineeAge";
    private String nomineemobile = "nomineemobile";
    private String relation = "relation";
    private String Father = "Father";
    private String occupationCategory = "occupationCategory";
    private String occupation = "occupation";
    private String advisor_image = "advisor_image";
    private String aadharImage = "aadharImage";
    private String panImage = "panImage";
    private String agentSign = "agentSign";
    private String aadharNo = "aadharNo";
    private String panNo = "panNo";
    private String relationType = "relationType";
    private String relationName = "relationName";
    private String advisor_rank = "advisor_rank";
    private String gender = "gender";
    private String dob = "dob";
    private String dep_id = "dep_id";
    private String dep_name = "dep_name";
    private String isTL = "isTL";


    private Context mContext;

    public UserDetails(Context mContext) {
        this.mContext = mContext;
    }

    public void setNomineeAge(String nomineeAges) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(nomineeAge, nomineeAges);
        editor.apply();
    }

    public String getNomineeAge() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(nomineeAge, "0");
    }

    public void setInitials(String initialss) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(initials, initialss);
        editor.apply();
    }

    public String getFcm_msg() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(fcm_msg, "0");
    }

    public void setFcm_msg(String gender1) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(fcm_msg, gender1);
        editor.apply();
    }

    public String getInitials() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(initials, "");
    }

    public void setAlternate_no(String alternate) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(alternate_no, alternate);
        editor.apply();
    }

    public String getAlternate_no() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(alternate_no, "0");
    }

    public void setWhatsapp(String whatsapps) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(whatsapp, whatsapps);
        editor.apply();
    }

    public String getWhatsapp() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(whatsapp, "0");
    }

    public void setAdvisor_email(String admail) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(advisor_email, admail);
        editor.apply();
    }

    public String getAdvisor_email() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(advisor_email, "0");
    }

    public void setFlatHouseNo(String flats) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(flatHouseNo, flats);
        editor.apply();
    }

    public String getFlatHouseNo() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(flatHouseNo, "0");
    }

    public void setLandMark(String landMarks) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(landMark, landMarks);
        editor.apply();
    }

    public String getLandMark() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(landMark, "0");
    }

    public void setCountry_id(String country_ids) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(country_id, country_ids);
        editor.apply();
    }

    public String getCountry_id() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(country_id, "0");
    }

    public void setState_id(String stid) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(state_id, stid);
        editor.apply();
    }

    public String getState_id() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(state_id, "0");
    }

    public void setCity_id(String city_ids) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(city_id, city_ids);
        editor.apply();
    }

    public String getCity_id() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(city_id, "0");
    }

    public void setArea_id(String area_ids) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(area_id, area_ids);
        editor.apply();
    }

    public String getArea_id() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(area_id, "0");
    }

    public void setTehsil(String tehsils) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(tehsil, tehsils);
        editor.apply();
    }

    public String getTehsil() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(tehsil, "0");
    }

    public void setPin_code(String pcode) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(pin_code, pcode);
        editor.apply();
    }

    public String getPin_code() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(pin_code, "0");
    }

    public void setVillage(String villages) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(village, villages);
        editor.apply();
    }

    public String getVillage() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(village, "0");
    }

    public void setAdvisordob(String advisordobs) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(advisordob, advisordobs);
        editor.apply();
    }

    public String getAdvisordob() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(advisordob, "0");
    }

    public void setAcHolder(String acHolders) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(acHolder, acHolders);
        editor.apply();
    }

    public String getAcHolder() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(acHolder, "0");
    }

    public void setAcNo(String acNos) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(acNo, acNos);
        editor.apply();
    }

    public String getAcNo() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(acNo, "0");
    }

    public void setIFSC(String ifscs) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(IFSC, ifscs);
        editor.apply();
    }

    public String getIFSC() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(IFSC, "0");
    }

    public void setBank(String banks) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(bank, banks);
        editor.apply();
    }

    public String getBank() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(bank, "0");
    }

    public void setBranch(String branchs) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(branch, branchs);
        editor.apply();
    }

    public String getBranch() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(branch, "0");
    }

    public void setNomineeName(String nomineeNames) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(nomineeName, nomineeNames);
        editor.apply();
    }

    public String getNomineeName() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(nomineeName, "0");
    }

    public void setNomineemobile(String nomineemobiles) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(nomineemobile, nomineemobiles);
        editor.apply();
    }

    public String getNomineemobile() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(nomineemobile, "0");
    }

    public void setRelation(String relations) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(relation, relations);
        editor.apply();
    }

    public String getRelation() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(relation, "0");
    }

    public void setFather(String fathers) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Father, fathers);
        editor.apply();
    }

    public String getFather() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(Father, "0");
    }

    public void setOccupationCategory(String occupationCategorys) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(occupationCategory, occupationCategorys);
        editor.apply();
    }

    public String getOccupationCategory() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(occupationCategory, "0");
    }

    public void setOccupation(String occupations) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(occupation, occupations);
        editor.apply();
    }

    public String getOccupation() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(occupation, "0");
    }

    public void setAdvisor_image(String advisor_images) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(advisor_image, advisor_images);
        editor.apply();
    }

    public String getAdvisor_image() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(advisor_image, "0");
    }

    public void setAadharImage(String aadharImages) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(aadharImage, aadharImages);
        editor.apply();
    }

    public String getAadharImage() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(aadharImage, "0");
    }

    public void setPanImage(String panImages) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(panImage, panImages);
        editor.apply();
    }

    public String getPanImage() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(panImage, "0");
    }

    public void setAgentSign(String agentSigns) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(agentSign, agentSigns);
        editor.apply();
    }

    public String getAgentSign() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(agentSign, "0");
    }

    public void setAadharNo(String aadharNos) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(aadharNo, aadharNos);
        editor.apply();
    }

    public String getAadharNo() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(aadharNo, "0");
    }

    public void setPanNo(String panNos) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(panNo, panNos);
        editor.apply();
    }

    public String getPanNo() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(panNo, "0");
    }

    public void setRelationType(String relationTypes) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(relationType, relationTypes);
        editor.apply();
    }

    public String getRelationType() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(relationType, "0");
    }

    public void setRelationName(String relationNames) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(relationName, relationNames);
        editor.apply();
    }

    public String getRelationName() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(relationName, "0");
    }

    public void setAdvisor_rank(String advisor_ranks) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(advisor_rank, advisor_ranks);
        editor.apply();
    }

    public String getAdvisor_rank() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(advisor_rank, "0");
    }


    public void setMobile_no(String mobile) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(mobile_no, mobile);
        editor.apply();
    }

    public String getMobile_no() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(mobile_no, "0");
    }


    public void setFIREBASE(String firebaseid) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(FIREBASE, firebaseid);
        editor.apply();
    }

    public String getFIREBASE() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(FIREBASE, "0");
    }

    public void setuserid(String userid) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(uid, userid);
        editor.apply();
    }

    public void setName(String Name) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(name, Name);
        editor.apply();
    }


    public void setprofilef(String profile) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(profilef, profile);
        editor.apply();
    }

    public String getprofilef() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(profilef, "0");

    }

    public void setLanguage(String lans) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(language, lans);
        editor.apply();
    }

    public String getLanguage() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(language, "en");
    }


    public String getuserid() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(uid, "0");
    }

    public String getName() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(name, "0");

    }

    public String getCust_json() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(cust_json, "");
    }

    public void setCust_json(String cjson) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(cust_json, cjson);
        editor.apply();
    }

    public String getUser_type() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(user_type, "0");
    }

    public void setUser_type(String utype) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(user_type, utype);
        editor.apply();
    }

    public String getAgentId() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(agentId, "0");
    }

    public void setAgentId(String agi) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(agentId, agi);
        editor.apply();
    }


    public String getTypess() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(typess, "0");
    }

    public void setTypess(String agi) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(typess, agi);
        editor.apply();
    }


    public String getGender() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(gender, "");
    }

    public void setGender(String gen) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(gender, gen);
        editor.apply();
    }

    public String getDOB() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(dob, "");
    }

    public void setDob(String dobs) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(dob, dobs);
        editor.apply();
    }

    public String getDep_id() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(dep_id, "");
    }

    public void setDep_id(String dep_iid) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(dep_id, dep_iid);
        editor.apply();
    }


    public String getDep_name() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(dep_name, "");
    }

    public void setDep_name(String dep_n) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(dep_name, dep_n);
        editor.apply();
    }


    public String getIsTL() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        return sharedPreferences.getString(isTL, "");
    }

    public void setIsTL(String itl) {
        mSharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(isTL, itl);
        editor.apply();
    }

    public void clearAll() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(userinfo, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
