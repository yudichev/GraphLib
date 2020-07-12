package graph;


import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestGraphMultiThreaded
{
	@Test
	public void testGraphCreatedByMulitpleThreads() throws InterruptedException
	{
		Graph<String,Edge> graph = SimpleGraph.newDirected(30, 50);
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch endLatch = new CountDownLatch(5);
		ExecutorService executorService = Executors.newCachedThreadPool();

		for(int i = 0; i < 5; i++)
		{
			Runnable task = () -> addVerticesAndEdges(graph, startLatch, endLatch);
			executorService.execute(task);
		}


		startLatch.countDown();
		int v1 = graph.addVertex("Vertex 1 ");
		int v2 = graph.addVertex("Vertex 2 ");
		int v3 = graph.addVertex("Vertex 3 ");
		int v4 = graph.addVertex("Vertex 4 ");
		int v5 = graph.addVertex("Vertex 5 ");
		endLatch.await();

		Assert.assertEquals(30, graph.getVertices().size());
	}

	private void addVerticesAndEdges(Graph<String,Edge> graph, CountDownLatch startLatch, CountDownLatch endLatch)
	{
		String threadName = Thread.currentThread().getName();
		try
		{
			startLatch.await();
		}
		catch(InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
		int v1 = graph.addVertex("Vertex 1 [" + threadName + "]");
		int v2 = graph.addVertex("Vertex 2 [" + threadName + "]");
		int v3 = graph.addVertex("Vertex 3 [" + threadName + "]");
		int v4 = graph.addVertex("Vertex 4 [" + threadName + "]");
		int v5 = graph.addVertex("Vertex 5 [" + threadName + "]");
		graph.addEdge(new Edge(v1,v2));
		graph.addEdge(new Edge(v2,v3));
		graph.addEdge(new Edge(v3,v4));
		graph.addEdge(new Edge(v1,v5));
		graph.addEdge(new Edge(v5,v2));
		graph.addEdge(new Edge(v3,v5));
		graph.addEdge(new Edge(v1,v5));
		graph.addEdge(new Edge(v2,v1));
		endLatch.countDown();
	}
}

