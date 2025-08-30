package com.wooze.mid_point.ui.homeScreenUi

import android.app.StatusBarManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.wooze.mid_point.R
import com.wooze.mid_point.service.FloatControlTile
import com.wooze.mid_point.ui.ButtonExp
import com.wooze.mid_point.viewModel.MainViewModel

@RequiresApi(Build.VERSION_CODES.Q)
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
            .background(MaterialTheme.colorScheme.inverseOnSurface)
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
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(top = 30.dp)
        ) {
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
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.size(24.dp),
                                contentDescription = null
                            )
                        })

                }

            }

        }
    }


}

