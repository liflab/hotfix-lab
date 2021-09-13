package ca.uqac.lif.cornisel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.pagen.Box;
import ca.uqac.lif.pagen.LayoutConstraint;
import ca.uqac.lif.pagen.LayoutConstraint.Contained;
import ca.uqac.lif.pagen.LayoutConstraint.Disjoint;
import ca.uqac.lif.pagen.LayoutConstraint.HorizontallyAligned;
import ca.uqac.lif.pagen.opl.OplAbsoluteRenderer;

import ilog.concert.*;
import ilog.opl.*;

public class test {

	static PrintStream print;
	static IdedBox boxedWebSite;
	static ArrayList<Double> top, left, width, height;
	static IdedBox child;
	static IdedBox child2;
	static IdedBox parent;

	public static void main(String[] args) throws InterruptedException {
		//Declare variables
		print = System.out;
		top = new ArrayList<Double>();
		left = new ArrayList<Double>();
		width = new ArrayList<Double>();
		height = new ArrayList<Double>();
		String propertyString = "For each $x in $(body) {$x's color matches \"RGB\\(255,0,0\\)\"}"; 

		final Charset charset = StandardCharsets.UTF_8;

		//System.setProperty("webdriver.chrome.driver", "C:\\Users\\xavierchamberland\\Documents\\UQAC\\Maitrise\\CPLEX\\chromedriver\\chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
		RemoteWebDriver chromeDriver = new ChromeDriver();
		CorniSelWebDriver corniSelDriver = new CorniSelWebDriver(chromeDriver);

		try 
		{	
			//Open output streams 
			FileOutputStream oplFileStream = new FileOutputStream("oplWebSite.txt");
			PrintStream ps = new PrintStream(oplFileStream, true, charset.name());

			//Open web page
			//corniSelDriver.setCornipickleProperties(propertyString);
			//corniSelDriver.get("https://www.google.ca");
			//corniSelDriver.get("C:\\Users\\xavierchamberland\\Downloads\\Google_contained_error_connexion.html");
			//corniSelDriver.get("C:\\Users\\xavierchamberland\\Downloads\\Moodle-overlap\\1399132246769_132\\index.html");
			corniSelDriver.get("file:///home/sylvain/Desktop/1399132246769_132/index.html");

			//Recover all the DOM tree of the web page
			JavascriptExecutor js = (JavascriptExecutor)  chromeDriver;			
			String n = js.executeScript(Scripts.nodifyScript).toString();
			//WebNode w = new WebNode(n);
			IdedBox boxedWebSite = generateBoxedWebSite(WebNode.buildFromJSON(n));

			//Declare constraints for CPLEX
			Set<LayoutConstraint> constraints = new HashSet<LayoutConstraint>();
			//constraints.add(new Contained(child,parent));
			constraints.add(new Disjoint(child2, child));
			//constraints.addAll(Contained.addContainmentConstraints(boxedWebSite));
			//constraints.addAll(Disjoint.addContainmentConstraints(boxedWebSite));
			//constraints.addAll(HorizontallyAligned);
			//constraints.addAll(VerticallyAligned);
			PrintStream debug_ps = new PrintStream(new FileOutputStream(new File("/tmp/before.txt")));
			printNodeProperties(debug_ps, boxedWebSite);
			debug_ps.close();

			//Generate OPL for CPLEX
			OplAbsoluteRenderer oplRenderer = new OplAbsoluteRenderer();
			oplRenderer.addConstraints(constraints);
			oplRenderer.render(ps, boxedWebSite);
			ps.close();
			oplFileStream.close();

			/** TODO Cornipickle serialization
			 * Program doesn't seem to evaluate cornipickle's statements
			 */
			/*WebElement body = corniSelDriver.findElementByTagName("body");
			corniSelDriver.evaluateAll(corniSelDriver.m_webDriver.findElementByTagName("body"));
			body.click();
			System.out.println(corniSelDriver.getVerdicts());
			corniSelDriver.addListener(new Listener());*/


			//Generate corrections
			IloOplFactory.setDebugMode(false);
			IloOplFactory oplF = new IloOplFactory();

			IloOplCompiler compiler = oplF.createOplCompiler();
			FileOutputStream compiled_model_os = new FileOutputStream(new File("response.txt"));
			IloOplModelSource modelSource = oplF.createOplModelSource("oplWebSite.txt");
			compiler.compile(modelSource, compiled_model_os);
			compiler.end();
			compiled_model_os.close();

			IloOplErrorHandler errHandler = oplF.createOplErrorHandler();
			IloOplRunConfiguration rc = null;
			rc = oplF.createOplRunConfiguration("oplWebSite.txt");
			rc.setErrorHandler(errHandler);
			IloOplModel opl = rc.getOplModel();
			IloOplSettings settings = opl.getSettings();
			settings.setWithLocations(true);
			settings.setWithNames(true);
			opl.generate();
			//opl.printConflict(System.out);
			boolean result = opl.getCplex().solve();
			if (result) {
				
				//opl.postProcess();
				ByteArrayOutputStream stream = new ByteArrayOutputStream(); //CPlexSolution.txt
			
				opl.printSolution(stream);
				String solution = new String(stream.toByteArray());
				
				readCPLEXSolution(solution);
				correctWebPage(js);
			} 

			/**utiliser querySelectorAll pour aller chercher l'�l�ment � modifier
			 * 
			 * var matches = container.querySelectorAll("li[data-active=1]");
			 */
		} /*catch (ParseException e) {			
			boxedWebSite = null;
			print.println("There was an error during the page rendering");
			e.printStackTrace();	
			corniSelDriver.close();
		} */
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	//Using the DOM structure, recreate the website with boxes to later generate the OPL
	private static IdedBox generateBoxedWebSite(WebNode root)
	{
		IdedBox rootBox = new IdedBox(root.getId(), root.getX(), root.getY(), root.getW(), root.getH());
		if(rootBox.getId() == 0)
			child = rootBox;
		if(rootBox.getId() == 9)
			parent = rootBox;
		if(rootBox.getId() == 8)
			child2 = rootBox;
		root.getChildren().forEach((n)->rootBox.addChild(generateBoxedWebSite(n)));

		return rootBox;
	}
	
	protected static void printNodeProperties(PrintStream ps, Box b)
	{
		ps.println("ID: " + b.getId() + ", " + b.getWidth() + "x" + b.getHeight() + " at (" + b.getX() + "," + b.getY() + ")");
		for (Box c : b.getChildren())
		{
			printNodeProperties(ps, c);
		}
	}

	private static void readCPLEXSolution(String s_content)
	{
			getNumbers(s_content, "top", top);
			getNumbers(s_content, "left", left);
			getNumbers(s_content, "Width", width);
			getNumbers(s_content, "Height", height);
	}
	
	private static void getNumbers(String s_content, String param_name, ArrayList<Double> list)
	{
		Pattern top_pattern = Pattern.compile(param_name + " = \\[(.*?)\\];", Pattern.DOTALL);
		Matcher top_matcher = top_pattern.matcher(s_content);
		if (top_matcher.find())
		{
			String numbers = top_matcher.group(1);
			String n_values[] = numbers.split("\\s+");
			for (String s : n_values)
			{
				list.add(Double.parseDouble(s.trim()));
			}
		}
	}

	private static void correctWebPage(JavascriptExecutor js) throws IOException
	{
		//Check if there are any corrections and if there was no error during corrections generation
		if((top.size() == left.size()) && 
				(top.size() == width.size()) && 
				(width.size() == height.size()) &&
				!top.isEmpty())
		{
			PrintStream debug_ps = new PrintStream(new FileOutputStream(new File("/tmp/after.txt")));
			//Apply corrections for every element of the web page
			for(int i = 0; i < top.size(); i++)
			{
				debug_ps.println("ID: " + i + ", " + width.get(i) + "x" + height.get(i) + " at (" + left.get(i) + "," + top.get(i) + ")");
				/*if (i != 0)
				{
					continue;
				}*/
				String correctionString = Scripts.correctionScript(i, top.get(i), left.get(i), width.get(i), height.get(i));
				
				//System.out.println(correctionString);
 				/*System.out.println(i);
 				System.out.println(top);
 				System.out.println(left);
 				System.out.println(width);
 				System.out.println(height);*/
				js.executeScript(correctionString);
			}
			debug_ps.close();
		}
	}
}

class WebNode{
	private int id;
	private ArrayList<WebNode> children;
	private float x, y, w, h;

	public WebNode()
	{
		children = new ArrayList<WebNode>();
		x = y = w = h = 0;
		id = 0;
		
	}
	
	public static WebNode buildFromJSON(String s)
	{
		WebNode n = new WebNode();
		try 
		{
		
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(s);
			n.id = Integer.valueOf(obj.get("nodeName").toString());
			n.x = Float.valueOf(obj.get("x").toString());
			n.y = Float.valueOf(obj.get("y").toString());
			n.w = Float.valueOf(obj.get("width").toString());
			n.h = Float.valueOf(obj.get("height").toString());
			JSONArray a = (JSONArray) obj.get("children");
			for(Object o: a){
				if ( o instanceof JSONObject ) {
					
					n.children.add(buildFromJSON(((JSONObject) o).toJSONString()));
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(n.id);
			System.out.println(s);
		}
		return n;
	}

	public int getId() {
		return id;
	}

	public ArrayList<WebNode> getChildren() {
		return children;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getW() {
		return w;
	}

	public float getH() {
		return h;
	}
}

//Extension of Pagegen boxes to allow manual id assignment 
class IdedBox extends Box
{
	public IdedBox(int id, float x, float y, float w, float h) {
		super(x, y, w, h);
		this.m_id = id;
	}

	public void addChild(Box r)
	{
		m_children.add(r);
	}
}



//Listener test for cornipickle statements
class Listener implements EvaluationListener
{

	@Override
	public void evaluationEvent(CorniSelWebDriver driver, Interpreter interpreter) {
		System.out.println(driver.getVerdicts());
	}

}


