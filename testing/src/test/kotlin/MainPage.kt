import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait

class MainPage(private val driver: WebDriver) {
    private val pageUrl: String = "http://webdev.local/"

    @FindBy(css = "aside[class~='main__aside']")
    lateinit var aside: WebElement

    @FindBy(id = "usage-input")
    lateinit var asideUsageInput: WebElement

    @FindBy(id = "submit-button")
    lateinit var asideSubmitButton: WebElement

    @FindBy(css = "table[class~='MuiTable-root']")
    lateinit var table: WebElement

    var charts: List<WebElement> = listOf()
    var usageWidgets: List<WebElement> = listOf()
    var asideUsageTypeSelector: Select? = null

    init {
        PageFactory.initElements(driver, this)
    }

    fun open() = driver.get(pageUrl)

    fun verifyUrl() {
        WebDriverWait(driver, 10).until { it.currentUrl == pageUrl }
    }

    fun getCharts() {
        WebDriverWait(driver, 10).until { it.currentUrl == pageUrl }
        this.charts = driver.findElements(
            By.className(
                "responsive-line"
            )
        )
    }

    fun getUsageWidgets() {
        WebDriverWait(driver, 10).until { it.currentUrl == pageUrl }
        this.usageWidgets = driver.findElements(
            By.className(
                "usage-widget"
            )
        )
    }

    fun showAsideSelect() {
        val script = "document.getElementById('open-select-hidden').style.display = 'block'"
        (driver as JavascriptExecutor).executeScript(script)
    }

    fun getAsideSelect() {
        val script = "return document.getElementById('open-select-hidden')"
        asideUsageTypeSelector = Select((driver as JavascriptExecutor).executeScript(script) as WebElement)
    }

    fun hideAsideSelect() {
        val script = "document.getElementById('open-select-hidden').style.display = 'none'"
        (driver as JavascriptExecutor).executeScript(script)
    }
}