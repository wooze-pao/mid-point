package com.wooze.mid_point.ui.homeScreenUi

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.wooze.mid_point.R
import com.wooze.mid_point.ui.ButtonExp
import com.wooze.mid_point.viewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    openFloat: () -> Unit,
    closeFloat: () -> Unit,
    viewModel: MainViewModel,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(start = 5.dp, end = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            LeftInformation(viewModel, modifier = Modifier.weight(2f))

            RightButtonGroup(
                modifier = Modifier.weight(1f),
                viewModel,
                context,
                { openFloat() },
                { closeFloat() })
        }
        Column(modifier = Modifier
            .weight(2f)
            .padding(top = 30.dp)) {
            Card(Modifier.height(100.dp)) {
                Row {
                    ButtonExp(
                        Modifier.weight(1f),
                        onClick = {
                            val intent = Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                "package:${context.packageName}".toUri()
                            )
                            context.startActivity(intent)
                        },
                        "管理权限",
                        image = {
                            Icon(
                                painterResource(R.drawable.ic_open),
                                modifier = Modifier.size(24.dp),
                                contentDescription = null
                            )
                        })

                }

            }

        }
    }


}

