package com.example.cupcake.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.cupcake.CupcakeApp
import com.example.cupcake.CupcakeScreen
import com.example.cupcake.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CupcakeScreenNavigationTest() {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupCupcakeNavHost() {
        // setupCupcakeNavHost co danh dau @Before do do, moi khi chay cac ham @Test,
        // setupCupcakeNavHost deu chay truoc cac ham @Test do, de setup test rule va
        // TestNavHostController, navController

        // composeTestRule automatically launches the app, displaying the CupcakeApp composable
        // before the execution of any @Test method. Therefore, you do not need to take
        // any extra steps in the test methods to launch the app.
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            CupcakeApp(navController = navController)
        }
    }

    @Test
    fun cupcakeNavHost_verifyStartDestination() {
        // goi ham extension assertCurrentRouteName so sanh tham so cua ham voi
        // route cua item nam tren cung cua stack co bang nhau ko
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
        // ben trong ham extension assertCurrentRouteName cho NavController
        // trong file ScreenAssertions.kt  co noi dung
        // assertEquals(CupcakeScreen.Start.name, navController.currentBackStackEntry?.destination?.route)
    }

    @Test
    fun cupcakeNavHost_verifyBackNavigationNotShownOnStartOrderScreen() {
        // Khi tuong tac voi 1 nut bam, o text tren compose thi phai truy cap duoc den
        // nut bam, o text do. Cac thanh phan nay co id de truy cap dat trong file string resource
        // Dung Context.getString de doc id, sau do dung id de truy cap den component tren
        // giao dien va thao tac, danh gia voi cac component do
        // backText o day la id cua nut bam lui nam tren AppBar
        val backText = composeTestRule.activity.getString(R.string.back_button)
        // Truy cap den nut bam lui backText, danh gia nut day khong co tren AppBar
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist()
    }
    @Test
    fun cupcakeNavHost_clickOneCupcake_navigatesToSelectFlavorScreen(){
        // Ten ham co dinh dang: thingUnderTest_TriggerOfTest_ResultOfTest
        // Dung extension onNodeWithStringId cua ComposeRule de doc component theo id
        // thuc hien bam vao nut 1 coc cupcake o trang Start Order Screen
        composeTestRule.onNodeWithStringId(R.string.one_cupcake)
            .performClick()
        // danh gia route hien tai cua trang sau khi bam 1 coc cupcake co phai
        // la trang Flavor ko => kiem tra nav host co navigate dung ko.
        navController.assertCurrentRouteName(CupcakeScreen.Flavor.name)
    }

    // Test viec Navigating to the Start screen by clicking the Up button from the Flavor screen
    @Test
    fun cupcakeNavHost_clickUpButtonFromFlavor_navigatesBackToStartScreen(){
        // Ten ham co dinh dang: thingUnderTest_TriggerOfTest_ResultOfTest
        // Tu man hinh start di den man hinh Flavor
        navigateToFlavorScreen()
        // tim nut Up o app bar cua man hinh Flavor, bam vao nut do
        performNavigateUp()
        // da quay lai man hinh Start, kiem tra man hinh hien tai co phai la Start ko
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    // Test viec Navigating to the Start screen by clicking the Cancel button from the Flavor screen
    @Test
    fun cupcakeNavHost_clickCancelFromFlavor_navigatesBackToStartScreen(){
        // Ten ham co dinh dang: thingUnderTest_TriggerOfTest_ResultOfTest
        // Tu man hinh start di den man hinh Flavor
        navigateToFlavorScreen() // dang o man hinh Flavor
        // tim va bam vao nut Cancel o man hinh Flavor co id la R.string.cancel
        composeTestRule.onNodeWithStringId(R.string.cancel).performClick()
        // da quay lai man hinh Start
        // kiem tra man hinh hien tai co phai la Start ko
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    // Test Navigating to the Pickup screen
    @Test
    fun cupcakeNavHost_navigatesToSelectPickupScreen(){
        // di den trang Pickup
        navigateToPickupScreen()
        // danh gia route hien tai cua trang sau khi bam 1 coc cupcake co phai
        // la trang Flavor ko => kiem tra nav host co navigate dung ko.
        navController.assertCurrentRouteName(CupcakeScreen.Pickup.name)
    }

    // Test Navigating to the Flavor screen by clicking the Up button from the Pickup screen
    @Test
    fun cupcakeNavHost_clickUpButtonFromPickup_navigatesBackToFlavorScreen(){
        // Ten ham co dinh dang: thingUnderTest_TriggerOfTest_ResultOfTest
        // Tu man hinh start di den man hinh Flavor
        navigateToPickupScreen()
        // tim nut Up o app bar cua man hinh Flavor, bam vao nut do
        performNavigateUp()
        // da quay lai man hinh Start, kiem tra man hinh hien tai co phai la Start ko
        navController.assertCurrentRouteName(CupcakeScreen.Flavor.name)
    }

    // Test Navigating to the Start screen by clicking the Cancel button from the Pickup screen
    @Test
    fun cupcakeNavHost_clickCancelFromPickup_navigatesBackToStartScreen(){
        // Ten ham co dinh dang: thingUnderTest_TriggerOfTest_ResultOfTest
        // Tu man hinh start di den man hinh Flavor
        navigateToPickupScreen() // dang o man hinh Flavor
        // tim va bam vao nut Cancel o man hinh Flavor co id la R.string.cancel
        composeTestRule.onNodeWithStringId(R.string.cancel).performClick()
        // da quay lai man hinh Start
        // kiem tra man hinh hien tai co phai la Start ko
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    // Navigating to the Summary screen
    @Test
    fun cupcakeNavHost_navigatesToSummaryScreen(){
        // di den trang Pickup
        navigateToSummaryScreen()
        // danh gia route hien tai cua trang sau khi bam 1 coc cupcake co phai
        // la trang Flavor ko => kiem tra nav host co navigate dung ko.
        navController.assertCurrentRouteName(CupcakeScreen.Summary.name)
    }

    // Navigating to the Start screen by clicking the Cancel button from the Summary screen
    @Test
    fun cupcakeNavHost_clickCancelFromSummary_navigatesBackToStartScreen(){
        // Ten ham co dinh dang: thingUnderTest_TriggerOfTest_ResultOfTest
        // Tu man hinh start di den man hinh Flavor
        navigateToSummaryScreen() // dang o man hinh Flavor
        // tim va bam vao nut Cancel o man hinh Flavor co id la R.string.cancel
        composeTestRule.onNodeWithStringId(R.string.cancel).performClick()
        // da quay lai man hinh Start
        // kiem tra man hinh hien tai co phai la Start ko
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    // Helpers
    private fun navigateToFlavorScreen() {
        // ham nay de goi di goi lai nhieu navigate den trang Flavor, ko can viet code lai
        // command to find the One Cupcake button and perform a click action on it
        composeTestRule.onNodeWithStringId(R.string.one_cupcake)
            .performClick()
        // tim den nut radio Chocolate de bam vao => Nut Next cua trang flavor
        // chuyen sang trang thai co them bam duoc
        composeTestRule.onNodeWithStringId(R.string.chocolate)
            .performClick()
    }
    // tu luc mo app, chuyen den trang thu 3 chon ngay pickup
    private fun navigateToPickupScreen() {
        // de den duoc trang Pickup, trang thu 3 trong (1. chon so coc, 2. chon flavor, 3. chon ngay pickup)
        // thi phai den trang 2 flavor truoc
        navigateToFlavorScreen()
        // tim nut co chu Next tren trang 2 flavor va bam de chuyen sang trang 3
        composeTestRule.onNodeWithStringId(R.string.next)
            .performClick()
    }
    // tu luc mo app, chuyen den trang thu 4 summary
    private fun navigateToSummaryScreen() {
        navigateToPickupScreen()
        // chon 1 ngay pickup va bam vao ngay do
        composeTestRule.onNodeWithText(getFormattedDate())
            .performClick()
        // nut Next duoc enable, bam vao nut Next cua trang pickup
        composeTestRule.onNodeWithStringId(R.string.next)
            .performClick()
    }

    // bam vao nut back cua app bar
    private fun performNavigateUp() {
        val backText = composeTestRule.activity.getString(R.string.back_button)
        // nut back o app bar duoc dinh nghia bang
        //                 IconButton(onClick = navigateUp) {
        //                    Icon(
        //                        imageVector = Icons.Filled.ArrowBack,
        //                        contentDescription = stringResource(R.string.back_button)
        //                    )
        //                }
        // nen co the search nut back bang contentDescription
        composeTestRule.onNodeWithContentDescription(backText).performClick()
    }

    private fun getFormattedDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(java.util.Calendar.DATE, 1)
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        return formatter.format(calendar.time)
    }

}
