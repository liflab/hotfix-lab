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

import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.table.ExperimentTable;
import ca.uqac.lif.mtnp.plot.TwoDimensionalPlot.Axis;
import ca.uqac.lif.mtnp.plot.gnuplot.Scatterplot;

import static hotfixlab.SolverExperiment.NUM_CONSTRAINTS;
import static hotfixlab.SolverExperiment.NUM_FAULTS;
import static hotfixlab.SolverExperiment.SIZE;
import static hotfixlab.SolverExperiment.TIME;

public class HotfixLaboratory extends Laboratory
{

	@Override
	public void setup()
	{
		// Get random seed from lab
		int seed = 1; //getRandomSeed();

		// Number of pages to generate
		int num_pages = 50;

		ExperimentTable et_time = new ExperimentTable(SIZE, TIME);
		et_time.setTitle("Solving time with respect to page size");
		add(et_time);
		Scatterplot p_time = new Scatterplot(et_time);
		p_time.setCaption(Axis.X, "Size").setCaption(Axis.Y, "Time (ms)");
		p_time.setTitle(et_time.getTitle());
		add(p_time);
		ExperimentTable et_faults = new ExperimentTable(NUM_FAULTS, TIME);
		et_faults.setTitle("Solving time with respect to number of faults");
		add(et_faults);
		Scatterplot p_faults = new Scatterplot(et_faults);
		p_faults.setCaption(Axis.X, "Faults").setCaption(Axis.Y, "Time (ms)");
		p_faults.setTitle(et_faults.getTitle());
		add(p_faults);
		ExperimentTable et_constraints = new ExperimentTable(NUM_CONSTRAINTS, TIME);
		et_constraints.setTitle("Solving time with respect to number of constraints");
		add(et_constraints);
		Scatterplot p_constraints = new Scatterplot(et_constraints);
		p_constraints.setCaption(Axis.X, "Constraints").setCaption(Axis.Y, "Time (ms)");
		p_constraints.setTitle(et_constraints.getTitle());
		add(p_constraints);
		for (int i = 0; i < num_pages; i++)
		{
			PickerBoxProvider pbp = new PickerBoxProvider(seed + i);
			SolverExperiment e = new SolverExperiment(pbp);
			add(e, et_time, et_faults, et_constraints);
		}
	}

	public static void main(String[] args)
	{
		// Nothing else to do here
		HotfixLaboratory.initialize(args, HotfixLaboratory.class);
	}

}
