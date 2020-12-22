package ca.uqac.lif.cornisel;

public final class Scripts {

	/** 
	 * Javascript that recovers all the DOM tree of a web page
	 */
	public static final String nodifyScript = FileUtilities.readFrom(Scripts.class, "resources/nodify.js");
	
	protected static final String correctScript = FileUtilities.readFrom(Scripts.class, "resources/correct.js");
	
	public static String correctionScript(int id, double top, double left, double width, double height)
	{
		String script = correctScript;
		script = script.replaceAll("%ID%", Integer.toString(id));
		script = script.replaceAll("%TOP%", Double.toString(top));
		script = script.replaceAll("%LEFT%", Double.toString(left));
		script = script.replaceAll("%WIDTH%", Double.toString(width));
		script = script.replaceAll("%HEIGHT%", Double.toString(height));
		return script;
	}
	

}
