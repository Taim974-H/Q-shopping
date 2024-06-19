package com.team3.qshopping.view.composables.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.team3.qshopping.R
import com.team3.qshopping.ui.theme.LightGrey
import com.team3.qshopping.view.composables.buttons.BackButton
import com.team3.qshopping.view.composables.buttons.FilterButton
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScaffoldScope
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun CollapsingHeader(
    modifier: Modifier = Modifier,
    start: @Composable (() -> Unit)? = null,
    middle: @Composable () -> Unit = { },
    end: (@Composable () -> Unit)? = null,
    body: @Composable (scope: CollapsingToolbarScaffoldScope) -> Unit = {},
) {
    CollapsingToolbarScaffold(
        modifier = Modifier,
        state = rememberCollapsingToolbarScaffoldState(),
        scrollStrategy = ScrollStrategy.EnterAlways,
        toolbar = {
            Surface(
                modifier = Modifier
                    .zIndex(1f)
                    .shadow(elevation = 10.dp, spotColor = LightGrey)
                    .then(modifier),
                color = MaterialTheme.colors.background
            ) {
                HeaderContent(start, middle, end)
            }
        },
        body = body
    )
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    start: @Composable (() -> Unit)? = null,
    middle: @Composable () -> Unit = { },
    end: (@Composable () -> Unit)? = null,
) {
    Surface(
        modifier = Modifier
            .zIndex(1f)
            .shadow(elevation = 10.dp, spotColor = LightGrey)
            .then(modifier),
        color = MaterialTheme.colors.background
    ) {
        HeaderContent(start, middle, end)
    }
}


@Composable
private fun HeaderContent(
    start: @Composable (() -> Unit)? = null,
    middle: @Composable () -> Unit = { },
    end: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier.padding(
            start = if (start == null) 0.dp else 16.dp,
            end = if (end == null) 0.dp else 16.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (start != null)
            start()
        else
            Spacer(Modifier.weight(1f))

        Spacer(Modifier.weight(1f))
        middle()
        Spacer(Modifier.weight(1f))
        if (end != null)
            end()
        else
            Spacer(Modifier.weight(1f))
    }
}

@Composable
@Preview
fun HeaderPreview() {
    Scaffold() {
        Box(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 0.dp)
        ) {
            Surface() {
                Box {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState(0))
                    ) {
                        Header(start = {
                            BackButton { }
//Box(modifier=Modifier.background(Color.Red).size(50.dp))
                        },
                            middle = {
                                Image(
                                    painter = painterResource(R.drawable.ic_logo),
                                    contentDescription = "Q Shopping Logo",
                                    modifier = Modifier.size(100.dp, 100.dp)
                                )
                            }, end = { FilterButton {} })
                    }
                }
            }
        }
    }
}