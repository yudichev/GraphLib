package graph;


public class EdgeImpl implements IEdge
{
	private final int from;
	private final int to;

	EdgeImpl(int from, int to)
	{
		this.from = from;
		this.to = to;
	}

	@Override public int getFrom()
	{
		return this.from;
	}

	@Override public int getTo()
	{
		return this.to;
	}


	public static IEdge newInstance(int from, int to)
	{
		return new EdgeImpl(from, to);
	}
}
