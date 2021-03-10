package com.example.seninkitapligin;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class Kitap {

    private String kitapAdi, kitapYazari, kitapOzeti;
    private Bitmap kitapResim;

    public Kitap (){}

    public Kitap(String kitapAdi, String kitapYazari, String kitapOzeti, Bitmap kitapResim) {
        this.kitapAdi = kitapAdi;
        this.kitapYazari = kitapYazari;
        this.kitapOzeti = kitapOzeti;
        this.kitapResim = kitapResim;
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

    public Bitmap getKitapResim() {
        return kitapResim;
    }

    public void setKitapResim(Bitmap kitapResim) {
        this.kitapResim = kitapResim;
    }


    //verileri veri tabanından alıp kitapList için içine ekleyeceğiz
    //Context context kullanarak Kitap classının içinde olmayan appCompatActivity'nin eksikliğini gidermeye çalışıyoruz. context e MainActivity'i yollayacağız
    static public ArrayList<Kitap> getData(Context context){
        ArrayList<Kitap> kitapList = new ArrayList<>();

        //databaseden gelecek verilerin aktarılacağı listeleri oluşturduk
        ArrayList<String> kitapAdiList = new ArrayList<>();
        ArrayList<String> kitapYazariList = new ArrayList<>();
        ArrayList<String> kitapOzetiList = new ArrayList<>();
        ArrayList<Bitmap> kitapResimList = new ArrayList<>();

        try {
            //Daha önceden var olan "Kitaplar" isimli database'imizi açtık
            SQLiteDatabase database = context.openOrCreateDatabase("Kitaplar", Context.MODE_PRIVATE , null);
            Cursor cursor = database.rawQuery("SELECT * FROM kitaplar",null);

            int kitapAdiIndex = cursor.getColumnIndex("kitapAdi");
            int kitapYazariIndex = cursor.getColumnIndex("kitapYazari");
            int kitapOzetiIndex = cursor.getColumnIndex("kitapOzeti");
            int kitapResimIndex = cursor.getColumnIndex("kitapResim");

            while (cursor.moveToNext()){
                kitapAdiList.add(cursor.getString(kitapAdiIndex));
                kitapYazariList.add(cursor.getString(kitapYazariIndex));
                kitapOzetiList.add(cursor.getString(kitapOzetiIndex));

                byte[] gelenResimByte = cursor.getBlob(kitapResimIndex);
                Bitmap gelenResim = BitmapFactory.decodeByteArray(gelenResimByte,0,gelenResimByte.length);
                kitapResimList.add(gelenResim);
            }
            cursor.close();
            //Database den gelen verileri kendi çeşitteki listelerine atadık sırasıyla şimdi bu işi sırasıyla eş zamanlı olarak
            //tüm listelerden alıp aynı indexteki verileri aynı indexe koymakta

            for (int i = 0; i < kitapAdiList.size();i++){
                Kitap kitap = new Kitap();

                kitap.setKitapAdi(kitapAdiList.get(i));
                kitap.setKitapYazari(kitapYazariList.get(i));
                kitap.setKitapOzeti(kitapOzetiList.get(i));
                kitap.setKitapResim(kitapResimList.get(i));

                kitapList.add(kitap);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return kitapList;
    }


}
