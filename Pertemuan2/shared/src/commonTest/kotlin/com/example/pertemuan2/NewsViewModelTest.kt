package com.example.pertemuan2

import com.example.pertemuan2.model.FormattedNews
import com.example.pertemuan2.model.NewsCategory
import com.example.pertemuan2.viewmodel.NewsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    @Test
    fun testFlowGeneratesNewsAndFilteringWorks() = runTest {
        val viewModel = NewsViewModel()

        // Poin 1 & 3: Flow menghasilkan berita dan ditransformasikan
        val initialList = viewModel.displayedNews.first { it.isNotEmpty() }
        assertTrue(initialList.isNotEmpty(), "Data berita dari Flow tidak boleh kosong")
        
        val firstNews = initialList.first()
        assertTrue(firstNews.displayCategory.startsWith("[") && firstNews.displayCategory.endsWith("]"), "Kategori harus ditransformasikan dengan format [KATEGORI]")
        assertTrue(firstNews.sourceFormatted.startsWith("Sumber: "), "Format sumber harus diawali dengan 'Sumber: '")

        // Poin 2: Filter berita berdasarkan kategori
        viewModel.onCategorySelected(NewsCategory.TECHNOLOGY)
        assertEquals(NewsCategory.TECHNOLOGY, viewModel.selectedCategory.value)

        val filteredTechList = viewModel.displayedNews.first { list ->
            list.isNotEmpty() && list.all { it.category == NewsCategory.TECHNOLOGY }
        }
        assertTrue(filteredTechList.isNotEmpty())
        assertTrue(filteredTechList.all { it.category == NewsCategory.TECHNOLOGY })

        // Kembalikan ke ALL
        viewModel.onCategorySelected(NewsCategory.ALL)
        assertEquals(NewsCategory.ALL, viewModel.selectedCategory.value)
    }

    @Test
    fun testStateFlowReadCountAndAsyncDetailFetching() = runTest {
        val viewModel = NewsViewModel()

        // Poin 4: Verifikasi StateFlow hitungan awal 0
        assertEquals(0, viewModel.readCount.value)
        assertTrue(viewModel.readNewsIds.value.isEmpty())

        val testNews = FormattedNews(
            id = "test-news-101",
            title = "Uji Coba Berita Kotlin Coroutines",
            category = NewsCategory.TECHNOLOGY,
            displayCategory = "[TEKNOLOGI]",
            timeAgo = "Baru saja",
            sourceFormatted = "Sumber: Test Source",
            summary = "Deskripsi ringkas berita uji coba.",
            isRead = false
        )

        // Poin 5: Coroutines untuk mengambil detail berita secara async
        viewModel.openNewsDetail(testNews)

        // Tunggu coroutine async selesai mengambil detail
        val detail = viewModel.selectedNewsDetail.first { it != null }
        assertNotNull(detail)
        assertEquals("test-news-101", detail.id)
        assertEquals(testNews.title, detail.title)
        assertFalse(viewModel.isLoadingDetail.value)

        // Poin 4: Verifikasi StateFlow bertambah menjadi 1 setelah dibaca
        assertEquals(1, viewModel.readCount.value)
        assertTrue(viewModel.readNewsIds.value.contains("test-news-101"))

        // Membuka berita yang sama tidak boleh menduplikasi read count
        viewModel.openNewsDetail(testNews)
        viewModel.selectedNewsDetail.first { it != null }
        assertEquals(1, viewModel.readCount.value)

        // Reset hitungan
        viewModel.resetReadCount()
        assertEquals(0, viewModel.readCount.value)
        assertTrue(viewModel.readNewsIds.value.isEmpty())
    }
}
