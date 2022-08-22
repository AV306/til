package me.av306.til;

import java.util.ArrayList;

public class MemoryHeap
{
	private final int size;
	private ArrayList<Boolean> stack;
	
	public MemoryHeap( int size )
	{
		// initialise the stack and size
		this.size = size;
		this.stack = new ArrayList<Boolean>( this.size );

		for ( int i = 0; i < this.size; i++ ) this.stack.add( false );
	}

	public void write( int addr, boolean v )
	{
		// may throw ArrayIndexOutOfBoundsException
		this.stack.set( addr, v );
	}

	public boolean read( int addr )
	{
		// may throw ArrayIndexOutOfBoundsException
		return this.stack.get( addr );
	}

	public Object[] dump()
	{
		return this.stack.toArray();
	}
}