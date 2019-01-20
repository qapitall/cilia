package com.example.oguz.salgnapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class InfoScreen extends AppCompatActivity {
    ArrayList<String> name,nedir,korunma,ortalamaTedaviSuresi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_screen);
        Intent intent=getIntent();
        name=intent.getStringArrayListExtra("Salgin");
        nedir=intent.getStringArrayListExtra("Nedir");
        korunma=intent.getStringArrayListExtra("Korunma");
        ortalamaTedaviSuresi=intent.getStringArrayListExtra("Sure");
        ListView lstView = findViewById(R.id.listView);
        liste_class adapter = new liste_class(name,nedir,korunma,ortalamaTedaviSuresi,this);//Bilgilerin listelenmesi için adapterla bağlanılması
        lstView.setAdapter(adapter);//Adapter ın liste ile bağlanılması
    }

}
