package graph;


import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TestGraphMultiThreaded
{
	@Test
	public void testGraphCreatedByMultipleThreads()
	{
		Graph<String,Edge> graph = SimpleGraph.newDirected();
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch endLatch = new CountDownLatch(5);
		ExecutorService executorService = Executors.newCachedThreadPool();

		try
		{
			for(int i = 1; i <= 5; i++)
			{
				final int diff = 10*i;
				Runnable task = () -> addVerticesAndEdges(diff, graph, startLatch, endLatch);
				executorService.execute(task);
			}

			startLatch.countDown();
			graph.addVertex(1,"Vertex 1 ");
			graph.addVertex(2,"Vertex 2 ");
			graph.addVertex(3,"Vertex 3 ");
			graph.addVertex(4,"Vertex 4 ");
			graph.addVertex(5,"Vertex 5 ");
			endLatch.await();

			Assert.assertEquals(30, graph.getVertices().size());

			graph.addEdge(new Edge(1, 2));
			graph.addEdge(new Edge(5, 4));
			graph.addEdge(new Edge(1, 3));
			graph.addEdge(new Edge(3, 5));
			graph.addEdge(new Edge(4, 2));

			List<Edge> path = graph.getPath(1, 4);
			Assert.assertFalse("Path 1->4 not found for graph: " + graph.toString(), path.isEmpty());
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

	private void addVerticesAndEdges(int diff, Graph<String,Edge> graph, CountDownLatch startLatch, CountDownLatch endLatch)
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
		graph.addEdge(new Edge(diff + 1,diff + 2));
		graph.addEdge(new Edge(diff + 2,diff + 3));
		graph.addEdge(new Edge(diff + 3,diff + 4));
		graph.addEdge(new Edge(diff + 1,diff + 5));
		graph.addEdge(new Edge(diff + 5,diff + 2));
		graph.addEdge(new Edge(diff + 3,diff + 5));
		graph.addEdge(new Edge(diff + 1,diff + 5));
		graph.addEdge(new Edge(diff + 2,diff + 1));

		endLatch.countDown();
	}


	@Test
	public void testUndirectedConnectedGraphCreatedByMultipleThreads()
	{
		Graph<String,Edge> graph = SimpleGraph.newUndirected();
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch endLatch = new CountDownLatch(5);
		ExecutorService executorService = Executors.newCachedThreadPool();

		try
		{
			for(int i = 0; i < 5; i++)
			{
				final int diff = 10*i;
				Runnable task = () -> addVerticesAndEdges(diff, graph, startLatch, endLatch);
				executorService.execute(task);
			}

			startLatch.countDown();
			Thread.sleep(1000);
			endLatch.await();

			//Make graph connected
			graph.addEdge(new Edge(3, 13));
			graph.addEdge(new Edge(14, 22));
			graph.addEdge(new Edge(25, 31));
			graph.addEdge(new Edge(34, 43));

			Assert.assertEquals(25, graph.getVertices().size());

			List<Edge> path = graph.getPath(5, 21);
			Assert.assertFalse("Path 5->21 not found for graph: " + graph.toString(), path.isEmpty());
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



	@Test
	public void testApplyFunctionToVertices() throws InterruptedException
	{
		Graph<String,Edge> graph = SimpleGraph.newDirected();
		graph.addVertex(1,"Vertex");
		graph.addVertex(2,"Vertex");
		graph.addVertex(3,"Vertex");
		graph.addVertex(4,"Vertex");
		graph.addVertex(5,"Vertex");

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
			Map<Integer,Optional<String>> vertices = graph.getVertices();
			String vertex1 = vertices.get(1).get();
			List<String> difference = vertices.values().stream()
					.filter(Optional::isPresent).map(Optional::get)
					.filter(str -> !str.equals(vertex1)).collect(Collectors.toList());
			Assert.assertTrue(difference.isEmpty());
		}
		finally
		{
			executorService.shutdown();
		}
	}
}

