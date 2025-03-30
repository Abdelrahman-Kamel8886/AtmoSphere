package com.abdok.atmosphere.ui.Screens.Home.Components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.abdok.atmosphere.R
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Preview
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GifEffectBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Load the GIF
        GlideImage(
            contentScale = ContentScale.FillHeight,
            model = R.drawable.night, // Use the GIF in res/drawable
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()

        )
    }
}
