package com.wooze.mid_point.ui.floatWindowUi

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.wooze.mid_point.R
import com.wooze.mid_point.data.SizesDp
import com.wooze.mid_point.viewModel.FloatViewModel

data class DropMenuItem(val text: Int, val action: () -> Unit)


@Composable
fun DropMenu(viewModel: FloatViewModel) {
    val context = LocalContext.current

    val dropItems = listOf(
        DropMenuItem(R.string.drop_menu_share) { viewModel.shareTo(context) },
        DropMenuItem(R.string.drop_menu_clear) {
            viewModel.resetFloatData()
            viewModel.toggleMenu()
        },
        DropMenuItem(R.string.drop_menu_select) {
            viewModel.toggleSelectMode()
            viewModel.toggleMenu()
        }
    )

    DropdownMenu(
        expanded = viewModel.menuExpanded,
        onDismissRequest = { viewModel.toggleMenu() },
        modifier = Modifier.width(SizesDp.ITEM_WIDTH),
        shape = RoundedCornerShape(SizesDp.R_INNER)
    ) {
        dropItems.forEach { item ->
            DropdownMenuItem({ Text(stringResource(item.text)) }, item.action)
        }
    }
}

