package fruits

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import kotlin.random.Random
import data.userName

enum class Chapter(val title: String) {
    About("Введение"),
    Cycles("Циклы"),
    Lists("Обработка списков"),
    Conditions("Условия")
    ;
    companion object {
        val values: List<Chapter> = Chapter.values().toList()
    }
}

enum class Lessons(
    val chapter: Chapter,
    val title: String,
    val spawn: ()->Triple<List<List<Fruit>>, List<Fruit>, String>,
    val description: AnnotatedString? = null,
    val tries: Int = 1,
    val size: Int = 100
) {
    About(Chapter.About,"Об этом задачнике", { Triple(listOf(listOf(Apple)), emptyList(), "90") },
        AnnotatedString("""Здравствуйте, $userName!
Этот задачник в игровой форме поможет вам изучить основы работы со списками в языке Kotlin.
Используйте функцию Apple(), чтобы положить в корзину яблоко"""), size = 200),

    More(Chapter.About,"Ваши фрукты, $userName", { Triple(listOf(Fruits.all), emptyList(), "901234567") },
        AnnotatedString("Положите в корзину фрукты: " + Fruits.all.joinToString())),

    Baskets(Chapter.About,"Сортировка по корзинам", {
        Triple(listOf(Fruits.of("256"), Fruits.of("134"), Fruits.of("07")), Fruits.all, "92569134907")
    }, buildAnnotatedString {
        append("Положите фрукты в разные корзины по цветам. Используйте составной блок Basket:")
        withStyle(mono) { append("\nBasket { ... }") }
    }, size = 73),

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    AllFruits(Chapter.Cycles, "Все фрукты", { Triple(listOf(Fruits.all), emptyList(), "901234567") },
        buildAnnotatedString {
            append("Положите в корзину все фрукты из полного набора Fruits.all, используя цикл for:")
            withStyle(mono) { append("\nfor (fruit in Fruits.all) { fruit() }") }
        }
    ),

    Some(Chapter.Cycles, "Несколько фруктов", {
        Fruits.random(8).let { Triple(listOf(it), it, it.ordinals("9")) }
    }, AnnotatedString("Положите в корзину все фрукты из случайного набора fruits (он даётся вам в параметре функции)"), 3),

    SomeMore(Chapter.Cycles, "Ещё несколько фруктов", {
        Fruits.random(15).let { Triple(listOf(it), it, it.ordinals("9")) }
    }, buildAnnotatedString {
            append("Положите в корзину все фрукты из случайного набора fruits, используя функцию-расширение forEach:")
            withStyle(mono) { append("\nfruits.forEach { it() }") }
        }, 3, 55
    ),

    Separate(Chapter.Cycles, "По разным корзинам", {
        Fruits.random(5).let { f -> Triple(f.map { listOf(it) }, f, f.joinToString("") { "9${it.ordinal}" }) }
    }, AnnotatedString("Разложите фрукты из случайного набора fruits по разным корзинам (Basket)"), 3, 52),

    PackSome(Chapter.Cycles, "Положите несколько", {
        Fruits.of("000333333555555555555").let { Triple(listOf(it), it, it.ordinals("9")) }
    }, buildAnnotatedString {
            append("Положите в корзину три яблока, шесть бананов и 12 ягод клубники. Используйте циклы с диапазоном:")
            withStyle(mono) { append("\nfor (i in 1..3) ...") }
        }, size = 50
    ),

    PackSomeMore(Chapter.Cycles, "Положите отдельно", {
        listOf(Fruits.of("222222222222222"), Fruits.of("4444"), Fruits.of("77")).let { f ->
            Triple(f, emptyList(), f.joinToString("") { it.ordinals("9") })
        }
    }, AnnotatedString("Разложите по разным корзинам 15 черешен, четыре лимона и два арбуза"), size = 55),

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Reversed(Chapter.Lists, "Задом наперёд (reversed)", {
        Fruits.random(8).let { Triple(listOf(it), it.reversed(), it.ordinals("9")) }
    }, buildAnnotatedString {
        append("Положите в корзину все фрукты случайного набора в обратном порядке:")
        withStyle(mono) { append("\nfruits.reversed().forEach...") }
    }, 3),

    FirstLast(Chapter.Lists, "Первый и последний (first, last)", {
        Fruits.random(Random.nextInt(5,16)).let {
            Triple(listOf(listOf(it.first(), it.last())), it, "9"+it.first().ordinal+it.last().ordinal)
        }
    }, buildAnnotatedString {
        append("Положите в корзину только первый и последний фрукты:")
        withStyle(mono) { append("\nfruits.first()()\nfruits.last()()") }
    }, 3, 200),

    TakeFirst(Chapter.Lists, "Первые пять (take)", {
        Fruits.random(Random.nextInt(5,16)).let {
            Triple(listOf(it.take(5)), it, it.take(5).ordinals("9"))
        }
    }, buildAnnotatedString {
        append("Положите в корзину только первые пять фруктов:")
        withStyle(mono) { append("\nfruits.take(5).forEach...") }
    }, 3, 150),

    TakeLast(Chapter.Lists, "Последние три (takeLast)", {
        Fruits.random(Random.nextInt(5,16)).let {
            Triple(listOf(it.takeLast(3)), it, it.takeLast(3).ordinals("9"))
        }
    }, AnnotatedString("Положите в корзину только последние (takeLast) три фрукта"), 3, 150),

    DropFirst(Chapter.Lists, "Убрать первые четыре (drop)", {
        Fruits.random(Random.nextInt(5,16)).let {
            Triple(listOf(it.drop(4)), it, it.drop(4).ordinals("9"))
        }
    }, buildAnnotatedString {
        append("Заберите первые четыре фрукта себе, остальные положите в корзину:")
        withStyle(mono) { append("\nfruits.drop(4).forEach...") }
    }, 3, 75),

    DropLast(Chapter.Lists, "Убрать последние два (dropLast)", {
        Fruits.random(Random.nextInt(5,16)).let {
            Triple(listOf(it.dropLast(2)), it, it.dropLast(2).ordinals("9"))
        }
    }, AnnotatedString("Положите в корзину все фрукты, кроме двух последних (dropLast)"), 3, 65),

    TakeWhile(Chapter.Lists, "Работаем до первого арбуза (takeWhile)", {
        Fruits.random(Random.nextInt(10,16)).let { f ->
            Triple(listOf(f.takeWhile {  it != Fruit.Watermelon }), f,
                f.takeWhile { it != Fruit.Watermelon }.ordinals("9"))
        }
    }, buildAnnotatedString {
        append("Кладите в корзину фрукты, пока не попадётся первый арбуз:")
        withStyle(mono) { append("\nfruits.takeWhile { it != Fruit.Watermelon }.forEach...") }
    }, 3, 55),

    DropWhile(Chapter.Lists, "Ждём, пока не найдём первый лимон (dropWhile)", {
        Fruits.random(Random.nextInt(10,16)).let { f ->
            Triple(listOf(f.dropWhile { it != Fruit.Lemon }), f, f.dropWhile { it != Fruit.Lemon }.ordinals("9"))
        }
    }, AnnotatedString("Отложите в сторону все фрукты, пока не найдёте лимон, остальное положите в корзину (dropWhile)"), 3, 65),

    Filter(Chapter.Lists, "Ну не люблю я бананы... (filter)", {
        Fruits.random(Random.nextInt(10,16)).let { f ->
            Triple(listOf(f.filter { it != Fruit.Banana }), f, f.filter { it != Fruit.Banana }.ordinals("9"))
        }
    }, AnnotatedString("Положите в корзину все фрукты, кроме бананов (filter)"), 3, 65),

    FilterMore(Chapter.Lists, "Зато люблю клубнику и черешню (filter)", {
        Fruits.random(Random.nextInt(10,16)).let { f ->
            Triple(listOf(f.filter { it == Fruit.Strawberry || it == Fruit.Cherry }), f,
                f.filter { it == Fruit.Strawberry || it == Fruit.Cherry }.ordinals("9"))
        }
    }, buildAnnotatedString {
        append("Положите в корзину только клубнику и черешню, остальное отдайте детям. Подсказка:")
        withStyle(mono) { append("\nit == Fruit.Strawberry || it ==...") }
    }, 3),

    FilterBy(Chapter.Lists, "И, вообще, все красные фрукты (filter)", {
        Fruits.random(Random.nextInt(10,16)).let { f ->
            Triple(listOf(f.filter { it.color == Color.Red }), f, f.filter { it.color == Color.Red }.ordinals("9"))
        }
    }, AnnotatedString("Положите в корзину все красные фрукты (it.color == Color.Red)"), 3, 65),

    Sorted(Chapter.Lists, "Упорядочивание фруктов (sorted)", {
        Fruits.random(Random.nextInt(10,16)).let { f ->
            Triple(listOf(f.sorted()), f, f.sorted().ordinals("9"))
        }
    }, buildAnnotatedString {
        append("Отсортируйте фрукты в наборе согласно их номеру по прейскуранту (порядок как в задании №2):")
        withStyle(mono) { append("\nfruits.sorted()...") }
    }, 3, 55),

    SortedBy(Chapter.Lists, "Упорядочивание фруктов по цвету (sortedBy)", {
        Fruits.random(Random.nextInt(10,16)).let { f ->
            Triple(listOf(f.sortedBy { it.color.value }), f, f.sortedBy { it.color.value }.ordinals("9"))
        }
    }, AnnotatedString("Отсортируйте фрукты в наборе по цвету (sortedBy, сравнивайте их it.color.value)"), 3, 55),

    SortedByDescending(Chapter.Lists, "Обратный порядок по размеру (sortedByDescending)", {
        Fruits.random(Random.nextInt(10,16)).let { f ->
            Triple(listOf(f.sortedByDescending { it.size }), f, f.sortedByDescending { it.size }.ordinals("9"))
        }
    }, AnnotatedString("Положите фрукты в корзину, чтобы они не подавились, сначала крупные, в конце – мелкие\n" +
            "(sortedByDescending, сравнивайте их it.size)"), 3, 55),

    Distinct(Chapter.Lists, "Всё по одной штучке, пожалуйста (distinct)", {
        Fruits.random(Random.nextInt(10,16)).let {
            Triple(listOf(it.distinct()), it, it.distinct().ordinals("9"))
        }
    }, buildAnnotatedString {
        append("Положите в корзину по одному фрукту каждого вида из исходного набора:")
        withStyle(mono) { append("\nfruits.distinct()...") }
    }, 3),

    DistinctBy(Chapter.Lists, "По одной каждого цвета, пожалуйста (distinctBy)", {
        Fruits.random(Random.nextInt(10,16)).let { f ->
            Triple(listOf(f.distinctBy { it.color }), f, f.distinctBy { it.color }.ordinals("9"))
        }
    }, buildAnnotatedString {
        append("Положите в корзину по одному первому фрукту каждого цвета из исходного набора:")
        withStyle(mono) { append("\nfruits.distinctBy { it.color }...") }
    }, 3, 200),

    Group(Chapter.Lists, "Все фрукты по разным корзинам (groupBy)", {
        Fruits.random(Random.nextInt(10,16)).let { fruits ->
            Triple(fruits.groupBy { it }.map { it.value }, fruits,
                fruits.groupBy { it }.values.joinToString("") { f -> f.ordinals("9") })
        }
    }, buildAnnotatedString {
        append("Разложите фрукты каждого вида по разным корзинам:")
        withStyle(mono) { append("\nfruits.groupBy { it }...\n{ ...it.value.forEach{...} }") }
    }, 3, 27),

    GroupBy(Chapter.Lists, "Все цвета по разным корзинам (groupBy)", {
        Fruits.random(Random.nextInt(10,16)).let { fruits ->
            Triple(fruits.groupBy { it.color }.map { it.value }, fruits,
                fruits.groupBy { it.color }.values.joinToString("") { f -> f.ordinals("9") })
        }
    }, AnnotatedString("Разложите фрукты каждого цвета по разным корзинам:"), 3, 73),

    Partition(Chapter.Lists, "Арбузы – отдельно (partition)", {
        Fruits.random(Random.nextInt(10,16)).let { fruits ->
            Triple(fruits.partition { it.size == Size.Big }.toList(), fruits,
                fruits.partition { it.size == Size.Big }.toList().joinToString("") { f -> f.ordinals("9") })
        }
    }, buildAnnotatedString {
        append("Положите арбузы в одну корзину, а остальные фрукты – в другую:")
        withStyle(mono) { append("\nfruits.partition { it.size == Size.Big }.toList()...") }
    }, 3, 55),

    Map(Chapter.Lists, "Всё на клубнику (map)", {
        Fruits.random(Random.nextInt(10,16)).let {
            Triple(listOf(List(it.size) { Strawberry }), it, List(it.size) { Strawberry }.ordinals("9"))
        }
    }, buildAnnotatedString {
        append("Поменяйте все фрукты на клубнику:")
        withStyle(mono) { append("\nfruits.map { Strawberry }...") }
    }, 3, 55),

    Add(Chapter.Lists, "И ещё яблочко (+)", {
        Fruits.random(7).filter { it != Apple }.let {
            Triple(listOf(it + Apple), it, (it + Apple).ordinals("9"))
        }
    }, buildAnnotatedString {
        append("Добавьте к набору фруктов одно своё яблоко:")
        withStyle(mono) { append("\n(fruits + Apple)...") }
    }, 3),

    AddAll(Chapter.Lists, "И ещё немного черешни (+)", {
        Fruits.random(7).let {
            Triple(listOf(it + List(8){ Cherry }), it, it.ordinals("9") + "22222222")
        }
    }, buildAnnotatedString {
        append("Добавьте к набору фруктов ещё 8 черешен:")
        withStyle(mono) { append("\n(fruits + List(8){ Cherry })...") }
    }, 3, 55),

    Double(Chapter.Lists, "Удвоить набор (+)", {
        Fruits.random(Random.nextInt(5,8)).let { Triple(listOf(it+it), it, (it+it).ordinals("9")) }
    }, AnnotatedString("Удвойте набор фруктов"), 3, 55),

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    IsNotEmpty(Chapter.Conditions,"Узнать, что набор не пуст (isNotEmpty)", {
        Fruits.random(Random.nextInt(3)).let {
            val c = if (it.isNotEmpty()) Apple else Nectarine
            Triple(listOf(listOf(c)), it, "9${c.ordinal}")
        }
    }, buildAnnotatedString {
        append("Если в исходном наборе есть фрукты, бросьте в корзину яблоко.\n")
        append("Если исходный набор пуст, бросьте в корзину нектарин. Как проверить:")
        withStyle(mono) { append("\nif (fruits.isNotEmpty()) ... else ...") }
    }, 7, 200),

    CompSize(Chapter.Conditions,"Проверить размер набора (size)", {
        Fruits.random(Random.nextInt(3, 8)).let {
            val c = if (it.size >= 5) Apple else Nectarine
            Triple(listOf(listOf(c)), it, "9${c.ordinal}")
        }
    }, AnnotatedString(
        "Если в исходном наборе есть хотя бы 5 фруктов (fruits.size), бросьте в корзину яблоко, иначе нектарин"
    ), 7, 200),

    CompFirst(Chapter.Conditions,"Проверить первый элемент (first)", {
        Fruits.random(Random.nextInt(6)).let {
            val c = if (it.firstOrNull() != Banana) Apple else Nectarine
            Triple(listOf(listOf(c)), it, "9${c.ordinal}")
        }
    }, AnnotatedString(
        "Если первый (first) фрукт в наборе не банан, бросьте в корзину яблоко, иначе нектарин\n\n" +
        "Если возникает ошибка «Список пуст», попробуйте заменить first на firstOrNull"), 7, 200),

    FirstLastColor(Chapter.Conditions,"Сравнить первый и последний (first, last)", {
        Fruits.random(Random.nextInt(3, 8)).let {
            val c = if (it.firstOrNull()?.color == it.lastOrNull()?.color) Apple else Nectarine
            Triple(listOf(listOf(c)), it, "9${c.ordinal}")
        }
    }, AnnotatedString(
        "Если первый и последний фрукты в наборе одного цвета, бросьте в корзину яблоко, иначе нектарин\n\n" +
        "Если возникает ошибка «Список пуст», попробуйте использовать firstOrNull()? и lastOrNull()?"), 7, 200),

    Contains(Chapter.Conditions,"Присутствие в наборе (contains)", {
        Fruits.random(Random.nextInt(8, 16)).let {
            val c = if (it.contains(Banana)) Apple else Nectarine
            Triple(listOf(listOf(c)), it, "9${c.ordinal}")
        }
    }, buildAnnotatedString {
        append("Если в наборе есть хоть один банан, бросьте в корзину яблоко, иначе нектарин:")
        withStyle(mono) { append("\nfruits.contains(Banana)") }
    }, 7, 200),

    In(Chapter.Conditions,"Присутствие в наборе (in)", {
        Fruits.random(Random.nextInt(8, 16)).let {
            val c = if (Banana in it) Apple else Nectarine
            Triple(listOf(listOf(c)), it, "9${c.ordinal}")
        }
    }, buildAnnotatedString {
        append("Та же задача, но ещё более простой способ:")
        withStyle(mono) { append("\nBanana in fruits") }
    }, 7, 200),

    Any(Chapter.Conditions,"Наличие в наборе (any)", {
        Fruits.random(Random.nextInt(8, 16)).let {
            val c = if (it.any { f -> f == Banana }) Apple else Nectarine
            Triple(listOf(listOf(c)), it, "9${c.ordinal}")
        }
    }, buildAnnotatedString {
        append("Та же задача, третий способ решения:")
        withStyle(mono) { append("\nfruits.any { it == Banana }") }
    }, 7, 200),

    None(Chapter.Conditions,"Отсутствие в наборе (none)", {
        Fruits.random(Random.nextInt(8, 16)).let {
            val c = if (it.none { f -> f == Banana }) Apple else Nectarine
            Triple(listOf(listOf(c)), it, "9${c.ordinal}")
        }
    }, AnnotatedString(
        "Если в наборе нет ни одного банана (none), бросьте в корзину яблоко, иначе нектарин"
    ), 7, 200),

    All(Chapter.Conditions,"Полное заполнение набора (all)", {
        (Fruits.random(Random.nextInt(3)) + List(Random.nextInt(1,4)) { Banana }).let {
            val c = if (it.all { f -> f == Banana }) Apple else Nectarine
            Triple(listOf(listOf(c)), it, "9${c.ordinal}")
        }
    }, AnnotatedString(
        "Если весь набор состоит из одних бананов (all), бросьте в корзину яблоко, иначе нектарин"
    ), 7, 200),

    Count(Chapter.Conditions,"Количество в наборе (count)", {
        Fruits.random(Random.nextInt(12, 16)).let {
            val c = if (it.count { f -> f == Banana } == 1) Apple else Nectarine
            Triple(listOf(listOf(c)), it, "9${c.ordinal}")
        }
    }, AnnotatedString(
        "Если в наборе есть ровно один банан (count), бросьте в корзину яблоко, иначе нектарин.\n\n" +
        "Подсказка: сравните результат функции с единицей"
    ), 7, 200),

    Yellow(Chapter.Conditions,"Количество жёлтых фруктов (самостоятельно)", {
        Fruits.random(Random.nextInt(12, 16)).let {
            val f = List(it.count { f -> f.color == Color.Yellow }) { Apple }
            Triple(listOf(f), it, f.ordinals("9"))
        }
    }, AnnotatedString("Бросьте в корзину столько яблок, сколько всего жёлтых фруктов в исходном наборе"), 7, 55),

    MinMax(Chapter.Conditions,"Наименьший и наибольший по номеру (min, max)", {
        Fruits.random(Random.nextInt(3, 8)).let {
            Triple(listOf(listOf(it.min(), it.max())), it, listOf(it.min(), it.max()).ordinals("9"))
        }
    }, AnnotatedString("Положите в корзину наименьший и наибольший по номеру в прейскуранте фрукты.\n\n" +
            "Используйте функции-расширения .min() и .max(), добавьте ещё одни () для помещения фрукта в корзину"), 7, 200),

    MinMaxBy(Chapter.Conditions,"Наименьший и наибольший по ... (minBy, maxBy)", {
        Fruits.random(Random.nextInt(3, 8)).let { f ->
            Triple(listOf(listOf(f.minBy { it.size }, f.maxBy { it.size })), f,
                listOf(f.minBy { it.size }, f.maxBy { it.size }).ordinals("9"))
        }
    }, AnnotatedString("Положите в корзину наименьший (minBy) и наибольший (maxBy) по размеру (it.size) фрукты.\n\n" +
            "Добавьте ещё одни () после блока {...} функции-расширения для помещения фрукта в корзину"), 7, 200),

    ;
    companion object {
        val values: List<Lessons> = values().toList()
        fun of(ordinal: Int) = values[ordinal]
    }
}

private val mono = androidx.compose.ui.text.SpanStyle(
    fontSize = 20.sp,
    fontFamily = androidx.compose.ui.text.font.FontFamily.Companion.Monospace
)

internal val answer = mutableStateOf("")
internal val condition = mutableStateListOf<Fruit>()
internal val menu = mutableStateListOf<List<Fruit>>()