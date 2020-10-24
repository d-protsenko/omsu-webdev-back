package omsu.webdev.backend.api.configurations

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.FlywayException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class DatabaseMigration(@Autowired var dataSource: DataSource?) {

    @Value("\${flyway.migration-location.webdev}")
    private val migrationLocation: String? = null

    @Bean
    fun hardwareInfoFlyway(): Flyway? {
        println("Campaign Management - Flyway: starting to look for and executing new migrations...")
        val flyway: Flyway = Flyway
            .configure()
            .dataSource(dataSource)
            .table("flyway_schema_webdev_hardware_info")
            .baselineOnMigrate(true)
            .locations(migrationLocation)
            .load()
        try {
            flyway.migrate()
        } catch (e: FlywayException) {
            println(e.localizedMessage)
            flyway.repair()
            flyway.migrate()
        }
        println("Flyway: work finished ...")
        return flyway
    }
}