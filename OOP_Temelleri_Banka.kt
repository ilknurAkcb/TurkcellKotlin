
// ============================================================
// BÖLÜM 1 — KAPSÜLLEME (Encapsulation)
// ============================================================

class BankaKullanicisi(
    val ad: String,
    val soyad: String,
    val tcKimlikNo: String,           // dışarıdan okunabilir ama yazılamaz
    private val sifre: String,        // tamamen gizli — dışarıdan erişilemez
    baslangicBakiye: Double = 0.0
) {
    // ── Özellikler ──────────────────────────────────────────
    val hesapNo: String = hesapNoUret(tcKimlikNo)  // otomatik üretilir, değiştirilemez

    // "private set" → dışarıdan okunan ama dışarıdan değiştirilemeyen bakiye
    var bakiye: Double = baslangicBakiye
        private set

    private var hataliGirisSayisi: Int = 0
    private var hesapKilitli: Boolean = false

    // ── İkincil Kurucu (Secondary Constructor) ──────────────
    // Sadece ad, soyad ve TC ile de hesap açılabilsin:
    constructor(ad: String, soyad: String, tcKimlikNo: String)
            : this(ad, soyad, tcKimlikNo, sifre = "12345", baslangicBakiye = 0.0)

    // ── init Bloğu — nesne oluşturulduğunda çalışır ──────────
    init {
        require(ad.isNotBlank())      { "Ad boş olamaz." }
        require(soyad.isNotBlank())   { "Soyad boş olamaz." }
        require(tcKimlikNo.length == 11 && tcKimlikNo.all { it.isDigit() }) {
            "TC Kimlik No 11 haneli rakamdan oluşmalıdır."
        }
        require(baslangicBakiye >= 0) { "Başlangıç bakiyesi negatif olamaz." }
        println("✅ Hesap oluşturuldu → Hesap No: $hesapNo | Sahip: $ad $soyad")
    }

    // ── Metotlar (Davranışlar) ───────────────────────────────

    /** Bakiyeye para ekler; negatif tutar veya kilitli hesap kabul edilmez. */
    fun paraYatir(tutar: Double) {
        kontolEt()
        require(tutar > 0) { "Yatırılacak tutar pozitif olmalıdır." }
        bakiye += tutar
        println("💰 $tutar TL yatırıldı. Güncel bakiye: $bakiye TL")
    }

    /** Bakiyeden para çeker; yetersiz bakiye veya kilitli hesap reddedilir. */
    fun paraCek(tutar: Double) {
        kontolEt()
        require(tutar > 0)      { "Çekilecek tutar pozitif olmalıdır." }
        require(tutar <= bakiye) { "Yetersiz bakiye." }
        bakiye -= tutar
        println("💸 $tutar TL çekildi. Kalan bakiye: $bakiye TL")
    }

    fun bilgileriGoster() {
        println("""
            ┌─────────────────────────────────┐
            │  Ad Soyad  : $ad $soyad
            │  Hesap No  : $hesapNo
            │  Bakiye    : $bakiye TL
            │  Durum     : ${if (hesapKilitli) "KİLİTLİ 🔒" else "Aktif ✅"}
            └─────────────────────────────────┘
        """.trimIndent())
    }

    // ── Private Yardımcı Metot ───────────────────────────────
    private fun kontolEt() {
        check(!hesapKilitli) { "Bu hesap kilitlidir, işlem yapılamaz." }
    }

    // ── Eşlik Eden Nesne (Companion Object) — static benzer ─
    companion object {
        private var sayac = 1000

        /** TC'nin son 4 hanesi + artan sayaçtan oluşan hesap numarası üretir. */
        fun hesapNoUret(tc: String): String {
            sayac++
            return "TR-${tc.takeLast(4)}-$sayac"
        }
    }
}


// ============================================================
// BÖLÜM 2 — SOYUTLAMA (Abstraction)
// ============================================================


// Tüm banka ürünlerine uygulanabilecek arayüz:
interface BankaUrunu {
    val urunAdi: String
    val yillikFaizOrani: Double   // %
    fun faizHesapla(): Double
    fun ozetYazdir()
}

// Temel hesap türü — doğrudan nesne oluşturulamaz
abstract class TemelHesap(
    val hesapSahibi: BankaKullanicisi,
    override val urunAdi: String,
    override val yillikFaizOrani: Double
) : BankaUrunu {

    // Alt sınıflar bu metodu kendi mantığıyla doldurmak ZORUNDA
    abstract fun hesapTuruAcikla(): String

    // Ortak somut davranış — tüm hesaplar bunu miras alır
    override fun ozetYazdir() {
        println("📋 ${hesapSahibi.ad} ${hesapSahibi.soyad} | $urunAdi | Faiz: %$yillikFaizOrani")
    }
}


// ============================================================
// BÖLÜM 3 — KALITIM (Inheritance)
// ============================================================


open class VadesizHesap(
    hesapSahibi: BankaKullanicisi
) : TemelHesap(hesapSahibi, urunAdi = "Vadesiz Mevduat Hesabı", yillikFaizOrani = 0.5) {

    override fun hesapTuruAcikla() =
        "İstediğiniz zaman para yatırıp çekebileceğiniz standart hesap."

    // Yıllık faiz → günlük bakiye üzerinden hesaplanıyor (basitleştirilmiş)
    override fun faizHesapla(): Double {
        val yillikFaiz = hesapSahibi.bakiye * (yillikFaizOrani / 100)
        println("📈 Vadesiz hesap yıllık faizi: $yillikFaiz TL")
        return yillikFaiz
    }
}

class VadeliHesap(
    hesapSahibi: BankaKullanicisi,
    val vadeSuresiAy: Int,            // 1, 3, 6, 12 ay
    private val yatirimMiktari: Double
) : TemelHesap(hesapSahibi, urunAdi = "Vadeli Mevduat Hesabı", yillikFaizOrani = 22.0) {

    override fun hesapTuruAcikla() =
        "Belirli bir süre için bağlanan, vadesiz hesaba göre daha yüksek faiz getiren hesap."

    override fun faizHesapla(): Double {
        val donemFaiz = yatirimMiktari * (yillikFaizOrani / 100) * (vadeSuresiAy / 12.0)
        println("📈 $vadeSuresiAy aylık vadeli hesap faizi: $donemFaiz TL")
        return donemFaiz
    }

    // Üst sınıf metodunu GENIŞLETIYOR (super çağrısı):
    override fun ozetYazdir() {
        super.ozetYazdir()   // TemelHesap'ın metodunu çalıştır
        println("   ↳ Vade: $vadeSuresiAy ay | Yatırım: $yatirimMiktari TL")
    }
}

// Kredi hesabı — VadesizHesap'tan türer (çok katmanlı kalıtım örneği)
class KrediHesabi(
    hesapSahibi: BankaKullanicisi,
    val krediLimiti: Double
) : VadesizHesap(hesapSahibi) {

    private var kullanılanKredi: Double = 0.0

    fun krediKullan(tutar: Double) {
        require(tutar > 0)                              { "Tutar pozitif olmalıdır." }
        require(kullanılanKredi + tutar <= krediLimiti) { "Kredi limiti aşılamaz." }
        kullanılanKredi += tutar
        println("💳 $tutar TL kredi kullanıldı. Kalan limit: ${krediLimiti - kullanılanKredi} TL")
    }

    override fun hesapTuruAcikla() =
        "Belirlenen limit dahilinde harcama yapılabilen kredi ürünü."

    // Kredi faizi vadesiz hesaptan farklı hesaplanır:
    override fun faizHesapla(): Double {
        val krediKostSuresi = krediLimiti * 0.18   // Basit model
        println("📈 Yıllık kredi faiz maliyeti: $krediKostSuresi TL")
        return krediKostSuresi
    }
}


// ============================================================
// BÖLÜM 4 — ÇOK BİÇİMLİLİK (Polymorphism)
// ============================================================



object BankaSistemi {

    /**
     * Polimorfizm örneği:
     * Parametre türü "TemelHesap" — hangi alt sınıf gelirse gelsin çalışır.
     */
    fun aylikRaporYazdir(hesap: TemelHesap) {
        println("\n── Hesap Raporu ──────────────────────────")
        hesap.ozetYazdir()          // hangi sınıfın metodu çağrılacak? → çalışma zamanında belirlenir
        hesap.faizHesapla()         // her alt sınıf kendi hesaplamasını yapar
        println("  Açıklama: ${hesap.hesapTuruAcikla()}")
        println("────────────────────────────────────────\n")
    }

    /** Birden fazla hesabı tek döngüde işleme — polimorfizmin gücü */
    fun tumHesaplariRaporla(hesaplar: List<TemelHesap>) {
        println("=== BANKA AYLIK FAİZ RAPORU ===")
        hesaplar.forEach { aylikRaporYazdir(it) }
    }

    /** Overloading örneği: aynı metot adı, farklı parametreler */
    fun havaleYap(gonderici: BankaKullanicisi, alici: BankaKullanicisi, tutar: Double) {
        println("🔁 Havale: ${gonderici.ad} → ${alici.ad} | $tutar TL")
        gonderici.paraCek(tutar)
        alici.paraYatir(tutar)
    }

    fun havaleYap(gonderici: BankaKullanicisi, aliciHesapNo: String, tutar: Double) {
        // Gerçekte hesap numarasına göre alıcı bulunur; burada sadeleştirildi
        println("🔁 Havale gönderildi: ${gonderici.ad} → Hesap #$aliciHesapNo | $tutar TL")
        gonderici.paraCek(tutar)
    }
}


// ============================================================
// BÖLÜM 5 — DATA CLASS ve ENUM CLASS (Kotlin Özellikleri)
// ============================================================
//
//  data class:
//    Yalnızca veri taşımak için kullanılan özel sınıf türüdür.
//    Kotlin otomatik olarak equals(), hashCode(), toString(),
//    copy() metotlarını üretir.
//
//  enum class:
//    Sabit değer kümelerini temsil eder; tip güvenliğini artırır.

enum class HesapTuru {
    VADESİZ, VADELİ, KREDİ, TASARRUF
}

enum class IslemTipi(val aciklama: String) {
    PARA_YATIRMA("Para Yatırma"),
    PARA_CEKME("Para Çekme"),
    HAVALE("EFT/Havale"),
    FAIZ("Faiz Tahakkuku")
}

data class IslemKaydi(
    val hesapNo: String,
    val tip: IslemTipi,
    val tutar: Double,
    val tarih: String,          // gerçek projede: LocalDateTime
    val aciklama: String = ""
) {
    // data class içine metot da eklenebilir
    fun ozet() = "[${tarih}] ${tip.aciklama}: $tutar TL | $aciklama"
}


// ============================================================
// BÖLÜM 6 — EXTENSION FUNCTION (Kotlin'e Özgü)
// ============================================================
//
//  Mevcut sınıflara kaynak kodunu değiştirmeden yeni metot ekler.
//  OOP ilkelerini bozmadan sınıfı genişletmenin Kotlin yolu.

fun BankaKullanicisi.tamAd(): String = "$ad $soyad"

fun BankaKullanicisi.maskeliBakiye(): String {
    return if (bakiye > 0) "***,*** TL" else "0,00 TL"
}

fun Double.tlFormatla(): String = String.format("%.2f TL", this)


// ============================================================
// main — Tüm Kavramların Bir Arada Çalışması
// ============================================================

fun main() {
    println("╔══════════════════════════════════════════╗")
    println("║   KOTLIN OOP — BANKA SİSTEMİ DEMOSU     ║")
    println("╚══════════════════════════════════════════╝\n")

    // ── Nesne Oluşturma ─────────────────────────────────────
    println("▶ SINIF & NESNE — Kullanıcı oluşturma")
    val kullanici1 = BankaKullanicisi(
        ad = "Ayşe",
        soyad = "Kaya",
        tcKimlikNo = "12345678901",
        sifre = "bank2024",
        baslangicBakiye = 5000.0
    )
    val kullanici2 = BankaKullanicisi("Mehmet", "Demir", "98765432100")

    kullanici1.bilgileriGoster()

    // ── Kapsülleme ──────────────────────────────────────────
    println("\n▶ KAPSÜLLEME — Kontrollü erişim")
    kullanici1.paraYatir(1500.0)
    kullanici1.paraCek(200.0)
    // kullanici1.bakiye = 999.0    // ← DERLEME HATASI! private set
    // kullanici1.sifre             // ← DERLEME HATASI! private
    kullanici1.sifreDogrula("yanlis")
    kullanici1.sifreDogrula("yanlis")
    kullanici1.sifreDogrula("bank2024")   // doğru şifre

    // ── Kalıtım & Soyutlama ─────────────────────────────────
    println("\n▶ KALITIM & SOYUTLAMA — Hesap türleri")
    val vadesiz = VadesizHesap(kullanici1)
    val vadeli   = VadeliHesap(kullanici1, vadeSuresiAy = 6, yatirimMiktari = 10_000.0)
    val kredi    = KrediHesabi(kullanici2, krediLimiti = 25_000.0)

    kredi.krediKullan(3000.0)

    // ── Çok Biçimlilik ──────────────────────────────────────
    println("\n▶ ÇOK BİÇİMLİLİK — Aynı metot, farklı davranışlar")
    val tumHesaplar: List<TemelHesap> = listOf(vadesiz, vadeli, kredi)
    BankaSistemi.tumHesaplariRaporla(tumHesaplar)

    // Overloading örneği:
    kullanici2.paraYatir(10_000.0)
    BankaSistemi.havaleYap(kullanici1, kullanici2, 500.0)
    BankaSistemi.havaleYap(kullanici1, "TR-8901-1002", 250.0)

    // ── Data Class & Enum ────────────────────────────────────
    println("\n▶ DATA CLASS & ENUM — İşlem kaydı")
    val islem1 = IslemKaydi(
        hesapNo = kullanici1.hesapNo,
        tip     = IslemTipi.PARA_YATIRMA,
        tutar   = 1500.0,
        tarih   = "2025-04-12",
        aciklama= "Maaş yatırma"
    )
    val islem2 = islem1.copy(tip = IslemTipi.FAIZ, tutar = 75.0, aciklama = "Aylık faiz")
    println(islem1.ozet())
    println(islem2.ozet())

    // ── Extension Functions ──────────────────────────────────
    println("\n▶ EXTENSION FUNCTION — Sınıfı dışarıdan genişletme")
    println("Tam Ad: ${kullanici1.tamAd()}")
    println("Maskelenmiş bakiye: ${kullanici1.maskeliBakiye()}")
    println("Formatlanmış tutar: ${kullanici1.bakiye.tlFormatla()}")

    println("\n╔══════════════════════════════════════════╗")
    println("║   DEMO TAMAMLANDI ✅                      ║")
    println("╚══════════════════════════════════════════╝")
}

