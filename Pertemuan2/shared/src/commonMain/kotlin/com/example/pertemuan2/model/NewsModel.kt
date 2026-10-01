package com.example.pertemuan2.model

enum class NewsCategory(val displayName: String) {
    ALL("Semua"),
    TECHNOLOGY("Teknologi"),
    SPORTS("Olahraga"),
    BUSINESS("Bisnis"),
    ENTERTAINMENT("Hiburan")
}

data class RawNews(
    val id: String,
    val title: String,
    val category: NewsCategory,
    val timestampMillis: Long,
    val source: String,
    val contentSummary: String
)

data class FormattedNews(
    val id: String,
    val title: String,
    val category: NewsCategory,
    val displayCategory: String,
    val timeAgo: String,
    val sourceFormatted: String,
    val summary: String,
    val isRead: Boolean = false
)

data class NewsDetail(
    val id: String,
    val title: String,
    val category: NewsCategory,
    val author: String,
    val publishedAt: String,
    val fullContent: String,
    val readCount: Int
)
