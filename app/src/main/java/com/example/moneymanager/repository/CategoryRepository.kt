package com.example.moneymanager.repository

import android.util.Log
import com.example.moneymanager.api.CategoryApi
import com.example.moneymanager.models.category.CategoryRequest
import com.example.moneymanager.models.category.CategoryResponse
import retrofit2.Response
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryApi: CategoryApi) {

	suspend fun getCategories(): Response<List<CategoryResponse>> {
		Log.d("MONEYMANAGERTAG", "CategoryRepository.getCategories")
		return categoryApi.getCategories()
	}

	suspend fun addCategory(categoryRequest: CategoryRequest): Response<CategoryResponse> {
		Log.d("MONEYMANAGERTAG", "CategoryRepository.addCategory")
		return categoryApi.addCategory(categoryRequest)
	}

	suspend fun deleteCategory(categoryId: String): Response<Unit> {
		return categoryApi.deleteCategory(categoryId)
	}
}