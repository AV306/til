package me.av306.til;

import java.io.File;

public class Main
{
	public static void main( String[] args )
	{
		try
		{
			File sourceFile = new File( args[0] );

			Runner runner = new Runner( sourceFile );

			runner.run();
		}
		catch ( ArrayIndexOutOfBoundsException oobe )
		{
			error( "Not enough arguments!" );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	public static void error( Object message )
	{
		System.err.println( message );
	}

	public static void log( Object message )
	{
		System.out.println( message );
	}
}