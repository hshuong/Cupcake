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
package com.example.cupcake.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cupcake.CupcakeScreen
import com.example.cupcake.R
import com.example.cupcake.data.DataSource
import com.example.cupcake.ui.theme.CupcakeTheme

/**
 * Composable that allows the user to select the desired cupcake quantity and expects
 * [onNextButtonClicked] lambda that expects the selected quantity and triggers the navigation to
 * next screen
 */
@Composable
fun StartOrderScreen(
    // the first Int is a resource ID for the string to display
    // on each button. The second Int is the actual quantity of cupcakes
    quantityOptions: List<Pair<Int, Int>>,
    // adding a function type parameter that is called
    // when one of the quantity buttons is pressed on the first screen
    // ham nay duoc nhan tu ngoai vao, khi bam vao 1 nut so luong thi
    // ham ben ngoai co trach nhiem cap nhat so luong vao UiState va
    // va dieu huong. So luong la tham so cua tham so kieu ham
    onNextButtonClicked: (Int) -> Unit, // khong co gia tri mac dinh cua tham so
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
            Image(
                painter = painterResource(R.drawable.cupcake),
                contentDescription = null,
                modifier = Modifier.width(300.dp)
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
            Text(
                text = stringResource(R.string.order_cupcakes),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_medium)
            )
        ) {
            quantityOptions.forEach { item ->
                SelectQuantityButton(
                    labelResourceId = item.first,
                    // item.second la phan tu thu 2 trong Pair<Int, Int>
                    // cua 1 thanh phan trong List quantityOptions.
                    // Vi du la so 1 trong Pair(R.string.one_cupcake, 1)
                    // We'll pass the second property of the selected pair
                    // when calling the onNextButtonClicked() function.
                    // Bam vao 1 tuy chon so luong banh muon mua, vi du chon button 6 coc
                    // Trong than cua dinh nghia ham (ko phai than loi goi ham), se xac
                    // dinh tham so onNextButtonClicked cua ham StartOrderScreen duoc
                    // dung nhu the nao. O day la tham so onNextButtonClicked duoc goi
                    // de chay.
                    onClick = {onNextButtonClicked(item.second)}
                    // Se goi va thuc hien ham onNextButtonClicked(6 cai)
                    // onNextButtonClicked duoc truyen tu CupcakeScreen.kt vao, voi dinh nghia la
                    //    onNextButtonClicked = {
                    //    //  Before navigating to the next screen, you should update the view model
                    //   //  so that the app displays the correct subtotal
                    //    viewModel.setQuantity(it)
                    // => it la item.second la so coc
                    //    navController.navigate(CupcakeScreen.Flavor.name)
                    // => chuyen huong den va hien thi trang SelectOptionsScreen kieu Flavor
                    //},
                )
            }
        }
    }
}

/**
 * Customizable button composable that displays the [labelResourceId]
 * and triggers [onClick] lambda when this composable is clicked
 */
@Composable
fun SelectQuantityButton(
    @StringRes labelResourceId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.widthIn(min = 250.dp)
    ) {
        Text(stringResource(labelResourceId))
    }
}

@Preview(showBackground = true)
@Composable
fun StartOrderPreview() {
    CupcakeTheme(darkTheme = false) {
        StartOrderScreen(
            quantityOptions = DataSource.quantityOptions,
            onNextButtonClicked = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}
