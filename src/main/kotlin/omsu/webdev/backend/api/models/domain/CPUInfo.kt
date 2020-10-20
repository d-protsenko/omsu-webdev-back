package omsu.webdev.backend.api.models.domain

import lombok.Data
import omsu.webdev.backend.api.common.db.operations.IIndexedModel
import omsu.webdev.backend.api.configurations.TimeZoneSingleton
import omsu.webdev.backend.api.models.forms.CPUInfoForm
import java.time.Instant
import java.time.ZonedDateTime

@Data
class CPUInfo(
        override var id: String? = null,
        var threads: Int? = null,
        var cores: Int? = null,
        var clock: Double? = null,
        var cpuUsage: Double? = null,
        var updatedAt: ZonedDateTime? = null
) : IIndexedModel {
    companion object {
        fun from(model: CPUInfoForm?): CPUInfo? {
            return model?.let {
                CPUInfo(
                        threads = it.threads,
                        cores = it.cores,
                        clock = it.clock,
                        cpuUsage = it.cpuUsage,
                        updatedAt = ZonedDateTime.ofInstant(Instant.now(), TimeZoneSingleton.mscTimeZone)
                )
            }
        }
    }
}
