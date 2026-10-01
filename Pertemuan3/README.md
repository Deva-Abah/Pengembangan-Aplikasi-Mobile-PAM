# My Profile App

Aplikasi My Profile App berbasis Compose Multiplatform (Android & Desktop) yang dibangun menggunakan Kotlin dan Jetpack Compose. Proyek ini dibuat untuk memenuhi tugas Pemrograman Aplikasi Mobile (Pertemuan 3).

---
Nama : Deva Hafid Chairul Fani  
Nim : 123140026  
Kelas : RA

---
## 📱 Fitur & Komponen Aplikasi

1. **Halaman Profil Lengkap**:
   - Header dengan Foto Profil melingkar (Circular Avatar) menggunakan gradient border.
   - Nama pengguna dan Title/Peran.
   - Tombol interaktif "Ikuti Profil / Mengikuti".
   - Section Tentang Saya (Bio / deskripsi singkat).
   - Section Informasi Kontak : Email, Phone (Nomor Telepon), dan Location (Lokasi).

2. **Reusable Composable Functions**:
   - `ProfileHeader`: Komponen avatar melingkar, nama, dan profesi.
   - `InfoItem`: Komponen baris informasi kontak dengan icon badge, label, dan value.
   - `ProfileCard`: Komponen container Card Material3 untuk membungkus section profil.

3. **UI Components & Layout**:
   - Menggunakan: `Column`, `Row`, `Box`, `Card`, `Text`, `Button`, `Image`, `Spacer`.
   - Modifiers: styling gradient border, background, rounded corner, padding, clip, alignment, dan `verticalScroll`.

4. **Bonus (+10%) Animasi Interaktif**:
   - Fitur toggle tombol untuk menyembunyikan / menampilkan kontak dengan efek animasi `AnimatedVisibility` (slide & fade).

---

## Screenshot



## Cara Menjalankan Aplikasi & Build (Clean Build)

### 1. Android App
```bash
./gradlew :androidApp:assembleDebug
```

