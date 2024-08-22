package com.example.weatherforecastapp.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.weatherforecastapp.presentation.details.DetailsComponent
import com.example.weatherforecastapp.presentation.favorite.FavoriteComponent
import com.example.weatherforecastapp.presentation.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>


   sealed interface Child {
        class Favorite(val component: FavoriteComponent) : Child
        class Search(val component: SearchComponent) : Child
        class Details(val component: DetailsComponent) : Child
    }


}