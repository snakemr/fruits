import androidx.compose.ui.graphics.Color
import fruits.*

fun main() = mainUI { lesson, input ->
    //Basket {
        when (lesson) {
            Lessons.About -> Basket { Apple() }
            Lessons.More -> Basket { Fruits.all.forEach { it() } }
            Lessons.Baskets -> Store {
                Basket { Cherry() ; Strawberry() ; Nectarine() }
                Basket { Pear() ; Banana() ; Lemon() }
                Basket { Apple() ; Watermelon() }
            }
            Lessons.All -> Basket { for (fruit in Fruits.all) { fruit() } }
            Lessons.Some -> Basket { for (fruit in input) { fruit() } }
            Lessons.SomeMore -> Basket { input.forEach { it() } }
            Lessons.Separate -> Store { input.forEach { Basket { it() } } }
            Lessons.PackSome -> Basket {
                (List(3) { Apple } + List(6) { Banana } + List(12) { Strawberry }).forEach { it() }
            }
            Lessons.PackSomeMore -> Store {
                Basket { repeat(15) { Cherry() } }
                Basket { repeat(4) { Lemon() } }
                Basket { repeat(2) { Watermelon() } }
            }
            Lessons.Reversed -> Basket { input.reversed().forEach { it() } }
            Lessons.FirstLast -> Basket { input.first()() ; input.last()() }
            Lessons.TakeFirst -> Basket { input.take(5).forEach { it() } }
            Lessons.TakeLast -> Basket { input.takeLast(3).forEach { it() } }
            Lessons.DropFirst -> Basket { input.drop(4).forEach { it() } }
            Lessons.DropLast -> Basket { input.dropLast(2).forEach { it() } }
            Lessons.TakeWhile -> Basket { input.takeWhile { it != Fruit.Watermelon }.forEach { it() } }
            Lessons.DropWhile -> Basket { input.dropWhile { it != Fruit.Lemon }.forEach { it() } }
            Lessons.Filter -> Basket { input.filter { it != Fruit.Banana }.forEach { it() } }
            Lessons.FilterMore -> Basket { input.filter { it == Fruit.Strawberry || it == Fruit.Cherry }.forEach { it() } }
            Lessons.FilterBy -> Basket { input.filter { it.color == Color.Red }.forEach { it() } }
            Lessons.Distinct -> Basket { input.distinct().forEach { it() } }
            Lessons.DistinctBy -> Basket { input.distinctBy { it.color }.forEach { it() } }
            Lessons.Sorted -> Basket { input.sorted().forEach { it() } }
            Lessons.SortedBy -> Basket { input.sortedBy { it.color.value }.forEach { it() } }
            Lessons.SortedByDescending -> Basket { input.sortedByDescending { it.size }.forEach { it() } }
            Lessons.Group -> Store { input.groupBy { it }.forEach { b -> Basket { b.value.forEach { f -> f() } } } }
            Lessons.GroupBy -> Store { input.groupBy { it.color }.forEach { b -> Basket { b.value.forEach { f -> f() } } } }
            Lessons.Partition -> Store {
                input.partition { it.size == Size.Big }.toList().forEach { b -> Basket { b.forEach { f -> f() } } }
            }
            Lessons.Map -> Basket { input.map { Strawberry }.forEach { it() } }
            Lessons.Add -> Basket { (input + Apple).forEach { it() } }
            Lessons.AddAll -> Basket { (input + List(8){ Cherry }).forEach { it() } }
            Lessons.Double -> Basket { (input + input).forEach { it() } }
            else -> {}
        }
    //}
}
