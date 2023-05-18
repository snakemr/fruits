import androidx.compose.ui.graphics.Color
import fruits.*

fun main() = mainUI { lesson, input ->
        when (lesson) {
            Lessons.About -> Basket { Apple() }
            Lessons.More -> Basket { Fruits.all.forEach { it() } }
            Lessons.Baskets -> Store {
                Basket { Cherry() ; Strawberry() ; Nectarine() }
                Basket { Pear() ; Banana() ; Lemon() }
                Basket { Apple() ; Watermelon() }
            }
            ///////
            Lessons.AllFruits -> Basket { for (fruit in Fruits.all) { fruit() } }
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
            ///////
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
            ///////
            Lessons.IsNotEmpty -> Basket { if (input.isNotEmpty()) Apple() else Nectarine() }
            Lessons.CompSize -> Basket { if (input.size >= 5) Apple() else Nectarine() }
            Lessons.CompFirst -> Basket { if (input.firstOrNull() != Banana) Apple() else Nectarine() }
            Lessons.FirstLastColor -> Basket {
                if (input.firstOrNull()?.color == input.lastOrNull()?.color) Apple() else Nectarine()
            }
            Lessons.Contains -> Basket { if (input.contains(Banana)) Apple() else Nectarine() }
            Lessons.In -> Basket { if (Banana in input) Apple() else Nectarine() }
            Lessons.Any -> Basket { if (input.any { it == Banana }) Apple() else Nectarine() }
            Lessons.None -> Basket { if (input.none { it == Banana }) Apple() else Nectarine() }
            Lessons.All -> Basket { if (input.all { it == Banana }) Apple() else Nectarine() }
            Lessons.Count -> Basket { if (input.count { it == Banana } == 1) Apple() else Nectarine() }
            Lessons.Yellow -> Basket { repeat (input.count { it.color == Color.Yellow }) { Apple() } }
            Lessons.MinMax -> Basket { input.min()() ; input.max()() }
            Lessons.MinMaxBy -> Basket { input.minBy { it.size }() ; input.maxBy { it.size }() }
            Lessons.Minus -> Basket { (input - Apple - Cherry - Cherry).forEach { it() } }
            Lessons.ReplaceBanana -> Basket { input.forEach { if (it == Banana) Strawberry() else it() } }
            Lessons.ReplaceYellow -> Basket { input.forEach { if (it.color == Color.Yellow) Strawberry() else it() } }
            Lessons.Odd -> Basket { input.forEachIndexed { index, fruit -> if (index%2 == 0) fruit() } }
            Lessons.EvenRed -> Basket {
                input.forEachIndexed { index, fruit -> if (index%2 != 0 && fruit.color == Color.Red) fruit() }
            }
            Lessons.YellowOdd -> Basket {
                input.filter { it.color == Color.Yellow } .forEachIndexed { index, fruit -> if (index%2 == 0) fruit() }
            }
            else -> {}
        }
}
