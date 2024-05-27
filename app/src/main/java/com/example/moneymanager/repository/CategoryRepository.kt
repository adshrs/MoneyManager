package com.example.moneymanager.repository

import com.example.moneymanager.api.CategoryApi
import com.example.moneymanager.models.category.CategoryRequest
import com.example.moneymanager.models.category.CategoryResponse
import retrofit2.Response
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryApi: CategoryApi) {

	suspend fun getCategories(type: String): Response<List<CategoryResponse>> {
		return categoryApi.getCategories(type)
	}

	suspend fun addCategory(categoryRequest: CategoryRequest): Response<CategoryResponse> {
		return categoryApi.addCategory(categoryRequest)
	}

	suspend fun deleteCategory(categoryId: String): Response<Unit> {
		return categoryApi.deleteCategory(categoryId)
	}
}