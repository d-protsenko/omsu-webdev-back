package omsu.webdev.backend.api.models.forms

class RAMInfoForm(
        var total: Double? = null,
        var available: Double? = null,
        var free: Double? = null,
        var used: Double? = null
)