package com.angelova.w510.calmmom.models;

import java.io.Serializable;

/**
 * Created by W510 on 19.7.2018 г..
 */

public class ExaminationDocument implements Serializable {

    private String downloadUri;
    private String name;

    public ExaminationDocument(String downloadUri, String name) {
        this.downloadUri = downloadUri;
        this.name = name;
    }

    public ExaminationDocument() { }

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
