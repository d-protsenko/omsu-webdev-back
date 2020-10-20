package omsu.webdev.backend.api.configurations

import java.time.ZoneId

class TimeZoneSingleton {
    companion object {
        val mscTimeZone = ZoneId.of("Europe/Moscow")
    }
}