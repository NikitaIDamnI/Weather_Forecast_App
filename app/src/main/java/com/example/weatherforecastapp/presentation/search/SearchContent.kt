package com.example.weatherforecastapp.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.entity.Forecast
import com.example.weatherforecastapp.domane.entity.SearchCity
import com.example.weatherforecastapp.presentation.details.CityInfo
import com.example.weatherforecastapp.presentation.details.ListForecastNextDay
import com.example.weatherforecastapp.presentation.details.WeatherInfo
import com.example.weatherforecastapp.presentation.ui.theme.Gradients

@Composable
fun SearchContent(
    component: SearchComponent,
) {
    val state by component.model.collectAsState()
    val focusRequester = remember { FocusRequester() }


    SearchContent(focusRequester, state, component)
    PreviewContent(state, component)
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SearchContent(
    focusRequester: FocusRequester,
    state: SearchStore.State,
    component: SearchComponent
) {
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    SearchBar(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester),
        query = state.searchQuery,
        placeholder = { Text(text = stringResource(R.string.search)) },
        onQueryChange = { component.changeSearchQuery(it) },
        onSearch = { component.onClickSearch() },
        active = true,
        onActiveChange = {},
        leadingIcon = {
            IconButton(onClick = { component.onClickBack() }) {
                Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = null)
            }
        },
        trailingIcon = {
            IconButton(onClick = { component.onClickSearch() }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        },
    ) {
        when (val searchState = state.searchState) {
            SearchStore.State.SearchState.EmptyResult -> {
                Text(text = "No results found")
            }

            SearchStore.State.SearchState.Error -> {
                Text(text = "Error occurred while searching")
            }

            SearchStore.State.SearchState.Initial -> {
            }

            SearchStore.State.SearchState.Loading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            is SearchStore.State.SearchState.Loaded -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(
                        items = searchState.cities,
                        key = { it.id }
                    ) { searchCity ->
                        CityCard(
                            searchCity = searchCity,
                            onCityClick = { component.onClickCity(searchCity) }
                        )

                    }

                }
            }

        }

    }
}

@Composable
private fun CityCard(
    searchCity: SearchCity,
    onCityClick: (SearchCity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCityClick(searchCity) }
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
        ) {
            Text(
                text = searchCity.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = searchCity.country,
            )

        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreviewContent(
    state: SearchStore.State,
    component: SearchComponent,
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    when (val statePreview = state.previewState) {
        SearchStore.State.PreviewState.Error -> {}
        SearchStore.State.PreviewState.Initial -> {
            openBottomSheet = false
        }

        is SearchStore.State.PreviewState.Loaded -> {
            ModalBottomSheet(
                onDismissRequest = { component.onClickClosePreview() },
                sheetState = bottomSheetState,
                containerColor = Color.Yellow,
                dragHandle = null,
            ) {
                PreviewCity(
                    city = statePreview.city,
                    forecast = statePreview.forecast,
                    isFavorite = statePreview.isFavorite,
                    onAddClick = {component.onClickAddFavorite(city = statePreview.city)}
                )
            }
        }

        SearchStore.State.PreviewState.Loading -> {
        }
    }

}

@Composable
private fun PreviewCity(
    city: City,
    forecast: Forecast,
    isFavorite: Boolean,
    onAddClick: () -> Unit,
) {

    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(Gradients.cardGradients[0].primaryGradient)
    ) {
        Spacer(modifier = Modifier.padding(15.dp))

            if (!isFavorite) {
                IconButton(modifier = Modifier
                    .align(Alignment.End)
                    .padding(end= 10.dp)
                    ,
                    onClick = { onAddClick() }
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = null)
                }
            }

        Spacer(modifier = Modifier.padding(10.dp))
        CityInfo(
            modifier = Modifier
                .padding(10.dp),
            name = city.name,
            data = city.weather.date
        )
        WeatherInfo(
            modifier = Modifier
                .padding(top = 5.dp, start = 10.dp, end = 10.dp),
            currentWeather = city.weather,
            forecast = forecast,
        )

        ListForecastNextDay(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 30.dp,
                    bottom = 20.dp
                ),
            list = forecast.upcoming
        )
    }
}

