package com.example.oguz.salgnapp;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class liste_class extends ArrayAdapter<String> {
    private final ArrayList<String> name;
    private final ArrayList<String> nedir;
    private final ArrayList<String> korunma;
    private final ArrayList<String> ortalamaTedaviSuresi;

    private final Activity context;

    public liste_class(ArrayList<String> name, ArrayList<String> nedir, ArrayList<String> korunma, ArrayList<String> ortalamaTedaviSuresi, Activity context) {
        super(context,R.layout.custom_view,name);//name ArrayListindeki her eleman için bir liste elemanı oluşturulması
        this.name = name;
        this.nedir = nedir;
        this.korunma = korunma;
        this.ortalamaTedaviSuresi = ortalamaTedaviSuresi;

        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=context.getLayoutInflater();
        final View customView=layoutInflater.inflate(R.layout.custom_view,null,true);//custom_view adlı layout dosyasının liste'nin tüm elemanlarının tasarımını oluşturması
        TextView isim=customView.findViewById(R.id.nameText);
        TextView info=customView.findViewById(R.id.nedirText);
        TextView korun=customView.findViewById(R.id.korunmaText);
        TextView ortalama=customView.findViewById(R.id.tedaviSuresiText);

        isim.setText(name.get(position));
        info.setText(nedir.get(position));
        korun.setText(korunma.get(position));
        ortalama.setText(ortalamaTedaviSuresi.get(position));

        return customView;
    }
}
