package omsu.webdev.backend.api.common.db.operations

import lombok.Data
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream


interface IQueryFilter {
    fun build(): String?
    val arguments: Array<Any?>?
}

@Data
class CaseSensitiveFilter(
        private val expression: String?,
        private val argument: Any?
) : IQueryFilter {
    override fun build(): String? {
        return expression
    }

    override val arguments: Array<Any?>
        get() = if (null != argument) {
            arrayOf(argument)
        } else {
            emptyArray()
        }
}

@Data
class MultiArgumentCaseSensitiveFilter(
        private val expression: String? = null,
        private val argument: Array<Any?>?
) : IQueryFilter {

    override fun build(): String? {
        return expression
    }

    override val arguments: Array<Any?>?
        get() = if (null != argument && argument.isNotEmpty()) {
            argument
        } else arrayOf()
}

@Data
class Filters : IQueryFilter {
    private val filterRules: List<String?>
    private val filters: List<IQueryFilter>?

    constructor(filtersRule: String?, filters: List<IQueryFilter>?) {
        val rulesSize = if (filters != null && filters.isNotEmpty()) filters.size - 1 else 1
        filterRules = Stream.generate { filtersRule }.limit(rulesSize.toLong()).collect(Collectors.toList())
        this.filters = filters
    }

    constructor(filterRules: List<String?>, filters: List<IQueryFilter>?) {
        this.filterRules = filterRules
        this.filters = filters
    }

    private fun getFilterRule(index: Int): String {
        if (index == 0) {
            return ""
        }
        var rule: String? = AND_CONDITION
        if (filterRules.size >= index) {
            rule = filterRules[index - 1]
            rule = rule ?: AND_CONDITION
        }
        return rule!!.toLowerCase()
    }

    override fun build(): String {
        val value = StringBuilder()
        for ((index, filter) in filters!!.withIndex()) {
            when (val filterRule = getFilterRule(index)) {
                AND_CONDITION, OR_CONDITION -> if (index > 0) {
                    value
                            .append(WHITE_SPACE)
                            .append(filterRule)
                            .append(WHITE_SPACE)
                }
                else -> {
                }
            }
            if (filter is Filters) {
                value.append(LEFT_BRACKET)
                value.append(filter.build())
                value.append(RIGHT_BRACKET)
            } else {
                value.append(filter.build())
            }
        }
        return value.toString()
    }

    override val arguments: Array<Any?>?
        get() {
            val arguments: MutableList<Any> = ArrayList()
            filters!!.forEach { filter ->
                filter.arguments?.let { args ->
                    Arrays.stream(args).forEach {
                        it?.let {
                            arguments.add(it)
                        }
                    }
                }
            }
            return arguments.toTypedArray()
        }

    companion object {
        private const val AND_CONDITION = "and"
        private const val OR_CONDITION = "or"
        private const val LEFT_BRACKET = "("
        private const val RIGHT_BRACKET = ")"
        private const val WHITE_SPACE = " "
    }
}