package com.example.movieapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.movieapp.models.Movie
import com.example.movieapp.models.getMovies
import com.example.movieapp.ui.theme.MovieAppTheme
import NavigationCtrl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppTheme {
                MainContent()
                Surface(color = MaterialTheme.colors.background) { NavigationCtrl() }
            }
        }
    }
}

@Composable
fun MainContent() {
    val bodyContent = remember { mutableStateOf("Select menu to change content") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "App Title") },
                actions = { TopAppBarDropdownMenu(bodyContent) }
            )
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {
            Text(bodyContent.value)
        }
    }
}

@Composable
fun TopAppBarDropdownMenu(bodyContent: MutableState<String>) {
    val expanded = remember { mutableStateOf(false) }

    Box(Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(
            onClick = {
                expanded.value = true
                bodyContent.value = "Menu Opening"
            }
        ) {
            Icon(Icons.Filled.MoreVert, contentDescription = "More Menu")
        }
    }

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
    ) {
        DropdownMenuItem(
            onClick = {
                expanded.value = false
                bodyContent.value = "First Item Selected"
            }
        ) {
            Icon(
                tint = MaterialTheme.colors.onPrimary,
                imageVector = Icons.Default.Favorite,
                contentDescription = "Add to favorites"
            )
            Text("Favorites")
        }
    }
}

@Composable
fun HomeScreen() {
    MessageList()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieRow(movie: Movie) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (expandedState) 180f else 0f)

    Card(
        modifier =
        Modifier.fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(delayMillis = 300, easing = LinearOutSlowInEasing)
            )
            .padding(5.dp),
        shape = RoundedCornerShape(corner = CornerSize(15.dp)),
        elevation = 5.dp,
        onClick = { expandedState = !expandedState }
    ) {
        Column {
            Box(
                modifier = Modifier.height(150.dp).fillMaxWidth(),
            ) {
                val painter =
                    rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = movie.images[0])
                            .apply(block = fun ImageRequest.Builder.() {})
                            .build(),
                        contentScale = ContentScale.FillWidth
                    )
                val painterState = painter.state
                Image(
                    painter = painter,
                    contentDescription = "Movie Poster",
                    contentScale = ContentScale.FillWidth
                )

                Box(
                    modifier = Modifier.fillMaxSize().padding(10.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Icon(
                        tint = MaterialTheme.colors.secondary,
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Add to favorites"
                    )
                }
                if (painterState is AsyncImagePainter.State.Loading) {
                    CircularProgressIndicator()
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    movie.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6
                )
                IconButton(
                    modifier = Modifier.alpha(ContentAlpha.medium).rotate(rotationState),
                    onClick = { expandedState = !expandedState }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Show details"
                    )
                }
            }
            if (expandedState) {
                Text(
                    text = "Director: " + movie.director,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Normal,
                    maxLines = 5
                )
                Text(
                    text = "Released: " + movie.year,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Normal,
                    maxLines = 5
                )
                Text(
                    text = "Genre: " + movie.genre,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Normal,
                    maxLines = 5
                )
                Text(
                    text = "Actors: " + movie.actors,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Normal,
                    maxLines = 5
                )
                Text(
                    text = "Rating: " + movie.rating,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Normal,
                    maxLines = 5
                )
                Divider(color = Color.Black, thickness = 1.dp)
                Text(
                    text = "Plot: " + movie.plot,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Normal,
                    maxLines = 5
                )
            }
        }
    }
}

@Composable
fun MessageList() {
    val bodyContent = remember { mutableStateOf("Select menu to change content") }
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Movies") },
                actions = { TopAppBarDropdownMenu(bodyContent) }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "fab icon")
            }
        },
        drawerContent = { Text(text = "Drawer Menu 1") },
        content = { padding ->
            print(getMovies())
            LazyColumn {
                (Modifier.padding(padding))
                items(getMovies()) { message -> MovieRow(message) }
            }
        }
    )
}
