package ca.uqac.lif.cornisel;

import ca.uqac.lif.cornipickle.Interpreter;

public interface EvaluationListener {
	void evaluationEvent(CorniSelWebDriver driver, Interpreter interpreter);
}
