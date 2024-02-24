/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cupcake.data.DataSource
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.OrderViewModel
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.ui.StartOrderScreen

enum class CupcakeScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Flavor(title = R.string.choose_flavor),
    Pickup(title = R.string.choose_pickup_date),
    Summary(title = R.string.order_summary)
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@Composable
fun CupcakeAppBar(
    currentScreen: CupcakeScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun CupcakeApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
    // navController de navigate qua lai giua cac screen
    // instance navController nay duoc dung o 2 noi la o NavHost va o AppBar nen
    // khai la 1 bien o day de dung duoc o ca 2 composable NavHost va AppBar
    // Logic dieu huong dung navController can tach khoi UI Composable. Do do, ko
    // truyen truc tiep navController vao cac Composable de thuc hien dieu huong
    // ma truyen vao dang function type, de viec dieu huong se thuc hien o ben
    // ngoai Composable, cac Composable se quyet dinh khi nao thi goi ham
    // dieu huong
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = CupcakeScreen.valueOf(
        backStackEntry?.destination?.route ?: CupcakeScreen.Start.name
    )
    Scaffold(
        topBar = {
            CupcakeAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        // NavHost la 1 composable, displays other composable destinations, based on a given route.
        // For example, if the route is Flavor, then the NavHost would show the screen
        // to choose the cupcake flavor
        NavHost(
            navController = navController, // de navigate qua lai giua cac screen
            startDestination = CupcakeScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            // ben trong than ham NavHost la tham so cuoi dung cua NavHost co
            // kieu function (trailing lambda) moi mot route la doi tuong, route
            // duoc dinh nghia bang ham composable voi 2 tham so chinh
            // route ten cua 1 screen va composable noi dung la 1 screen se hien thi
            composable(CupcakeScreen.Start.name) {
                StartOrderScreen(
                    // List cac tuy chon radio, 1 tuy chon co cau truc Pair(R.string.one_cupcake, 1)
                    // DataSource la 1 singleton object, doi tuong duy nhat chia se giua nhieu class
                    quantityOptions = DataSource.quantityOptions,
                    // 3. WHAT. Ham dang lambda. Dinh nghia ham se lam gi
                    // it bieu dien tham so viet gon cua lambda
                    onNextButtonClicked = {//ab ->
                        //  Before navigating to the next screen, you should update the view model
                        //  so that the app displays the correct subtotal
                        // viewModel.setQuantity(ab)
                        // it chi co y nghia trong pham vi lambda nay thoi
                        // it la tham so cua tham so kieu ham onNextButtonClicked cua
                        // ham @Composable StartOrderScreen.
                        // Gia tri thuc su cua tham so it duoc gan trong phan dinh nghia
                        // ham @Composable StartOrderScreen, chinh la item.second
                        viewModel.setQuantity(it)
                        // Cach lam tren, truyen vao 1 ham co tham so duoc lay tu ben trong
                        // va thuc thi ham do voi tham so tu ben trong
                        navController.navigate(CupcakeScreen.Flavor.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }

            composable(route = CupcakeScreen.Flavor.name) {
                val context = LocalContext.current
                // LocalContext do Android cung cap, dung de truy cap cac tai nguyen cua app,
                // va truy cap cac class cua app, cho goi lan cac muc cao hon cua context la
                // hanh dong nhu goi activity khac.
                // O day, dung context de truy cap den tai nguyen cua app la cac string trong
                // /res/values/strings.xml
                // context.resources lay duoc thu muc res
                // context.resources.getString tra ve String trong file strings.xml co id bang
                // voi tham so id truyen vao ham getString(id)
                SelectOptionScreen(
                    subtotal = uiState.price, // lay tong gia tri don hang tu uiState
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Pickup.name) },
                    onCancelButtonClicked = {cancelOrderAndNavigateToStart(viewModel, navController)},
                    // List goc la list of integer cac resource id cua cac String
                    // list dich la list cac string sau khi map tu Integer sang String
                    // Trong context.resources.getString(id) id la Integer, resources la cac
                    // resource trong thu muc res nhu drawable, layout, values, mipmap
                    // R.string.chocolate la 1 so Integer
                    // context.resources.getString(id) tra ve String ma so Integer do tro den
                    // chu Chocolate trong String resource cua file app/main/res/values/strings.xml
                    // <resources>
                    //    <string name="chocolate">Chocolate</string>
                    //  context.resources.getString(id) tra ve string "Chocolate"
                    options = DataSource.flavors.map { id -> context.resources.getString(id) },
                    // dung state hoisting. khi co event do composable tao ra thi push len viewModel
                    // de thuc thi ham o viewModel, ham o viewModel thay doi uiState hoac thuoc tinh
                    // duoc kieu mutableState cua viewModel
                    // (var userGuess by mutableStateOf("")
                    //        private set
                    //  fun updateUserGuess(guessedWord: String){
                    //      userGuess = guessedWord
                    //  }
                    //  )
                    onSelectionChanged = { viewModel.setFlavor(it) },
                    // only change flavor not change price
                    modifier = Modifier.fillMaxHeight()
                )
            }

            composable(route = CupcakeScreen.Pickup.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Summary.name) },
                    onCancelButtonClicked = {cancelOrderAndNavigateToStart(viewModel, navController)},
                    options = uiState.pickupOptions,
                    onSelectionChanged = { viewModel.setDate(it) }, // change price
                    modifier = Modifier.fillMaxHeight()
                )
            }

            composable(route = CupcakeScreen.Summary.name) {
                val context = LocalContext.current
                OrderSummaryScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = {cancelOrderAndNavigateToStart(viewModel, navController)},
                    onSendButtonClicked = { subject: String, summary: String ->
                        shareOrder(context, subject = subject, summary = summary)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }

        }

    }
}

// 1 ham dung chung cho nhieu loi goi bam nut Cancel cua ca 3 screens
private fun cancelOrderAndNavigateToStart(
    // dung viewModel de reset lai UiState
    viewModel: OrderViewModel,
    // de navigate
    navController: NavHostController
) {
    // xoa het don hang
    viewModel.resetOrder()
    // navigate den man hinh Start, nhung ko xoa ban than man hinh Start trong Stack
    navController.popBackStack(CupcakeScreen.Start.name, inclusive = false)
}

private fun shareOrder(context: Context, subject: String, summary: String) {
    // An intent is a request for the system to perform some action,
    // commonly presenting a new activity. O day Intent kieu ACTION_SEND
    // de truyen du lieu tu app nay sang app khac, sang cho khac
    // EXTRA_SUBJECT la ten noi dung se truyen
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }

    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.new_cupcake_order)
        )
    )

}
