package com.example.pertemuan2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pertemuan2.model.FormattedNews
import com.example.pertemuan2.model.NewsCategory
import com.example.pertemuan2.model.NewsDetail
import com.example.pertemuan2.model.RawNews
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class NewsViewModel : ViewModel() {

    private val sampleHeadlines = listOf(
        "AI Generatif Semakin Mengubah Cara Kerja Programmer" to NewsCategory.TECHNOLOGY,
        "Peluncuran Chipset 3nm Terbaru Catat Rekor Efisiensi Daya" to NewsCategory.TECHNOLOGY,
        "Eksplorasi Framework Multiplatform Kian Populer di Kalangan Developer" to NewsCategory.TECHNOLOGY,
        "Kemenangan Dramatis di Menit Akhir Liga Utama Mengubah Klasemen" to NewsCategory.SPORTS,
        "Atlet Lari Pecahkan Rekor Dunia Sprint 100 Meter" to NewsCategory.SPORTS,
        "Final Turnamen Bulutangkis Internasional Berlangsung Sengit" to NewsCategory.SPORTS,
        "Pasar Saham Regional Menguat Ditopang Tren Positif Sektor Teknologi" to NewsCategory.BUSINESS,
        "Startup FinTech Raih Pendanaan Seri B Senilai Puluhan Juta Dolar" to NewsCategory.BUSINESS,
        "Bank Sentral Rilis Kebijakan Baru Suku Bunga Acuan" to NewsCategory.BUSINESS,
        "Film Petualangan Fantasi Baru Tembus Box Office Akhir Pekan" to NewsCategory.ENTERTAINMENT,
        "Festival Musik Tahunan Sukses Sedot Puluhan Ribu Penonton" to NewsCategory.ENTERTAINMENT,
        "Serial Animasi Populer Resmi Umumkan Musim Terbarunya" to NewsCategory.ENTERTAINMENT
    )

    private val sources = listOf("TechNews", "Radar Sports", "Bisnis Hari Ini", "Showbiz Daily", "Kabar Terkini")

    // 1. Flow yang mensimulasikan data berita baru setiap 2 detik
    private val rawNewsListFlow = flow {
        val list = mutableListOf<RawNews>()
        var counter = 1
        while (true) {
            val randomItem = sampleHeadlines[Random.nextInt(sampleHeadlines.size)]
            val source = sources[Random.nextInt(sources.size)]
            val news = RawNews(
                id = "news-$counter",
                title = "${randomItem.first} (#$counter)",
                category = randomItem.second,
                timestampMillis = counter * 1000L,
                source = source,
                contentSummary = "Laporan mendalam mengenai perkembangan terkini terkait topik '${randomItem.first}'. Simak ulasan komprehensifnya."
            )
            list.add(0, news) // Tambahkan ke paling atas
            if (list.size > 50) {
                list.removeAt(list.lastIndex)
            }
            emit(list.toList())
            counter++
            delay(2000L) // Setiap 2 detik
        }
    }

    // Filter Kategori
    private val _selectedCategory = MutableStateFlow(NewsCategory.ALL)
    val selectedCategory: StateFlow<NewsCategory> = _selectedCategory.asStateFlow()

    // 4. StateFlow untuk menyimpan jumlah berita yang sudah dibaca
    private val _readNewsIds = MutableStateFlow<Set<String>>(emptySet())
    val readNewsIds: StateFlow<Set<String>> = _readNewsIds.asStateFlow()

    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    // Status loading async detail & dialog detail
    private val _selectedNewsDetail = MutableStateFlow<NewsDetail?>(null)
    val selectedNewsDetail: StateFlow<NewsDetail?> = _selectedNewsDetail.asStateFlow()

    private val _isLoadingDetail = MutableStateFlow(false)
    val isLoadingDetail: StateFlow<Boolean> = _isLoadingDetail.asStateFlow()

    // 2. Filter berita berdasarkan kategori tertentu
    // 3. Transform data menjadi format yang ditampilkan (menggunakan .map dan combine)
    val displayedNews: StateFlow<List<FormattedNews>> = combine(
        rawNewsListFlow,
        _selectedCategory,
        _readNewsIds
    ) { newsList, category, readIds ->
        // Langkah 2: Filter berita berdasarkan kategori
        val filtered = if (category == NewsCategory.ALL) {
            newsList
        } else {
            newsList.filter { it.category == category }
        }

        // Langkah 3: Transform data menjadi format yang ditampilkan
        filtered.map { raw ->
            FormattedNews(
                id = raw.id,
                title = raw.title,
                category = raw.category,
                displayCategory = "[${raw.category.displayName.uppercase()}]",
                timeAgo = "Baru saja",
                sourceFormatted = "Sumber: ${raw.source}",
                summary = raw.contentSummary,
                isRead = readIds.contains(raw.id)
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun onCategorySelected(category: NewsCategory) {
        _selectedCategory.value = category
    }

    // 5. Coroutines untuk mengambil detail berita secara async
    fun openNewsDetail(news: FormattedNews) {
        viewModelScope.launch {
            _isLoadingDetail.value = true
            _selectedNewsDetail.value = null

            // Simulasikan background asynchronous fetching menggunakan withContext
            val detail = withContext(Dispatchers.Default) {
                // Simulasi network / database delay
                delay(900L)

                NewsDetail(
                    id = news.id,
                    title = news.title,
                    category = news.category,
                    author = "Jurnalis Independen",
                    publishedAt = "25 Sep 2026, 19:50 WIB",
                    fullContent = """
                        Artikel lengkap untuk "${news.title}".
                        
                        Topik ini sedang hangat dibicarakan di sektor ${news.category.displayName}. Menurut laporan investigasi dan analisis para pakar, perkembangan ini membawa dampak signifikan bagi industri serta masyarakat luas.
                        
                        Diharapkan ke depan inovasi dan kolaborasi terus berlanjut guna menghadirkan solusi terbaik bagi masyarakat.
                    """.trimIndent(),
                    readCount = _readCount.value + 1
                )
            }

            // Tandai sudah dibaca & update StateFlow jumlah berita yang sudah dibaca (Poin 4)
            if (!_readNewsIds.value.contains(news.id)) {
                _readNewsIds.value = _readNewsIds.value + news.id
                _readCount.value = _readNewsIds.value.size
            }

            _selectedNewsDetail.value = detail
            _isLoadingDetail.value = false
        }
    }

    fun closeDetail() {
        _selectedNewsDetail.value = null
        _isLoadingDetail.value = false
    }

    fun resetReadCount() {
        _readNewsIds.value = emptySet()
        _readCount.value = 0
    }
}
