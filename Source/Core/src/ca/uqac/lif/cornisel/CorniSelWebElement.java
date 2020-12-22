package ca.uqac.lif.cornisel;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

public class CorniSelWebElement extends WebElementDecorator{
	
	private ICornipickleInterpreter m_interpreter;

	public CorniSelWebElement(RemoteWebElement webElement, ICornipickleInterpreter interpreter) {
		super(webElement);
		m_interpreter = interpreter;
	}
	
	@Override
	public void click() {
		if(m_interpreter.getUpdateMode().equals(CorniSelWebDriver.UpdateMode.AUTOMATIC)) {
			m_interpreter.evaluateAll(this.getWebElement());
			super.click();
		}
		else {
			super.click();
		}
	}
	
	@Override
	public void submit() {
		if(m_interpreter.getUpdateMode().equals(CorniSelWebDriver.UpdateMode.AUTOMATIC)) {
			m_interpreter.evaluateAll(this.getWebElement());
			super.submit();
		}
		else {
			super.submit();
		}
	}
	
	@Override
	public WebElement findElement(By by) {
		WebElement we = super.findElement(by);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we,m_interpreter);
		return cswe;
	}
	
	@Override
	public List<WebElement> findElements(By by) {
		List<WebElement> welist = super.findElements(by);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element,m_interpreter));
		}
		return cswelist;
	}
	
	@Override
	public WebElement findElementByXPath(String arg0) {
		WebElement we = super.findElementByXPath(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we, m_interpreter);
		return cswe;
	}

	@Override
	public List<WebElement> findElementsByXPath(String arg0) {
		List<WebElement> welist = super.findElementsByXPath(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element, m_interpreter));
		}
		return cswelist;
	}

	@Override
	public WebElement findElementByTagName(String arg0) {
		WebElement we = super.findElementByTagName(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we, m_interpreter);
		return cswe;
	}

	@Override
	public List<WebElement> findElementsByTagName(String arg0) {
		List<WebElement> welist = super.findElementsByTagName(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element, m_interpreter));
		}
		return cswelist;
	}

	@Override
	public WebElement findElementByName(String arg0) {
		WebElement we = super.findElementByName(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we, m_interpreter);
		return cswe;
	}

	@Override
	public List<WebElement> findElementsByName(String arg0) {
		List<WebElement> welist = super.findElementsByName(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element, m_interpreter));
		}
		return cswelist;
	}

	@Override
	public WebElement findElementByLinkText(String arg0) {
		WebElement we = super.findElementByLinkText(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we, m_interpreter);
		return cswe;
	}

	@Override
	public WebElement findElementByPartialLinkText(String arg0) {
		WebElement we = super.findElementByPartialLinkText(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we, m_interpreter);
		return cswe;
	}

	@Override
	public List<WebElement> findElementsByLinkText(String arg0) {
		List<WebElement> welist = super.findElementsByLinkText(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element, m_interpreter));
		}
		return cswelist;
	}

	@Override
	public List<WebElement> findElementsByPartialLinkText(String arg0) {
		List<WebElement> welist = super.findElementsByPartialLinkText(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element, m_interpreter));
		}
		return cswelist;
	}

	@Override
	public WebElement findElementById(String arg0) {
		WebElement we = super.findElementById(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we, m_interpreter);
		return cswe;
	}

	@Override
	public List<WebElement> findElementsById(String arg0) {
		List<WebElement> welist = super.findElementsById(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element, m_interpreter));
		}
		return cswelist;
	}

	@Override
	public WebElement findElementByCssSelector(String arg0) {
		WebElement we = super.findElementByCssSelector(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we, m_interpreter);
		return cswe;
	}

	@Override
	public List<WebElement> findElementsByCssSelector(String arg0) {
		List<WebElement> welist = super.findElementsByCssSelector(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element, m_interpreter));
		}
		return cswelist;
	}

	@Override
	public WebElement findElementByClassName(String arg0) {
		WebElement we = super.findElementByClassName(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we, m_interpreter);
		return cswe;
	}

	@Override
	public List<WebElement> findElementsByClassName(String arg0) {
		List<WebElement> welist = super.findElementsByClassName(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element, m_interpreter));
		}
		return cswelist;
	}
}