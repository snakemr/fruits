package data

import java.time.LocalDate
import java.time.LocalTime

private val data = SQLite("data")

data class Data(
    val id: Int,
    val lesson: Int,
    val date: LocalDate,
    val time: LocalTime,
    val percent: Int
)

data class Settings(
    val lesson: Int?,
    val name: String?,
    val login: String?,
    val password: String?
)

fun initData() {
    data.create(Data::class.java, name = "data", key = "id")
    data.create(Settings::class.java, name = "settings")
    data.alter(Settings::class.java, name = "settings", fields = listOf(Settings::login.name, Settings::password.name))
    if (settings() == null)
        data.insert("settings", "lesson, name", null as Int?, null as String?)
}

fun solutionLog() = data.select(
    "select * from data order by `date`, `time`"
).list(Data::class.java)

fun solution(lesson: Int) = data.select(
    "select * from data where lesson=$lesson"
).list(Data::class.java).lastOrNull()

fun solutions() = solutionLog().groupBy { it.lesson }.map { (_, value) ->
    value.maxBy { it.percent }
}

fun storePercentValue(lesson: Int, percent: Int) =
    data.insert("data", "lesson, `date`, `time`, percent", lesson, LocalDate.now(), LocalTime.now(), percent)

private fun settings() = data.select("select * from settings").list(Settings::class.java).firstOrNull()
val userName: String?
    get() = settings()?.name
    fun setUserName(name: String?, key: String) {
        if (key == "DJehQwEpAL") data.update("update settings set name=?", name)
    }
var currentLesson: Int?
    get() = settings()?.lesson
    set(value) { data.update("update settings set lesson=$value") }

//var lessonsLogin: String?
//    get() = settings()?.login
//    set(value) { data.update("update settings set login=?", value) }
//var lessonsPassword: String?
//    get() = settings()?.password
//    set(value) { data.update("update settings set password=?", value) }