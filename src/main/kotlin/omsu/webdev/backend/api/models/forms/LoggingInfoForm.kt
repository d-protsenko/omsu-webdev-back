package omsu.webdev.backend.api.models.forms

import java.time.ZonedDateTime

class LoggingInfoForm(
        var threads: Int? = null,
        var message: String? = null,
        var updatedAt: ZonedDateTime? = null
)