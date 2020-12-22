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

import ca.uqac.lif.pagen.Box;
import ca.uqac.lif.pagen.HorizontalFlowLayout;
import ca.uqac.lif.pagen.LayoutManager;
import ca.uqac.lif.pagen.RandomBoxPicker;
import ca.uqac.lif.pagen.VerticalFlowLayout;
import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.random.PoissonInteger;
import ca.uqac.lif.synthia.random.RandomBoolean;
import ca.uqac.lif.synthia.random.RandomFloat;
import ca.uqac.lif.synthia.random.RandomInteger;
import ca.uqac.lif.synthia.random.RandomIntervalFloat;
import ca.uqac.lif.synthia.util.ElementPicker;

/**
 * Provides a box by using PageGen's random page generator.
 */
public class PickerBoxProvider implements BoxProvider
{
	/**
	 * The picker used to provide a box
	 */
	Picker<Box> m_picker;
	
	protected transient HorizontalFlowLayout m_horizontalFlowLayout1;
	
	protected transient HorizontalFlowLayout m_horizontalFlowLayout2;
	
	protected transient VerticalFlowLayout m_verticalFlowLayout;
	
	/**
	 * Creates a new instance of picker box provider.
	 * @param picker The picker used to provide a box
	 */
	public PickerBoxProvider(int seed)
	{
		super();
		int min_depth = 2; // 4
		int max_depth = 5; // 5
		float p_degree = 1.5f; //2.2f;
		float p_misalignment = 0.005f, p_overlap = 0.005f, p_overflow = 0.005f;

		// Initialize RNGs and seed
		RandomInteger depth = new RandomInteger(min_depth, max_depth); // 6-22
		PoissonInteger degree = new PoissonInteger(p_degree);
		RandomIntervalFloat width = new RandomIntervalFloat(5, 20);
		RandomIntervalFloat height = new RandomIntervalFloat(5, 10);
		RandomFloat float_source = new RandomFloat();
		RandomInteger row_size = new RandomInteger(0, 10);
		RandomInteger column_size = new RandomInteger(0, 10);
		RandomBoolean misalignment = new RandomBoolean(p_misalignment);
		RandomInteger misalignment_shift = new RandomInteger(2, 10);
		RandomBoolean overlap = new RandomBoolean(p_overlap);
		RandomInteger overlap_shift = new RandomInteger(2, 10);
		RandomBoolean overflow = new RandomBoolean(p_overflow);
		RandomInteger overflow_shift = new RandomInteger(2, 10);
		ElementPicker<LayoutManager> layout = new ElementPicker<LayoutManager>(float_source);
		if (seed >= 0)
		{
			depth.setSeed(seed);
			degree.setSeed(seed + 1);
			float_source.setSeed(seed + 2);
			width.setSeed(seed + 3);
			height.setSeed(seed + 4);
			row_size.setSeed(seed + 5);
			column_size.setSeed(seed + 6);
			misalignment_shift.setSeed(seed + 7);
			misalignment.setSeed(seed + 8);
			overlap_shift.setSeed(seed + 9);
			overlap.setSeed(seed + 10);
			overflow_shift.setSeed(seed + 11);
			overflow.setSeed(seed + 12);
			//rand_color.setSeed(seed + 13);
		}

		// Setup box picker
		m_horizontalFlowLayout1 = new HorizontalFlowLayout();
		m_horizontalFlowLayout1.setAlignmentFault(misalignment, misalignment_shift);
		m_horizontalFlowLayout1.setOverlapFault(overlap, overlap_shift);
		m_horizontalFlowLayout1.setOverflowFault(overflow, overflow_shift);
		m_horizontalFlowLayout2 = new HorizontalFlowLayout(row_size);
		m_horizontalFlowLayout2.setAlignmentFault(misalignment, misalignment_shift);
		m_horizontalFlowLayout2.setOverlapFault(overlap, overlap_shift);
		m_horizontalFlowLayout2.setOverflowFault(overflow, overflow_shift);
		m_verticalFlowLayout = new VerticalFlowLayout(column_size);
		m_verticalFlowLayout.setAlignmentFault(misalignment, misalignment_shift);
		m_verticalFlowLayout.setOverlapFault(overlap, overlap_shift);
		m_verticalFlowLayout.setOverflowFault(overflow, overflow_shift);
		layout.add(m_horizontalFlowLayout1, 0.2);
		layout.add(m_horizontalFlowLayout2, 0.4);
		layout.add(m_verticalFlowLayout, 0.4);
		m_picker = new RandomBoxPicker(degree, depth, layout, width, height);
	}
	
	@Override
	public Box getBox()
	{
		m_picker.reset();
		return m_picker.pick();
	}

	@Override
	public int getOverlapCount()
	{
		return m_horizontalFlowLayout1.getOverlapCount() + m_horizontalFlowLayout2.getOverlapCount() + m_verticalFlowLayout.getOverlapCount();
	}
	
	@Override
	public int getOverflowCount()
	{
		return m_horizontalFlowLayout1.getOverflowCount() + m_horizontalFlowLayout2.getOverflowCount() + m_verticalFlowLayout.getOverflowCount();
	}
	
	@Override
	public int getMisalignmentCount()
	{
		return m_horizontalFlowLayout1.getMisalignmentCount() + m_horizontalFlowLayout2.getMisalignmentCount() + m_verticalFlowLayout.getMisalignmentCount();
	}
}
