package graph;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestGraphSingleThreaded
{
	@Test
	public void testCreateDirectedGraph()
	{
		Graph<String,Edge> graph = SimpleGraph.newDirected(5, 10);
		graph.addVertex("Vertex 1");
		graph.addVertex("Vertex 2");
		graph.addVertex("Vertex 3");
		graph.addVertex("Vertex 4");
		graph.addVertex("Vertex 5");


		graph.addEdge(new Edge(1,2));
		graph.addEdge(new Edge(2,3));
		graph.addEdge(new Edge(2,4));
		graph.addEdge(new Edge(3,5));
		graph.addEdge(new Edge(4,3 ));
		graph.addEdge(new Edge(5,2));
		graph.addEdge(new Edge(1,3));

		List<Edge> path = graph.getPath(1, 4);
		Assert.assertFalse(path.isEmpty());
		Edge firstEdge = path.get(0);
		Assert.assertEquals(1, firstEdge.getFrom());
		Edge lastEdge = path.get(path.size() - 1);
		Assert.assertEquals(4, lastEdge.getTo());

		for(int i = 1; i < path.size(); i++)
		{
			Assert.assertEquals(path.get(i - 1).getTo(), path.get(i).getFrom());
		}
	}

	@Test
	public void testAddEdgeWithWrongVertexID()
	{
		Graph<String,Edge> graph = SimpleGraph.newDirected(5, 10);
		graph.addVertex("Vertex 1");
		graph.addVertex("Vertex 2");


		graph.addEdge(new Edge(1,2));

		try
		{
			graph.addEdge(new Edge(2, 3));
			Assert.fail("An exception must have been thrown here");
		}
		catch(IllegalArgumentException iaex)
		{
			Assert.assertEquals("Unable to add edge: vertex 2 or 3 not found.", iaex.getMessage());
		}
		catch(Throwable ex)
		{
			Assert.fail("IllegalArgumentException was expected here. Thrown: " + ex.getClass());
		}
	}


	@Test
	public void testDirectedGraphPathEmpty()
	{
		Graph<String,Edge> graph = SimpleGraph.newDirected(10, 20);
		int v1 = graph.addVertex("Vertex 1");
		int v2 = graph.addVertex("Vertex 2");
		int v3 = graph.addVertex("Vertex 3");
		int v4 = graph.addVertex("Vertex 4");
		int v5 = graph.addVertex("Vertex 5");
		int v6 = graph.addVertex("Vertex 6");
		int v7 = graph.addVertex("Vertex 7");
		int v8 = graph.addVertex("Vertex 8");
		int v9 = graph.addVertex("Vertex 9");
		int v10 = graph.addVertex("Vertex 10");




		graph.addEdge(new Edge(v1,v2));
		graph.addEdge(new Edge(v2,v3));
		graph.addEdge(new Edge(v3,v4));
		graph.addEdge(new Edge(v1,v5));
		graph.addEdge(new Edge(v5,v6));
		graph.addEdge(new Edge(v6,v7));
		graph.addEdge(new Edge(v7,v8));
		graph.addEdge(new Edge(v7,v9));
		graph.addEdge(new Edge(v9,v10));
		graph.addEdge(new Edge(v8,v5));
		graph.addEdge(new Edge(v7,v3));


		List<Edge> path = graph.getPath(3, 7);
		Assert.assertTrue(path.isEmpty());
	}


	@Test
	public void testCreateUndirectedGraph()
	{
		Graph<String,Edge> graph = SimpleGraph.newUndirected(10, 20);
		int v1 = graph.addVertex("Vertex 1");
		int v2 = graph.addVertex("Vertex 2");
		int v3 = graph.addVertex("Vertex 3");
		int v4 = graph.addVertex("Vertex 4");
		int v5 = graph.addVertex("Vertex 5");
		int v6 = graph.addVertex("Vertex 6");
		int v7 = graph.addVertex("Vertex 7");
		int v8 = graph.addVertex("Vertex 8");
		int v9 = graph.addVertex("Vertex 9");
		int v10 = graph.addVertex("Vertex 10");


		graph.addEdge(new Edge(v1,v2));
		graph.addEdge(new Edge(v2,v3));
		graph.addEdge(new Edge(v3,v4));
		graph.addEdge(new Edge(v1,v5));
		graph.addEdge(new Edge(v5,v6));
		graph.addEdge(new Edge(v6,v7));
		graph.addEdge(new Edge(v7,v8));
		graph.addEdge(new Edge(v7,v9));
		graph.addEdge(new Edge(v9,v10));
		graph.addEdge(new Edge(v8,v5));
		graph.addEdge(new Edge(v7,v3));


		List<Edge> path = graph.getPath(3, 7);
		for(int i = 1; i < path.size(); i++)
		{
			Assert.assertEquals(path.get(i - 1).getTo(), path.get(i).getFrom());
		}

		path = graph.getPath(1, 7);
		for(int i = 1; i < path.size(); i++)
		{
			Assert.assertEquals(path.get(i - 1).getTo(), path.get(i).getFrom());
		}
	}

	@Test
	public void testCreateDirectedGraphWithWeighedEdges()
	{
		Graph<String,WeighedEdge> graph = SimpleGraph.newDirected(5, 10);
		graph.addVertex("Vertex 1");
		graph.addVertex("Vertex 2");
		graph.addVertex("Vertex 3");
		graph.addVertex("Vertex 4");
		graph.addVertex("Vertex 5");


		graph.addEdge(new WeighedEdge(1,2, 1.4f));
		graph.addEdge(new WeighedEdge(2,3, 2.7f));
		graph.addEdge(new WeighedEdge(2,4, 3.1f));
		graph.addEdge(new WeighedEdge(3,5, 6.5f));
		graph.addEdge(new WeighedEdge(4,3, 0.2f ));
		graph.addEdge(new WeighedEdge(5,2, 12.f));
		graph.addEdge(new WeighedEdge(1,3, 2.f));

		List<WeighedEdge> path = graph.getPath(1, 4);
		Assert.assertFalse(path.isEmpty());
		WeighedEdge firstEdge = path.get(0);
		Assert.assertEquals(1, firstEdge.getFrom());
		WeighedEdge lastEdge = path.get(path.size() - 1);
		Assert.assertEquals(4, lastEdge.getTo());


		for(int i = 1; i < path.size(); i++)
		{
			Assert.assertEquals(path.get(i - 1).getTo(), path.get(i).getFrom());
		}

		//Assert that the edges are of type WeightEdge, rather than the superclass Edge
		Assert.assertTrue(graph.toString().contains("|"));
		Assert.assertTrue(firstEdge.toString().contains("|"));
	}


	@Test
	public void testVerticesCopiesAreDifferentCollections()
	{
		Graph<String,Edge> graph = SimpleGraph.newDirected(5, 10);
		String v1str = "Vertex 1";
		String v2str = "Vertex 2";

		graph.addVertex(v1str);
		graph.addVertex(v2str);

		List<String> vertices = graph.getVertices();
		Assert.assertEquals(2, vertices.size());

		Assert.assertEquals(v1str,vertices.get(0));
		Assert.assertEquals(v2str,vertices.get(1));

		List<String> vertices2 = graph.getVertices();
		Assert.assertEquals(v1str,vertices2.get(0));
		Assert.assertEquals(v2str,vertices2.get(1));

		Assert.assertNotSame(vertices, vertices2);
	}

	@Test
	public void testApplyFunctionToVertices()
	{
		Graph<String,Edge> graph = SimpleGraph.newDirected(5, 10);

		graph.addVertex("Vertex 1");
		graph.addVertex("Vertex 2");
		graph.addVertex("Vertex 3");

		List<String> vertices = graph.getVertices();

		Assert.assertArrayEquals(new String[]{"Vertex 1","Vertex 2","Vertex 3"}, vertices.toArray());

		graph.apply(str -> str + "A");

		vertices = graph.getVertices();

		Assert.assertArrayEquals(new String[]{"Vertex 1A","Vertex 2A","Vertex 3A"}, vertices.toArray());


	}


}
