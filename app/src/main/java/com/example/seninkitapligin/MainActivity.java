package com.example.seninkitapligin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private KitapAdapter adapter;
    static public KitapDetayi kitapDetayi;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Action Bara add_menu layoutunu ekledik artık bir buton var
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Sağ üstteki ekle butonuna basılınca intent geçişi sağlanıyor
        if (item.getItemId() == R.id.add_menu_add_book)
        {
            Intent intent = new Intent(MainActivity.this,AddBookActivity.class);
            finish();
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_activity_recyclerView);
        adapter = new KitapAdapter(Kitap.getData(this),this);

        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new KitapAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Kitap kitap) {
                Toast.makeText(getApplicationContext(), kitap.getKitapAdi(),Toast.LENGTH_SHORT).show();

                kitapDetayi = new KitapDetayi(kitap.getKitapAdi(),kitap.getKitapYazari(),kitap.getKitapOzeti(),kitap.getKitapResim());

                Intent detayIntent = new Intent(MainActivity.this,DetayActivity.class);



                startActivity(detayIntent);

            }
        });

    }
}