package omsu.webdev.backend.api.services

import omsu.webdev.backend.api.common.db.Paged
import omsu.webdev.backend.api.common.db.Parameters
import omsu.webdev.backend.api.configurations.TimeZoneSingleton
import omsu.webdev.backend.api.models.domain.RAMInfo
import omsu.webdev.backend.api.models.forms.RAMInfoForm
import omsu.webdev.backend.api.models.views.RAMInfoView
import omsu.webdev.backend.api.repositories.RAMInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZonedDateTime

@Service
class RAMInfoService(
    @Autowired var ramInfoRepository: RAMInfoRepository,
    @Autowired var hardwareUsageService: HardwareUsageService
) {
    fun getLatestInfoPaged(parameters: Parameters): Paged<RAMInfoView?>? {
        val pagedInfo: Paged<RAMInfo> = ramInfoRepository.findLatestPaged(parameters)!!
        return Paged(
            pagedInfo.content?.map { it ->
                RAMInfoView.from(it)
            },
            pagedInfo.totalPages,
            pagedInfo.totalEntities
        )
    }

    fun generateAndInsertLatest(parameters: Parameters): RAMInfoView? {
        val ramInfo = hardwareUsageService.getRAMInfo()
        val info = RAMInfo.Builder()
            .total(ramInfo.total)
            .available(ramInfo.available)
            .free(ramInfo.free)
            .used(ramInfo.used)
            .updatedAt(ZonedDateTime.ofInstant(Instant.now(), TimeZoneSingleton.getInstance().zone))
            .build()
        ramInfoRepository.insertInfo(parameters, info)
        return RAMInfoView.from(info)
    }

    fun insert(parameters: Parameters, form: RAMInfoForm?): RAMInfoView? {
        form?.let { data ->
            RAMInfo.from(data)?.let {
                ramInfoRepository.insertInfo(parameters, it)
                return RAMInfoView.from(it)
            }
        }
        return null
    }
}