package ca.uqac.lif.cornisel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.cornipickle.Verdict;
import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;

public class CorniSelWebDriver extends WebDriverDecorator implements ICornipickleInterpreter{

	private Interpreter m_interpreter;
	
	private String m_serializationScript;
	
	private String m_highlightScript;
	
	private List<EvaluationListener> m_listeners;
	
	public static enum UpdateMode {MANUAL, AUTOMATIC};
	
	public UpdateMode m_updateMode = UpdateMode.AUTOMATIC;
	
	public CorniSelWebDriver(RemoteWebDriver driver) {
		super(driver);
		m_interpreter = new Interpreter();
		m_serializationScript = readJS("resources/serialization.js");
		m_highlightScript = readJS("resources/highlight.js");
		m_listeners = new ArrayList<EvaluationListener>();
	}
	
	@Override
	public void setCornipickleProperties(String properties) throws ParseException{
		m_interpreter.parseProperties(properties);
	}

	@Override
	public void evaluateAll(WebElement event) {
		String jsonString = (String)super.m_webDriver.executeScript(m_serializationScript, event, m_interpreter.getAttributes(), m_interpreter.getTagNames());
		JsonElement j;
		try {
			j = new JsonParser().parse(jsonString);
			m_interpreter.evaluateAll(j);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
		}
		
		highlightElements();
		
		for(EvaluationListener listener : m_listeners) {
			listener.evaluationEvent(this, m_interpreter);
		}
	}
	
	@Override
	public void resetHistory(){
		m_interpreter.resetHistory();
	}
	
	@Override
	public void clear(){
		m_interpreter.clear();
	}
	
	public void outputEvaluation(String filename) throws IOException{
		Map<StatementMetadata,Verdict> verdicts = m_interpreter.getVerdicts();
		String currentURL = super.m_webDriver.getCurrentUrl();
		String width = String.valueOf(super.m_webDriver.manage().window().getSize().getWidth());
		String height = String.valueOf(super.m_webDriver.manage().window().getSize().getHeight());
		
		FileWriter fw = new FileWriter(new File(filename), true);
		
		fw.write("-----------------------------------------------\n");
		fw.write("Evaluation\n");
		fw.write("URL: " + currentURL + "\n");
		fw.write("Width: " + width + " px\n");
		fw.write("Height: " + height + " px\n");
		fw.write("Overall result: ");
		
		Verdict.Value overallVerdict = Verdict.Value.TRUE;
		for(Entry<StatementMetadata, Verdict> entry : verdicts.entrySet())
		{
			if(entry.getValue().is(Verdict.Value.INCONCLUSIVE))
			{
				overallVerdict = Verdict.Value.INCONCLUSIVE;
			}
			else if(entry.getValue().is(Verdict.Value.FALSE))
			{
				overallVerdict = Verdict.Value.FALSE;
				break;
			}
		}
		fw.write(overallVerdict.toString() + "\n\n");
		
		for(Entry<StatementMetadata, Verdict> entry : verdicts.entrySet())
		{
			fw.write("Statement:\n" + entry.getKey().toString());
			fw.write("Verdict: " + entry.getValue().getValue().toString() + "\n");
			if(entry.getValue().getValue() == Verdict.Value.FALSE)
			{
				fw.write("Witness: " + entry.getValue().getWitnessFalse().toString() + "\n\n");
			}
			else
			{
				fw.write("\n");
			}
		}
		
		fw.write("\n");
		fw.close();
	}
	
	public Map<StatementMetadata,Verdict> getVerdicts() {
		return m_interpreter.getVerdicts();
	}
	
	@Override
	public UpdateMode getUpdateMode() {
		return m_updateMode;
	}
	
	public void setAutomaticMode() {
		m_updateMode = UpdateMode.AUTOMATIC;
	}
	
	public void setManualMode() {
		m_updateMode = UpdateMode.MANUAL;
	}
	
	/*
	 * Add a listener that will throw an event every time an evaluation has finished
	 */
	public void addListener(EvaluationListener listener) {
		m_listeners.add(listener);
	}
	
	@Override
	public WebElement findElement(By by) {
		WebElement we = super.findElement(by);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we,this);
		return cswe;
	}
	
	@Override
	public List<WebElement> findElements(By by) {
		List<WebElement> welist = super.findElements(by);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element,this));
		}
		return cswelist;
	}
	
	@Override
	public WebElement findElementByXPath(String arg0) {
		WebElement we = super.findElementByXPath(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we,this);
		return cswe;
	}

	@Override
	public List<WebElement> findElementsByXPath(String arg0) {
		List<WebElement> welist = super.findElementsByXPath(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element,this));
		}
		return cswelist;
	}

	@Override
	public WebElement findElementByTagName(String arg0) {
		WebElement we = super.findElementByTagName(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we,this);
		return cswe;
	}

	@Override
	public List<WebElement> findElementsByTagName(String arg0) {
		List<WebElement> welist = super.findElementsByTagName(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element,this));
		}
		return cswelist;
	}

	@Override
	public WebElement findElementByName(String arg0) {
		WebElement we = super.findElementByName(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we,this);
		return cswe;
	}

	@Override
	public List<WebElement> findElementsByName(String arg0) {
		List<WebElement> welist = super.findElementsByName(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element,this));
		}
		return cswelist;
	}

	@Override
	public WebElement findElementByLinkText(String arg0) {
		WebElement we = super.findElementByLinkText(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we,this);
		return cswe;
	}

	@Override
	public WebElement findElementByPartialLinkText(String arg0) {
		WebElement we = super.findElementByPartialLinkText(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we,this);
		return cswe;
	}

	@Override
	public List<WebElement> findElementsByLinkText(String arg0) {
		List<WebElement> welist = super.findElementsByLinkText(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element,this));
		}
		return cswelist;
	}

	@Override
	public List<WebElement> findElementsByPartialLinkText(String arg0) {
		List<WebElement> welist = super.findElementsByPartialLinkText(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element,this));
		}
		return cswelist;
	}

	@Override
	public WebElement findElementById(String arg0) {
		WebElement we = super.findElementById(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we,this);
		return cswe;
	}

	@Override
	public List<WebElement> findElementsById(String arg0) {
		List<WebElement> welist = super.findElementsById(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element,this));
		}
		return cswelist;
	}

	@Override
	public WebElement findElementByCssSelector(String arg0) {
		WebElement we = super.findElementByCssSelector(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we,this);
		return cswe;
	}

	@Override
	public List<WebElement> findElementsByCssSelector(String arg0) {
		List<WebElement> welist = super.findElementsByCssSelector(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element,this));
		}
		return cswelist;
	}

	@Override
	public WebElement findElementByClassName(String arg0) {
		WebElement we = super.findElementByClassName(arg0);
		CorniSelWebElement cswe = new CorniSelWebElement((RemoteWebElement)we,this);
		return cswe;
	}

	@Override
	public List<WebElement> findElementsByClassName(String arg0) {
		List<WebElement> welist = super.findElementsByClassName(arg0);
		List<WebElement> cswelist = new ArrayList<WebElement>();
		for(WebElement element : welist) {
			cswelist.add(new CorniSelWebElement((RemoteWebElement)element,this));
		}
		return cswelist;
	}
	
	private String readJS(String path) {
		InputStream is;
		try {
			is = getClass().getResourceAsStream(path);
			BufferedReader bf = new BufferedReader(new InputStreamReader(is));
			String inputLine;
			String script = "";
	        while ((inputLine = bf.readLine()) != null) {
	        	script = script + inputLine + "\n";
	        }
	        
	        return script;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<List<Number>> getIdsToHighlight(Verdict v)
	{
		List<List<Number>> ids = new LinkedList<List<Number>>();
	    Set<Set<JsonElement>> tuples = v.getWitness().flatten();
	    for (Set<JsonElement> tuple : tuples)
	    {
	    	LinkedList<Number> out = new LinkedList<Number>();
	    	for (JsonElement e : tuple)
	    	{
	    		if (!(e instanceof JsonMap))
	    		{
	    			continue;
	    		}
	    		JsonMap m = (JsonMap) e;
	    		JsonElement id = m.get("cornipickleid");
	    		if (id == null || !(id instanceof JsonNumber))
	    		{
	    			continue;
	    		}
	    		out.add(((JsonNumber) id).numberValue());
	    	}
	    	ids.add(out);
	    }
	    return ids;
	}
	
	private void highlightElements()
	{
		Map<StatementMetadata, Verdict> verdicts = m_interpreter.getVerdicts();
		List<List<List<Number>>> highlight_ids = new LinkedList<List<List<Number>>>();
		for (StatementMetadata key : verdicts.keySet())
	    {
	    	List<List<Number>> id_to_highlight = new LinkedList<List<Number>>();
			Verdict v = verdicts.get(key);
			if (v.is(Verdict.Value.FALSE))
			{
				id_to_highlight.addAll(getIdsToHighlight(v));
			}
			highlight_ids.add(id_to_highlight);
	    }
		
		super.m_webDriver.executeScript(m_highlightScript, highlight_ids);
	}
}
