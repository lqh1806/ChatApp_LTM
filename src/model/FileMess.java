/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author lequo
 */
public class FileMess implements Serializable{
    private int idSend;
    private int idReceive;
    private byte [] fileData;
    private String createdTime;
    private String fileName;
    private String extension;

    public FileMess(int idSend, int idReceive, byte[] file, String createdTime) {
        this.idSend = idSend;
        this.idReceive = idReceive;
        this.fileData = file;
        this.createdTime = createdTime;
    }

    public FileMess(int idSend, int idReceive, byte[] fileData, String fileName, String extension, String createdTime) {
        this.idSend = idSend;
        this.idReceive = idReceive;
        this.fileData = fileData;
        this.createdTime = createdTime;
        this.fileName = fileName;
        this.extension = extension;
    }
    
    

    public FileMess() {
    }

    public int getIdSend() {
        return idSend;
    }

    public void setIdSend(int idSend) {
        this.idSend = idSend;
    }

    public int getIdReceive() {
        return idReceive;
    }

    public void setIdReceive(int idReceive) {
        this.idReceive = idReceive;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] file) {
        this.fileData = file;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
    
    

    @Override
    public String toString() {
        return fileName;
    }

}
