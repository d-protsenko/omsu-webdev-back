import io.kotlintest.specs.StringSpec
import junit.framework.Assert.assertTrue
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.Select
import java.util.concurrent.TimeUnit

class TestMonitoringApplication : StringSpec() {
    private val driver: WebDriver = ChromeDriver()
    private val url: String = "http://webdev.local/"
    private val mainPage = MainPage(driver)

    init {
        driver.manage()?.timeouts()?.implicitlyWait(10, TimeUnit.SECONDS)
        driver.manage()?.window()?.maximize()

        "Startup" {
            mainPage.run {
                open()
                verifyUrl()
            }
        }

        "Contains Aside" {
            mainPage.run {
                assertTrue(aside.isDisplayed)
                assertTrue(aside.isEnabled)
                assertTrue(aside.text.contains("submit", ignoreCase = true))
            }
        }

        "Contains Charts" {
            mainPage.run {
                getCharts()
                assertTrue(charts.size == 2)
                assertTrue(charts.stream().allMatch { it.isEnabled && it.isDisplayed })
                assertTrue(charts.stream().anyMatch { it.text.contains("cpu usage", ignoreCase = true) })
                assertTrue(charts.stream().anyMatch { it.text.contains("ram usage", ignoreCase = true) })
            }
        }

        "Contains Table" {
            mainPage.run {
                getCharts()
                assertTrue(table.text.contains("Log message", ignoreCase = true))
                assertTrue(table.text.contains("Time point", ignoreCase = true))
            }
        }

        "Contains UsageWidgets" {
            mainPage.run {
                getUsageWidgets()
                assertTrue(usageWidgets.size == 2)
                assertTrue(usageWidgets.stream().allMatch { it.isEnabled && it.isDisplayed })
                assertTrue(usageWidgets.stream().anyMatch { it.text.contains("cpu usage", ignoreCase = true) })
                assertTrue(usageWidgets.stream().anyMatch { it.text.contains("ram usage", ignoreCase = true) })
            }
        }

        "Submit form Aside" {
            mainPage.run {
                asideUsageInput.sendKeys("48")
                showAsideSelect()
                getAsideSelect()
                asideUsageTypeSelector!!.selectByIndex(1)
                hideAsideSelect()
                asideSubmitButton.click()
            }
        }
    }
}