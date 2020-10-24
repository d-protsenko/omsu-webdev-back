package omsu.webdev.backend.api.configurations

import java.time.ZoneId

class TimeZoneSingleton private constructor(
    val zone: ZoneId
) {
    companion object {
        private var instance: TimeZoneSingleton? = null
        fun getInstance(): TimeZoneSingleton {
            if (instance == null) {
                instance = TimeZoneSingleton(ZoneId.of("Europe/Moscow"))
            }
            return instance as TimeZoneSingleton
        }
    }
}