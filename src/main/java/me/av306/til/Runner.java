package me.av306.til;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class Runner
{
	private final File sourceFile;

	private int lineNumber = 0;

	private MemoryHeap randomAccessStack;
	private MemoryHeap persistentStorage;
	private MemoryHeap activeStack = null;
	
	public Runner( File sourceFile )
	{
		this.sourceFile = sourceFile;

		// 32-bit RAM
		this.randomAccessStack = new MemoryHeap( 32 );
		//this.persistentStorage = new MemoryHeap( 64 );
		this.activeStack = this.randomAccessStack;
	}

	public void run()
	{
		// begin parsing hte sourcefile
		try ( BufferedReader sourceFileReader = new BufferedReader( new FileReader( this.sourceFile ) ) )
		{
			sourceFileReader.lines().forEach(
				(line) ->
				{
					lineNumber++;

					// skip comments
					if ( line.startsWith( "#" ) || line.isEmpty() || line.isBlank() ) return;
					
					this.parseLine( line );
				}
			);
		}
		catch ( FileNotFoundException notFound )
		{
			Main.error( "File not found, terminating" );
		}
		catch ( ArrayIndexOutOfBoundsException oobe )
		{
			Main.error( "Out of bounds address at line: " + lineNumber );
		}
		catch ( IOException io )
		{
			Main.error( io );
			io.printStackTrace();
		}

		this.dumpStacks();
	}

	private void parseLine( String line )
	{
		// split the instruction
		String[] instruction = line.split( " " );

		switch ( instruction[0] )
		{
			// set the active stack to a reference to the RAM
			case "[main]" -> this.activeStack = this.randomAccessStack;

			// set a byte
			case "set" ->
			{
				int addr = Integer.parseInt( instruction[1] );

				this.activeStack.write( addr, instruction[2] );
			}

			// add two bytes together and write the result
			case "add" -> {}

			// subtract two bytes and return the result
			case "sub" -> {}

			// interrupt
			// for use when graphics buffer is implemented
			case "int " -> {}

			case "setchar" ->
			{
				int addr = Integer.parseInt( instruction[1] );
				char c = instruction[2].charAt( 0 );

				this.activeStack.writeAsciiChar( addr, c );
			}

			/*case "writestr" ->
			{
				int startAddr = Integer.parseInt( instruction[1] );

				char[] str = instruction[2].toCharArray();

				for ( char c : str )
				{
					int asciiValue = (int) c;

					// each character can be converted to an 8-bit binary number
					String asciiBinaryString = Integer.toBinaryString( asciiValue );
					
				}
			}*/

			default -> Main.error( String.format( "Invalid instruction %s at line: %d", instruction[0], lineNumber ) );
		}
	}

	private void dumpStacks()
	{
		Main.log( "Dumping stack(s)" );

		Main.log( this.randomAccessStack.dumpContentsAsString() );
	}
	
	/*public void run()
	{
		// initialise heaps
		for ( int i = 0; i < this.stackSize; i++ )
		{
			this.randomAccessStack.add( false );
		}
		
		this.activeStack = this.randomAccessStack;
		
		// create the reader for the source file
		try ( BufferedReader sourceFileReader = new BufferedReader( new FileReader( this.sourceFile ) ) )
		{
			sourceFileReader.lines().forEach(
				(line) ->
				{
					// e.g. set 0 1
					String[] instruction = line.split( " " );

					switch ( instruction[0] )
					{
						case "[main]" -> 
						{
							// use main (random-access) stack
							this.activeStack = this.randomAccessStack;
						}

						case "[graphics]" ->
						{
							// use graphics buffer
							// this.activeStack = this.graphicsStack;
							Main.error( "Graphics buffer is not implemented yet, terminating execution" );
							return;
						}

						case "set" ->
						{
							int address = Integer.parseInt( instruction[1] );
							boolean value = Integer.parseInt( instruction[2] ) > 0;

							this.activeStack.set( address, value );
						}

						case "cmp" ->
						{
							int address = Integer.parseInt( instruction[1] );
							int address2 = Integer.parseInt( instruction[2] );
							int targetAddress = Integer.parseInt( instruction[3] );
							
							boolean result = this.activeStack.get( address ) && this.activeStack.get( address2 );
						
							this.activeStack.set( targetAddress, result );
						}

						default -> Main.error( "Syntax error at line " + line );
					}
				}
			);

			// at the end of it all, dump the stacks out to console
			Main.log( "\n\nExecution finished, dumping stack" );
			Main.log( Arrays.toString( this.randomAccessStack.toArray() ) );
			return;
		}
		catch ( FileNotFoundException notFound )
		{
			Main.error( "File not found, processing terminated" );
			return;
		}
		catch ( IOException ioe )
		{
			Main.error( "IOException thrown while parsing source file, terminating" );
			ioe.printStackTrace();
			return;
		}
		catch ( NumberFormatException nfe )
		{
			Main.error( nfe );
		}
	}*/
}