package ca.uqac.lif.cornisel;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.internal.HasIdentity;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.RemoteWebElement;

public class WebElementDecorator implements WebElement, FindsByClassName, FindsByCssSelector, FindsById,
	FindsByLinkText, FindsByName, FindsByTagName, FindsByXPath, HasIdentity, Locatable, WrapsDriver, 
	SearchContext, TakesScreenshot{

	private RemoteWebElement m_webElement;
	
	public WebElementDecorator(RemoteWebElement webElement) {
		m_webElement = webElement;
	}
	
	@Override
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		return m_webElement.getScreenshotAs(target);
	}

	@Override
	public void clear() {
		m_webElement.clear();
	}

	@Override
	public void click() {
		m_webElement.click();
	}

	@Override
	public WebElement findElement(By by) {
		return m_webElement.findElement(by);
	}

	@Override
	public List<WebElement> findElements(By by) {
		return m_webElement.findElements(by);
	}

	@Override
	public String getAttribute(String name) {
		return m_webElement.getAttribute(name);
	}

	@Override
	public String getCssValue(String propertyName) {
		return m_webElement.getCssValue(propertyName);
	}

	@Override
	public Point getLocation() {
		return m_webElement.getLocation();
	}

	@Override
	public Rectangle getRect() {
		return m_webElement.getRect();
	}

	@Override
	public Dimension getSize() {
		return m_webElement.getSize();
	}

	@Override
	public String getTagName() {
		return m_webElement.getTagName();
	}

	@Override
	public String getText() {
		return m_webElement.getText();
	}

	@Override
	public boolean isDisplayed() {
		return m_webElement.isDisplayed();
	}

	@Override
	public boolean isEnabled() {
		return m_webElement.isEnabled();
	}

	@Override
	public boolean isSelected() {
		return m_webElement.isSelected();
	}

	@Override
	public void sendKeys(CharSequence... keysToSend) {
		m_webElement.sendKeys(keysToSend);
	}

	@Override
	public void submit() {
		m_webElement.submit();
	}
	
	protected WebElement getWebElement() {
		return m_webElement;
	}

	@Override
	public WebDriver getWrappedDriver() {
		return m_webElement.getWrappedDriver();
	}

	@Override
	public Coordinates getCoordinates() {
		return m_webElement.getCoordinates();
	}

	@Override
	public String getId() {
		return m_webElement.getId();
	}

	@Override
	public WebElement findElementByXPath(String arg0) {
		return m_webElement.findElementByXPath(arg0);
	}

	@Override
	public List<WebElement> findElementsByXPath(String arg0) {
		return m_webElement.findElementsByXPath(arg0);
	}

	@Override
	public WebElement findElementByTagName(String arg0) {
		return m_webElement.findElementByTagName(arg0);
	}

	@Override
	public List<WebElement> findElementsByTagName(String arg0) {
		return m_webElement.findElementsByTagName(arg0);
	}

	@Override
	public WebElement findElementByName(String arg0) {
		return m_webElement.findElementByName(arg0);
	}

	@Override
	public List<WebElement> findElementsByName(String arg0) {
		return m_webElement.findElementsByName(arg0);
	}

	@Override
	public WebElement findElementByLinkText(String arg0) {
		return m_webElement.findElementByLinkText(arg0);
	}

	@Override
	public WebElement findElementByPartialLinkText(String arg0) {
		return m_webElement.findElementByPartialLinkText(arg0);
	}

	@Override
	public List<WebElement> findElementsByLinkText(String arg0) {
		return m_webElement.findElementsByLinkText(arg0);
	}

	@Override
	public List<WebElement> findElementsByPartialLinkText(String arg0) {
		return m_webElement.findElementsByPartialLinkText(arg0);
	}

	@Override
	public WebElement findElementById(String arg0) {
		return m_webElement.findElementById(arg0);
	}

	@Override
	public List<WebElement> findElementsById(String arg0) {
		return m_webElement.findElementsById(arg0);
	}

	@Override
	public WebElement findElementByCssSelector(String arg0) {
		return m_webElement.findElementByCssSelector(arg0);
	}

	@Override
	public List<WebElement> findElementsByCssSelector(String arg0) {
		return m_webElement.findElementsByCssSelector(arg0);
	}

	@Override
	public WebElement findElementByClassName(String arg0) {
		return m_webElement.findElementByClassName(arg0);
	}

	@Override
	public List<WebElement> findElementsByClassName(String arg0) {
		return m_webElement.findElementsByClassName(arg0);
	}
}
