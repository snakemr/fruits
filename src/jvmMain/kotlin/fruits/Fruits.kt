package fruits

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlin.random.Random

enum class Size { Tiny, Small, Compact, Long, Medium, Big }

enum class Fruit(val file: String) {
    Apple("p1.jpg"),
    Pear("p2.jpg"),
    Cherry("p3.jpg"),
    Banana("p4.jpg"),
    Lemon("p5.jpg"),
    Strawberry("p6.jpg"),
    Nectarine("p7.jpg"),
    Watermelon("p8.jpg");

    val color: Color get() = when(this) {
        Cherry, Strawberry, Nectarine -> Color.Red
        Pear, Banana, Lemon -> Color.Yellow
        Apple, Watermelon -> Color.Green
    }

    val size get(): Size = when(this) {
        Watermelon -> Size.Big
        Apple, Pear -> Size.Medium
        Banana -> Size.Long
        Lemon, Nectarine -> Size.Compact
        Strawberry -> Size.Small
        Cherry -> Size.Tiny
    }

    @Composable
    operator fun invoke(
        modifier: Modifier = Modifier,
        alignment: Alignment = Alignment.Center,
        contentScale: ContentScale = ContentScale.Fit,
        alpha: Float = DefaultAlpha,
        colorFilter: ColorFilter? = null
    ) = TheFruit(this, modifier, alignment, contentScale, alpha, colorFilter)

    @Composable
    internal fun out(
        modifier: Modifier = Modifier,
        alignment: Alignment = Alignment.Center,
        contentScale: ContentScale = ContentScale.Fit,
        alpha: Float = DefaultAlpha,
        colorFilter: ColorFilter? = null
    ) = Fruit(this, modifier, alignment, contentScale, alpha, colorFilter)

    companion object {
        private val values = Fruit.values()
        fun of(ordinal: Int) = values[ordinal]
    }
}

@Composable
internal fun Fruit(
    what: Fruit,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) = Image(painterResource(what.file), what.name, modifier, alignment, contentScale, alpha, colorFilter)

@Composable
private fun TheFruit(
    what: Fruit,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    LaunchedEffect(what) {
        fruitBowl += what.ordinal
    }
    Image(painterResource(what.file),
        what.name, modifier.size(fruitSize.value.dp), alignment, contentScale, alpha, colorFilter
    )
}

object Fruits {
    private val allFruits = Fruit.values()
    val all: List<Fruit> = allFruits.toList()
    internal fun all(count: Int) = List(count) { allFruits[it % 8] }
    internal fun random(count: Int) = List(count) { allFruits[Random.nextInt(8)] }
    internal fun of(ordinals: String) = ordinals.filter { it in '0'..'7' }.map { Fruit.of(it.toString().toInt()) }
}

val Apple = Fruit.Apple
val Pear = Fruit.Pear
val Cherry = Fruit.Cherry
val Banana = Fruit.Banana
val Lemon = Fruit.Lemon
val Strawberry = Fruit.Strawberry
val Nectarine = Fruit.Nectarine
val Watermelon = Fruit.Watermelon

@Composable
fun ColumnScope.Basket(fruits: @Composable RowScope.()->Unit) {
    LaunchedEffect(Unit) { fruitBowl += 9 }
    Row(content = fruits)
    Divider()
}

@Composable
fun Basket(fruits: @Composable RowScope.()->Unit) = Column { Basket(fruits = fruits) }

@Composable
fun Store(baskets: @Composable ColumnScope.()->Unit) = Column(content = baskets)

internal val fruitBowl = mutableStateListOf<Int>()
internal val fruitSize = mutableStateOf(100)

internal fun List<Fruit>.ordinals(prefix: String = "") = joinToString("", prefix) { it.ordinal.toString() }