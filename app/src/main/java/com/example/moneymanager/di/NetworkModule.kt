package com.example.moneymanager.di

import com.example.moneymanager.api.AuthApi
import com.example.moneymanager.api.AuthInterceptor
import com.example.moneymanager.api.CategoryApi
import com.example.moneymanager.api.ExpenseApi
import com.example.moneymanager.api.IncomeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

	@Singleton
	@Provides
	fun providesRetrofitBuilder(): Retrofit.Builder {
		return Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create())
			.baseUrl("http://192.168.1.69:8090")
	}

	@Singleton
	@Provides
	fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
		return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
	}

	@Singleton
	@Provides
	fun providesAuthAPI(retrofitBuilder: Retrofit.Builder): AuthApi {
		return retrofitBuilder.build().create(AuthApi::class.java)
	}

	@Singleton
	@Provides
	fun providesCategoryAPI(retrofitBuilder: Builder, okHttpClient: OkHttpClient): CategoryApi {
		return retrofitBuilder
			.client(okHttpClient)
			.build().create(CategoryApi::class.java)
	}

	@Singleton
	@Provides
	fun providesExpenseAPI(retrofitBuilder: Builder, okHttpClient: OkHttpClient): ExpenseApi {
		return retrofitBuilder
			.client(okHttpClient)
			.build().create(ExpenseApi::class.java)
	}

	@Singleton
	@Provides
	fun providesIncomeAPI(retrofitBuilder: Builder, okHttpClient: OkHttpClient): IncomeApi {
		return retrofitBuilder
			.client(okHttpClient)
			.build().create(IncomeApi::class.java)
	}
}