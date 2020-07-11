package graph;

public class Edge implements IEdge
{
	private final int from;
	private final int to;

	public Edge(int from, int to)
	{
		this.from = from;
		this.to = to;
	}

	public int getFrom()
	{
		return this.from;
	}

	public int getTo()
	{
		return this.to;
	}

	public Edge copy()
	{
		return new Edge(from, to);
	}

	public Edge reverse()
	{
		return new Edge(to, from);
	}

	public String toString()
	{
		return "(" + from + "->" + to + ")";
	}
}
