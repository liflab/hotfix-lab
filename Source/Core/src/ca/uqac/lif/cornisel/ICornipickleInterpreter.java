package ca.uqac.lif.cornisel;

import java.util.Map;

import org.openqa.selenium.WebElement;

import ca.uqac.lif.cornipickle.Verdict;
import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;

public interface ICornipickleInterpreter {
	void setCornipickleProperties(String properties) throws ParseException;
	void evaluateAll(WebElement event);
	CorniSelWebDriver.UpdateMode getUpdateMode();
	Map<StatementMetadata,Verdict> getVerdicts();
	void resetHistory();
	void clear();
}
