package com.cws.shop.model;

/**
 * Simple POJO stored as JSON array inside product_details.version_history column.
 * NOT an @Entity — lives inside ProductDetails.versionHistory as JSON.
 */
public class VersionEntry {

    private String version;      // e.g. "2.3.1"
    private String releasedOn;   // e.g. "2025-01-15"
    private String notes;        // brief changelog for this version

    public VersionEntry() {}

    public VersionEntry(String version, String releasedOn, String notes) {
        this.version = version;
        this.releasedOn = releasedOn;
        this.notes = notes;
    }

    public String getVersion() { 
    	return version; 
    	}
    public void setVersion(String version) { 
    	this.version = version; 
    	}

    public String getReleasedOn() { 
    	return releasedOn;
    	}
    public void setReleasedOn(String releasedOn) { 
    	this.releasedOn = releasedOn; 
    	}

    public String getNotes() { 
    	return notes; 
    	}
    public void setNotes(String notes) { 
    	this.notes = notes; 
    	}
}