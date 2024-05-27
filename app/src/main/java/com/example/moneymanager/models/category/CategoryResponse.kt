package com.example.moneymanager.models.category

data class CategoryResponse(
	val id: String,
	val name: String,
	val color: String,
	val type: String,
	val userId: String
)