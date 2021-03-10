package com.example.seninkitapligin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class DetayActivity extends AppCompatActivity {

    private ImageView imgKitapResimi;
    private TextView txtKitapAdi, txtKitapYazari, txtKitapOzeti;
    private String kitapAdi, kitapYazari, kitapOzeti;
    private Bitmap kitapResimi;

    private void tanimla(){

        imgKitapResimi = (ImageView)findViewById(R.id.detay_activity_imageViewKitapResim);
        txtKitapAdi = (TextView)findViewById(R.id.detay_activity_textViewKitapAdi);
        txtKitapYazari = (TextView)findViewById(R.id.detay_activity_textViewKitapYazari);
        txtKitapOzeti = (TextView)findViewById(R.id.detay_activity_textViewKitapOzeti);

        kitapAdi = MainActivity.kitapDetayi.getKitapAdi();
        kitapYazari = MainActivity.kitapDetayi.getKitapYazari();
        kitapOzeti = MainActivity.kitapDetayi.getKitapOzeti();
        kitapResimi = MainActivity.kitapDetayi.getKitapResimi();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detay);

        tanimla();

        if (!TextUtils.isEmpty(kitapAdi) && !TextUtils.isEmpty(kitapYazari) && !TextUtils.isEmpty(kitapOzeti)){

            txtKitapAdi.setText(kitapAdi);
            txtKitapYazari.setText(kitapYazari);
            txtKitapOzeti.setText(kitapOzeti);
            imgKitapResimi.setImageBitmap(kitapResimi);

        }

    }
}