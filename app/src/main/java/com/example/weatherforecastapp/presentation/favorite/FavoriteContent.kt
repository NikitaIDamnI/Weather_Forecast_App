package com.example.weatherforecastapp.presentation.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.presentation.extensions.tempToFormattedString
import com.example.weatherforecastapp.presentation.favorite.FavoriteStore.State.WeatherState.Loading
import com.example.weatherforecastapp.presentation.ui.theme.CardGradients
import com.example.weatherforecastapp.presentation.ui.theme.Gradient
import com.example.weatherforecastapp.presentation.ui.theme.SearchColor

@Composable
fun FavoriteContent(
    component: FavoriteComponent,
    paddingValues: PaddingValues
) {

    val state = component.model.collectAsState()


    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color.Black),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item(span = { GridItemSpan(2) }) {
            Text(
                text = stringResource(R.string.weather),
                fontSize = 30.sp,
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold


            )
        }
        item(span = { GridItemSpan(2) }) {
            SearchCard(
                onClick = { component.onClickSearch() }
            )
        }
        itemsIndexed(
            items = state.value.cityItems,
            key = { _, item -> item.city.id }
        ) { index, item ->
            CityCard(
                index = index,
                city = item,
                onClick = { component.onCityItemClick(item.city) }
            )
        }

    }


}

@Composable
private fun SearchCard(
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.padding(bottom = 10.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardColors(
            containerColor = SearchColor,
            contentColor = Color.Black,
            disabledContentColor = Color.Black,
            disabledContainerColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Icon(
                modifier = Modifier
                    .padding(top = 5.dp, start = 5.dp, bottom = 5.dp, end = 16.dp),
                imageVector = Icons.Default.Search,
                tint = Color.Gray,
                contentDescription = null
            )

            Text(
                text = stringResource(R.string.search),
                fontSize = 16.sp,
                color = Color.Gray
            )

        }

    }
}

@Preview
@Composable
private fun PrevSearchCard() {
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
                .sizeIn(minHeight = 196.dp)
                .padding(24.dp)
                .drawBehind {
                    drawCircle(
                        brush = gradient.secondaryGradient,
                        center = Offset(
                            x = center.x - size.width / 10,
                            y = center.y + size.height / 2
                        ),
                        radius = size.minDimension / 1.5f
                    )

                }
                .clickable { onClick() }

        ) {
            when (val weatherState = city.weatherState) {
                FavoriteStore.State.WeatherState.Error -> {}

                FavoriteStore.State.WeatherState.Initial -> {}

                is FavoriteStore.State.WeatherState.Loaded -> {
                    GlideImage(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(56.dp),
                        model = weatherState.iconUrl,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(top = 24.dp, start = 10.dp, bottom = 45.dp),
                        text = weatherState.tempC,
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 48.sp)

                    )
                }

                is Loading -> {
                    GlideImage(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(56.dp),
                        model = city.city.weather.conditionIconUrl,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(top = 24.dp, start = 10.dp, bottom = 50.dp),
                        text = city.city.weather.tempC,
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 48.sp)

                    )
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(20.dp),
                        color = MaterialTheme.colorScheme.background
                    )


                }
            }




            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                ,
                text = city.city.name,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                style = TextStyle(
                    color = Color.Black,
                    drawStyle = Stroke(
                        width = 1f, // толщина обводки
                        join = StrokeJoin.Round
                    )
                )
            )
            // Белый текст поверх обводки
            Text(
                modifier =  Modifier
                    .align(Alignment.BottomStart)
                ,
                text = city.city.name,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                color = Color.White // Цвет заливки текста
            )

        }
    }

}

private fun getGradientByIndex(index: Int): Gradient {
    val gradients = CardGradients.gradients
    return gradients[index % gradients.size]
}