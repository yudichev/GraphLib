package graph;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TestGraphSingleThreaded
{
	@Test
	public void testCreateGraph()
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
	public void testCreateGraph2()
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

		path = graph.getPath(1, 7);
		for(int i = 1; i < path.size(); i++)
		{
			Assert.assertEquals(path.get(i - 1).getTo(), path.get(i).getFrom());
		}
	}


	@Test
	public void testCreateGraphUndirected()
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
		if(path.isEmpty()) System.out.println("Path is empty");
		System.out.println(path.stream().map(edge -> edge.toString()).collect(Collectors.joining()));

		path = graph.getPath(1, 7);
		if(path.isEmpty()) System.out.println("Path is empty");
		System.out.println(path.stream().map(edge -> edge.toString()).collect(Collectors.joining()));
		for(int i = 1; i < path.size(); i++)
		{
			Assert.assertEquals(path.get(i - 1).getTo(), path.get(i).getFrom());
		}
	}

	@Test
	public void testDirectedGraphApplyFunction()
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


		List<Edge> path = graph.getPath(1, 7);

		StringBuffer sb = new StringBuffer();
		Consumer<String> stringConcatenator = str -> sb.append(str).append("; ");
		graph.applyToVerticesOnThePath(stringConcatenator, path);
		System.out.println(sb.toString());
	}
}
