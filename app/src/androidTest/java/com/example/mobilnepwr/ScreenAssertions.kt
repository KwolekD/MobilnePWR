package com.example.mobilnepwr

import androidx.navigation.NavController
import org.junit.Assert.assertEquals

fun NavController.assertCurrentRouteName(expectedRouteName: String) {
    assertEquals(currentBackStackEntry?.destination?.route, expectedRouteName)
}