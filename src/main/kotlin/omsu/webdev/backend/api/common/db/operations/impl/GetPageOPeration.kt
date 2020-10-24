package omsu.webdev.backend.api.common.db.operations.impl

import com.fasterxml.jackson.databind.ObjectMapper
import omsu.webdev.backend.api.common.db.Paged
import omsu.webdev.backend.api.common.db.Parameters
import omsu.webdev.backend.api.common.db.operations.IGetPageOperation
import omsu.webdev.backend.api.common.db.operations.IQueryFilter
import omsu.webdev.backend.api.common.db.operations.QueryHelper
import omsu.webdev.backend.api.common.db.operations.ReadDataFromDatabaseException
import org.hibernate.type.SerializationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.*
import java.util.function.Consumer
import kotlin.math.ceil

class GetPageOperation<M>(
        private val jdbcTemplate: JdbcTemplate,
        private val objectMapper: ObjectMapper,
        sqlScript: String,
        private val clazz: Class<M>,
        arguments: MutableList<String>?,
        rowMapper: RowMapper<M>? = null
) : IGetPageOperation<M> {
    private val sqlQuery: String = sqlScript
    private var arguments: MutableList<String>? = null
    private var rowMapper: RowMapper<M>? = null
    private val FILTERING_TAG = "%FILTERING%"

    init {
        if (null != arguments && arguments.isNotEmpty()) {
            this.arguments = arguments
        } else {
            val args: ArrayList<String> = ArrayList()
            args.add("filters")
            args.add("pagination")
            args.add("criteria")
            this.arguments = args
        }
        if (null != rowMapper) {
            this.rowMapper = rowMapper
        } else {
            this.rowMapper = RowMapper { rs: ResultSet, i: Int ->
                try {
                    return@RowMapper objectMapper.readValue(
                            rs.getString(1),
                            this.clazz
                    )
                } catch (e: Exception) {
                    throw SerializationException("Cannot deserialize class", e)
                }
            }
        }
    }

    fun getEntity(parameters: Parameters?): M? {
        val queryParameters = Parameters.builder()
            .add("query", sqlQuery)
            .add("replaceable_tag", FILTERING_TAG)
            .add("required_filters", parameters?.get("required_filters")!!)
            .build()
        return try {
            updateParametersByArguments(parameters!!, queryParameters)
            val query: String? = QueryHelper.addFiltersToQuery(queryParameters)
            jdbcTemplate.queryForObject(
                    sqlQuery,
                    buildArguments(queryParameters),
                    rowMapper!!
            )!!
        } catch (e: EmptyResultDataAccessException) {
            null
        } catch (e: Exception) {
            throw ReadDataFromDatabaseException(e)
        }
    }

    override fun getPage(parameters: Parameters?): Paged<M>? {
        return try {
            val total: Int = parameters?.get("total")!!
            val pagination: Pageable = parameters["pagination"]!!
            val query: String = sqlQuery
            val queryParameters = Parameters.builder()
                .add("query", query)
                .add("replaceable_tag", FILTERING_TAG)
                .add("required_filters", parameters.get<IQueryFilter>("required_filters"))
                .build()
            updateParametersByArguments(parameters, queryParameters)
            val queryWithFilters: String? = QueryHelper.addFiltersToQuery(queryParameters)
            val list: List<M> = jdbcTemplate.query(
                    queryWithFilters!!,
                    buildArguments(queryParameters),
                    rowMapper!!
            )
            val totalPages = ceil(total.toDouble() / pagination.pageSize.toDouble())
            Paged(list, totalPages.toInt(), total)
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
                    when (index) {
                        "filters" -> {
                            val filters: IQueryFilter? = parameters["filters"]
                            filters?.arguments?.let { arguments.addAll(it) }
                        }
                        "pagination" -> {
                            parameters.get<Pageable>("pagination")?.let {
                                arguments.add(it.pageSize)
                                arguments.add(it.offset)
                            }
                        }
                        else -> {
                            parameters.get<Any>(index)?.let {
                                arguments.add(it)
                            }
                        }
                    }
                }
        )
        return arguments.toTypedArray()
    }
}
