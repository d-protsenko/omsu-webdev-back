package omsu.webdev.backend.api.models.domain

import lombok.Data
import omsu.webdev.backend.api.common.db.operations.IIndexedModel
import java.time.LocalDateTime

@Data
class RAMInfo(
        override var id: String? = null,
        var total: Double? = null,
        var available: Double? = null,
        var free: Double? = null,
        var used: Double? = null,
        var updatedAt: LocalDateTime? = null
) : IIndexedModel
