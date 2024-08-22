package com.example.weatherforecastapp.presentation.favorite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherforecastapp.data.mapper.addWeather
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.usecase.ChangeFavoriteStateUseCase
import com.example.weatherforecastapp.domane.usecase.CheckFromUpdateUseCase
import com.example.weatherforecastapp.domane.usecase.GetCurrentWeatherUseCase
import com.example.weatherforecastapp.domane.usecase.GetFavoriteCitiesUseCase
import com.example.weatherforecastapp.presentation.favorite.FavoriteStore.Intent
import com.example.weatherforecastapp.presentation.favorite.FavoriteStore.Label
import com.example.weatherforecastapp.presentation.favorite.FavoriteStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavoriteStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class CityItemClicked(
            val indexCity: Int,
            val cities: List<City>
        ) : Intent

        data object ClickSearch : Intent

        data class DeleteCity(val cityId: Int) : Intent

    }

    data class State(
        val cityItems: List<CityItem>
    ) {
        data class CityItem(
            val city: City,
            val weatherState: WeatherState
        )

        sealed class WeatherState {
            data object Initial : WeatherState()
            data object Loading : WeatherState()
            data object Error : WeatherState()
            data class Loaded(
                val tempC: String,
                val iconUrl: String
            ) : WeatherState()
        }

    }

    sealed interface Label {
        data class CityItemClicked(
            val indexCity: Int,
            val cities: List<City>
        ) : Label

        data object ClickSearch : Label
    }
}

class FavoriteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase,
    private val checkFromUpdateUseCase: CheckFromUpdateUseCase

) {

    fun create(): FavoriteStore =
        object : FavoriteStore, Store<Intent, State, Label> by storeFactory.create(
            name = "FavoriteStore",
            initialState = State(cityItems = listOf()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}


    private sealed interface Action {
        data class FavoriteCitiesLoaded(val cities: List<City>) : Action
    }

    private sealed interface Msg {

        data class FavoriteCitiesLoaded(val cities: List<City>) : Msg
        data class WeatherLoaded(
            val cityId: Int,
            val tempC: String,
            val condition: String,
        ) : Msg

        data class WeatherLoadingError(val cityId: Int) : Msg
        data class WeatherIsLoading(val cityId: Int) : Msg


    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavoriteCitiesUseCase().collect {
                    dispatch(Action.FavoriteCitiesLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        var update = false
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.CityItemClicked -> {
                    publish(
                        Label.CityItemClicked(
                            intent.indexCity,
                            intent.cities
                        )
                    )
                }

                Intent.ClickSearch -> {
                    publish(Label.ClickSearch)
                }

                is Intent.DeleteCity -> {
                    scope.launch {
                        changeFavoriteStateUseCase.removeFavorite(intent.cityId)
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavoriteCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavoriteCitiesLoaded(cities))
                    scope.launch {
                        if (!update) {
                            loadWeatherNetwork(cities)
                        } else {
                            loadFromLocal(cities)
                        }
                    }
                }
            }
        }


        private suspend fun loadWeatherNetwork(cities: List<City>) {
            if (checkFromUpdateUseCase(cities[0])) {
                cities.forEach { city ->
                    scope.launch {
                        dispatch(Msg.WeatherIsLoading(city.id))
                        try {
                            val weather = getCurrentWeatherUseCase(city.id)
                            dispatch(
                                Msg.WeatherLoaded(
                                    cityId = city.id,
                                    tempC = weather.tempC,
                                    condition = weather.conditionIconUrl
                                )
                            )
                            val newCity = city.addWeather(weather)
                            update = true
                            changeFavoriteStateUseCase.addFavorite(newCity)
                        } catch (e: Exception) {
                            dispatch(Msg.WeatherLoadingError(city.id))
                        }
                    }
                }
            } else {
                loadFromLocal(cities)
                update = true
            }

        }


        private fun loadFromLocal(cities: List<City>) {
            cities.forEach { city ->
                scope.launch {
                    dispatch(
                        Msg.WeatherLoaded(
                            cityId = city.id,
                            tempC = city.weather.tempC,
                            condition = city.weather.conditionIconUrl
                        )
                    )
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavoriteCitiesLoaded -> {
                copy(
                    cityItems = msg.cities.map {
                        State.CityItem(
                            city = it,
                            weatherState = State.WeatherState.Initial
                        )
                    }
                )
            }

            is Msg.WeatherIsLoading -> {
                copy(
                    cityItems = cityItems.map { city ->
                        if (city.city.id == msg.cityId) {
                            city.copy(weatherState = State.WeatherState.Loading)
                        } else {
                            city
                        }
                    }

                )
            }

            is Msg.WeatherLoaded -> {
                copy(
                    cityItems = cityItems.map { city ->
                        if (city.city.id == msg.cityId) {
                            city.copy(
                                weatherState = State.WeatherState.Loaded(
                                    tempC = msg.tempC,
                                    iconUrl = msg.condition,
                                )
                            )
                        } else {
                            city
                        }
                    }

                )
            }

            is Msg.WeatherLoadingError -> {
                copy(cityItems = cityItems.map { city ->
                    if (city.city.id == msg.cityId) {
                        city.copy(weatherState = State.WeatherState.Error)
                    } else {
                        city
                    }
                }
                )
            }

        }

    }


}


