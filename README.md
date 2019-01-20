# cilia

<p>Yapılan proje database'den alınan bilgiler ile kullanıcının bulunduğu konumda ya da 
kulanıcının girdiği herhangi bir konumda bulunan hastalıkların dağılımları belirlenmektedir.</p>

<p>
<b>MainActivity</b> class ında kullanıcının konumunun çekilmesi ya da istediği yeri araması işlemleri yapılmıştır.
<code>permissionCheck();</code> satırıyla kullanıcıdan gerekli izinlerin alınıp alınmadığının kontrolü yapılmıştır.</br>
<i>Android kullanılırken kullanıcının konumuna erişmek, depolama alanına erişmek vs. için gerekli izinlerin alınılması gerektiği unutulmamalıdır.</i>
</p>

<p>
<b>searchAddress()</b> methoduyla anahtar kelimeler ile adres bulunması işlemi yapılmıştır. Adres bulunurken anahtar kelimeler veya GPS servisi
kullanılarak konum bulunma işlemi yapılmıştır.
</p>


<p>
<b>MapsActivity</b> class ında ise <b>MainActivity</b> class ından alınan konum bilgileri yer almaktadır. Bu bilgiler kullanılarak 
kullanıcıya görsel olarak aranan ya da bulunulan konumun gösterilme işlemi <b>onMapReady()</b> methodu aracılıyla yapılmıştır.
</p>

<p>
Database den verilerin çekilmesi yani konum bilgileri kullanılarak o bölgedeki hastalıkların ve hastalıkların sayısının bulunması 
işlemi ise <b>veriCek()</b> methoduyla sağlanmıştır.<i>Projede <b>Real Time Database</b> kullanılmıştır.</i> 
</p>
<p>
<b>veriCek()</b> methodu içerisinde yer alan <b>onDataChange()</b> methodu ile database den sürekli bir dinleme olmaktadır.
Burada daha önceki senelere ait verilere(hastalık ve hasta sayısı) sahip olmadığımızdan bu veriler <i>(int)(Math.random() * 15 + 1</i> methodu
kullanılarak random olarak üretilmiştir.
</p>

<p><i><b>onDataChange()</b> methodu asenkron bir methodtur</i> Projede bundan kaynaklı bazı sorunlarla karşılaştığımızdan dolayı
<b>veriCek()</b> methodunun <b>Handler</b> içerisinde çağrılmasıyla sorun çözülmüştür.
</p>

<p>
Database de bizim oluşturduğumuz bazı hastalıklar ve belli bir periyoda ait hasta sayıları bulunmaktadır. <b>veriCek()</b> methodunda
bu değerlerin toplanması ve daha önceki seneye ait sayıların karşılaştırılmasıyla o bölgeye ait hastalığın olup olmadığına dair
bir karar verilmektedir. Bu işlem ise for döngüsü kullanılarak yapılmıştır.<i> Burada fazladan değişken tutulmaması için hastalık ve
hasta sayısı aynı <i>String</i> değişkeninde tutulmaktadır.</i> Sonrasında ise değerler <b>split()</b> ve <b>parseInt()</b> methodları
kullanılarak elde edilmiştir.
</p>

<p>
Konum bilgileri dolayısıyla adres alındıktan sonra <b>getAddress()</b> methoduyla alınan bilgilerin database'den veri çekilecek formata çekilecek
hale getirilmesi sağlanmıştır. Bu method sayesinde ülke, şehir, semt bilgilerine ulaşılabilmektedir.
</p>

<p>Projede kullanılan database in dili ile Türkçe'nin uyumlu olamaması nedeniyle <b>cevir()</b> methoduyla Türkçe İngilizceye çevrilmiştir.</p>

<p> <b>addInfo()</b> ve <b>sendInfo()</b> methodlarıyla hastalıkların bilgilerinin (tanımı,tedavi süresi, korunma yolları) alınması ve diğer sayfaya göderilmesi sağlanmıştır.</p>


