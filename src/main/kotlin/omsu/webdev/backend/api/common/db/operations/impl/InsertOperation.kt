package omsu.webdev.backend.api.common.db.operations.impl

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import omsu.webdev.backend.api.common.db.Parameters
import omsu.webdev.backend.api.common.db.operations.DatabaseException
import omsu.webdev.backend.api.common.db.operations.DuplicateKeyOrCombinationOfKeysException
import omsu.webdev.backend.api.common.db.operations.IIndexedModel
import omsu.webdev.backend.api.common.db.operations.IInsertOperation
import omsu.webdev.backend.api.common.db.operations.ITwoArgsAction
import omsu.webdev.backend.api.common.db.operations.InsertEntityToDatabaseException
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.JdbcTemplate
import java.util.function.BiFunction
import java.util.*


class InsertOperation<M : IIndexedModel?>(
        jdbcTemplate: JdbcTemplate,
        private val objectMapper: ObjectMapper,
        sqlScript: String?,
        private val indexName: String?,
        normalizer: ITwoArgsAction<Parameters?, M>?,
        arguments: MutableList<String>?
) : ModifyingCommonOperation(jdbcTemplate, sqlScript), IInsertOperation<M> {
    private var normalizer: ITwoArgsAction<Parameters?, M> = object : ITwoArgsAction<Parameters?, M> {
        override fun execute(arg1: Parameters?, arg2: M) {

        }
    }
    private var idGenerator: BiFunction<Parameters?, M, String> = BiFunction { _, _ -> UUID.randomUUID().toString() }

    constructor(
            jdbcTemplate: JdbcTemplate,
            objectMapper: ObjectMapper,
            sqlScript: String?,
            indexName: String?,
            normalizer: ITwoArgsAction<Parameters?, M>?,
            arguments: MutableList<String>?,
            idGenerator: BiFunction<Parameters?, M, String>?
    ) : this(jdbcTemplate, objectMapper, sqlScript, indexName, normalizer, arguments) {
        if (null != idGenerator) {
            this.idGenerator = idGenerator
        }
    }

    override fun create(parameters: Parameters?, model: M) {
        try {
            val id = idGenerator.apply(parameters, model)
            model?.id = id
            normalizer.execute(parameters, model)
            val serializedModel = objectMapper.writeValueAsString(model)
            super.execute(parameters, serializedModel)
            indexName?.let {
                parameters?.set(it, id)
            }
        } catch (e: DatabaseException) {
            throw InsertEntityToDatabaseException(e)
        } catch (e: JsonProcessingException) {
            throw InsertEntityToDatabaseException(e)
        }
    }

    init {
        normalizer?.let { this.normalizer = it }
        var args: MutableList<String> = ArrayList()
        if (null != arguments) {
            args = arguments
        } else {
            args.add("model")
        }
        super.setArgumentList(args)
    }
}

abstract class ModifyingCommonOperation(
        private val jdbcTemplate: JdbcTemplate,
        private val sqlScript: String?
) {
    private var arguments: List<String>? = null
    fun execute(parameters: Parameters?, model: String) {
        val args = buildArguments(parameters, model)
        try {
            jdbcTemplate.update(
                    sqlScript!!,
                    *args
            )
        } catch (e: DuplicateKeyException) {
            throw DuplicateKeyOrCombinationOfKeysException("The entity with the same fields already exists", e)
        } catch (e: Exception) {
            throw DatabaseException(e)
        }
    }

    fun setArgumentList(argumentList: MutableList<String>?) {
        argumentList?.let {
            arguments = if (it.isNotEmpty()) {
                it
            } else {
                ArrayList()
            }
        }
    }

    private fun buildArguments(parameters: Parameters?, model: String): Array<Any> {
        val arguments: MutableList<Any> = ArrayList()
        this.arguments?.forEach { index: String ->
            if (index == "model") {
                arguments.add(model)
            } else {
                parameters?.get<Any>(index).let {
                    if (null != it) {
                        arguments.add(it)
                    }
                }
            }
        }
        return arguments.toTypedArray()
    }
}
