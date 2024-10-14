package com.example.stockmate

enum class ArticleType(val id : Int) {
    MINERALES(0),
    BIERES(1),
    VINS(2),
    ALCOOLS (3),
    COCKTAILS (4),
    SOFT_1L (5),
    SIROPS (6),
    FUTS(7),

    CAFE(8);

    companion object {
        fun fromId(id: Int) : ArticleType? {
            return values().find { it.id == id }
        }
    }
}

