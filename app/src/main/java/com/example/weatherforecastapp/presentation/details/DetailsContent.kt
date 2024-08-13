package com.example.weatherforecastapp.presentation.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.weatherforecastapp.domane.entity.City

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailsContent(
    component: DetailsComponent,
    paddingValues: PaddingValues,
    pagerStart: Int = 0
) {

    val list = listOf(
        City(id = 0, name = "Москва", country = "Россия"),
        City(id = 1, name = "Санкт-Петербург", country = "Россия"),
        City(id = 2, name = "Новосибирск", country = "Россия"),
    )


    val pagerState = rememberPagerState(
        initialPage = pagerStart,
        pageCount = { list.size }
    )



    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        ToolBarBottom(
            modifier = Modifier
                .size(20.dp)
                .fillMaxWidth()
                .align(Alignment.CenterStart),
            page = pagerState, list = list
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize() // Убедитесь, что Pager занимает достаточно места

        ) { page ->
            Text(text = page.toString())
        }

    }


}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToolBarBottom(
    modifier: Modifier,
    page: PagerState,
    list: List<City>
) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
        ,
        contentColor = Color.White,

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
            ,
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