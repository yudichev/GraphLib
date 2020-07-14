package graph;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestGraphSingleThreaded
{
	@Test
	public void testCreateDirectedGraph()
	{
		Graph<String,Edge> graph = SimpleGraph.newDirected();
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
	public void testDirectedGraphPathEmpty()
	{
		Graph<String,Edge> graph = SimpleGraph.newDirected();

		graph.addEdge(new Edge(1,2));
		graph.addEdge(new Edge(2,3));
		graph.addEdge(new Edge(4,5));
		graph.addEdge(new Edge(1,5));
		graph.addEdge(new Edge(5,6));
		graph.addEdge(new Edge(6,7));
		graph.addEdge(new Edge(7,8));
		graph.addEdge(new Edge(7,9));
		graph.addEdge(new Edge(9,10));
		graph.addEdge(new Edge(8,5));
		graph.addEdge(new Edge(7,3));


		List<Edge> path = graph.getPath(3, 7);
		Assert.assertTrue(path.isEmpty());
	}


	@Test
	public void testCreateUndirectedGraph()
	{
		Graph<String,Edge> graph = SimpleGraph.newUndirected();
		graph.addEdge(new Edge(1,2));
		graph.addEdge(new Edge(2,3));
		graph.addEdge(new Edge(3,4));
		graph.addEdge(new Edge(1,5));
		graph.addEdge(new Edge(5,6));
		graph.addEdge(new Edge(6,7));
		graph.addEdge(new Edge(7,8));
		graph.addEdge(new Edge(7,9));
		graph.addEdge(new Edge(9,10));
		graph.addEdge(new Edge(8,5));
		graph.addEdge(new Edge(7,3));


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
		Graph<String,WeighedEdge> graph = SimpleGraph.newDirected();

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
		Graph<String,Edge> graph = SimpleGraph.newDirected();
		String v1str = "Vertex 1";
		String v2str = "Vertex 2";

		graph.addVertex(1,v1str);
		graph.addVertex(2,v2str);

		Map<Integer,Optional<String>> vertices = graph.getVertices();
		Assert.assertEquals(2, vertices.size());

		Assert.assertEquals(v1str,vertices.get(1).get());
		Assert.assertEquals(v2str,vertices.get(2).get());

		Map<Integer,Optional<String>> vertices2 = graph.getVertices();
		Assert.assertEquals(v1str,vertices2.get(1).get());
		Assert.assertEquals(v2str,vertices2.get(2).get());

		Assert.assertNotSame(vertices, vertices2);
	}

	@Test
	public void testApplyFunctionToVertices()
	{
		Graph<String,Edge> graph = SimpleGraph.newDirected();

		graph.addVertex(1,"Vertex 1");
		graph.addVertex(2,"Vertex 2");
		graph.addVertex(3,"Vertex 3");

		List<String> vertices = graph.getVertices().values().stream()
				.map(op -> op.orElse("")).collect(Collectors.toList());

		Assert.assertArrayEquals(new String[]{"Vertex 1","Vertex 2","Vertex 3"}, vertices.toArray());

		graph.apply(str -> str + "A");

		vertices = graph.getVertices().values().stream()
				.map(op -> op.orElse("")).collect(Collectors.toList());
		Assert.assertArrayEquals(new String[]{"Vertex 1A","Vertex 2A","Vertex 3A"}, vertices.toArray());


	}


}
