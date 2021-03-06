package graph;


import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class TestGraphMultiThreaded
{
	@Test
	public void testGraphCreatedByMultipleThreads()
	{
		Graph<String,Edge> graph = SimpleGraph.newDirected(30, 50);
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch endLatch = new CountDownLatch(5);
		ExecutorService executorService = Executors.newCachedThreadPool();

		try
		{
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

			List<Edge> path = graph.getPath(v1, v4);
			Assert.assertFalse("Path " + v1 + "->" + v4 + " not found for graph: " + graph.toString(), path.isEmpty());
		}
		catch(Throwable ex)
		{
			Assert.fail("No exception is expected here. Thrown: " + ex.getClass() + " Message: " + ex.getMessage());
		}
		finally
		{
			executorService.shutdown();
		}
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
	public void testUndirectedConnectedGraphCreatedByMultipleThreads()
	{
		Graph<String,Edge> graph = SimpleGraph.newUndirected(30, 50);
		CountDownLatch startLatch = new CountDownLatch(1);
		ExecutorService executorService = Executors.newCachedThreadPool();

		try
		{
			Callable<Integer[]> task = () -> addVerticesAndEdgesAndGetID(graph, startLatch);
			Future<Integer[]> future1 = executorService.submit(task);
			Future<Integer[]> future2 = executorService.submit(task);
			Future<Integer[]> future3 = executorService.submit(task);
			Future<Integer[]> future4 = executorService.submit(task);
			Future<Integer[]> future5 = executorService.submit(task);

			startLatch.countDown();
			int vv12 = future1.get()[1];
			int vv21 = future2.get()[0];
			int vv22 = future2.get()[1];
			int vv31 = future3.get()[0];
			int vv32 = future3.get()[1];
			int vv41 = future4.get()[0];
			int vv42 = future4.get()[1];
			int vv51 = future5.get()[0];

			//Make graph connected
			graph.addEdge(new Edge(vv12, vv21));
			graph.addEdge(new Edge(vv22, vv31));
			graph.addEdge(new Edge(vv32, vv41));
			graph.addEdge(new Edge(vv42, vv51));

			Assert.assertEquals(25, graph.getVertices().size());

			List<Edge> path = graph.getPath(5, 20);
			Assert.assertFalse("Path 5->20 not found for graph: " + graph.toString(), path.isEmpty());
		}
		catch(Throwable ex)
		{
			Assert.fail("No exception is expected here. Thrown: " + ex.getClass() + " Message: " + ex.getMessage());
		}
		finally
		{
			executorService.shutdown();
		}
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

	@Test
	public void testApplyFunctionToVertices() throws InterruptedException
	{
		Graph<String,Edge> graph = SimpleGraph.newDirected(5,10);
		graph.addVertex("Vertex");
		graph.addVertex("Vertex");
		graph.addVertex("Vertex");
		graph.addVertex("Vertex");
		graph.addVertex("Vertex");

		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch endLatch = new CountDownLatch(10);
		ExecutorService executorService = Executors.newCachedThreadPool();


		try
		{
			for(int i = 0; i < 10; i++)
			{
				final String idx = String.valueOf(i);
				Runnable task = () -> {
					try
					{
						startLatch.await();
						graph.apply(str -> " A[" + idx + "]");
						endLatch.countDown();
					}
					catch(InterruptedException ex)
					{
						Thread.currentThread().interrupt();
					}
				};
				executorService.execute(task);
			}

			startLatch.countDown();
			Thread.sleep(1000);
			endLatch.await();
			List<String> vertices = graph.getVertices();
			String vertex1 = vertices.get(0);
			List<String> difference = vertices.stream().filter(str -> !str.equals(vertex1)).collect(Collectors.toList());
			Assert.assertTrue(difference.isEmpty());
		}
		finally
		{
			executorService.shutdown();
		}
	}
}

