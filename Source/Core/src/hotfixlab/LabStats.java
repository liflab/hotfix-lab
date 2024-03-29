/*
    A benchmark for Cayley graph test sequence generation
    Copyright (C) 2020 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package hotfixlab;

import java.util.Map;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.json.JsonParser.JsonParseException;
import ca.uqac.lif.labpal.FileHelper;
import ca.uqac.lif.labpal.macro.MacroMap;

/**
 * Macro map producing various statistics about the experimental results
 * in the lab
 * @author Sylvain Hallé
 */
public class LabStats extends MacroMap 
{
	/**
	 * Instantiates the macro and defines its named data points
	 * @param lab The lab from which to fetch the values
	 */
	public LabStats(HotfixLaboratory lab)
	{
		super(lab);
		add("machinestring", "Basic info about the machine running the lab");
		add("jvmram", "RAM available to the JVM");
		add("numexperiments", "The number of experiments in the lab");
		add("numdatapoints", "The number of data points in the lab");
		add("numpages", "The number of pages used in our experiments");
		add("maxsize", "The maximum size of pages");
		add("solvertimeout", "The time limit (in s) given to the solver");
	}

	@Override
	public void computeValues(Map<String,JsonElement> map)
	{
		map.put("machinestring", new JsonString(getMachineString()));
		map.put("jvmram", new JsonString(getMemory()));
		map.put("numexperiments", new JsonNumber(m_lab.getExperimentIds().size()));
		map.put("numdatapoints", new JsonNumber(m_lab.countDataPoints()));
		map.put("numpages", new JsonNumber(((HotfixLaboratory) m_lab).getNumberOfPages()));
		map.put("maxsize", new JsonNumber(((HotfixLaboratory) m_lab).getMaxSize()));
		map.put("solvertimeout", new JsonNumber(SolverExperiment.TIMEOUT));
	}

	protected String getMemory()
	{
		long mem = Runtime.getRuntime().maxMemory();
		long mem_in_mb = mem / (1024 * 1024);
		return Long.toString(mem_in_mb);
	}
	
	protected String getMachineString()
	{
		String host = m_lab.getHostName();
		JsonParser parser = new JsonParser();
		try 
		{
			JsonMap je = (JsonMap) parser.parse(FileHelper.internalFileToString(HotfixLaboratory.class, "machine-specs.json"));
			if (!je.containsKey(host))
				return "";
			JsonMap els = (JsonMap) je.get(host);
			return ((JsonString) els.get("CPU")).stringValue() + " running " 
					+ ((JsonString) els.get("OS")).stringValue();
		} 
		catch (JsonParseException e) 
		{
			// Do nothing
		}
		return "";
	}
}
