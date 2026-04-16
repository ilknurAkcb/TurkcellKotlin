package com.ilknurakcaba.aboutme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfilSayfasi()
        }
    }
}

@Composable
fun ProfilSayfasi() {

    val adSoyad    = "İlknur Akcaba"
    val universite = "Erzurum Teknik Üniversitesi"
    val meslek     = "Bilgisayar Mühendisi"
    val skills     = listOf("Kotlin", "AI", "Veri Bilimi", "Git", "SQL")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))  // açık gri arka plan
    ) {

        // ── ÜST HEADER (mor arka plan) ──────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF3C3489))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar dairesi
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFAFA9EC), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "İA",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3C3489)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = adSoyad, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = meslek,  fontSize = 14.sp, color = Color(0xFFAFA9EC))
        }

        // ── BİLGİ SATIRLARI ─────────────────────────────────
        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                text = "BİLGİLER",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Üniversite satırı
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFEEEDFE), shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎓", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = universite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text(text = "Bilgisayar Mühendisliği", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Meslek satırı
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFEEEDFE), shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💼", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = meslek, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }

            // ── YETENEKLER ───────────────────────────────────
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "YETENEKLER",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Skill chip'leri — Row içinde yan yana
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (skill in skills) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFEEEDFE), shape = RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(text = skill, fontSize = 13.sp, color = Color(0xFF3C3489))
                    }
                }
            }
        }
    }
}