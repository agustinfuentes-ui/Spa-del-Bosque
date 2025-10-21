package com.example.spadelbosque.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.spadelbosque.R
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Carrusel() {
    data class CarouselItem(
        val id: Int,
        @DrawableRes val imageResId: Int,
        val contentDescription: String
    )

    val carouselItems = remember {
        listOf(
            CarouselItem(0, R.drawable.sauna, "cupcake"),
            CarouselItem(1, R.drawable.chocolaterapia, "donut"),
            CarouselItem(2, R.drawable.piedras_calientes, "eclair"),
            CarouselItem(3, R.drawable.circuito, "froyo"),
            CarouselItem(4, R.drawable.escapada_amigas, "gingerbread")
        )
    }

    val screenWith = LocalConfiguration.current.screenWidthDp.dp
    val itemWith = screenWith / 1.1f

    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { carouselItems.count() },

        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp, bottom = 16.dp),
        preferredItemWidth   = itemWith,
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { i ->
        val item = carouselItems[i]
        Image(
            modifier = Modifier
                .height(205.dp)
                .maskClip(MaterialTheme.shapes.extraLarge),
            painter = painterResource(id = item.imageResId),
            contentDescription = item.contentDescription,
            contentScale = ContentScale.Crop
        )
    }
}