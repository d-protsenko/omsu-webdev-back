package omsu.webdev.backend.api.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import omsu.webdev.backend.api.common.db.operations.impl.GetOperation
import omsu.webdev.backend.api.common.db.operations.impl.GetPageOperation
import omsu.webdev.backend.api.common.db.Paged
import omsu.webdev.backend.api.common.db.Parameters
import omsu.webdev.backend.api.common.db.operations.CaseSensitiveFilter
import omsu.webdev.backend.api.common.db.operations.Filters
import omsu.webdev.backend.api.common.db.operations.ICountOperation
import omsu.webdev.backend.api.common.db.operations.IGetOperation
import omsu.webdev.backend.api.common.db.operations.IGetPageOperation
import omsu.webdev.backend.api.common.db.operations.IInsertOperation
import omsu.webdev.backend.api.common.db.operations.impl.CountOperation
import omsu.webdev.backend.api.common.db.operations.impl.InsertOperation
import omsu.webdev.backend.api.models.domain.LoggingInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.Instant


@Repository
class LoggingInfoRepository(
    @Autowired var template: JdbcTemplate,
    @Autowired var objectMapper: ObjectMapper
) {
    private var getInfo: IGetOperation<LoggingInfo> = GetOperation(
        template,
        objectMapper,
        "select data from logging_info %FILTERING%",
        LoggingInfo::class.java,
        null,
        null
    )
    private var countInfo: ICountOperation = CountOperation(
        template,
        objectMapper,
        "select count(*) from logging_info %FILTERING%",
        Int::class.java,
        null
    )
    private var getInfoPaged: IGetPageOperation<LoggingInfo> = GetPageOperation(
        template,
        objectMapper,
        "select data from logging_info %FILTERING% order by logging_info.data->>'updatedAt' DESC limit ? offset ?",
        LoggingInfo::class.java,
        null,
        null
    )
    private var insertInfo: IInsertOperation<LoggingInfo> = InsertOperation(
        template,
        objectMapper,
        "insert into logging_info (data) values ((?::jsonb))",
        "logging_info",
        null,
        null
    )

    fun findLatestPaged(parameters: Parameters): Paged<LoggingInfo>? {
        parameters.get<Instant>("since")?.let {
            parameters["filters"] = Filters("AND",
                listOf(CaseSensitiveFilter(
                    "((data->>'updatedAt')::timestamptz) >= (?::timestamptz)", it.toString())))
        }
        countInfo.count(parameters)?.let {
            parameters["total"] = it
        }
        return getInfoPaged.getPage(parameters)
    }

    fun insertInfo(parameters: Parameters, model: LoggingInfo) {
        this.insertInfo.create(parameters, model)
    }
}