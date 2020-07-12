package graph;

import org.junit.Assert;
import org.junit.Test;

public class TestEdges
{
	@Test
	public void testEdgeCreate()
	{
		int from = 1;
		int to = 2;
		Edge edge = new Edge(from, to);
		Assert.assertEquals(from, edge.getFrom());
		Assert.assertEquals(to, edge.getTo());
	}

	@Test
	public void testEdgeCopy()
	{
		int from = 1;
		int to = 2;
		float weight = 1.5f;
		Edge edge = new Edge(from, to);
		Edge edgeCopy = edge.copy();

		Assert.assertEquals(from, edgeCopy.getFrom());
		Assert.assertEquals(to, edgeCopy.getTo());
	}

	@Test
	public void testEdgeReverse()
	{
		int from = 1;
		int to = 2;
		Edge edge = new Edge(from, to);
		Edge edgeReversed = edge.reverse();

		Assert.assertEquals(to, edgeReversed.getFrom());
		Assert.assertEquals(from, edgeReversed.getTo());
	}

	@Test
	public void testEdgeToString()
	{
		int from = 1;
		int to = 2;
		Edge edge = new Edge(from, to);
		Assert.assertEquals("(1,2)", edge.toString());
	}


		@Test
	public void testWeighedEdgeCreate()
	{
		int from = 1;
		int to = 2;
		float weight = 1.5f;
		WeighedEdge edge = new WeighedEdge(from, to, weight);
		Assert.assertEquals(from, edge.getFrom());
		Assert.assertEquals(to, edge.getTo());
		Assert.assertEquals(weight, edge.getWeight(), 0.0f);
	}

	@Test
	public void testWeighedEdgeCopy()
	{
		int from = 1;
		int to = 2;
		float weight = 1.5f;
		WeighedEdge edge = new WeighedEdge(from, to, weight);
		WeighedEdge edgeCopy = edge.copy();

		Assert.assertEquals(from, edgeCopy.getFrom());
		Assert.assertEquals(to, edgeCopy.getTo());
		Assert.assertEquals(weight, edgeCopy.getWeight(), 0.0f);
	}

	@Test
	public void testWeighedEdgeReverse()
	{
		int from = 1;
		int to = 2;
		float weight = 1.5f;
		WeighedEdge edge = new WeighedEdge(from, to, weight);
		WeighedEdge edgeReverse = edge.reverse();

		Assert.assertEquals(to, edgeReverse.getFrom());
		Assert.assertEquals(from, edgeReverse.getTo());
		Assert.assertEquals(weight, edgeReverse.getWeight(), 0.0f);
	}

	@Test
	public void testWeightEdgeToString()
	{
		int from = 1;
		int to = 2;
		float weight = 1.5f;
		WeighedEdge edge = new WeighedEdge(from, to, weight);
		Assert.assertEquals("(1,2|1.5)", edge.toString());
	}

}
