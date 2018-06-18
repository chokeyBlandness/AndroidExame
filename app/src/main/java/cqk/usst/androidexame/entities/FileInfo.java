package cqk.usst.androidexame.entities;

import java.io.Serializable;

/**
 * Created by 10033 on 2018/3/27.
 */

public class FileInfo implements Serializable {
    private int id;
    private String name;
    private String url;
    private int length;
    private int procedure;


    public FileInfo(int id, String name, String url, int length, int procedure) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.length = length;
        this.procedure = procedure;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", length=" + length +
                ", procedure=" + procedure +
                '}';
    }

    public int getProcedure() {
        return procedure;
    }

    public void setProcedure(int procedure) {
        this.procedure = procedure;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
