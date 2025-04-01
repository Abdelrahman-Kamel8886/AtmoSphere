package com.abdok.atmosphere.screens.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abdok.atmosphere.R
import com.abdok.atmosphere.utils.viewHelpers.GifBackground
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Preview
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GifEffectBackground(modifier: Modifier = Modifier , condition: String = "01d") {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        GlideImage(
            contentScale = ContentScale.FillBounds,
            model = GifBackground.getGif(condition),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}
