
typealias Hesap = MutableMap<String, Any>

// ─────────────────────────────────────────────────────────────
// 1. hesapOlustur — yeni bir hesap kaydı döndürür
// ─────────────────────────────────────────────────────────────
fun hesapOlustur(id: String, sahip: String, baslangicBakiye: Double = 0.0): Hesap {
    require(baslangicBakiye >= 0) { "Başlangıç bakiyesi negatif olamaz." }
    return mutableMapOf(
        "id"           to id,
        "owner"        to sahip,
        "balance"      to baslangicBakiye,
        "transactions" to mutableListOf<String>()
    )
}

// ─────────────────────────────────────────────────────────────
// 2. para yatır — hesaba para ekler
// ─────────────────────────────────────────────────────────────
fun paraYatir(hesap: Hesap, miktar: Double): Boolean {
    if (miktar <= 0) {
        println("Yatırılacak miktar sıfırdan büyük olmalıdır.")
        return false
    }
    hesap["balance"] = (hesap["balance"] as Double) + miktar
    @Suppress("UNCHECKED_CAST")
    (hesap["transactions"] as MutableList<String>)
        .add("+ ${miktar.formatla()} TL yatırıldı | Yeni bakiye: ${(hesap["balance"] as Double).formatla()} TL")
    println("${hesap["owner"]} hesabına ${miktar.formatla()} TL yatırıldı.")
    return true
}

// ─────────────────────────────────────────────────────────────
// 3. para çek — bakiye yeterliyse hesaptan para çeker
// ─────────────────────────────────────────────────────────────
fun paraCek(hesap: Hesap, miktar: Double): Boolean {
    if (miktar <= 0) {
        println("⛔  Çekilecek miktar sıfırdan büyük olmalıdır.")
        return false
    }
    val mevcutBakiye = hesap["balance"] as Double
    if (miktar > mevcutBakiye) {
        println("⛔  Yetersiz bakiye! Mevcut: ${mevcutBakiye.formatla()} TL, İstenen: ${miktar.formatla()} TL")
        return false
    }
    hesap["balance"] = mevcutBakiye - miktar
    @Suppress("UNCHECKED_CAST")
    (hesap["transactions"] as MutableList<String>)
        .add("- ${miktar.formatla()} TL çekildi | Yeni bakiye: ${(hesap["balance"] as Double).formatla()} TL")
    println("✅  ${hesap["owner"]} hesabından ${miktar.formatla()} TL çekildi.")
    return true
}

// ─────────────────────────────────────────────────────────────
// 4. para transferi — iki hesap arasında transfer yapar
// ─────────────────────────────────────────────────────────────
fun paraTransfer(gonderen: Hesap, alici: Hesap, miktar: Double): Boolean {
    println("\n🔄  Transfer: ${gonderen["owner"]} → ${alici["owner"]} | ${miktar.formatla()} TL")
    val basarili = paraCek(gonderen, miktar)
    if (!basarili) return false
    paraYatir(alici, miktar)
    return true
}

// ─────────────────────────────────────────────────────────────
// 5. bakiye sorgula — hesabın güncel bakiyesini gösterir
// ─────────────────────────────────────────────────────────────
fun bakiyeSorgula(hesap: Hesap) {
    println("💰  [${hesap["id"]}] ${hesap["owner"]} — Bakiye: ${(hesap["balance"] as Double).formatla()} TL")
}

// ─────────────────────────────────────────────────────────────
// 6. işlem geçmişi — hesabın tüm hareketlerini listeler
// ─────────────────────────────────────────────────────────────
@Suppress("UNCHECKED_CAST")
fun islemGecmisi(hesap: Hesap) {
    val hareketler = hesap["transactions"] as List<String>
    println("\n📋  ── ${hesap["owner"]} İşlem Geçmişi (${hareketler.size} kayıt) ──")
    if (hareketler.isEmpty()) {
        println("   (henüz işlem yapılmamış)")
    } else {
        hareketler.forEachIndexed { i, kayit -> println("   ${i + 1}. $kayit") }
    }
    println()
}

// ─────────────────────────────────────────────────────────────
// 7. faiz uygula — bakiyeye yıllık faiz oranını ekler
// ─────────────────────────────────────────────────────────────
fun faizUygula(hesap: Hesap, yillikFaizOrani: Double) {
    require(yillikFaizOrani in 0.0..100.0) { "Faiz oranı 0–100 aralığında olmalıdır." }
    val eskiBakiye  = hesap["balance"] as Double
    val faizMiktari = eskiBakiye * (yillikFaizOrani / 100.0)
    hesap["balance"] = eskiBakiye + faizMiktari
    @Suppress("UNCHECKED_CAST")
    (hesap["transactions"] as MutableList<String>)
        .add("📈 %${"%.2f".format(yillikFaizOrani)} faiz uygulandı (+${faizMiktari.formatla()} TL) | Yeni bakiye: ${(hesap["balance"] as Double).formatla()} TL")
    println("📈  Faiz uygulandı: +${faizMiktari.formatla()} TL (%${"%.2f".format(yillikFaizOrani)})")
}

// ─────────────────────────────────────────────────────────────
// Yardımcı — Double biçimlendirici
// ─────────────────────────────────────────────────────────────
fun Double.formatla(): String = "%,.2f".format(this)

// =============================================================
//  M A I N
// =============================================================
fun main() {
    println("=" .repeat(55))
    println("       🏦  Kotlin Bankacılık Uygulaması")
    println("=" .repeat(55))

    // ── Hesap oluşturma ──────────────────────────────────────
    println("\n--- 1. Hesap Oluşturma ---")
    val hesapA = hesapOlustur("TRK-001", "Ayşe Kaya",    5_000.0)
    val hesapB = hesapOlustur("TRK-002", "Mehmet Demir", 1_200.0)
    val hesapC = hesapOlustur("TRK-003", "Zeynep Arslan")     // başlangıç bakiyesiz
    println("✅  3 hesap oluşturuldu.")

    // ── Bakiye sorgulama ─────────────────────────────────────
    println("\n--- 2. Bakiye Sorgulama ---")
    bakiyeSorgula(hesapA)
    bakiyeSorgula(hesapB)
    bakiyeSorgula(hesapC)

    // ── Para yatırma ─────────────────────────────────────────
    println("\n--- 3. Para Yatırma ---")
    paraYatir(hesapC, 3_000.0)
    paraYatir(hesapB,   500.0)
    paraYatir(hesapC,    -50.0)   // ← hata senaryosu

    // ── Para çekme ───────────────────────────────────────────
    println("\n--- 4. Para Çekme ---")
    paraCek(hesapA, 1_500.0)
    paraCek(hesapB, 9_999.0)      // ← yetersiz bakiye senaryosu
    paraCek(hesapC,   200.0)

    // ── Para transferi ───────────────────────────────────────
    println("\n--- 5. Para Transferi ---")
    paraTransfer(hesapA, hesapC, 800.0)
    paraTransfer(hesapB, hesapA, 5_000.0)   // ← yetersiz bakiye

    // ── Faiz uygulama ────────────────────────────────────────
    println("\n--- 6. Faiz Uygulama (%15 Yıllık) ---")
    faizUygula(hesapA, 15.0)
    faizUygula(hesapB, 15.0)
    faizUygula(hesapC, 15.0)

    // ── Güncel bakiyeler ─────────────────────────────────────
    println("\n--- 7. Güncel Bakiyeler ---")
    bakiyeSorgula(hesapA)
    bakiyeSorgula(hesapB)
    bakiyeSorgula(hesapC)

    // ── İşlem geçmişleri ─────────────────────────────────────
    println("\n--- 8. İşlem Geçmişleri ---")
    islemGecmisi(hesapA)
    islemGecmisi(hesapB)
    islemGecmisi(hesapC)

    println("=" .repeat(55))
    println("       İşlemler tamamlandı.")
    println("=" .repeat(55))
}