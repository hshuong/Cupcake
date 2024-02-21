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
import androidx.navigation.compose.rememberNavController
import com.example.cupcake.data.DataSource
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.OrderViewModel
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.ui.StartOrderScreen

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@Composable
fun CupcakeAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
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
) {

    Scaffold(
        topBar = {
            CupcakeAppBar(
                canNavigateBack = false,
                navigateUp = { /* TODO: implement back navigation */ }
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
            // ben trong than ham NavHost la tham so cuoi dung cua NavHost co kieu function (trailing lambda)

            // moi mot route la doi tuong duoc dinh nghia bang ham composable voi 2 tham so chinh
            // route ten cua 1 screen va composable noi dung la 1 screen se hien thi
            composable(CupcakeScreen.Start.name) {
                StartOrderScreen(
                    // List cac tuy chon radio, 1 tuy chon co cau truc Pair(R.string.one_cupcake, 1)
                    // DataSource la 1 singleton object, doi tuong duy nhat chia se giua nhieu class
                    quantityOptions = DataSource.quantityOptions,
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
                    // Truyen vao tham so tuy chon dang List cac flavor
                    // Ham map tao ra 1 List tu 1 List goc.
                    // o day tu List cac flavors tao ra List cac String lay ra tu
                    // tai nguyen String, theo tham so id. Tham so id bao nhieu thi lay
                    // ra String co id tuong ung trong String resource (strings.xml)
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
                    options = uiState.pickupOptions,
                    onSelectionChanged = { viewModel.setDate(it) }, // change price
                    modifier = Modifier.fillMaxHeight()
                )
            }

            composable(route = CupcakeScreen.Summary.name) {
                OrderSummaryScreen(
                    orderUiState = uiState,
                    modifier = Modifier.fillMaxHeight()
                )
            }

        }

    }
}



enum class CupcakeScreen() {
    Start,
    Flavor,
    Pickup,
    Summary
}
