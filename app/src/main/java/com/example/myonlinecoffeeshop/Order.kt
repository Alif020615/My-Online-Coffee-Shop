package com.example.myonlinecoffeeshop

data class Order(
    val userId: String,
    val productName: String,
    val productPrice: Double,
    val quantity: Int,
    val amountPaid: Double,
    val orderDate: String
)

