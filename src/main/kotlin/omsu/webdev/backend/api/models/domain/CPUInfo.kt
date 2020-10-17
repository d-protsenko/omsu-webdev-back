package omsu.webdev.backend.api.models.domain

import lombok.Data
import omsu.webdev.backend.api.common.db.operations.IIndexedModel
import java.time.LocalDateTime

@Data
class CPUInfo(
        override var id: String? = null,
        var threads: Int? = null,
        var cores: Int? = null,
        var clock: Double? = null,
        var cpuUsage: Double? = null,
        var updatedAt: LocalDateTime? = null
) : IIndexedModel
