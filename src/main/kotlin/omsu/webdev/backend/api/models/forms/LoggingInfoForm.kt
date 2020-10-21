package omsu.webdev.backend.api.models.forms

import java.time.ZonedDateTime

class LoggingInfoForm(
        var message: String? = null,
        var updatedAt: ZonedDateTime? = null
)