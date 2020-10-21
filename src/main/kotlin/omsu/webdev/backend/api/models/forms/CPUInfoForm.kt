package omsu.webdev.backend.api.models.forms

import java.time.ZonedDateTime

class CPUInfoForm(
        var threads: Int? = null,
        var cores: Int? = null,
        var clock: Double? = null,
        var cpuUsage: Double? = null,
        var updatedAt: ZonedDateTime? = null
)