package com.example.spadelbosque.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.spadelbosque.R


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

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = screenWidth / 1.2f

    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState(itemCount = { carouselItems.size }),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        preferredItemWidth = itemWidth,
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = (screenWidth - itemWidth) / 2)
    ) {index ->
        val item = carouselItems[index]
        Card(
            modifier = Modifier
                .height(205.dp)
        ){
            Image(
                painter = painterResource(id = item.imageResId),
                contentDescription = item.contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.large)
            )
        }
    }
}
