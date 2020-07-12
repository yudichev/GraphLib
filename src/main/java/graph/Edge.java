package graph;

/**
 * The root class that implements a general representation of an edge.
 * It contains vertex IDs: 'from' and 'to'.
 * By default, the edge is directed, even if used in undirected graphs.
 * 'From' and 'to' are formal and can be swapped in undirected graphs.
 * In directed graphs, the direction is 'from' -> 'to'.
 */
public class Edge
{
	private final int from;
	private final int to;

	/**
	 * Constructor of Edge
	 * @param from start vertex ID
	 * @param to end vertex ID
	 */
	public Edge(int from, int to)
	{
		this.from = from;
		this.to = to;
	}

	/**
	 * Returns the start vertex ID
	 * @return vertex ID
	 */
	public int getFrom()
	{
		return this.from;
	}

	/**
	 * Returns the end vertex ID
	 * @return end vertex ID
	 */
	public int getTo()
	{
		return this.to;
	}

	/**
	 * Returns a new instance of the edge with fields values copied.
	 * @return a new instance of Edge
	 */
	public Edge copy()
	{
		return new Edge(from, to);
	}

	/**
	 * Returns a new instance of edge with swapped 'from' and 'to'.
	 * Thus the direction is reversed to opposite.
	 * @return a new instance of Edge
	 */
	public Edge reverse()
	{
		return new Edge(to, from);
	}

	/**
	 * Returns a string representation of the edge
	 * @return a string representation of edge
	 */
	public String toString()
	{
		return "(" + from + "," + to + ")";
	}
}
