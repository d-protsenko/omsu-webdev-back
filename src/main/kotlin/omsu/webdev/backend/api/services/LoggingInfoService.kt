package omsu.webdev.backend.api.services

import omsu.webdev.backend.api.common.db.Paged
import omsu.webdev.backend.api.common.db.Parameters
import omsu.webdev.backend.api.models.domain.LoggingInfo
import omsu.webdev.backend.api.models.forms.LoggingInfoForm
import omsu.webdev.backend.api.models.views.LoggingInfoView
import omsu.webdev.backend.api.repositories.LoggingInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LoggingInfoService(
        @Autowired var loggingInfoRepository: LoggingInfoRepository
) {
    fun getLatestInfoPaged(parameters: Parameters): Paged<LoggingInfoView?>? {
        val pagedInfo: Paged<LoggingInfo> = loggingInfoRepository.findLatestPaged(parameters)!!
        return Paged(
                pagedInfo.content?.map { it ->
                    LoggingInfoView.from(it)
                },
                pagedInfo.totalPages,
                pagedInfo.totalEntities
        )
    }

    fun insert(parameters: Parameters, form: LoggingInfoForm?): LoggingInfoView? {
        form?.let { data ->
            LoggingInfo.from(data)?.let {
                loggingInfoRepository.insertInfo(parameters, it)
                return LoggingInfoView.from(it)
            }
        }
        return null
    }
}