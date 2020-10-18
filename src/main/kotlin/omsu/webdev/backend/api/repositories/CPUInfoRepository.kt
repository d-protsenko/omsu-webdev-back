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
import omsu.webdev.backend.api.models.domain.CPUInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository


@Repository
class CPUInfoRepository(
        @Autowired var template: JdbcTemplate,
        @Autowired var objectMapper: ObjectMapper
) {
    private var getInfo: IGetOperation<CPUInfo> = GetOperation(
            template,
            objectMapper,
            "select data from cpu_info %FILTERING%",
            CPUInfo::class.java,
            null,
            null
    )
    private var countInfo: ICountOperation = CountOperation(
            template,
            objectMapper,
            "select count(*) from cpu_info %FILTERING%",
            Int::class.java,
            null
    )
    private var getInfoPaged: IGetPageOperation<CPUInfo> = GetPageOperation(
            template,
            objectMapper,
            "select data from cpu_info %FILTERING% limit ? offset ?",
            CPUInfo::class.java,
            null,
            null
    )
    private var insertInfo: IInsertOperation<CPUInfo> = InsertOperation(
            template,
            objectMapper,
            "insert into cpu_info (data) values ((?::jsonb))",
            "cpu_info",
            null,
            null
    )

    fun findLatestPaged(parameters: Parameters): Paged<CPUInfo>? {
        countInfo.count(parameters)?.let {
            parameters["total"] = it
        }
        return getInfoPaged.getPage(parameters)
    }

    fun insertInfo(parameters: Parameters, model: CPUInfo) {
        this.insertInfo.create(parameters, model)
    }
}