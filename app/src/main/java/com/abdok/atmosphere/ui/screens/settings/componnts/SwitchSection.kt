package com.abdok.atmosphere.ui.screens.settings.componnts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SwitchSection(
    switchIcon: ImageVector,
    switchText: String,
    isSwitchChecked: Boolean,
    onSwitchToggled: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = switchIcon,
                tint = Color.Gray,
                contentDescription = null
            )
            Text(
                text = switchText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Switch(
            checked = isSwitchChecked,
            onCheckedChange = { onSwitchToggled(it) }
            , colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = Color.White,
                checkedBorderColor = Color.Black,
                checkedIconColor = Color.White,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray,
                uncheckedBorderColor = Color.Gray,)
        )
    }
}
