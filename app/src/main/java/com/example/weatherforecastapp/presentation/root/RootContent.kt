package com.example.weatherforecastapp.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.example.weatherforecastapp.presentation.details.DetailsContent
import com.example.weatherforecastapp.presentation.favorite.FavoriteContent
import com.example.weatherforecastapp.presentation.search.SearchContent

@Composable
fun RootContent(component: RootComponent) {
    Children(
        stack = component.stack
    ) {
        when (val instance = it.instance) {
            is RootComponent.Child.Details -> DetailsContent(component = instance.component)
            is RootComponent.Child.Favorite -> FavoriteContent(component = instance.component)
            is RootComponent.Child.Search -> SearchContent(component = instance.component)
        }
    }

}