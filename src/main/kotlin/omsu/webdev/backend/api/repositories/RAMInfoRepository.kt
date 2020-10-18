package omsu.webdev.backend.api.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import omsu.webdev.backend.api.common.db.operations.impl.GetOperation
import omsu.webdev.backend.api.common.db.operations.impl.GetPageOperation
import omsu.webdev.backend.api.common.db.Paged
import omsu.webdev.backend.api.common.db.Parameters
import omsu.webdev.backend.api.common.db.operations.ICountOperation
import omsu.webdev.backend.api.common.db.operations.IGetOperation
import omsu.webdev.backend.api.common.db.operations.IGetPageOperation
import omsu.webdev.backend.api.common.db.operations.IInsertOperation
import omsu.webdev.backend.api.common.db.operations.impl.CountOperation
import omsu.webdev.backend.api.common.db.operations.impl.InsertOperation
import omsu.webdev.backend.api.models.domain.RAMInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository


@Repository
class RAMInfoRepository(
        @Autowired var template: JdbcTemplate,
        @Autowired var objectMapper: ObjectMapper
) {
    private var getInfo: IGetOperation<RAMInfo> = GetOperation(
            template,
            objectMapper,
            "select data from ram_info %FILTERING%",
            RAMInfo::class.java,
            null,
            null
    )
    private var countInfo: ICountOperation = CountOperation(
            template,
            objectMapper,
            "select count(*) from ram_info %FILTERING%",
            Int::class.java,
            null
    )
    private var getInfoPaged: IGetPageOperation<RAMInfo> = GetPageOperation(
            template,
            objectMapper,
            "select data from ram_info %FILTERING% limit ? offset ?",
            RAMInfo::class.java,
            null,
            null
    )
    private var insertInfo: IInsertOperation<RAMInfo> = InsertOperation(
            template,
            objectMapper,
            "insert into ram_info (data) values ((?::jsonb))",
            "ram_info",
            null,
            null
    )

    fun findLatestPaged(parameters: Parameters): Paged<RAMInfo>? {
        countInfo.count(parameters)?.let {
            parameters["total"] = it
        }
        return getInfoPaged.getPage(parameters)
    }

    fun insertInfo(parameters: Parameters, model: RAMInfo) {
        this.insertInfo.create(parameters, model)
    }
}