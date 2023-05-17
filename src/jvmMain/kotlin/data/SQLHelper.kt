package data

import java.lang.reflect.Field
import java.sql.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

abstract class GenericSQL {
    protected abstract val connection: Connection
    protected abstract val types: Map<String, String>
    private val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    private fun PreparedStatement.apply(params: List<Any?>) = this.apply {
        params.forEachIndexed { index, param ->
            when (param) {
                null -> setString(index + 1, null)
                is Int -> setInt(index + 1, param)
//                is Long -> setLong(index + 1, param)
//                is Float -> setFloat(index + 1, param)
//                is Double -> setDouble(index + 1, param)
                is String -> setString(index + 1, param)
//                is Boolean -> setBoolean(index + 1, param)
//                is Date -> setDate(index + 1, param)
//                is Time -> setTime(index + 1, param)
                is LocalDate -> setString(index + 1, param.format(dateFormat))
                is LocalTime -> setString(index + 1, param.format(timeFormat))
//                is BigDecimal -> setBigDecimal(index + 1, param)
                else -> setString(index + 1, param.toString())
            }
        }
    }

    open fun update(sql: String): Int =
        connection.createStatement().executeUpdate(sql)
    open fun update(sql: String, params: List<Any?>): Int =
        connection.prepareStatement(sql).apply(params).executeUpdate()
    open fun update(sql: String, vararg params: Any?): Int =
        update(sql, params.toList())

    open fun select(sql: String): Result =
        Result(connection.createStatement().executeQuery(sql))
    open fun select(sql: String, params: List<Any?>): Result =
        Result(connection.prepareStatement(sql).apply(params).executeQuery())
    open fun select(sql: String, vararg params: Any?): Result =
        select(sql, params.toList())

    open fun insert(table: String, fields: List<String>, params: List<Any?>): Int = update(
        "insert into $table (" + fields.joinToString() +
                ") values (" + List(params.size) { '?' }.joinToString() + ")", params
    )
    open fun insert(table: String, fields: String, vararg params: Any?): Int = update(
        "insert into $table ($fields) values (" +
                List(params.size) { '?' }.joinToString() + ")", params.toList()
    )

    open fun create(
        struct: Class<out Any>, ifNotExists: Boolean = true,
        name: String = struct.simpleName,
        key: String? = null, auto: Boolean = true,
        required: List<String> = emptyList()
    ) = connection.createStatement().executeUpdate(
        "create table " + (if (ifNotExists) "if not exists " else "") + name +
                struct.declaredFields.filter {
                    !it.name.startsWith('$')
                }.joinToString(prefix = " (", postfix = ")") {
                    it.columnDescription(key, auto, required)
                }
    )

    open fun alter(
        struct: Class<out Any>,
        name: String = struct.simpleName,
        fields: List<String> = listOf(),
        key: String? = null, auto: Boolean = true,
        required: List<String> = emptyList()
    ) = struct.declaredFields.filter {
        !it.name.startsWith('$') && (fields.isEmpty() || it.name in fields)
    }.forEach {
        val column = it.columnDescription(key, auto, required)
        try {
            connection.createStatement().executeUpdate("alter table $name add column $column")
        } catch (_: Exception) {}
    }

    private fun Field.columnDescription(key: String?, auto: Boolean, required: List<String>) =
        name + ' ' + (types[type.name] ?: type.simpleName) +
            (if (name == key || name in required) " not null" else " null") +
            if (name == key) " primary key" + (if (auto) " autoincrement" else "") else ""

    class Result(private val result: ResultSet) {
        fun next() = result.next().also { if (!it) result.close() }
        fun close() = result.close()
        fun rows() = size.also { close() }
        val size: Int get() = result.metaData.columnCount
//        fun int(index: Int): Int = result.getInt(index)
//        fun long(index: Int): Long = result.getLong(index)
//        fun float(index: Int): Float = result.getFloat(index)
//        fun double(index: Int): Double = result.getDouble(index)
//        fun string(index: Int): String? = result.getString(index)
//        fun boolean(index: Int): Boolean = result.getBoolean(index)
//        fun date(index: Int): Date? = result.getDate(index)
//        fun time(index: Int): Time? = result.getTime(index)
//        fun bigDecimal(index: Int): BigDecimal? = result.getBigDecimal(index)
//
//        fun strings() = List(result.metaData.columnCount) {
//            result.getString(it+1)
//        }

        @Suppress("UNCHECKED_CAST")
        fun<T> get(struct: Class<out T>): T {
            val members = struct.declaredFields.filter { !it.name.startsWith('$') }
            val par = Array(members.size) {
                when (members[it].type.name) {
                    "int" -> result.getInt(it+1)
//                    "long" -> result.getLong(it+1)
//                    "float" -> result.getFloat(it+1)
//                    "double" -> result.getDouble(it+1)
//                    "boolean" -> result.getBoolean(it+1)
                    "java.lang.String" -> result.getString(it+1)
                    "java.lang.Integer" -> result.getInt(it+1).takeIf { !result.wasNull() }
//                    "java.lang.Long" -> result.getLong(it+1).takeIf { !result.wasNull() }
//                    "java.lang.Float" -> result.getFloat(it+1).takeIf { !result.wasNull() }
//                    "java.lang.Double" -> result.getDouble(it+1).takeIf { !result.wasNull() }
//                    "java.lang.Boolean" -> result.getBoolean(it+1).takeIf { !result.wasNull() }
//                    "java.sql.Date" -> result.getDate(it+1)
//                    "java.sql.Time" -> result.getTime(it+1)
                    "java.time.LocalDate" -> LocalDate.parse(result.getString(it+1))
                    "java.time.LocalTime" -> LocalTime.parse(result.getString(it+1))
//                    "java.math.BigDecimal" -> result.getBigDecimal(it+1)
                    else -> result.getString(it+1)
                }
            }
            return struct.declaredConstructors.first().newInstance(*par) as T
        }

        fun<T> list(struct: Class<out T>): List<T> {
            val result = mutableListOf<T>()
            while (next()) result += get(struct)
            return result
        }
    }
}

class SQLite(dbName: String) : GenericSQL() {
    override val connection: Connection
    override val types = mapOf(
        "int" to "integer",
//        "long" to "integer",
//        "float" to "real",
//        "double" to "real",
//        "boolean" to "integer",
        "java.lang.String" to "text",
        "java.lang.Integer" to "integer",
//        "java.lang.Long" to "integer",
//        "java.lang.Float" to "real",
//        "java.lang.Double" to "real",
//        "java.lang.Boolean" to "integer",
//        "java.sql.Date" to "text",
//        "java.sql.Time" to "text",
        "java.time.LocalDate" to "text",
        "java.time.LocalTime" to "text",
//        "java.math.BigDecimal" to "text",
    )

    init {
        Class.forName("org.sqlite.JDBC")
        connection = DriverManager.getConnection("jdbc:sqlite:$dbName.s3db")
    }

    private fun lastRowId() = connection.createStatement()
        .executeQuery("SELECT last_insert_rowid()").run {
            getInt(1).also { close() }
        }

    override fun insert(table: String, fields: List<String>, params: List<Any?>): Int =
        if (super.insert(table, fields, params) > 0) lastRowId() else 0

    override fun insert(table: String, fields: String, vararg params: Any?): Int =
        if (super.insert(table, fields, *params) > 0) lastRowId() else 0
}