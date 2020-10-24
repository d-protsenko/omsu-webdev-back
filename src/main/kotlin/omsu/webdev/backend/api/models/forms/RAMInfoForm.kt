package omsu.webdev.backend.api.models.forms

import java.time.Instant

class RAMInfoForm(
    var total: Double? = null,
    var available: Double? = null,
    var free: Double? = null,
    var used: Double? = null,
    var updatedAt: Instant? = null
)