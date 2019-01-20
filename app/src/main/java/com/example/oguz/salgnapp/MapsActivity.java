package com.example.oguz.salgnapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double latitude;
    double longitude;
    String s;

    private String country = "Turkey";
    private String city = "";
    private String ilce = "";
    private String semt = "";


    ArrayList<String> salgin;
    ArrayList<String> info_nedir;
    ArrayList<String> info_korunma;
    ArrayList<String> info_tedavisuresi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        salgin=new ArrayList<>();
        info_nedir = new ArrayList<>();
        info_korunma = new ArrayList<>();
        info_tedavisuresi = new ArrayList<>();

        Intent intent=getIntent();
        latitude= intent.getDoubleExtra("Latitude",0);//Enlem bilgisinin bir önceki aktiviteden alınması
        longitude=intent.getDoubleExtra("Longitude",0);//Boylam bilgisinin bir önceki aktiviteden alınması
        s=intent.getStringExtra("Address");//Adres bilgisinin bir önceki aktiviteden alınması
        veriCek();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng adres = new LatLng(latitude, longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(adres,15));//Kameranın istenilen lokasyonu göstermesi
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!salgin.isEmpty())//Salgın var mı kontrolü
                {
                    for (int i=0;i<salgin.size();i++)
                        addInfo(salgin.get(i));//tespit edilen salgınların, bilgilerinin info ekranına yollanmak için arraylistlere eklenmesi
                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(latitude, longitude))
                            .radius(500)
                            .strokeColor(Color.rgb(128, 128, 0))
                            .fillColor(Color.rgb(230, 230, 0))//Belirlenen konum, renk  ve yarıçapta circle oluşturulup haritaya eklenilmesi

                    );

                    circle.setClickable(true);
                    mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {//Oluşturulan circle a tıklanıp tıklanmadığının dinlenilmesi
                        @Override
                        public void onCircleClick(Circle circle) {
                            sendInfo();
                        }
                    });
                }
            }
        }, 4000);//firebase den verilerin çekilmesi bekleniyor




        }




    public void veriCek(){

        final ArrayList<String> dsAndCount;//Hastalık ve hasta sayılarının tutulacağı arraylist
        final ArrayList<String> gecmisSeneler;
        dsAndCount=new ArrayList<>();
        gecmisSeneler=new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference newReference = firebaseDatabase.getReference(country);
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                getAddress(s);
                city=cevir(city);
                semt=cevir(semt);
                ilce=cevir(ilce);
                //Databasede bulunan periyottaki hasta sayılarının hesaplanması
                for(DataSnapshot data : dataSnapshot.child(city).child(ilce).child(semt).child("Diseases").getChildren()) {
                    int counter=0;
                    System.out.println("Checking");
                    gecmisSeneler.add(data.getKey()+" "+(int)(Math.random() * 15 + 1));//Sağlık bakanlığından alınmadığından dolayı dönemsel hasta sayılarının random olarak üretilmesi
                    for (DataSnapshot data2 : data.getChildren()) {
                        counter = counter + Integer.parseInt(data2.getValue().toString());
                    }
                    dsAndCount.add(data.getKey()+" "+counter);
                }
                for (int i=0;i<dsAndCount.size();i++){//Hastalığın ve hasta sayısının beraber tutulması için birlikte tutulan stringin split edilerek ayrılması
                    String[] splited=dsAndCount.get(i).split(" ");
                    String[] splited2=gecmisSeneler.get(i).split(" ");
                    if(Integer.parseInt(splited[1])>Integer.parseInt(splited2[1]))
                        salgin.add(i,splited[0]);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Aradığınız semt henüz sisteme girilmedi",Toast.LENGTH_LONG);
            }
        });


    }
    public void getAddress (String _location){
        try {
            String[] adres = _location.split(",");
            int adrescount = adres.length;
            System.out.println("adres index" + adrescount);
            String[] smt = adres[0].split(" ");
            semt = smt[0];
            country = adres[adrescount - 1];
            String[] _ilce = adres[adrescount - 2].split(" ");
            _ilce = _ilce[2].split("/");
            city = _ilce[1];
            ilce = _ilce[0];
            semt = semt.replace(".", ",");
            System.out.println("ülke:" + country + " " + city + " " + ilce + " " + semt);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Adres Uyumlu Degil",Toast.LENGTH_LONG);
        }


    }
    public void addInfo (String hastalik){
        switch (hastalik){
            case "Flu":
                //Flu
                info_nedir.add("İnfluenza, başlıca burun, boğaz, bronşlar ve bazen akciğerleri etkileyen, hafif veya ağır bir seyir gösterebilen," +
                        " bulaşıcı viral bir hastalıktır. A, B, C olmak üzere 3 tip influenza virüsü vardır.");

                info_tedavisuresi.add("2 hafta ya da daha uzun süre devam edebilir." +
                        "İnsanların çoğu herhangi bir tıbbi tedaviye gerek kalmadan 1 hafta içinde iyileşirler.");

                info_korunma.add("En etkili yol grip aşısı olmaktır.En iyi ikinci koruma yöntemi sağlıklı beslenmektir." +
                        "Grip en çok soluduğumuz havadan geçer." +
                        "Bu nedenle havalandırması yetersiz ve çok kalabalık ortamlardan uzak durmamız bizi gribe karşı koruyacaktır.");
                break;
            case "Measles":
                //Measles
                info_nedir.add("Kış sonu ve ilkbahar döneminde ortaya çıkan, ateş ve döküntülerle karakterize bulaşıcı bir viral hastalıktır." +
                        "Kızamığın ilk belirtisi genellikle yüksek ateştir." +
                        "Başlangıç aşamasında burun akıntısı, gözlerde kızarma, sulanma, öksürük ve yanak içlerinde beyaz noktacıklar görülür. " +
                        "Birkaç gün sonra, (yaklaşık olarak virüse maruziyetten 14 gün sonra (7-18 gün) ) genellikle yüz ve üst boyun bölgesinden bir döküntü başlar. Yaklaşık üç gün içinde döküntü tüm vücuda yayılır");
                info_tedavisuresi.add("Virüse maruz kalındıktan 10-12 gün sonra başlar ve 4-7 gün boyunca sürebilir." );

                info_korunma.add("Çok bulaşıcı olan bu hastalıktan korunmanın tek yolunun aşı olmaktır.");
                break;
            case "Tuberculosis":
                //Tuberculosis
                info_nedir.add("Tüberküloz; Mycobacterium tuberculosis denilen bakteri tarafından meydana getirilen, genellikle akciğerleri etkileyen, beyin, omurga, böbrekler, sindirim sistemi," +
                        "kemikler gibi vücudun değişik organ ve sistemlerini de tutabilen bir enfeksiyondur. Önlenebilen ve tedavi edilebilen bir hastalıktır ancak tedavi edilmezse öldürücü olabilir.");

                info_tedavisuresi.add("Üç hafta veya daha uzun süre devam eden öksürük," +
                        "öksürükle kan veya balgam çıkarma, göğüs ağrısı, halsizlik veya yorgunluk, kilo kaybı," +
                        "iştahsızlık, ateş, titreme, gece terlemeleri görülür.");
                info_korunma.add("Veremden korunmak için artık günümüzde aşı kullanımı yaygındır. İki aylıkken ve yedi yaşında verem aşısı uygulanır. Çevresinde veya ailesinde verem hastası olanlar kontrol altında olmalı, gerekli tetkikler yapılmalıdır."+
                        "Hastalığın bulaşmaması için bir müddet ilaç da kullanabilir.");}
    }
    public void sendInfo(){
        Intent go = new Intent(getApplicationContext(), InfoScreen.class);
        go.putStringArrayListExtra("Salgin", salgin);
        System.out.println(salgin);
        go.putStringArrayListExtra("Nedir", info_nedir);
        System.out.println(info_nedir);
        go.putStringArrayListExtra("Korunma", info_korunma);
        System.out.println(info_korunma);
        go.putStringArrayListExtra("Sure", info_tedavisuresi);
        System.out.println(info_tedavisuresi);
        startActivity(go);
    }
    public String cevir(String turk){
        turk= turk.replaceAll("ç","c");
        turk= turk.replaceAll("Ç","C");
        turk= turk.replaceAll("ö","o");
        turk= turk.replaceAll("Ö","O");
        turk= turk.replaceAll("İ","I");
        turk= turk.replaceAll("ı","i");

        return turk;
    }//Turkce karakterlerin ingilizce karakterlere cevrilmesi

}
