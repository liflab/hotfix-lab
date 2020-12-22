package ca.uqac.lif.cornisel;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebDriverDecorator implements WebDriver, HasCapabilities, HasInputDevices, Interactive,
	FindsByClassName, FindsByCssSelector, FindsById, FindsByLinkText, FindsByName, FindsByTagName,
	FindsByXPath, JavascriptExecutor, SearchContext, TakesScreenshot
{
	protected RemoteWebDriver m_webDriver;
	
	public WebDriverDecorator(RemoteWebDriver webDriver) {
		m_webDriver = webDriver;
	}

	@Override
	public void close() {
		m_webDriver.close();
	}

	@Override
	public WebElement findElement(By by) {
		return m_webDriver.findElement(by);
	}
	
	@Override
	public List<WebElement> findElements(By by) {
		return m_webDriver.findElements(by);
	}
	
	@Override
	public void get(String url) {
		m_webDriver.get(url);
	}

	@Override
	public String getCurrentUrl() {
		return m_webDriver.getCurrentUrl();
	}
	
	@Override
	public String getPageSource() {
		return m_webDriver.getPageSource();
	}
	
	@Override
	public String getTitle() {
		return m_webDriver.getTitle();
	}

	@Override
	public String getWindowHandle() {
		return m_webDriver.getWindowHandle();
	}

	@Override
	public Set<String> getWindowHandles() {
		return m_webDriver.getWindowHandles();
	}

	@Override
	public Options manage() {
		return m_webDriver.manage();
	}

	@Override
	public Navigation navigate() {
		return m_webDriver.navigate();
	}
	
	@Override
	public void quit() {
		m_webDriver.quit();
	}

	@Override
	public TargetLocator switchTo() {
		return m_webDriver.switchTo();
	}
	
	@Override
	public String toString() {
		return m_webDriver.toString();
	}

	@Override
	public <X> X getScreenshotAs(OutputType<X> arg0) throws WebDriverException {
		return m_webDriver.getScreenshotAs(arg0);
	}

	@Override
	public Object executeAsyncScript(String arg0, Object... arg1) {
		return m_webDriver.executeAsyncScript(arg0, arg1);
	}

	@Override
	public Object executeScript(String arg0, Object... arg1) {
		return m_webDriver.executeScript(arg0, arg1);
	}

	@Override
	public WebElement findElementByXPath(String arg0) {
		return m_webDriver.findElementByXPath(arg0);
	}

	@Override
	public List<WebElement> findElementsByXPath(String arg0) {
		return m_webDriver.findElementsByXPath(arg0);
	}

	@Override
	public WebElement findElementByTagName(String arg0) {
		return m_webDriver.findElementByTagName(arg0);
	}

	@Override
	public List<WebElement> findElementsByTagName(String arg0) {
		return m_webDriver.findElementsByTagName(arg0);
	}

	@Override
	public WebElement findElementByName(String arg0) {
		return m_webDriver.findElementByName(arg0);
	}

	@Override
	public List<WebElement> findElementsByName(String arg0) {
		return m_webDriver.findElementsByName(arg0);
	}

	@Override
	public WebElement findElementByLinkText(String arg0) {
		return m_webDriver.findElementByLinkText(arg0);
	}

	@Override
	public WebElement findElementByPartialLinkText(String arg0) {
		return m_webDriver.findElementByPartialLinkText(arg0);
	}

	@Override
	public List<WebElement> findElementsByLinkText(String arg0) {
		return m_webDriver.findElementsByLinkText(arg0);
	}

	@Override
	public List<WebElement> findElementsByPartialLinkText(String arg0) {
		return m_webDriver.findElementsByPartialLinkText(arg0);
	}

	@Override
	public WebElement findElementById(String arg0) {
		return m_webDriver.findElementById(arg0);
	}

	@Override
	public List<WebElement> findElementsById(String arg0) {
		return m_webDriver.findElementsById(arg0);
	}

	@Override
	public WebElement findElementByCssSelector(String arg0) {
		return m_webDriver.findElementByCssSelector(arg0);
	}

	@Override
	public List<WebElement> findElementsByCssSelector(String arg0) {
		return m_webDriver.findElementsByCssSelector(arg0);
	}

	@Override
	public WebElement findElementByClassName(String arg0) {
		return m_webDriver.findElementByClassName(arg0);
	}

	@Override
	public List<WebElement> findElementsByClassName(String arg0) {
		return m_webDriver.findElementsByClassName(arg0);
	}

	@Override
	public void perform(Collection<Sequence> arg0) {
		((Interactive) m_webDriver).perform(arg0);
	}

	@Override
	public void resetInputState() {
		((Interactive) m_webDriver).resetInputState();
	}

	@Override
	public Keyboard getKeyboard() {
		return m_webDriver.getKeyboard();
	}

	@Override
	public Mouse getMouse() {
		return m_webDriver.getMouse();
	}

	@Override
	public Capabilities getCapabilities() {
		return m_webDriver.getCapabilities();
	}
}
