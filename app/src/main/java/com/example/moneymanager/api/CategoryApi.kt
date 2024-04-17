package com.example.moneymanager.api

import com.example.moneymanager.models.category.CategoryRequest
import com.example.moneymanager.models.category.CategoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface CategoryApi {

	@GET("/api/category")
	suspend fun getCategories(): Response<List<CategoryResponse>>

	@POST("/api/category")
	suspend fun addCategory(@Body categoryRequest: CategoryRequest): Response<CategoryResponse>

	@DELETE("/api/category/{categoryId}")
	suspend fun deleteCategory(@Path("categoryId") categoryId: String): Response<Unit>
}