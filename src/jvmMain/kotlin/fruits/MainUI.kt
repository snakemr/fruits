package fruits

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.InsetBox

@OptIn(ExperimentalAnimationApi::class)
fun mainUI(app: @Composable (Lessons, List<Fruit>)->Unit) = application {
    val state = rememberWindowState(size = DpSize(1024.dp, 768.dp))
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf(null as String?) }
    var chapter by remember { mutableStateOf<Chapter?>(null) }
    var lesson by remember { mutableStateOf<Lessons?>(null) }
    var last by remember { mutableStateOf<Int?>(null) }
    LaunchedEffect(Unit) {
        initData()
        name = userName
        if (name != null) {
            lesson = currentLesson?.let(Lessons::of) ?: Lessons.About
            chapter = lesson?.chapter
        }
    }
    Window(::exitApplication, state,
        title = name?.let { "Фрукты Котлина 0.2. Задание выполнил(и): $it" } ?: "Введите имя",
        icon = painterResource(Fruit.Apple.file),
        resizable = false
    ) {
        if (name == null) Row(Modifier.height(IntrinsicSize.Min).padding(10.dp)) {
            var text by remember { mutableStateOf("") }
            OutlinedTextField(text, { text = it }, Modifier.weight(1f),
                label = { Text("Введите свои фамилию и имя (или несколько)") }, singleLine = true
            )
            Button({ if (text.length > 3) {
                name = text
                setUserName(text, "DJehQwEpAL")
            } }, Modifier.fillMaxHeight().padding(start = 8.dp, top = 8.dp)) { Text("Запомнить в этом проекте") }
        }
        AnimatedVisibility(chapter == null && name != null, enter = slideInHorizontally { -it }, exit = slideOutHorizontally { -it }) {
            val solutions by produceState(emptyMap()) {
                value = solutions().groupBy { Lessons.of(it.lesson).chapter }.map { entry ->
                    entry.key to (Lessons.values.count { it.chapter == entry.key } == entry.value.size)
                }.toMap()
            }
            Box(Modifier.fillMaxSize()) {
                LazyColumn(Modifier.align(Alignment.Center)) {
                    item {
                        Text(buildAnnotatedString {
                            withStyle(SpanStyle(Color.LightGray, textDecoration = TextDecoration.LineThrough)) {
                                append("Не")
                            }
                            append("серьёзный тренажёр.\nРабота ")
                            withStyle(SpanStyle(Color.LightGray, textDecoration = TextDecoration.LineThrough)) {
                                append("с фруктами")
                            }
                            append(" со списками на языке Kotlin")
                        },  Modifier.width(500.dp).padding(bottom = 8.dp),
                            Color.Gray, 20.sp, textAlign = TextAlign.Center
                        )
                    }
                    items(Chapter.values) {
                        Card(Modifier.size(500.dp, 80.dp).padding(12.dp, 8.dp), RoundedCornerShape(10.dp), elevation = 8.dp) {
                            Row(Modifier.clickable { chapter = it }, verticalAlignment = Alignment.CenterVertically) {
                                Fruit.of(it.ordinal).out(Modifier.padding(start = 5.dp))
                                Text(it.title, Modifier.weight(1f).padding(10.dp), Color.DarkGray, 16.sp)
                                if (solutions[it] == true) Icon(Icons.Default.CheckCircle, null,
                                    Modifier.padding(end = 10.dp), Color(0xFF008040))
                            }
                        }
                    }
                    item {
                        Text("© Андрей Светличный, 2023", Modifier.width(500.dp).padding(top = 8.dp),
                            Color.Gray, 16.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }
        AnimatedVisibility(chapter != null && lesson == null,
            enter = if (last != null) fadeIn() else slideInHorizontally { it },
            exit = if (lesson != null) fadeOut() else slideOutHorizontally { it*2 }
        ) {
            LaunchedEffect(Unit) {
                last = null
            }
            val solutions by produceState(emptyMap()) {
                value = solutions().associate { it.lesson to (it.percent == 100) }
            }
            LazyHorizontalGrid(GridCells.Adaptive(60.dp)) {
                item {
                    Card(Modifier.size(500.dp, 40.dp).padding(12.dp, 8.dp), RoundedCornerShape(10.dp), elevation = 8.dp) {
                        Row(Modifier.fillMaxWidth().clickable { chapter = null }, verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ArrowBack, "back",
                                Modifier.padding(10.dp), MaterialTheme.colors.primaryVariant)
                            Text(chapter?.title ?: "", Modifier.padding(10.dp), Color.DarkGray, 16.sp,
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                items(Lessons.values.filter { it.chapter == chapter }) {
                    Card(Modifier.size(500.dp, 40.dp).padding(12.dp, 8.dp), RoundedCornerShape(10.dp), elevation = 8.dp) {
                        Box(Modifier.clickable { lesson = it }) {
                            Row(Modifier.fillMaxWidth().align(Alignment.CenterStart),
                                verticalAlignment = Alignment.CenterVertically) {
                                Text("${(it.ordinal + 1)}.", Modifier.padding(start = 10.dp).width(24.dp),
                                    Color.DarkGray, 16.sp, textAlign = TextAlign.Center)
                                Text(it.title, Modifier.padding(10.dp), Color.DarkGray, 16.sp,
                                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            if (solutions[it.ordinal] == true) Icon(Icons.Default.CheckCircle, null,
                                    Modifier.align(Alignment.CenterEnd).padding(5.dp), Color(0xFF008040))
                        }
                    }
                }
            }
        }
        AnimatedVisibility(lesson != null, enter = fadeIn(), exit = fadeOut()) {
            var correct by remember { mutableStateOf(true) }
            val less = remember { lesson ?: Lessons.About }
            val saved by produceState(false) {
                value = solution(less.ordinal)?.percent == 100
            }
            var alpha by remember { mutableStateOf(1f) }
            val cycle by produceState(0) {
                last = less.ordinal
                currentLesson = last
                while (correct && value < less.tries) {
                    if (condition.isNotEmpty() && condition.toList() == menu.flatten().toList())
                        condition.clear()
                    menu.clear()
                    delay(500)
                    fruitBowl.clear()
                    if (condition.isNotEmpty()) condition.clear()
                    fruitSize.value = less.size
                    less.spawn().let {
                        menu += it.first
                        condition += it.second
                        answer.value = it.third
                    }
                    alpha = 1f
                    delay(500)
                    println(fruitBowl.joinToString(""){"$it"} + " <=> " + answer.value)
                    correct = fruitBowl.joinToString(""){"$it"} == answer.value
                    value ++
                    delay(500)
                }
            }
            val titleBack by animateColorAsState(
                if (!correct && (fruitBowl - 9).isNotEmpty()) MaterialTheme.colors.error
                else if (saved || correct && cycle == less.tries) Color(0xFF008040)
                else Color.White
            )
            Row {
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            IconButton({ lesson = null ; menu.clear() }, Modifier.offset(10.dp)) {
                                Icon(Icons.Default.ArrowBack, "back", tint = MaterialTheme.colors.primaryVariant)
                            }
                            IconButton({ scope.launch {
                                lesson = null
                                menu.clear()
                                delay(500)
                                lesson = less
                            } }, Modifier.offset(10.dp)) {
                                Icon(Icons.Default.Refresh, "retry", tint = MaterialTheme.colors.error)
                            }
                        }
                        InsetBox(Modifier.padding(10.dp).fillMaxHeight(0.15f),
                            "Урок ${less.ordinal + 1}. ${less.chapter.title}. ${less.title}"
                        ) {
                            Box(Modifier.fillMaxSize().background(titleBack)) {
                                Text(less.description ?: AnnotatedString("Повторите содержимое верхней картинки"),
                                    Modifier.align(Alignment.Center),
                                    color = if (titleBack == Color.White) Color.DarkGray else Color.LightGray,
                                    fontSize = 16.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                    if (condition.isNotEmpty() && condition.toList() != menu.flatten().toList())
                    InsetBox(Modifier.fillMaxHeight(0.15f).padding(10.dp), "Допустим, у вас есть:") {
                        AnimatedVisibility(menu.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
                            Column {
                                Row { condition.forEach { it.out() } }
                            }
                        }
                    }
                    InsetBox(Modifier.fillMaxHeight(0.5f).padding(10.dp), "Пример выполнения:") {
                        AnimatedVisibility(menu.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
                            Column {
                                menu.forEach { row ->
                                    Row { row.forEach { it.out(Modifier.size(fruitSize.value.dp)) } }
                                    Divider()
                                }
                            }
                        }
                    }
                    InsetBox(Modifier.fillMaxHeight().padding(10.dp), "Ваша работа:") {
                        AnimatedVisibility(menu.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
                            app(less, condition)
                        }
                    }
                }
                InsetBox(Modifier.padding(10.dp).width(100.dp), "Тесты (${less.tries})") {
                    Column(Modifier.fillMaxSize(), Arrangement.Bottom, Alignment.CenterHorizontally) {
                        AnimatedVisibility (menu.isEmpty(), enter = fadeIn(), exit = fadeOut()) {
                            Text("попытка ${cycle+1}", color = Color.Gray, fontSize = 16.sp)
                        }
                        AnimatedVisibility(correct && cycle == less.tries, enter = fadeIn()) {
                            Text("Успех!", color = Color(0xFF008000), fontSize = 16.sp)
                            LaunchedEffect(Unit) {
                                if (!saved) storePercentValue(less.ordinal, 100)
                            }
                        }
                        AnimatedVisibility(!correct, enter = fadeIn()) {
                            Text("Неудача", color = Color(0xFFA00000), fontSize = 16.sp)
                        }
                        Fruits.all.reversed().forEach {
                            AnimatedVisibility(it.ordinal < cycle, enter = scaleIn()) {
                                Box {
                                    it.out()
                                    if (correct || it.ordinal < cycle-1)
                                        Icon(Icons.Default.CheckCircle, "Ok",
                                            Modifier.align(Alignment.BottomEnd), Color(0xFF008000))
                                    else
                                        Icon(Icons.Default.Clear, "Error",
                                            Modifier.align(Alignment.BottomEnd), Color(0xFFA00000))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
