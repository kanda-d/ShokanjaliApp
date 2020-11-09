package com.traidev.shokanjali.PanditsClass;

public class ClassPanditsModal {

    private String PanditName;
    private String PanditProfile;
    private String PanditDetails;
    private String PanditId;

    public ClassPanditsModal(String panditName, String panditProfile, String panditDetails,String panditId) {
        PanditName = panditName;
        PanditProfile = panditProfile;
        PanditDetails = panditDetails;
        PanditId = panditId;
    }


    public String getPanditName() {
        return PanditName;
    }

    public String getPanditId() {
        return PanditId;
    }

    public String getPanditProfile() {
        return PanditProfile;
    }

    public String getPanditDetails() {
        return PanditDetails;
    }

    public void setPanditName(String panditName) {
        PanditName = panditName;
    }

    public void setPanditProfile(String panditProfile) {
        PanditProfile = panditProfile;
    }

    public void setPanditDetails(String panditDetails) {
        PanditDetails = panditDetails;
    }
}
