package com.jobportal.model;

/**
 * Model entity representing an Employer / Company.
 * Course: 25ITT32 Java Programming
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class Company {
    private int companyId;
    private String companyName;
    private String industry;
    private String location;
    private String contactEmail;
    private String website;

    public Company() {}

    public Company(int companyId, String companyName, String industry, String location,
                   String contactEmail, String website) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.industry = industry;
        this.location = location;
        this.contactEmail = contactEmail;
        this.website = website;
    }

    public Company(String companyName, String industry, String location,
                   String contactEmail, String website) {
        this(0, companyName, industry, location, contactEmail, website);
    }

    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    @Override
    public String toString() {
        return String.format("| %-4d | %-28s | %-24s | %-12s | %-24s |",
                companyId, companyName, industry, location, contactEmail);
    }
}
