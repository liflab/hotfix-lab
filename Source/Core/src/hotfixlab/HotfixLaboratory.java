/*
  A lab for web site hotfix benchmarking
  Copyright (C) 2020-2021 Xavier Chamberland-Thibeault,
  Stéphane Jacquet and Sylvain Hallé

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package hotfixlab;

import ca.uqac.lif.labpal.CliParser;
import ca.uqac.lif.labpal.CliParser.Argument;
import ca.uqac.lif.labpal.CliParser.ArgumentMap;
import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.table.ExperimentTable;
import ca.uqac.lif.mtnp.plot.TwoDimensionalPlot.Axis;
import ca.uqac.lif.mtnp.plot.gnuplot.Scatterplot;

import static hotfixlab.SolverExperiment.INITIAL_SIZE;
import static hotfixlab.SolverExperiment.NUM_FAULTS;
import static hotfixlab.SolverExperiment.NUM_MODEL_CONSTRAINTS;
import static hotfixlab.SolverExperiment.NUM_VARIABLES;
import static hotfixlab.SolverExperiment.SIZE;
import static hotfixlab.SolverExperiment.TIME;
import static hotfixlab.SolverExperiment.TREE_TYPE;
import static hotfixlab.SolverExperiment.TREE_TYPE_COMPLETE;
import static hotfixlab.SolverExperiment.TREE_TYPE_DEPENDENCY;
import static hotfixlab.SolverExperiment.TREE_TYPE_TRIMMED;

public class HotfixLaboratory extends Laboratory
{
	/**
	 * Number of pages to generate
	 */
	protected int m_numPages = 100;
	
	@Override
	public void setup()
	{
		// Get random seed from lab
		int seed = 1; //getRandomSeed();
		
		// Maximum number of faults in a page
		int max_faults = 1;
		
		// Whether to send the experiments to the external solver
		boolean solve_complete = true, solve_reduced = true;
		
		// Parse CLI arguments
		ArgumentMap arguments = getCliArguments();
		if (arguments.hasOption("no-solve"))
		{
			solve_complete = false;
			solve_reduced = false;
			System.out.println("Solver disabled for all trees by command-line parameter");
		}
		if (arguments.hasOption("no-complete"))
		{
			solve_complete = false;
			System.out.println("Solver disabled for complete trees by command-line parameter");
		}
		if (arguments.hasOption("max-faults"))
		{
			max_faults = Integer.parseInt(arguments.getOptionValue("max-faults").trim());
			System.out.println("Number of faults limited to " + max_faults);
		}

		ExperimentTable et_time = new ExperimentTable(INITIAL_SIZE, TIME);
		et_time.setTitle("Solving time with respect to page size");
		et_time.setNickname("etTimeVsSize");
		add(et_time);
		Scatterplot p_time = new Scatterplot(et_time);
		p_time.setCaption(Axis.X, "Size").setCaption(Axis.Y, "Time (ms)");
		p_time.setLogscale(Axis.X);
		p_time.withLines(false);
		p_time.setTitle(et_time.getTitle());
		p_time.setNickname("pTimeVsSize");
		add(p_time);
		ExperimentTable et_faults = new ExperimentTable(NUM_FAULTS, TIME);
		et_faults.setTitle("Solving time with respect to number of faults");
		et_faults.setNickname("etTimeVsFaults");
		add(et_faults);
		Scatterplot p_faults = new Scatterplot(et_faults);
		p_faults.setCaption(Axis.X, "Faults").setCaption(Axis.Y, "Time (ms)");
		p_faults.setTitle(et_faults.getTitle());
		p_faults.setNickname("pTimeVsFaults");
		add(p_faults);
		ExperimentTable et_constraints = new ExperimentTable(NUM_MODEL_CONSTRAINTS, TIME);
		et_constraints.setTitle("Solving time with respect to number of constraints");
		et_constraints.setNickname("etTimeVsConstraints");
		add(et_constraints);
		Scatterplot p_constraints = new Scatterplot(et_constraints);
		p_constraints.setCaption(Axis.X, "Constraints").setCaption(Axis.Y, "Time (ms)");
		p_constraints.setTitle(et_constraints.getTitle());
		p_constraints.setNickname("pTimeVsConstraints");
		add(p_constraints);
		ComparisonTable t_trim_effect = new ComparisonTable(NUM_VARIABLES);
		t_trim_effect.setTitle("Impact of zone of influence on number of variables");
		t_trim_effect.setNickname("tZoneInfluenceSize");
		add(t_trim_effect);
		Scatterplot p_trim_effect = new Scatterplot(t_trim_effect);
		p_trim_effect.setCaption(Axis.X, "Full tree").setCaption(Axis.Y, "Zone of influence");
		p_trim_effect.setLogscale(Axis.X).setLogscale(Axis.Y);
		p_trim_effect.setTitle(t_trim_effect.getTitle());
		p_trim_effect.setNickname("pZoneInfluenceSize");
		p_trim_effect.withLines(false);
		add(p_trim_effect);
		ComparisonTable t_graph_effect_var = new ComparisonTable(NUM_VARIABLES);
		t_graph_effect_var.setTitle("Impact of dependency graph on number of variables");
		t_graph_effect_var.setNickname("tDepGraphSize");
		add(t_graph_effect_var);
		Scatterplot p_graph_effect_var = new Scatterplot(t_graph_effect_var);
		p_graph_effect_var.setCaption(Axis.X, "Full tree").setCaption(Axis.Y, "Dependency graph");
		p_graph_effect_var.setLogscale(Axis.X).setLogscale(Axis.Y);
		p_graph_effect_var.setTitle(t_graph_effect_var.getTitle());
		p_graph_effect_var.setNickname("pDepGraphSize");
		p_graph_effect_var.withLines(false);
		add(p_graph_effect_var);
		ComparisonTable t_graph_effect_cons = new ComparisonTable(NUM_MODEL_CONSTRAINTS);
		t_graph_effect_cons.setTitle("Impact of dependency graph on number of constraints");
		t_graph_effect_cons.setNickname("tDepGraphConstraints");
		add(t_graph_effect_cons);
		Scatterplot p_graph_effect_cons = new Scatterplot(t_graph_effect_cons);
		p_graph_effect_cons.setCaption(Axis.X, "Full tree").setCaption(Axis.Y, "Dependency graph");
		p_graph_effect_cons.setLogscale(Axis.X).setLogscale(Axis.Y);
		p_graph_effect_cons.setTitle(t_graph_effect_cons.getTitle());
		p_graph_effect_cons.setNickname("pDepGraphConstraints");
		p_graph_effect_cons.withLines(false);
		add(p_graph_effect_cons);
		for (int i = 0; i < m_numPages; i++)
		{
			PickerBoxProvider pbp = new PickerBoxProvider(seed + i, max_faults);
			SolverExperiment e_complete = new SolverExperiment(pbp, solve_complete);
			e_complete.setInput(TREE_TYPE, TREE_TYPE_COMPLETE);
			add(e_complete);
			SolverExperiment e_trimmed = new SolverExperiment(pbp, solve_reduced);
			e_trimmed.setInput(TREE_TYPE, TREE_TYPE_TRIMMED);
			add(e_trimmed, et_time, et_faults, et_constraints);
			t_trim_effect.add(e_complete, e_trimmed);
			SolverExperiment e_graph = new SolverExperiment(pbp, solve_reduced);
			e_graph.setInput(TREE_TYPE, TREE_TYPE_DEPENDENCY);
			add(e_graph, et_time, et_faults, et_constraints);
			t_graph_effect_var.add(e_complete, e_graph);
			t_graph_effect_cons.add(e_complete, e_graph);
		}
		
		// Global macros
		add(new LabStats(this));
	}

	public static void main(String[] args)
	{
		// Nothing else to do here
		HotfixLaboratory.initialize(args, HotfixLaboratory.class);
	}
	
	@Override
	public void setupCli(CliParser parser)
	{
		parser.addArgument(new Argument().withLongName("no-solve").withDescription("Disable solver"));
		parser.addArgument(new Argument().withLongName("no-complete").withDescription("Disable solver for complete trees only"));
		parser.addArgument(new Argument().withLongName("max-faults").withArgument("x").withDescription("Set maximum number of faults in a page to x"));
	}
	
	/**
	 * Gets the size of the largest DOM tree analyzed in the experiments.
	 * @return The maximum size
	 */
	public int getMaxSize()
	{
		int max = 0;
		for (Experiment e : getExperiments())
		{
			max = Math.max(max, e.readInt(INITIAL_SIZE));
		}
		return max;
	}
	
	/**
	 * Gets the total number of different pages analyzed in the experiments.
	 * @return The number of pages
	 */
	public int getNumberOfPages()
	{
		return m_numPages;
	}
}
