package com.abdok.atmosphere

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun CustomFloatingWindow(onClose: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .background(Color(0xCC000000), shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .padding(16.dp)
        , horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello from Floating Window!",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Button(
            onClick = { onClose() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Close", color = Color.White)
        }
    }
}
