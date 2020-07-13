package graph;

/**
 * Implements a weighed edges. Extends {@link Edge} by adding a weight.
 * Weight is represented by a {@code float} value.
 */
public class WeighedEdge extends Edge
{
	private final float weight;

	/**
	 * Constructor
	 * @param from starting vertex ID
	 * @param to ending vertex ID
	 * @param weight weight factor
	 */
	public WeighedEdge(int from, int to, float weight)
	{
		super(from, to);
		this.weight = weight;
	}

	/**
	 * Returns the weight of the edge
	 * @return weight
	 */
	public float getWeight()
	{
		return weight;
	}

	/**
	 * Returns a new object with all fields copied
	 * @return a new instance
	 */
	@Override public WeighedEdge copy()
	{
		return new WeighedEdge(getFrom(), getTo(), this.weight);
	}

	/**
	 * Returns a new instance of the edge with swapped 'from' and 'to' vertex IDs.
	 * @return a new instance
	 */
	@Override public WeighedEdge reverse()
	{
		return new WeighedEdge(getTo(), getFrom(), this.weight);
	}

	public String toString()
	{
		return "(" + getFrom() + "," + getTo() + "|" + getWeight() + ")";
	}
}
