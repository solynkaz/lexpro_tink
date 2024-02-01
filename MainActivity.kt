package com.example.lexpro_mobile

import com.example.lexpro_mobile.jsonDTO.responseJSON.rkkFilterJSON.RkkData
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lexpro_mobile.ui.screens.horizontal.AuthHorizontal
import com.example.lexpro_mobile.ui.screens.horizontal.RkkFilterCardDetail
import com.example.lexpro_mobile.ui.screens.horizontal.rkkFilterHorizontal
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref: SharedPreferences =
            this.getSharedPreferences("prefs", MODE_PRIVATE)
        var card: RkkData? = null
        setContent {
            val navController = rememberNavController()
            //NavHost(navController = navController, startDestination= "Auth") {
            NavHost(navController = navController, startDestination= "HorizontalAuth") {
                composable("HorizontalAuth"){
                    AuthHorizontal(onNavToNext = {
                        //TODO URL нужно хранить в БД
                        navController.navigate("HorizontalCardList")
                    })
                }
                composable("HorizontalCardList") {
                    rkkFilterHorizontal(onNavToNext = {
                        card = it
                        navController.navigate("HorizontalCardDetail")
                    })
                    BackHandler(true) {

                    }
                }
                composable("HorizontalCardDetail") {
                    RkkFilterCardDetail(card = card!!, onNavBack = { onBackPressed() })
                }
            }
        }
    }
}



