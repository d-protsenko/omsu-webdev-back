package omsu.webdev.backend.api.models.forms

class CPUInfoForm(
        var threads: Int? = null,
        var cores: Int? = null,
        var clock: Double? = null,
        var cpuUsage: Double? = null
)