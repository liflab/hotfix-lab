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

import java.util.Set;

import ca.uqac.lif.pagen.Box;
import ca.uqac.lif.pagen.BoxDependency;

/**
 * Provides a {@link Box} object.
 */
public interface BoxProvider
{
	/**
	 * Gets a box.
	 * @return A box
	 */
	public Box getBox();
	
	public int getMisalignmentCount();
	
	public int getOverlapCount();
	
	public int getOverflowCount();
	
	public Set<BoxDependency> getDependencies();
}
