package com.example.moneymanager.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.moneymanager.R

val provider = GoogleFont.Provider(
	providerAuthority = "com.google.android.gms.fonts",
	providerPackage = "com.google.android.gms",
	certificates = R.array.com_google_android_gms_fonts_certs
)

val Lato= FontFamily(
	Font(googleFont = GoogleFont("Lato"), fontProvider = provider)
)

// Set of Material typography styles to start with
val Typography = Typography(
	bodyMedium = TextStyle(
		fontFamily = Lato,
		fontWeight = FontWeight.Normal,
		fontSize = 17.sp,
		lineHeight = 22.sp,
		letterSpacing = 0.5.sp
	),
	bodySmall = TextStyle(
		fontFamily = Lato,
		fontWeight = FontWeight.SemiBold,
		fontSize = 16.sp,
		lineHeight = 21.sp,
		letterSpacing = 0.5.sp
	),
	headlineMedium = TextStyle(
		fontFamily = Lato,
		fontWeight = FontWeight.SemiBold,
		fontSize = 17.sp,
		lineHeight = 22.sp,
		letterSpacing = 0.5.sp
	),
	headlineLarge = TextStyle(
		fontFamily = Lato,
		fontWeight = FontWeight.SemiBold,
		fontSize = 18.sp,
		lineHeight = 22.sp,
		letterSpacing = 0.5.sp
	),
	labelLarge = TextStyle(
		fontFamily = Lato,
		fontWeight = FontWeight.Bold,
		fontSize = 17.sp,
		lineHeight = 22.sp,
		letterSpacing = 0.5.sp
	),
	titleLarge = TextStyle(
		fontFamily = Lato,
		fontWeight = FontWeight.Normal,
		fontSize = 28.sp,
		lineHeight = 34.sp,
		letterSpacing = 0.5.sp
	),
	titleMedium = TextStyle(
		fontFamily = Lato,
		fontWeight = FontWeight.Normal,
		fontSize = 22.sp,
		lineHeight = 28.sp,
		letterSpacing = 0.5.sp
	),
	titleSmall = TextStyle(
		fontFamily = Lato,
		fontWeight = FontWeight.Normal,
		fontSize = 20.sp,
		lineHeight = 24.sp,
		letterSpacing = 0.5.sp
	),
	labelMedium = TextStyle(
		fontFamily = Lato,
		fontWeight = FontWeight.Normal,
		fontSize = 15.sp,
		lineHeight = 18.sp,
		letterSpacing = 0.5.sp
	),
	/* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)