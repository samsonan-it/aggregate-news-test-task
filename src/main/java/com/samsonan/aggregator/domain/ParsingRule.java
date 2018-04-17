package com.samsonan.aggregator.domain;

public class ParsingRule {

    /**
     * Default RSS paths
     */
    private String itemXPath         = "//item";

    private String titleXPath           = "title";
    private String descriptionXPath     = "description";
    private String linkXPath            = "link";

    public String getItemXPath() {
        return itemXPath;
    }

    public void setItemXPath(String itemXPath) {
        this.itemXPath = itemXPath;
    }

    public String getTitleXPath() {
        return titleXPath;
    }

    public void setTitleXPath(String titleXPath) {
        this.titleXPath = titleXPath;
    }

    public String getDescriptionXPath() {
        return descriptionXPath;
    }

    public void setDescriptionXPath(String descriptionXPath) {
        this.descriptionXPath = descriptionXPath;
    }

    public String getLinkXPath() {
        return linkXPath;
    }

    public void setLinkXPath(String linkXPath) {
        this.linkXPath = linkXPath;
    }

    

    
}
