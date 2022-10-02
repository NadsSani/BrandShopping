package com.nads.shopping.datamodels

data class InnerCategory(
    val name: String,
    val image: String="",
)

data class HomeCategory(
    val name: String,
    val image: String,
    var isSelected: Boolean = false
)