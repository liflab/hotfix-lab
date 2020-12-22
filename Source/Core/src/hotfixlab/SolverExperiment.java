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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.ExperimentException;
import ca.uqac.lif.pagen.Box;
import ca.uqac.lif.pagen.LayoutConstraint;
import ca.uqac.lif.pagen.OplRenderer;
import ca.uqac.lif.pagen.LayoutConstraint.Contained;
import ca.uqac.lif.pagen.LayoutConstraint.Disjoint;
import ilog.concert.IloException;
import ilog.opl.IloOplCompiler;
import ilog.opl.IloOplErrorHandler;
import ilog.opl.IloOplFactory;
import ilog.opl.IloOplModel;
import ilog.opl.IloOplModelSource;
import ilog.opl.IloOplRunConfiguration;
import ilog.opl.IloOplSettings;

/**
 * Experiment that generates an OPL input file from a web page,
 * sends it to CPLEX and retrieves the result.
 * @author Xavier Chamberland-Thibeault, Sylvain Hallé
 */
public class SolverExperiment extends Experiment
{
	/**
	 * Name of parameter "time"
	 */
	public static final transient String TIME = "Duration";
	
	/**
	 * Name of parameter "tree depth"
	 */
	public static final transient String DEPTH = "Tree depth";
	
	/**
	 * Name of parameter "width"
	 */
	public static final transient String WIDTH = "Tree width";
	
	/**
	 * Name of parameter "tree size"
	 */
	public static final transient String SIZE = "Tree size";
	
	/**
	 * Name of parameter "number of constraints"
	 */
	public static final transient String NUM_CONSTRAINTS = "Constraints";
	
	/**
	 * Name of parameter "number of faults"
	 */
	public static final transient String NUM_FAULTS = "Faults";
	
	/**
	 * A provider to obtain a box model
	 */
	protected transient BoxProvider m_boxProvider;

	/**
	 * Creates a new instance of experiment.
	 */
	public SolverExperiment(BoxProvider provider)
	{
		super();
		describe(TIME, "The time taken to solve the constraints, in milliseconds");
		describe(DEPTH, "The maximum depth of the DOM tree");
		//describe(WIDTH, "The maximum degree of a node in the DOM tree");
		describe(SIZE, "The number of nodes in the DOM tree");
		describe(NUM_FAULTS, "The number of faults present in the page");
		describe(NUM_CONSTRAINTS, "The number of alignment constraints applied to the elements of the page");
		m_boxProvider = provider;
	}

	@Override
	public void execute() throws ExperimentException, InterruptedException
	{
		try
		{
			// Get page
			Box page = m_boxProvider.getBox();
			writeStats(page);

			// Open output streams 
			FileOutputStream oplFileStream = new FileOutputStream("oplWebSite.txt");
			PrintStream ps = new PrintStream(oplFileStream, true);

			// Declare constraints for CPLEX
			Set<LayoutConstraint> constraints = new HashSet<LayoutConstraint>();
			constraints.addAll(Contained.addContainmentConstraints(page));
			constraints.addAll(Disjoint.addContainmentConstraints(page));
			setInput(NUM_CONSTRAINTS, constraints.size());
			
			// Start stopwatch
			long start_time = System.currentTimeMillis();

			// Generate OPL for CPLEX
			OplRenderer oplRenderer = new OplRenderer();
			oplRenderer.addConstraints(constraints);
			oplRenderer.render(ps, page);
			ps.close();
			oplFileStream.close();

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
			
			// Stop stopwatch
			long end_time = System.currentTimeMillis();
			write(TIME, end_time - start_time);
					
			if (result)
			{
				/* System.out.println();
				 System.out.println();
				 System.out.println("OBJECTIVE: "
						 + opl.getCplex().getObjValue());*/
				//opl.postProcess();
				ByteArrayOutputStream stream = new ByteArrayOutputStream(); //CPlexSolution.txt
				//opl.printSolution(System.out);
				opl.printSolution(stream);
				//String solution = new String(stream.toByteArray());
				//readCPLEXSolution(solution);
			} 
		}
		catch (IOException e)
		{
			throw new ExperimentException(e);
		}
		catch (IloException e)
		{
			throw new ExperimentException(e);
		}
	}
	
	protected void writeStats(Box b)
	{
		setInput(DEPTH, b.getDepth());
		setInput(SIZE, b.getSize());
		setInput(NUM_FAULTS, m_boxProvider.getMisalignmentCount() + m_boxProvider.getOverflowCount() + m_boxProvider.getOverlapCount());
	}
}
