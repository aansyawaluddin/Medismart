package com.example.medismart.model;

public class Medicine {
    private String bentuk;
    private String nama;
    private String penyakit;
    private String url;

    public Medicine() {
    }

    public Medicine(String bentuk, String nama, String penyakit, String url) {
        this.bentuk = bentuk;
        this.nama = nama;
        this.penyakit = penyakit;
        this.url = url;
    }

    public String getBentuk() {
        return bentuk;
    }

    public void setBentuk(String bentuk) {
        this.bentuk = bentuk;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPenyakit() {
        return penyakit;
    }

    public void setPenyakit(String penyakit) {
        this.penyakit = penyakit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
