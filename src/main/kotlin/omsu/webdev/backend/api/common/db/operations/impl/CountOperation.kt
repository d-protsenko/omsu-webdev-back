package omsu.webdev.backend.api.common.db.operations.impl

import com.fasterxml.jackson.databind.ObjectMapper
import omsu.webdev.backend.api.common.db.Parameters
import omsu.webdev.backend.api.common.db.operations.ICountOperation
import omsu.webdev.backend.api.common.db.operations.IQueryFilter
import omsu.webdev.backend.api.common.db.operations.QueryHelper
import omsu.webdev.backend.api.common.db.operations.ReadDataFromDatabaseException
import org.hibernate.type.SerializationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcOperations
import java.sql.ResultSet
import java.util.*
import java.util.function.Consumer


class CountOperation<M>(
    private val jdbcOperations: JdbcOperations,
    private val objectMapper: ObjectMapper,
    sqlScript: String,
    clazz: Class<M>?,
    arguments: MutableList<String>?
) : ICountOperation {
    private val sqlQuery: String
    private var arguments: MutableList<String>? = null

    override fun count(parameters: Parameters?): Int? {
        val queryParameters: Parameters = Parameters
            .builder()
            .add("query", sqlQuery)
            .add("replaceable_tag", FILTERING_TAG)
            .add("required_filters", parameters?.get("required_filters"))
            .build()
        updateParametersByArguments(parameters!!, queryParameters)
        val query: String? = QueryHelper.addFiltersToQuery(queryParameters)
        return try {
            jdbcOperations.queryForObject(
                query!!,
                buildArguments(queryParameters)
            ) { rs: ResultSet, i: Int ->
                try {
                    return@queryForObject objectMapper.readValue(
                        rs.getString(1),
                        Int::class.java
                    )
                } catch (e: Exception) {
                    throw SerializationException("Unable to deserialize ...", e)
                }
            }
        } catch (e: EmptyResultDataAccessException) {
            0
        } catch (e: Exception) {
            throw ReadDataFromDatabaseException(e)
        }
    }

    private fun updateParametersByArguments(source: Parameters, target: Parameters) {
        arguments?.forEach(
            Consumer { arg: String ->
                if (arg != "filters" || null == target[arg]) {
                    source.get<Any>(arg)?.let { target[arg] = it }
                }
            }
        )
    }

    private fun buildArguments(parameters: Parameters): Array<Any?> {
        val arguments: MutableList<Any?> = ArrayList()
        this.arguments!!.forEach(
            Consumer { index: String ->
                if (index == "filters") {
                    val filters: IQueryFilter? = parameters["filters"]
                    filters?.arguments?.let { arguments.addAll(it) }
                } else {
                    parameters.get<Any>(index)?.let {
                        arguments.add(it)
                    }
                }
            }
        )
        return arguments.toTypedArray()
    }

    companion object {
        private const val FILTERING_TAG = "%FILTERING%"
    }

    init {
        sqlQuery = sqlScript
        if (null != arguments && arguments.isNotEmpty()) {
            this.arguments = arguments
        } else {
            val args: ArrayList<String> = ArrayList()
            args.add("filters")
            this.arguments = args
        }
    }
}
