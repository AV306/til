package me.av306.til;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;

/**
 * Class managing a stack / heap of memory
 * in the interpreter.
 *
 * Contains methods to read from and write to the stack.
 */
public class MemoryHeap
{
	private final int size;
	private ArrayList<Byte> stack;

	private final HexFormat hexFormat;

	/**
	 * @param size: the size of the stack in bytes
	 */
	public MemoryHeap( int size )
	{
		// initialise the stack and size
		this.size = size;
		this.stack = new ArrayList<Byte>( this.size );

		for ( int i = 0; i < this.size; i++ ) this.stack.add( Byte.MIN_VALUE );

		this.hexFormat = HexFormat.of();
	}

	/**
	 * Write a single byte to the stack.
	 * @param addr: the address of the byte
	 * @paran b: the byte to write
	 */
	public void write( int addr, byte b )
	{
		// may throw ArrayIndexOutOfBoundsException
		this.stack.set( addr, b );
	}

	/**
	 * Write a hex string to the stack.
   * @param startAddr: the start address of the byte
   * @param hexStr: the hexadecimal string to write
	 */
	public void write( int startAddr, String hexStr )
	{
		byte[] values = this.hexFormat.parseHex( hexStr );

		// this may overwrite bytes in memory
		for ( byte b : values )
		{
			this.write( startAddr, b );
			startAddr++;
		}
	}

	/**
	 * Write an ASCII char to the stack.
	 * @param addr: the address of the character.
	 * @param c: the ASCII character to write. Numbers will be encoded as their ASCII representation.
	 */
	public void writeAsciiChar( int addr, char c )
	{
		byte b = (byte) c; // this is a bit weird, see below
		// https://www.google.com.sg/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&ved=2ahUKEwjDr46Sh9z5AhVr7nMBHU4-C8gQFnoECAYQAQ&url=https%3A%2F%2Fstackoverflow.com%2Fquestions%2F4958658%2Fchar-into-byte-java&usg=AOvVaw2-CBSpYEzuDFw0_pBQ1tXc
		
		this.write( addr, b );
	}

	/**
	 * Read a single byte from the stack.
	 * @param addr: the address of the byte
	 */
	public byte read( int addr )
	{
		// may throw ArrayIndexOutOfBoundsException
		return this.stack.get( addr );
	}

	/**
	 * Read an array of bytes from the stack. May throw AIOOBE.
	 * @param startAddr: the start address of the array
	 * @param length: the length of the array
	 */
	public byte[] readArray( int startAddr, int length )
	{
		ArrayList<Byte> temp = new ArrayList<>( length );
		for ( int i = 0; i < length; i++ )
		{
			temp.add( this.read( startAddr + i ) );
		}

		return temp.toArray( new Byte[]{} );
	}

	/**
	 * Read an ASCII character from the stack.
	 * Even if the byte is not meant to be an ASCII character, it will be formatted as one.
	 * @param addr: the address of the byte.
	 */
	public char readChar( int addr )
	{
		return (char) this.read( addr );
	}

	/**
	 * Read an array of ASCII characters from the stack (aka C-style string)
	 * See readArray() and readChar().
	 * @param startAddr: the start address of the array.
	 * @param length: the length of the array
	 */
	public char[] readCharArray( int startAddr, int length )
	{
		ArrayList<Character> temp = new ArrayList<>( length );
		for ( int i = 0; i < length; i++ )
		{
			temp.add( this.readChar( startAddr + i ) );
		}

		return temp.toArray( new Character[]{} );
	}

	public String dumpContentsAsString()
	{
		return Arrays.toString( this.readArray( 0, this.size ) );
	}
}