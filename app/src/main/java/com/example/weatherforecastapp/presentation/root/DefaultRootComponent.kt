package com.example.weatherforecastapp.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.parcelable.Parcelable
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.presentation.details.DefaultDetailsComponent
import com.example.weatherforecastapp.presentation.extensions.componentScope
import com.example.weatherforecastapp.presentation.favorite.DefaultFavoriteComponent
import com.example.weatherforecastapp.presentation.search.DefaultSearchComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class DefaultRootComponent @AssistedInject constructor(
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val favoriteComponentFactory: DefaultFavoriteComponent.Factory,
    private val searchComponentFactory: DefaultSearchComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {


    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Favorite,
        handleBackButton = true,
        childFactory = ::child,

    )


    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            is Config.Details -> {
                RootComponent.Child.Details(
                    detailsComponentFactory.create(
                        indexCity = config.indexCity,
                        componentContext = componentContext,
                        cities =config.cities ,
                        onBackClick = { navigation.pop() }
                    )
                )
            }

            Config.Favorite -> {
                RootComponent.Child.Favorite(
                    favoriteComponentFactory.create(
                        componentContext = componentContext,
                        onCityClicked = {index,cities ->
                            navigation.push(Config.Details(
                                indexCity = index,
                                cities = cities
                            ))
                        },
                        onSearchClicked = {
                            navigation.push(Config.Search)
                        }
                    )
                )


            }

            is Config.Search -> {
                RootComponent.Child.Search(
                    searchComponentFactory.create(
                        componentContext = componentContext,
                        onBackClick = {
                            navigation.pop()
                        },
                       onClickAddFavorite = {
                           navigation.pop()
                       }
                    )
                )

            }
        }
    }
    @Parcelize
    sealed interface Config : Parcelable {
        @Parcelize
        data object Favorite : Config

        @Parcelize
        data object Search : Config

        @Parcelize
        data class Details(
            val indexCity: Int,
            val cities: List<City>
        ) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }

}





