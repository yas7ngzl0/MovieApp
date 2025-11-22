---

# 🛠 PROJECT_JOURNEY.md

### **Geliştirme Süreci ve Mühendislik Yol Haritam**

Bu proje, “Mobile Developer Challenge” kapsamında değerlendirilmek üzere geliştirilmiş olup, temel amaç modern Android geliştirme pratiğini uçtan uca göstermekti. Kodlama sürecine başlamadan önce belirli bir yol haritası oluşturarak, hem mimari bütünlüğü korumayı hem de projenin kontrollü bir şekilde ilerlemesini hedefledim.

Aşağıda, bu proje boyunca izlediğim gerçekçi ve adım adım yol haritası yer almaktadır.

---

## **1. API Hazırlığı ve Güvenli Kurulum**

İlk adım olarak TMDB platformuna kaydolup gerekli **API anahtarını** edindim.
Bu anahtarı doğrudan koda yazmak yerine, versiyon kontrolüne dahil olmayan **local.properties** dosyasına ekledim ve proje içinde **BuildConfig** aracılığıyla kullanılabilir hale getirdim.
Bu yaklaşım, küçük çaplı projelerde bile güvenli API yönetimi açısından önemlidir.

---

## **2. Dosya Yapısının Planlanması**

Kodlamaya başlamadan önce, projenin büyüdükçe karmaşık hale gelmesini önlemek için klasör yapısını belirledim.
Günümüzün gelişmiş yapay zeka araçlarından destek alınarak çalışıldığında, **net bir iskelet belirlenmemişse** projenin zamanla “spaghetti code” hâline gelme riski oldukça yüksektir.

Bu sebeple, daha en baştan **data / repository / ui / util** bölümlerini netleştirdim.

---

## **3. Veri Katmanı (Model – Repository – Serialization)**

Proje iskeleti oturduktan sonra veri katmanıyla başladım:

* TMDB’den dönen JSON yapısını inceleyerek gerekli modelleri oluşturdum.
* `Movie` ve `MovieResponse` sınıflarını hazırladım.
* Retrofit & Gson kullanarak **API servislerini** tanımladım.
* Repository katmanını MVVM’e uygun şekilde oluşturdum.

Bu aşamada proje henüz UI içermiyordu; sadece backend katmanı tamamen işlevsel hâle getirildi.

---

## **4. API Entegrasyonu ve İlk Testler**

Backend kodları tamamlandıktan sonra, API’den veri akışının doğru olup olmadığını doğrulamak için **integration test** yazdım (`MovieRepositoryTest`).

Testte şu soruları yanıtladım:

* API’ya gerçekten bağlanıyor muyuz?
* Dönen JSON beklediğimiz modele dönüşüyor mu?
* Hata durumları doğru şekilde sarılıp `Resource.Error` olarak dönüyor mu?

UI’a geçmeden önce veri katmanının %100 çalıştığından emin oldum.

---

## **5. UI Katmanı İçin Hazırlık: Tema + Dil Dosyaları**

Backend doğrulandıktan sonra UI tarafındaki temel yapı taşlarını oluşturdum:

* **Tema renk paletini** belirledim (Dark/Light).
* Material 3 kurallarına uygun `Theme.kt` dosyalarını hazırladım.
* İngilizce ve Türkçe için tüm **strings.xml** dosyalarını oluşturdum.

Bu aşama, Compose ekranlarına geçmeden önce görsel standartları belirlemek açısından önemliydi.

---

## **6. MVVM Yapısına Göre UI Geliştirme (İçten Dışa Yaklaşım)**

UI’ı doğrudan büyük ekranlarla başlatmak yerine, **içten dışa** (atomic → component → page) yaklaşım izledim:

1. Kart bileşenleri (`MovieItem`, rating görünümü, image loader, vb.) oluşturuldu.
2. Ardından Home, Detail, Search gibi ekranlar MVVM yapısına uygun olacak şekilde kodlandı.
3. ViewModel’lerde `StateFlow` kullanarak state yönetimi sağladım.

Bu yapı, hem test edilebilirliği artırdı hem de Compose ile uyumlu reaktif bir UI sağladı.

---

## **7. Pagination (Sonsuz Kaydırma) Mantığının Kurulması**

Paging 3 gibi daha karmaşık çözümlere ihtiyaç olmadığı için, proje ölçeğine uygun **özel bir sonsuz kaydırma mekanizması** kurdum.

* `LazyVerticalGrid` üzerinden scroll state takip edildi.
* Kullanıcı listenin sonuna yaklaştığında ViewModel otomatik olarak bir sonraki sayfayı çekti.
* Gelen sonuçlar mevcut listeye eklenerek kesintisiz bir akış sağlandı.

Bu yaklaşım, performans kaybı olmadan hafif bir pagination çözümü sunuyor.

---

## **8. Unit Testler (Home & Search Mantığı)**

API katmanı test edildikten sonra, uygulamanın mantığını taşıyan ViewModel’ler için de Unit Test yazdım.

Testlerde:

* Loading → Success → Error state geçişleri
* Search debounce mantığının fazla API isteğini engellemesi
* Pagination sayfa artışlarının doğru çalışması

gibi senaryolar doğrulandı.

Bu sayede UI’ın bağlı olduğu iş mantığı tamamen güvence altına alındı.

---

## **9. Kullanıcı Dostu Hata Yönetimi**

Son aşamada, ağ hataları veya API kesintileri durumunda kullanıcıya anlık geri bildirim verecek Error Screen tasarlandı.

* Açıklayıcı mesajlar
* "Retry" butonu
* Dark/Light uyumu

Bu ekran tüm sayfalara merkezi bir yapı üzerinden entegre edildi.

---

## **10. Yapay Zeka Araçlarının Kullanım Prensibi**

Proje boyunca modern AI araçlarından yararlandım; ancak kullanım biçimim belirli prensiplere bağlıydı:

* Aşırı kod üretimini değil, fikir ve hız kazanımını hedefledim.
* Anlamadığım, hakim olmadığım hiçbir kodu projeye dahil etmedim.
* Her kod parçası önce benim mantık filtremden geçti, ardından projeye entegre edildi.

Bu yaklaşım sayesinde hem zaman kazandım hem de proje üzerinde tam hâkimiyet sağladım.


## **11. Hilt Yerine Neden Manuel DI Tercih Ettim ? **

Challenge'da Hilt önerilmesine rağmen, proje ölçeği (3-4 ekran) ve teslim süresi (Time-to-Market) göz önüne alındığında, Constructor Injection yöntemiyle Manuel DI uygulamanın daha doğru bir yöntem olduğuna karar verdim. Bu sayede Hilt'in kurulumda çıkarması muhtemelen sorunlarından ve kurulum maliyetinden kaçınırken (zaman) aynı işlevi görecek basit ve küçük ölçekte sürdürülebilir bir yapı kurdum

---

# 🎯 Sonuç

Bu dosya, proje geliştirme sürecinin teknik ve sistematik bir özeti niteliğindedir. Tüm adımlar, projenin daha kontrollü, test edilebilir, modüler ve sürdürülebilir şekilde ilerlemesi için planlanmıştır.
Uygulama; modern Android geliştirme standartları, Compose mimarisi ve MVVM prensipleri doğrultusunda tamamlanmıştır.



