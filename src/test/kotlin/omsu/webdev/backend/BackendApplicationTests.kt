package omsu.webdev.backend

import omsu.webdev.backend.api.BackendApplication
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [BackendApplication::class])
class BackendApplicationTests {

    @Test
    fun contextLoads() {
    }
}
