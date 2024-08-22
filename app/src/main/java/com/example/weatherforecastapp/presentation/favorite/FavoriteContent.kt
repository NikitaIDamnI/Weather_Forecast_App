package com.example.weatherforecastapp.presentation.favorite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.presentation.favorite.FavoriteStore.State.WeatherState.Loading
import com.example.weatherforecastapp.presentation.ui.theme.ColorDeleteCard
import com.example.weatherforecastapp.presentation.ui.theme.Gradients
import com.example.weatherforecastapp.presentation.ui.theme.Gradient
import com.example.weatherforecastapp.presentation.ui.theme.JuraBolt
import com.example.weatherforecastapp.presentation.ui.theme.JuraMedium
import com.example.weatherforecastapp.presentation.ui.theme.SearchColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FavoriteContent(
    component: FavoriteComponent,
    paddingValues: PaddingValues
) {

    val state = component.model.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .background(Color.Black),

        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.weather),
                    fontSize = 30.sp,
                    color = Color.White,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold


                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    IconButton(onClick = { component.onClickSearch() }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

        }
        itemsIndexed(
            items = state.value.cityItems,
            key = { _, item -> item.city.id }
        ) { index, item ->
            val dismissState = rememberSwipeToDismissBoxState(
                positionalThreshold = { it * 0.5f },
                confirmValueChange = {
                    if (it == SwipeToDismissBoxValue.EndToStart) {
                        component.onDeleteCity(item.city.id)
                    }
                    true
                }
            )
            SwipeToDismissBox(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState,
                backgroundContent = {
                    val color = when (dismissState.dismissDirection) {
                        SwipeToDismissBoxValue.EndToStart -> ColorDeleteCard
                        else -> Color.Transparent
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(25.dp))
                            .background(color),
                    ) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 10.dp),
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                },

                enableDismissFromStartToEnd = false,
                enableDismissFromEndToStart = true
            ) {
                CityCard(
                    index = index,
                    city = item,
                    onClick = {
                        component.onCityItemClick(
                            indexCity = index,
                            cities = state.value.getCity()
                        )
                    }
                )
            }

        }

    }


}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun CityCard(
    modifier: Modifier = Modifier,
    city: FavoriteStore.State.CityItem,
    color: Color = Color.Red,
    index: Int = 0,
    onClick: () -> Unit
) {
    val gradient = getGradientByIndex(index)
    Card(
        modifier = modifier
            .fillMaxSize()
            .shadow(
                elevation = 16.dp,
                spotColor = gradient.shadowColor,
                shape = MaterialTheme.shapes.extraLarge

            ),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Box(
            modifier = Modifier
                .background(gradient.primaryGradient)
                .fillMaxSize()
                .size(150.dp)
                .padding(24.dp)
                .drawBehind {
                    drawCircle(
                        brush = gradient.secondaryGradient,
                        center = Offset(
                            x = 0f - size.width / 10,
                            y = center.y / 2 + size.height / 2
                        ),
                        radius = size.minDimension / 1.5f
                    )

                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick,
                )


        ) {


            when (city.weatherState) {
                FavoriteStore.State.WeatherState.Error -> {}
                FavoriteStore.State.WeatherState.Initial -> {}
                is FavoriteStore.State.WeatherState.Loaded -> {}

                is Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(20.dp),
                        color = MaterialTheme.colorScheme.background
                    )
                }


            }
            GlideImage(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(56.dp),
                model = city.city.weather.conditionIconUrl,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp),
                text = city.city.weather.tempC,
                fontFamily = JuraBolt,
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 48.sp)
            )
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart),
                text = city.city.name,
                fontSize = 20.sp,
                fontFamily = JuraMedium,
                color = Color.White
            )


        }
    }

}


private fun getGradientByIndex(index: Int): Gradient {
    val gradients = Gradients.cardGradients
    return gradients[index % gradients.size]
}


fun FavoriteStore.State.getCity(): List<City> {
    return this.cityItems.map {
        City(
            id = it.city.id,
            name = it.city.name,
            country = it.city.country,
            weather = it.city.weather
        )
    }

}