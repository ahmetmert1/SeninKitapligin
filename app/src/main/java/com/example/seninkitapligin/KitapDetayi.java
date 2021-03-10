package com.example.seninkitapligin;

import android.graphics.Bitmap;

public class KitapDetayi {

    private String kitapAdi, kitapYazari, kitapOzeti;
    private Bitmap kitapResimi;

    public KitapDetayi(String kitapAdi, String kitapYazari, String kitapOzeti, Bitmap kitapResimi) {
        this.kitapAdi = kitapAdi;
        this.kitapYazari = kitapYazari;
        this.kitapOzeti = kitapOzeti;
        this.kitapResimi = kitapResimi;
    }

    public String getKitapAdi() {
        return kitapAdi;
    }

    public void setKitapAdi(String kitapAdi) {
        this.kitapAdi = kitapAdi;
    }

    public String getKitapYazari() {
        return kitapYazari;
    }

    public void setKitapYazari(String kitapYazari) {
        this.kitapYazari = kitapYazari;
    }

    public String getKitapOzeti() {
        return kitapOzeti;
    }

    public void setKitapOzeti(String kitapOzeti) {
        this.kitapOzeti = kitapOzeti;
    }

    public Bitmap getKitapResimi() {
        return kitapResimi;
    }

    public void setKitapResimi(Bitmap kitapResimi) {
        this.kitapResimi = kitapResimi;
    }
}
