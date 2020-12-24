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

import ca.uqac.lif.synthia.random.RandomBoolean;

/**
 * Boolean picker that returns <tt>true</tt> up to a maximum number
 * of times.
 * @author Sylvain Hallé
 */
public class RandomBooleanMax
{
	/**
	 * The number of times that the picker has returned <code>true</code>
	 * so far.
	 */
	protected int m_numTrue = 0;
	
	/**
	 * The maximum number of times that this picker
	 * is allowed to return <tt>true</tt>.
	 */
	protected int m_maxTrue = 0;
	
	/**
	 * Creates a new instance of the picker.
	 * @param max_true The maximum number of times that this picker
	 * is allowed to return <tt>true</tt>.
	 */
	public RandomBooleanMax(int max_true)
	{
		super();
		m_maxTrue = max_true;
	}
	
	public SharedRandomBoolean getRandomBoolean(RandomBoolean picker)
	{
		return new SharedRandomBoolean(picker);
	}
	
	public class SharedRandomBoolean extends RandomBoolean
	{
		/**
		 * The underlying source of random Booleans
		 */
		protected RandomBoolean m_picker;
		
		/**
		 * Creates a new shared random Boolean picker.
		 * @param picker The underlying source of random Booleans.
		 */
		protected SharedRandomBoolean(RandomBoolean picker)
		{
			super();
			m_picker = picker;
		}
		
		@Override
		public Boolean pick()
		{
			if (m_numTrue >= m_maxTrue)
			{
				return false;
			}
			boolean b = m_picker.pick();
			if (b)
			{
				m_numTrue++;
			}
			return b;
		}
		
		@Override
		public void reset()
		{
			m_picker.reset();
			m_numTrue = 0;
		}
		
		@Override
		public SharedRandomBoolean duplicate(boolean with_state)
		{
			if (with_state)
			{
				SharedRandomBoolean rbm = new SharedRandomBoolean(m_picker.duplicate(with_state));
				return rbm;
			}
			return new SharedRandomBoolean(m_picker.duplicate(with_state));
		}
	}
}
