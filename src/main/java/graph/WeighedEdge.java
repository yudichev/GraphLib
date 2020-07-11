package graph;

public class WeighedEdge extends Edge implements IWeighedEdge
{
	private final float weight;

	public WeighedEdge(int from, int to, float weight)
	{
		super(from, to);
		this.weight = weight;
	}

	public float getWeight()
	{
		return weight;
	}

	@Override public WeighedEdge copy()
	{
		return new WeighedEdge(getFrom(), getTo(), this.weight);
	}

	@Override public WeighedEdge reverse()
	{
		return new WeighedEdge(getTo(), getFrom(), this.weight);
	}

	public String toString()
	{
		return "(" + getFrom() + "->" + getTo() + "; weight=" + getWeight() + ")";
	}
}
