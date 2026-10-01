# News Feed Simulator
Nama : Deva Hafid Chairul Fani  
NIM : 123140026  
Kelas : RA

## Deskripsi Aplikasi
Aplikasi simulator feed berita berbasis Kotlin Multiplatform & Compose Multiplatform (Tugas Pertemuan 2).

## Fitur
1. **Flow Data 2 Detik**: Menghasilkan berita baru secara otomatis setiap 2 detik.
2. **Filter Kategori**: Menyaring berita berdasarkan kategori (Semua, Teknologi, Olahraga, Bisnis, Hiburan).
3. **Transformasi Data**: Memformat data mentah (kategori, sumber, waktu) ke format tampilan.
4. **StateFlow Counter**: Menyimpan dan menampilkan jumlah berita yang sudah dibaca.
5. **Coroutines Async Detail**: Mengambil konten detail berita secara asynchronous di background thread.

## Screenshots
> <img src="C:\Users\Acer\Pictures\Screenshots\Screenshot 2026-09-25 200557.png"/>
> <img src="C:\Users\Acer\Pictures\Screenshots\Screenshot 2026-09-25 200611.png"/>

## Cara Menjalankan Aplikasi

> Pastikan JAVA_HOME mengarah ke JDK Android Studio:
> ```powershell
> $env:JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"
> ```

- **Desktop (JVM):**
  ```powershell
  .\gradlew :desktopApp:run
  ```
- **Android (Build APK Debug):**
  ```powershell
  .\gradlew :androidApp:assembleDebug
  ```
