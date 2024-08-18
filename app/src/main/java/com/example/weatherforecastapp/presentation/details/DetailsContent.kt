package com.example.weatherforecastapp.presentation.details

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.entity.ConditionValue
import com.example.weatherforecastapp.domane.entity.DetailedForecast
import com.example.weatherforecastapp.domane.entity.Weather
import com.example.weatherforecastapp.presentation.extensions.formatDate
import com.example.weatherforecastapp.presentation.ui.theme.Gradients
import com.example.weatherforecastapp.presentation.ui.theme.JuraBolt
import com.example.weatherforecastapp.presentation.ui.theme.JuraLight
import com.example.weatherforecastapp.presentation.ui.theme.JuraMedium
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailsContent(
    component: DetailsComponent,
) {
    Log.d(TAG, "DetailsContent: ")
    val state = component.model.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = state.value.indexCity,
        pageCount = { state.value.city.size }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),

        bottomBar = {
            ToolBarBottom(
                modifier = Modifier
                    .sizeIn(minHeight = 50.dp, maxHeight = 50.dp)
                    .fillMaxWidth(),
                page = pagerState,
                list = state.value.city
            )
        },
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) { page ->
            ContentCityForecast(
                page = page,
                paddingValues = paddingValues,
                city = state.value.city[page],
                forecast = state.value.forecastState,
            )
        }


    }
}


@Composable
private fun ContentCityForecast(
    city: City,
    forecast: DetailsStore.State.ForecastState,
    paddingValues: PaddingValues,
    page: Int,
) {
    Log.d(TAG, "ContentCityForecast: ")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gradients.cardGradients[page].primaryGradient)
    ) {
        Column {
            Spacer(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(40.dp)
            )
            CityInfo(
                modifier = Modifier
                    .padding(10.dp),
                name = city.name,
                data = city.weather.date
            )
            Spacer(modifier = Modifier.padding(10.dp))
            WeatherInfo(
                modifier = Modifier
                    .padding(10.dp),
                currentWeather = city.weather,
                forecast = forecast,
                page = page
            )
            Spacer(modifier = Modifier.padding(15.dp))
            if (forecast is DetailsStore.State.ForecastState.Loaded) {
                ListForecastNextDay(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    list = forecast.forecast[page].upcoming,
                    paddingValues = paddingValues
                )
            }

        }
    }
}

@Composable
fun WeatherInfo(
    modifier: Modifier = Modifier,
    currentWeather: Weather,
    forecast: DetailsStore.State.ForecastState,
    page: Int,

    ) {
    Log.d(TAG, "WeatherInfo: ")

    Box(modifier = modifier) {

        Row(
            modifier = modifier
                .height(400.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly

        ) {
            TempInfo(
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentWidth()
                ,
                tempC = currentWeather.tempC,
                conditionText = currentWeather.conditionText,
                conditionIconUrl = currentWeather.conditionIconUrl
            )
            Spacer(modifier = Modifier.weight(1f))
            if (forecast is DetailsStore.State.ForecastState.Loaded) {
                DetailedWeatherToList(
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .width(150.dp),
                    detailsForecast = forecast.forecast[page].currentWeather.detailedForecast
                )
            }

        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TempInfo(
    modifier: Modifier = Modifier,
    tempC: String,
    conditionText: String,
    conditionIconUrl: String,
) {
    Log.d(TAG, "TempInfo: ")

    Column(modifier = modifier) {
        Text(
            text = tempC,
            fontSize = 90.sp,
            fontFamily = JuraMedium,
            color = Color.White,
        )
        Row {
            GlideImage(
                modifier = Modifier
                    .size(50.dp),
                model = conditionIconUrl,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .width(100.dp),
                text = conditionText,
                color = Color.White,
                fontFamily = JuraLight,
                fontSize = 23.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
fun DetailedWeatherToList(
    modifier: Modifier = Modifier,
    detailsForecast: List<DetailedForecast>
) {
    Log.d(TAG, "DetailedWeatherToList: ")

    Box(
        modifier = modifier
    ) {

        VerticalLine(
            modifier = Modifier, sizeLine = 380.dp
        )

        LazyColumn(
            modifier = modifier
                .align(Alignment.Center)

        ) {
            items(
                items = detailsForecast
            ) { item ->
                DetailedForecastItem(detailedForecast = item)
            }


        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ToolBarBottom(
    modifier: Modifier,
    page: PagerState,
    list: List<City>
) {
    BottomAppBar(
        modifier = modifier,
        contentColor = Color.White,
        containerColor = Color.Transparent,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            list.forEachIndexed { index, item ->
                when (item.id) {
                    0 -> {
                        Icon(
                            modifier = Modifier
                                .size(10.dp),
                            imageVector = Icons.Default.Navigation,
                            tint = if (page.currentPage == index) Color.White else Color.Gray,
                            contentDescription = null
                        )
                    }

                    else -> {
                        Icon(
                            modifier = Modifier
                                .size(10.dp),
                            imageVector = Icons.Default.Circle,
                            tint = if (page.currentPage == index) Color.White else Color.Gray,
                            contentDescription = null
                        )
                    }
                }
                // Добавление отступа между иконками
                if (index < list.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}


@Composable
private fun CityInfo(
    modifier: Modifier = Modifier,
    name: String,
    data: Calendar
) {
    Log.d(TAG, "CityInfo: ")

    val formatDate = data.formatDate()
    Box(modifier = modifier) {
        Column {
            Text(
                text = name,
                fontSize = 50.sp,
                fontFamily = JuraBolt,
                color = Color.White
            )
            Text(
                text = formatDate,
                fontSize = 20.sp,
                fontFamily = JuraMedium,
                color = Color.White
            )
        }
    }
}







@Composable
fun VerticalLine(
    modifier: Modifier = Modifier,
    sizeLine: Dp,

    ) {
    Canvas(
        modifier = modifier
            .wrapContentSize()
    ) {
        drawLine(
            color = Color.White,
            start = Offset(x = 0f.toDp().toPx(), y = 0f.toDp().toPx()),
            end = Offset(x = 0f.toDp().toPx(), y = sizeLine.toPx()),
            strokeWidth = 2f
        )

    }
}

@Composable
fun DetailedForecastItem(
    modifier: Modifier = Modifier,
    detailedForecast: DetailedForecast,
) {

    Box(
        modifier = modifier
            .wrapContentSize()
            .padding(top = 20.dp),
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = detailedForecast.name,
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = JuraLight,
                maxLines = 1
            )
            Spacer(modifier = Modifier.padding(5.dp))



            if (detailedForecast.conditionValue != ConditionValue.KM_H) {
                Row(
                    modifier = Modifier
                        .wrapContentSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = detailedForecast.value.toInt().toString(),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = JuraLight,
                        maxLines = 1
                    )
                    Text(
                        text = detailedForecast.conditionValue.value,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = JuraLight,
                        maxLines = 1
                    )
                }
            } else {

                Text(
                    text = detailedForecast.value.toInt().toString(),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = JuraLight,
                    maxLines = 1
                )
                Text(
                    text = detailedForecast.conditionValue.value,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = JuraLight,
                    maxLines = 1 // Устанавливаем ограничение на одну строку
                )

            }
            Spacer(modifier = Modifier.padding(5.dp))
            if (detailedForecast.conditionValue == ConditionValue.DEGREE){
                TemperatureProgressBar(
                    modifier = Modifier
                        .width(100.dp)
                        .height(4.dp),
                    value = detailedForecast.value.toInt()
                )

            }else {

                LinearProgressIndicator(
                    modifier = Modifier
                        .width(100.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                    ,
                    progress = { detailedForecast.value },
                    color = detailedForecast.colorCondition,
                )
            }

        }
    }
}


@Composable
fun ListForecastNextDay(
    modifier: Modifier = Modifier,
    list: List<Weather>,
    paddingValues: PaddingValues
) {
    Log.d(TAG, "ListForecastNextDay: ")

    Card(
        modifier = modifier
            .wrapContentSize()
            .padding(bottom = paddingValues.calculateBottomPadding()),
        colors = CardDefaults.cardColors(Color.White.copy(0.1f)),
    ) {
        LazyRow(
            modifier = Modifier
        ) {
            items(list) {
                ForecastNextDayItem(it)
            }

        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ForecastNextDayItem(weather: Weather) {
    val dayOfWeek = weather.date.getDisplayName(
        Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()
    )
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.background(
                color = Color.Transparent
            ),
            verticalArrangement = Arrangement.Center,

            ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = dayOfWeek ?: "",
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = JuraLight,
                maxLines = 1,

                )
            Spacer(modifier = Modifier.padding(5.dp))
            GlideImage(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(50.dp),
                model = weather.conditionIconUrl,
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = weather.tempC,
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = JuraLight,
                maxLines = 1
            )


        }

    }
}

@Composable
fun TemperatureProgressBar(modifier: Modifier, value: Int) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Gradients.temperature)
            .drawBehind {
                val width = size.width
                val height = size.height

                val lineWidth = 2.dp.toPx()
                val lineHeight = height

                val minTemp = -50
                val maxTemp = 50
                val temperatureRange = maxTemp - minTemp

                val normalizedValue = (value - minTemp).toFloat() / temperatureRange
                val linePosition = width * normalizedValue

                drawLine(
                    color = Color.Black,
                    start = Offset(linePosition, -lineHeight / 2), // Вырезаем за верхнюю границу
                    end = Offset(linePosition, height + lineHeight / 2), // Вырезаем за нижнюю границу
                    strokeWidth = lineWidth
                )
            }
    )
}




const val TAG = "DetailsContent"

