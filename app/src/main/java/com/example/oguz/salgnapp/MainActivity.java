package com.example.oguz.salgnapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    EditText searchEdit;
    Button searchButton,getLocation;
    ConstraintLayout layout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchEdit=findViewById(R.id.editSearch);
        searchButton=findViewById(R.id.searchButton);
        getLocation=findViewById(R.id.button2);
        layout=findViewById(R.id.layout);



        permissionCheck();//Izin kontrolü


    }

    public void permissionCheck() {
        int gpsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);//Konum erişimine izin durumunu çek
        if (gpsPermission != PackageManager.PERMISSION_GRANTED) {//izin verilmediyse
            List<String> listPermissionsNeeded = new ArrayList<>();
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1905);//izin iste
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation(View view) {
        startLoadingScreen();
        final String gpsAcildi = "GPS Acıldı";
        final String gpsKapatildi = "GPS Kapalı";
        final Boolean[] konumBulunduMu = {false};//konum bulundu mu sorgusu,bulunduktan sonra tekrar methodların çağrılması engellenir

        LocationManager konumYoneticisi = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener konumDinleyicisi = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {//GPS den veri çekilmeye başlandı
                Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onProviderDisabled(String provider) {//GPS den veri çekilemiyor
                Toast.makeText(getApplicationContext(), gpsKapatildi, Toast.LENGTH_SHORT).show();
                System.out.println("GPS Bağlantı Bekleniyor...");
            }

            @Override
            public void onLocationChanged(Location loc) {//Enlem ve boylam bilgileri başarıyla çekildi
                loc.getLatitude();
                loc.getLongitude();


                try {
                    if(!konumBulunduMu[0]){//Enlem ve boylem bilgileri ilk çekildiğinde çağrının yapılması sağlanır
                     konumBulunduMu[0] =true;
                    getAddress(loc.getLatitude() ,loc.getLongitude());//Enlem ve Boylamdan adres bilgisi oluştur
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };


        konumYoneticisi.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, konumDinleyicisi);
    }
    public void getAddress(double ltt,double lng) throws IOException {
        Geocoder geocoder;
        final List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(ltt, lng, 1); //lokasyondan 1 tane adres bilgisi oluştur

        String address = addresses.get(0).getAddressLine(0);

        if(address!=null){//adres oluşturma işlemi başarılıysa


                    Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
                    intent.putExtra("Latitude",addresses.get(0).getLatitude());
                    intent.putExtra("Longitude",addresses.get(0).getLongitude());
                    intent.putExtra("Address",addresses.get(0).getAddressLine(0));
                    endLoadingScreen();
                    startActivity(intent);//Enlem Boylam ve adres bilgilerinin harita aktivitesine gönderilmesi

        }
        else//adres oluşturma işlemi başarısızsa
            Toast.makeText(getApplicationContext(), "Adresiniz Bulunamadı", Toast.LENGTH_SHORT).show();

    }
    public void searchAddress(View view){//Anahtar kelimelerle adres bulunması
        startLoadingScreen();
        Geocoder geocoder;
        final List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses=geocoder.getFromLocationName("Turkey,"+searchEdit.getText().toString(),1);//Anahtar kelimelerden 1 tane adres bilgisi oluştur

                    Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
                    intent.putExtra("Latitude",addresses.get(0).getLatitude());
                    intent.putExtra("Longitude",addresses.get(0).getLongitude());
                    intent.putExtra("Address",addresses.get(0).getAddressLine(0));
                    endLoadingScreen();
                    startActivity(intent);//Enlem Boylam ve adres bilgilerinin harita aktivitesine gönderilmesi

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startLoadingScreen(){//Loading screen başlatıldı
        searchButton.setVisibility(View.INVISIBLE);
        getLocation.setVisibility(View.INVISIBLE);
        layout.setVisibility(View.VISIBLE);
    }
    public void endLoadingScreen(){//Loading screen kapatıldı
        searchButton.setVisibility(View.VISIBLE);
        getLocation.setVisibility(View.VISIBLE);
        layout.setVisibility(View.INVISIBLE);
    }

}
