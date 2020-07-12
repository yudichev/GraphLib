package graph;


import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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

		graph.addEdge(new Edge(v1, v2));
		graph.addEdge(new Edge(v5, v4));
		graph.addEdge(new Edge(v1, v3));
		graph.addEdge(new Edge(v3, v5));
		graph.addEdge(new Edge(v4, v2));

		List<Edge> path = graph.getPath(v1,v4);
		System.out.println(path.stream().map(Edge::toString).collect(Collectors.joining()));
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


	@Test
	public void testUndirectedConnectedGraphCreatedByMulitpleThreads() throws InterruptedException, ExecutionException
	{
		Graph<String,Edge> graph = SimpleGraph.newUndirected(30, 50);
		CountDownLatch startLatch = new CountDownLatch(1);
		ExecutorService executorService = Executors.newCachedThreadPool();


		Callable<Integer[]> task = () -> {return addVerticesAndEdgesAndGetID(graph, startLatch);};
		Future<Integer[]> future1 = executorService.submit(task);
		Future<Integer[]> future2 = executorService.submit(task);
		Future<Integer[]> future3 = executorService.submit(task);
		Future<Integer[]> future4 = executorService.submit(task);
		Future<Integer[]> future5 = executorService.submit(task);


		startLatch.countDown();
		int vv12 = future1.get()[1].intValue();
		int vv21 = future2.get()[0].intValue();
		int vv22 = future2.get()[1].intValue();
		int vv31 = future3.get()[0].intValue();
		int vv32 = future3.get()[1].intValue();
		int vv41 = future4.get()[0].intValue();
		int vv42 = future4.get()[1].intValue();
		int vv51 = future5.get()[0].intValue();

		//Make graph connected
		graph.addEdge(new Edge(vv12, vv21));
		graph.addEdge(new Edge(vv22, vv31));
		graph.addEdge(new Edge(vv32, vv41));
		graph.addEdge(new Edge(vv42, vv51));

		Assert.assertEquals(25, graph.getVertices().size());

		List<Edge> path = graph.getPath(1,20);
		Assert.assertFalse(path.isEmpty());
	}

	private Integer[] addVerticesAndEdgesAndGetID(Graph<String,Edge> graph, CountDownLatch startLatch)
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
		return new Integer[]{v1,v5};
	}
}
