# Object-Oriented Programming (OOP)

## OOP Nedir?

Object-Oriented Programming (Nesne Yönelimli Programlama), yazılım geliştirme dünyasında karmaşıklığı yönetmek ve kodun tekrar kullanılabilirliğini artırmak için kullanılan güçlü yaklaşımlardan biridir. Yazılım geliştirmede verileri ve bu veriler üzerinde işlem yapan fonksiyonları bir araya getirerek nesneler şeklinde modellemeyi amaçlar. 
Gerçek dünyadaki banka hesabı, kullanıcı gibi varlıkları yazılımda temsil etmeyi kolaylaştırır. 

## Temel Kavramlar
OOP dünyasında geçen `Sınıf(Class)` ve `Nesne(Object)` kavramlarını iyi anlamamız gerekmektedir. 

**Sınıf (Class):** Sınıf bir nesnenin özelliklerini ve davranışlarını tanımlayan bir taslaktır. Kendi başına bellekte yer kaplamaz sadece bir şeyin nasıl olması gerektiğini tarif eder. 

* Örneğin:"Kullanıcı" bir sınıftır. Tüm kullanıcıların adı,soyadı, doğum tarihi gibi bilgiler olması gerektiğini söyler ancak Kullanıcı dediğimizde spesifik bir kullanıcıdan bahsetmeyiz. [OOP_Temelleri_Banka.kt](OOP_Temelleri_Banka.kt)->`6-12. satılara` BankaKullanicisi isimli sınıfı ve sınıf tanımlarının nasıl yapıldığını inceleyebilirsiniz. 

**Nesne (Object):** Nesne, bir sınıftan türetilmiş, somut ve bellekte yer kaplayan bir örnektir (instance). Sınıftaki taslağa göre oluşturulur ve kendine ait verileri vardır.

- Örneğin: İlknur AKCABA isim-soyisimli ve 2001 doğum tarihli kullanıcı bir nesnedir. [OOP_Temelleri_Banka.kt](OOP_Temelleri_Banka.kt)->`287-296. satılara` bakarak kullanıcı nesnesinin nasıl oluşturulduğu daha iyi anlayabilirsiniz. 

**Özellik(Property / Field):** Sınıfa ait değişkenlerdir. Nesnenin durumunu ve özelliklerini temsil eder.

- Örneğin:Ad,soyad,TC ve şifre [OOP_Temelleri_Banka.kt](OOP_Temelleri_Banka.kt)->`6-12` satırları arasında oluşturulan sınıfın içinde ne gibi özellikler olduğunu görebilirsiniz.

**Metot(Method / Function):** Sınıfın davranışlarını tanımlayan fonksiyonlardır. [OOP_Temelleri_Banka.kt](OOP_Temelleri_Banka.kt)->`39-84` arasında detaylıca nasıl farklı şekilde metot tanımlarının yapıldığını ve neler dikkate alındığını görebilirsiniz. 

- Örneğin: paraYatir(), paraCek(), bakiyeGoster()

OOP 4 temel başlık altında ele alınır:

- Kapsülleme

- Soyutlama

- Kalıtım

- Çok Biçimlilik

## 1. KAPSÜLLEME (Encapsulation)

Kapsülleme, verileri (değişkenleri) ve bu veriler üzerinde işlem yapan metodları bir birim (sınıf) içinde saklama tekniğidir. Amaç, veriyi dış müdahalelerden korumak ve sadece belirlenen yollarla erişilmesini sağlamaktır. Kapsülleme çeşitli türlerde yapılır. Kotlin'de kapsülleme çeşitli şekillerde yapılır:

- private  → yalnızca bu sınıf içinden erişilebilir 
- protected→ bu sınıf ve alt sınıflardan erişilebilir
- internal → aynı modül içinden erişilebilir
- public   → her yerden erişilebilir (varsayılan)

[OOP_Temelleri_Banka.kt](OOP_Temelleri_Banka.kt)->`1-85.` satırlar arsında Kapsülleme örneğine erişebilirsiniz. Kullanılan kapsülleme çeşitleri bir önceki ödevle ilişkili olarak banka uygulaması üzerinden verilmiştir. 


## 2. Soyutlama (Abstraction)

Soyutlama, gereksiz detayları gizleyip sadece işlevsel kısımları ön plana çıkarmaktır. "Ne yapıldığını" söyler ama "nasıl yapıldığını" detaylandırmaz. Soyut sınıflar ve arayüzler bu amaca hizmet eder. 

 **abstract class:** [OOP_Temelleri_Banka.kt](OOP_Temelleri_Banka.kt)->`101-114`
   - Doğrudan nesne oluşturulamaz (new ile örneklenemez).
   - Hem soyut (gövdesiz) hem de somut (gövdeli) metotlar içerebilir.
   - Alt sınıflar soyut metotları ZORUNLU olarak override etmek durumundadır.

 **interface** [OOP_Temelleri_Banka.kt](OOP_Temelleri_Banka.kt)->`93-98`
   - Tamamen soyut bir sözleşmedir ("bu metotları uygula" der).
   - Kotlin'de default implementasyon da yazılabilir.
   - Bir sınıf birden fazla interface uygulayabilir.

   ## 3. KALITIM (Inheritance)

Kalıtım, bir sınıfın özelliklerini (alt sınıf / child) ve metodlarını başka bir sınıfa (üst sınıf / parent) aktarmasıdır.
Bu sayede:
- Kod tekrarını azaltır
- Daha düzenli ve sürdürülebilir bir yapı sağlar
- Ortak özellikler tek bir yerde toplanır


Kotlin'de sınıflar varsayılan olarak finaldır (kalıtım alınamaz). Bir sınıfın miras bırakabilmesi için open anahtar kelimesiyle tanımlanması gerekir.[OOP_Temelleri_Banka.kt](OOP_Temelleri_Banka.kt)->`122-135` 


## 4. ÇOK BİÇİMLİLİK (Polymorphism)

Çok biçimlilik, bir nesnenin farklı formlarda davranabilme yeteneğidir. Yani aynı arayüzün farklı sınıflar tarafından farklı biçimlerde uygulanabilmesidir. Genellikle üst sınıf referansıyla alt sınıf nesnelerini tutmak ve aynı metodun farklı sonuçlar üretmesini sağlamak için kullanılır. Çok biçimliliğin en büyük avantajı, kodun esnekliğini artırmasıdır. Örneğin, bir bankacılık uygulamasında farklı hesap türleriniz olduğunu düşünelim. Tüm hesapları tek bir listede toplayıp, her biri için doğru hesaplama yöntemini otomatik olarak çağırabilirsiniz.

Çok biçimlilik, programın hangi kodu çalıştıracağına ne zaman karar verdiğine bağlı olarak ikiye ayrılır:

- **Derleme Zamanı Polimorfizmi (Static Polymorphism)**
Bu tür, Fonksiyon Aşırı Yükleme (Method Overloading) ile gerçekleştirilir. Aynı isimli fonksiyonun, farklı parametre türleri veya sayıları ile tanımlanmasıdır. Hangi fonksiyonun çalışacağı daha kod derlenirken (yazım aşamasında) bellidir.

- **Çalışma Zamanı Polimorfizmi (Dynamic Polymorphism)**
Bu, en yaygın kullanılan türdür ve Metot Geçersiz Kılma (Method Overriding) ile yapılır. Program çalışırken, üst sınıf referansı üzerinden hangi alt sınıf nesnesine erişiliyorsa o sınıfa ait metodun tetiklenmesidir.