package com.example.seninkitapligin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddBookActivity extends AppCompatActivity {

    private EditText editTextKitapIsmi, editTextKitapYazari,editTextKitapOzet;
    private ImageView imgKitapResim;
    private String kitapIsmi,kitapYazari,kitapOzeti;
    private Bitmap secilenResim,kucultulenResim;
    private Button btnKaydet;


    private int imgIzinAlmaKodu = 0;
    //Kullanıcı izin vermemişse izin almak için 0 göndericez
    private int imgIzinVerildiKodu = 1;
    //Kullanıcı izin vermişse  1 yollayacağız

    public void tanimla()
    {
        //activity_add_book daki layout bileşenlerini class da tanımladık
        editTextKitapIsmi = findViewById(R.id.add_book_activity_textViewKitapIsmi);
        editTextKitapYazari = findViewById(R.id.add_book_activity_textViewKitapYazari);
        editTextKitapOzet = findViewById(R.id.add_book_activity_textViewKitapOzeti);
        imgKitapResim = findViewById(R.id.add_book_activity_imageViewBookImage);
        btnKaydet = findViewById(R.id.add_book_activity_btnKaydet);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        tanimla();

    }

    //activity_add_book (kitap ekleme sayfası) daki kaydet butonuna basılınca olacaklar
    public void kitapKaydet(View v)
    {
        //activity_add_book (kitap ekleme sayfası) daki kaydet butonuna basılınca olacaklar

        //classda tanımladıgımız String degerleri uygulamadan alıp değişkenlere atıyoruz.
        kitapIsmi = editTextKitapIsmi.getText().toString();
        kitapYazari = editTextKitapYazari.getText().toString();
        kitapOzeti = editTextKitapOzet.getText().toString();

        //eğer herhangi bir edittext boş ise kitabı kaydetmeyecek ve uyarı verecek
        if (!TextUtils.isEmpty(kitapIsmi)){
            if (!TextUtils.isEmpty(kitapYazari)){
                if (!TextUtils.isEmpty(kitapOzeti)){
                    //kaydet

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    //resimSec fonksiyonundan gelen secilenResim'i databasede sıkıntı çıkarmasın diye küçültüyoruz.
                    kucultulenResim = resimiKucult(secilenResim);
                    //galeriden sectigimiz resmin once urisini almıştık sonra da Bitmap haline çevirmiştik. (secilenResim)
                    //şimdi de resmi kaydetmek için bytearray haline getirmemiz gerekli
                    kucultulenResim.compress(Bitmap.CompressFormat.PNG,75,outputStream);
                    byte[] kayitEdilecekResim = outputStream.toByteArray();

                    //Database mizi oluşturuyoz
                    SQLiteDatabase database = this.openOrCreateDatabase("Kitaplar",MODE_PRIVATE,null);
                    database.execSQL("CREATE TABLE IF NOT EXISTS kitaplar(id INTEGER PRIMARY KEY,kitapAdi VARCHAR,kitapYazari VARCHAR,kitapOzeti VARCHAR,kitapResim BLOB)");

                    //dataları eklemeye geldik
                    String sqlSorgusu = "INSERT INTO kitaplar(kitapAdi,kitapYazari,kitapOzeti,kitapResim) VALUES(?, ?, ?, ?)";
                    SQLiteStatement statement = database.compileStatement(sqlSorgusu);

                    statement.bindString(1,kitapIsmi);
                    statement.bindString(2,kitapYazari);
                    statement.bindString(3,kitapOzeti);
                    statement.bindBlob(4,kayitEdilecekResim);
                    statement.execute();

                    nesneleriTemizle();
                    btnKaydet.setEnabled(false);

                    Toast.makeText(getApplicationContext(),"Kitabınız listeye eklendi",Toast.LENGTH_SHORT).show();
                    //böylelikle verileri eklemiş olduk


                }else{
                    Toast.makeText(getApplicationContext(),"Kitap özeti boş bırakılamaz",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Kitap yazarı boş bırakılamaz",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Kitap ismi boş bırakılamaz",Toast.LENGTH_LONG).show();
        }
    }

    //SQlite databasesinde sıkıntı çıkmaması için resmimizi küçültüyoruz
    private Bitmap resimiKucult(Bitmap resim){

        return Bitmap.createScaledBitmap(resim,120,150,true);

    }

    //Bu metot kitap eklendikten sonra kaydet butonuna basıldıktan sonra ekranda
    //verilerin kalmaması için oluşturuldu
    private void nesneleriTemizle(){

        editTextKitapIsmi.setText("");
        editTextKitapYazari.setText("");
        editTextKitapOzet.setText("");
        imgKitapResim.setBackgroundResource(R.drawable.resimekle);

    }


    //activity_add_book sayfasındaki (xml,layout) kitap resmi eklencek bölge'nin fonksiyonu
    public void resimSec(View v)
    {
        //activity_add_book sayfasındaki (xml,layout) kitap resmi eklencek bölge'nin fonksiyonu
        //bu fonksiyon sayesinde sanırım bu kodun aynısını alıp kopyalayıp yapıştırırsam her resim almalı yerlerde kullanıbilirim ama şu anlık emin değilim

        //ilk önce kullacıdan izin almak gerekiyor bununu kontrol ediyoruz
        //manifestten <user-permission android:name = "android.permission.READ_EXTERNAL_STORAGE" dahil edilmeli

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //Kullanıcı eğer izni kabul etmemişse bu if'in içine giriyor

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},imgIzinAlmaKodu);
            Toast.makeText(getApplicationContext(),"Kitabınızın resmini ekleyebilmeniz için izin vermeniz gerekmektedir",Toast.LENGTH_LONG).show();
            //Kullanıcı izin vermemişse kullanıcının ekranına izin almak için tekrar istek gönderecek


        }else{
            //Kullanıcı izin vermiş hali

            Intent resimiAl = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(resimiAl,imgIzinVerildiKodu);
        }
    }


    //Bu fonksiyon da şunun için...
    //Eğer kullanıcı ilkten resim almak için dosya iznini vermemişse ve sonrasında vermişse bu fonksiyon çalışacak
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //eğer kullanıcı izin vermediyse alanı
        if (requestCode == imgIzinAlmaKodu)
        {
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Kullanıcı izni vermişse demek oluyor üstteki

                Intent resimiAl = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(resimiAl,imgIzinVerildiKodu);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //galeriye gittikten sonra seçilen resmin değerini alıyoruz
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == imgIzinVerildiKodu){
            //kullanıcı veriyi seçmişse
            if (resultCode == RESULT_OK && data != null){
                //resmin yolunu aldık artık bitmap yapmamız gerek
                Uri resimUri = data.getData();

                try {
                    if (Build.VERSION.SDK_INT >= 28)
                    {//yeni telefonlar için olan kod
                        ImageDecoder.Source resimSource = ImageDecoder.createSource(this.getContentResolver(), resimUri);
                        secilenResim = ImageDecoder.decodeBitmap(resimSource);
                        imgKitapResim.setImageBitmap(secilenResim);

                    }else{
                        //eski telefonlar için olan kod
                        //private Bitmap olarak oluşturduğumuz secilen resim değişkenini alttaki satır sayesinde doldurduk
                        secilenResim = MediaStore.Images.Media.getBitmap(this.getContentResolver(),resimUri);
                        //doldurdugumuz secilenResim degiskenini activity_add_book xml inde bulunan imageView e .setImageBitmap ile verdik.
                        //normalde imageView e bu şekilde resim vermiyoruz. setImageResource(); ile veriyoruz.
                        imgKitapResim.setImageBitmap(secilenResim);
                    }
                    btnKaydet.setEnabled(true);

                } catch (IOException e) {
                    //burda yapılan try-catch sistemin kendi yaptığı try-catch biz kendimiz yapmadık
                    //bu yapı .getBitmap den geliyor
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(this,MainActivity.class);

        finish();

        startActivity(backIntent);
    }
}