package com.wooze.mid_point.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.wooze.mid_point.viewModel.MainViewModel


sealed class SettingItemData {
    abstract val title: String
    abstract val label: String

    data class SwitchItem(
        override val title: String,
        override val label: String,
        val onChange: (Boolean) -> Unit,
        val value: Boolean
    ) : SettingItemData()

    data class ClickItem(
        override val title: String,
        override val label: String,
        val onClick: () -> Unit
    ) : SettingItemData()
}

// 假设我们有几项设置要显示
@Composable
fun SettingPage(viewModel: MainViewModel) {
    val context = LocalContext.current
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        CategoryItems(
            listOf(
                SettingItemData.SwitchItem(
                    "自动收起",
                    "关闭磁贴自动收起快捷设置",
                    { viewModel.changeData(it) },
                    viewModel.isAutoCollapse.value
                ),
                SettingItemData.ClickItem(
                    "添加磁贴",
                    "唤起添加磁贴对话框",
                    { viewModel.requestAddTile(context) }
                )
            ),
            "磁贴"
        )
    }
}

@Composable
fun CategoryItems(
    list: List<SettingItemData>, topLabel: String,
) {
    Text(
        topLabel,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier.padding(10.dp)
    )

    list.forEachIndexed { index, item ->
        val shape = when {
            list.size == 1 -> RoundedCornerShape(20.dp)
            index == 0 -> RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = 5.dp,
                bottomEnd = 5.dp
            )

            index == list.size - 1 -> RoundedCornerShape(
                bottomStart = 20.dp,
                bottomEnd = 20.dp,
                topStart = 5.dp,
                topEnd = 5.dp
            )

            else -> RoundedCornerShape(5.dp)


        }
        when (item) {
            is SettingItemData.ClickItem -> {
                ClickItem(item.title, item.label, shape, item.onClick)
            }

            is SettingItemData.SwitchItem -> {
                SettingItem(
                    title = item.title,
                    label = item.label,
                    shape,
                    { item.onChange(it) },
                    item.value
                )
            }
        }

    }
}

@Composable
fun SettingItem(
    title: String,
    label: String,
    clip: RoundedCornerShape,
    onChange: (Boolean) -> Unit,
    checked: Boolean
) {
    Row(
        modifier = Modifier
            .padding(bottom = 3.dp)
            .height(80.dp)
            .clip(clip)
            .fillMaxWidth()
            .background(Color.White)
            .padding(15.dp), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = label, style = MaterialTheme.typography.labelMedium)
        }
        Switch(checked = checked, onCheckedChange = { onChange(it) }, thumbContent = {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        })
    }
}

@Composable
fun ClickItem(
    title: String,
    label: String,
    clip: RoundedCornerShape,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(bottom = 3.dp)
            .height(80.dp)
            .clip(clip)
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .background(Color.White)
            .padding(15.dp), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = label, style = MaterialTheme.typography.labelMedium)
        }
    }
}