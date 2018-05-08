package com.timo.timolib;

import java.io.Serializable;
import java.util.List;

public class Timo_Params implements Serializable {

    private List<String> images;
    private String url;
    private String title;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
