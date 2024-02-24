package com.example.cupcake.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.cupcake.ui.SelectOptionScreen
import org.junit.Rule
import org.junit.Test
import com.example.cupcake.R

class CupcakeOrderScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun selectOptionScreen_verifyContent() {
        // Given list of options
        val flavors = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        // And subtotal
        val subtotal = "$100"
        // Vao truc tiep trang SelectOptionScreen de test luon, ko can dung navigate
        // di tu Start vao SelectOptionScreen.
        // When SelectOptionScreen is loaded
        composeTestRule.setContent {
            SelectOptionScreen(subtotal = subtotal, options = flavors)
        }
        // At this point in the test, the app launches the SelectOptionScreen composable
        // and you are then able to interact with it through test instructions.

        // Kiem tra cac tuy chon gom Vanilla, Chocolate... co hien thi dung tren Screen ko
        // Then all the options are displayed on the screen.
        flavors.forEach { flavor ->
            composeTestRule.onNodeWithText(flavor).assertIsDisplayed()

        }

        // And then the subtotal is displayed correctly.
        // Vi text trong subtotal_price duoc dinh nghia co dinh dang
        // <string name="subtotal_price">Subtotal %s</string>
        // nen khi tim node co text subtotal_price phai tim theo dinh dang "Subtotal %s"
        // nghia la String subtotal_price truyen cung voi tham so %s subtotal
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.subtotal_price,
                subtotal
            )
        ).assertIsDisplayed()

        // And then the next button is disabled
        composeTestRule.onNodeWithStringId(R.string.next).assertIsNotEnabled()
    }
}