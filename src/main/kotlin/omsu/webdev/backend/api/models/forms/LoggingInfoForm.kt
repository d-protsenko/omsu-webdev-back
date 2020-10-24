package omsu.webdev.backend.api.models.forms

import java.time.Instant

class LoggingInfoForm(
    var message: String? = null,
    var updatedAt: Instant? = null
)