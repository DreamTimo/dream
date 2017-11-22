package com.timo.timolib.http;

/**
 * Created by 蔡永汪 on 2017/10/9.
 */

public class FileBean extends BaseBean {
    private String fileName;
    private String filePath;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
