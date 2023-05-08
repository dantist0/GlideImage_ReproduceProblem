package com.test.lansca

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val composeView = ComposeView(this)
        setContentView(composeView)

        composeView.setContent { ScreenWithProblem() }
    }
}

@Composable
fun ScreenWithProblem() {

    val state = rememberCollapsingToolbarScaffoldState()
    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            /*
             * Without wrap this inside box, GlideImage 2.1.13 does not show image.
             * But it is working on any previous version
             */
            GlideImage(
                imageModel = { "https://i0.wp.com/www.printmag.com/wp-content/uploads/2021/02/4cbe8d_f1ed2800a49649848102c68fc5a66e53mv2.gif?fit=476%2C280&ssl=1" },
                modifier = Modifier
                    .background(Color.Cyan)
                    .fillMaxWidth()
                    .aspectRatio(390f / 320f)
                    .parallax(0.32f),
                loading = {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text("Loading", modifier = Modifier.align(Alignment.Center))
                    }
                },
                previewPlaceholder = R.drawable.ic_launcher_foreground,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                )
            )
            ApplicationToolbar(backgroundAlpha = 1f - state.toolbarState.progress)
        },

        body = {
            val cornerRadius = 24.dp * state.toolbarState.progress
            val headerSize = 80.dp
            Column(
                modifier = Modifier
                    .offset(y = -(state.toolbarState.progress) * headerSize)
                    .zIndex(1f)
                    .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(headerSize * (state.toolbarState.progress))
                        .background(
                            Brush.verticalGradient(
                                0f to Color.White.copy(alpha = 0.9f),
                                1f to Color.White
                            )
                        )
                ) {
                    val (title) = createRefs()
                    Text(
                        text = "Text",
                        color = Color.Black,
                        fontSize = 20.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .padding(top = 16.dp)
                            .alpha((2 * state.toolbarState.progress - 1).coerceIn(0f, 1f))
                            .constrainAs(title) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                    )
                }
                ImagesTable()
            }
        },
    )
}

@Composable
fun ImagesTable() {
    val space = 24.dp
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier,
        contentPadding = PaddingValues(space),
        verticalArrangement = Arrangement.spacedBy(space),
        horizontalArrangement = Arrangement.spacedBy(space)
    ) {
        items(
            count = 50,
            itemContent = {
                GlideImage(
                    imageModel = { "https://static.wikia.nocookie.net/cartoonnetwork/images/3/39/Tom_and_Jerry_Time.png/revision/latest?cb=20210913214525" },
                    modifier = Modifier
                        .background(Color.Gray)
                        .aspectRatio(1f)
                        .fillMaxHeight(),
                )
            }
        )
    }
}

@Composable
fun ApplicationToolbar(
    backgroundAlpha: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(SolidColor(Color.Red), RectangleShape, backgroundAlpha)
            .safeDrawingPadding()
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 14.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Title",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.weight(1.0f)
        )
    }
}
