package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SimpleGraph<V, T extends Edge> implements Graph<V, T>
{
	private AtomicInteger verticesCounter = new AtomicInteger();
	private ArrayList<V> vertices;
	private HashSet<T> edges;
	private final boolean directed;


	private SimpleGraph(int vertexCapacity, int edgeCapacity, boolean directed)
	{
     this.vertices = new ArrayList<>(vertexCapacity);
     this.edges = new HashSet<>(edgeCapacity);
     verticesCounter.set(0);
     this.directed = directed;
	}


	public static Graph newDirected(int vertexCapacity, int edgeCapacity)
	{
		return new SimpleGraph(vertexCapacity,edgeCapacity, true);
	}

	public static Graph newUndirected(int vertexCapacity, int edgeCapacity)
	{
		return new SimpleGraph(vertexCapacity,edgeCapacity, false);
	}

	@Override public int addVertex(V o)
	{
		int vertexID;
		synchronized(vertices)
		{
			vertices.add(o);
			vertexID = verticesCounter.incrementAndGet();
		}
		return vertexID;
	}


	@Override public void addEdge(T edge)
	{
		int last = verticesCounter.get();

		if(last < edge.getFrom() || last < edge.getTo())
		{
			throw new IllegalArgumentException("Unable to add edge: vertex " + edge.getFrom() + " or " + edge.getTo() + " not found." );
		}
		synchronized(this.edges)
		{
			this.edges.add(edge);
		}
	}

	@Override public List<T> getPath(int from, int to)
	{
		return getPath(from, to, (GraphPathFinder<T>) new DefaultGraphPathFinder());
	}

	public List<T> getPath(int from, int to, GraphPathFinder<T> finder)
	{
		finder.setEdgesMap(getEdgesMapSnapshot());
		return finder.find(from, to);
	}

	public void applyToVerticesOnThePath(Consumer<V> consumer, List<T> edges)
	{
		edges.stream().forEach(edge -> consumer.accept(this.vertices.get(edge.getFrom() - 1)));
	}

	private Map<Integer,List<T>> getEdgesMapSnapshot(){
		Map<Integer,List<T>> edgesMap;
		List<T> copyOfEdges;


		synchronized(edges)
		{
			copyOfEdges = edges.stream().map(edge -> edge.copy()).map(edge -> (T) edge).collect(Collectors.toList());
		}

		if(!directed)
		{
				List<T> reversedEdges = copyOfEdges.stream().filter(edge -> edge.getTo() != edge.getFrom())
						.map(edge -> edge.reverse())
						.map(edge -> (T) edge)
						.collect(Collectors.toList());
				copyOfEdges.addAll(reversedEdges);
		}

		edgesMap = copyOfEdges.stream().collect(Collectors.groupingBy(edge -> edge.getFrom()));
		return edgesMap;
	}
}
