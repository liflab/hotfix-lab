package ca.uqac.lif.cornisel;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class FileUtilities
{
	/**
	 * Reads the contents of an internal text file and puts it in a string.
	 * @param reference The reference class used as the filesystem's root 
	 * @param filename The name of the file to read from
	 * @return The file contents, or <code>null</code> if no file could be
	 * read
	 */
	public static String readFrom(Class<?> reference, String filename)
	{
		InputStream is = reference.getResourceAsStream(filename);
		if (is == null)
		{
			return null;
		}
		Scanner scanner = new Scanner(is);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		while (scanner.hasNextLine())
		{
			ps.println(scanner.nextLine());
		}
		scanner.close();
		return new String(baos.toByteArray());
	}
}
