package com.nads.shopping.datamodels

data class SelectedFilter(
    val id: String,
    val name: String,
    val type: FilterType
)

enum class FilterType{
    CATEGORY,
    SUB_CATEGORY,
    BRANDS,
    PRICE
}

enum class PriceType{
    LOW_HIGH,
    HIGH_LOW
}