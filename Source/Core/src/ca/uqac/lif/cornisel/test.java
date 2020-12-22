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
import ca.uqac.lif.pagen.OplRenderer;

import ilog.concert.*;
import ilog.opl.*;

public class test {

	static PrintStream print;
	static IdedBox boxedWebSite;
	static ArrayList<Double> top, left, width, height;

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
			corniSelDriver.get("https://www.google.ca");

			//Recover all the DOM tree of the web page
			JavascriptExecutor js = (JavascriptExecutor)  chromeDriver;			
			String n = js.executeScript(Scripts.nodifyScript).toString();
			WebNode w = new WebNode(n);
			IdedBox boxedWebSite = generateBoxedWebSite(w);

			//Declare constraints for CPLEX
			Set<LayoutConstraint> constraints = new HashSet<LayoutConstraint>();
			constraints.addAll(Contained.addContainmentConstraints(boxedWebSite));
			constraints.addAll(Disjoint.addContainmentConstraints(boxedWebSite));

			//Generate OPL for CPLEX
			OplRenderer oplRenderer = new OplRenderer();
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
			System.out.println(opl.getCplex().getStatus());
			if (result) {
				/* System.out.println();
				 System.out.println();
				 System.out.println("OBJECTIVE: "
						 + opl.getCplex().getObjValue());*/
				//opl.postProcess();
				ByteArrayOutputStream stream = new ByteArrayOutputStream(); //CPlexSolution.txt
				//opl.printSolution(System.out);
				opl.printSolution(stream);
				String solution = new String(stream.toByteArray());
				//String finalString = new String(stream.toByteArray());
				// print.println(finalString);
				readCPLEXSolution(solution);
				//correctWebPage(js);
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

		root.getChildren().forEach((n)->rootBox.addChild(generateBoxedWebSite(n)));

		return rootBox;
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

	private static void correctWebPage(JavascriptExecutor js)
	{
		//Check if there are any corrections and if there was no error during corrections generation
		if((top.size() == left.size()) && 
				(top.size() == width.size()) && 
				(width.size() == height.size()) &&
				!top.isEmpty())
		{
			//Apply corrections for every element of the web page
			for(int i = 0; i < top.size(); i++)
			{
				String correctionString = Scripts.correctionScript(i, top.get(i), left.get(i), width.get(i), height.get(i));
				/*System.out.println(correctionString);
 				System.out.println(i);
 				System.out.println(top);
 				System.out.println(left);
 				System.out.println(width);
 				System.out.println(height);*/
				js.executeScript(correctionString);
			}
		}
	}
}

class WebNode{
	private int id;
	private ArrayList<WebNode> children;
	private float x, y, w, h;

	public WebNode(String s)
	{
		children = new ArrayList<WebNode>();
		x = y = w = h = 0;

		try 
		{
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(s);
			id = Integer.valueOf(obj.get("nodeName").toString());
			x = Float.valueOf(obj.get("x").toString());
			y = Float.valueOf(obj.get("y").toString());
			w = Float.valueOf(obj.get("width").toString());
			h = Float.valueOf(obj.get("height").toString());
			JSONArray a = (JSONArray) obj.get("children");
			for(Object o: a){
				if ( o instanceof JSONObject ) {
					children.add(new WebNode(((JSONObject) o).toJSONString()));
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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


