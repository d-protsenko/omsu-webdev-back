package omsu.webdev.backend.api.configurations

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
class DatabaseConfiguration {
    companion object {
        private const val SLEEP_TIME_INTERVAL = 10000L
    }

    @Value("\${spring.datasource.driver-class-name}")
    private val driverClassName: String? = null

    @Value("\${spring.datasource.url}")
    private val databaseUrl: String? = null

    @Value("\${spring.datasource.username}")
    private val username: String? = null

    @Value("\${spring.datasource.password}")
    private val password: String? = null

    @Bean
    @Primary
    fun dataSource(): DataSource? {
        val dataSource = DriverManagerDataSource()
        driverClassName.let { { driverClassName: String -> dataSource.setDriverClassName(driverClassName) } }
        dataSource.url = databaseUrl
        dataSource.username = username
        dataSource.password = password
        return dataSource
    }

    @Bean
    @Primary
    fun template(dataSource: DataSource?): JdbcTemplate? {
        var template = dataSource?.let { JdbcTemplate(it) }
        var isReady = false
        while (!isReady) {
            try {
                template?.queryForList("SELECT 1")
                isReady = true
            } catch (e: Throwable) {
                println("Primary database is not ready. Wait for" + Companion.SLEEP_TIME_INTERVAL + " milliseconds.")
                e.printStackTrace()
                try {
                    Thread.sleep(Companion.SLEEP_TIME_INTERVAL)
                } catch (e1: InterruptedException) {
                    println("ERROR: Interrupted exception")
                    e1.printStackTrace()
                }
                template = dataSource?.let { JdbcTemplate(it) }
            }
        }
        return template
    }
}