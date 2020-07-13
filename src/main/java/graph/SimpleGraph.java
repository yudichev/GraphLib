package graph;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Implements interface {@link Graph}. Represents a simple graph.
 * Each graph contains a list of vertices and a set of edges.
 * Each vertices are of the same type, defined via parameter {@code V}
 * at the graph creation.
 * Each edge should be either an instance of class {@link Edge} of its
 * descendant.
 *
 * Allows to find a path between two vertices and return a list vertices.
 *
 * @param <V> defines the type of the object associated with a vertex
 * @param <T> defines the type of the edge. A subclass of {@link Edge}
 */

public class SimpleGraph<V, T extends Edge> implements Graph<V, T>
{
	private final CopyOnWriteArrayList<V> vertices;
	private final CopyOnWriteArraySet<T> edges;
	private final boolean directed;
	private final AtomicInteger verticesCounter = new AtomicInteger();


	private SimpleGraph(boolean directed)
	{
     this.vertices = new CopyOnWriteArrayList<>();
     this.edges = new CopyOnWriteArraySet<>();
     this.directed = directed;
     verticesCounter.set(0);
	}

	/**
	 * Returns an instance of empty directed graph
	 * @return an instance of directed graph
	 */
	public static <V,T extends Edge> Graph<V,T> newDirected()
	{
		return new SimpleGraph<>( true);
	}

	/**
	 Returns an instance of empty undirected graph
	 * @return an instance of undirected graph
	 */
	public static <V,T extends Edge> Graph<V,T> newUndirected()
	{
		return new SimpleGraph<>( false);
	}

	/**
	 * Adds a vertex to the graph, with an object of user-defined type assigned to the vertex.
	 * Returns a unique ID of the vertex.
	 * @param o an instance of object of type {@code V} assigned to the added vertex
	 * @return vertex ID
	 */
	@Override public int addVertex(V o)
	{
		int id;
		synchronized(vertices)
		{
			vertices.add(o);
			id = verticesCounter.incrementAndGet();
		}
		return id;
	}

	/**
	 * Adds an edge connecting two vertices.
	 * @param edge an instance of edge.
	 */
	@Override public void addEdge(T edge)
	{
		int last = verticesCounter.get();

		if(last < edge.getFrom() || last < edge.getTo())
		{
			throw new IllegalArgumentException("Unable to add edge: vertex " + edge.getFrom() + " or " + edge.getTo() + " not found." );
		}
		edges.add(edge);
	}

	/**
	 * Returns an oriented path between two vertices with IDs {@code from} and {@code to}.
	 * The path has a fixed direction for traversing it, even for an undirected graph.
	 * The path's direction is determined by ascending order of list indices.
	 * The returned list of edges is ordered according to the specified direction,
	 * so that the first edges starts at vertex with ID {@code form}
	 * and the last edge ends with the vertex with ID {@code to}.
	 *
	 * Uses the default path finder {@link DefaultGraphPathFinder}.
	 * The found path is not the shortest one.
	 *
	 * @param from ID of the first vertex in the path
	 * @param to ID of the last vertex in the path
	 * @return a list of edges ordered along the path.
	 */

	@SuppressWarnings("unchecked")
	@Override public List<T> getPath(int from, int to)
	{
		return getPath(from, to, (GraphPathFinder<T>) new DefaultGraphPathFinder());
	}

	/**
	 * Does the same as method {@link #getPath(int from, int to) getPath}.
	 * Allows to specify a user-defined path finer.
	 * @param from ID of the first vertex in the path
	 * @param to ID of the last vertex in the path
	 * @param finder an instance of path finder
	 * @return a list of edges ordered along the path.
	 */
	public List<T> getPath(int from, int to, GraphPathFinder<T> finder)
	{
		finder.setTransitionMap(getTransitionsMap());
		return finder.find(from, to);
	}

	/**
	 * Returns a snapshot of the graph's vertices collection
	 * @return a list of vertices
	 */
	public List<V> getVertices()
	{
		return vertices.stream().sequential().collect(Collectors.toList());
	}

	@Override public void apply(UnaryOperator<V> function)
	{
		ListIterator<V> iter = vertices.listIterator();
		while(iter.hasNext())
		{
			vertices.set(iter.nextIndex(), function.apply(iter.next()));
		}
	}

	/*
		Returns a transition map to be used for path finding.
		The key of each map entry corresponds to a vertex ID.
		The value of each map entry is a list of edges connected to the vertex with the ID equal to the key.
		Each edge is directed 'out' from the vertex.
		For undirected graphs, each undirected edge is represented by a couple of directed edges with opposite
		orientation, except the case when the edge closes to the same vertex (from = to).
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer,List<T>> getTransitionsMap(){
		Map<Integer,List<T>> edgesMap;
		List<T> copyOfEdges;

		copyOfEdges = edges.stream().map(Edge::copy).map(edge -> (T) edge).collect(Collectors.toList());

		if(!directed)
		{
				List<T> reversedEdges = copyOfEdges.stream().filter(edge -> edge.getTo() != edge.getFrom())
						.map(Edge::reverse)
						.map(edge -> (T) edge)
						.collect(Collectors.toList());
				copyOfEdges.addAll(reversedEdges);
		}

		edgesMap = copyOfEdges.stream().collect(Collectors.groupingBy(Edge::getFrom));
		return edgesMap;
	}

	/**
	 * Returns a string representation of the graph
	 * @return string representation of the graph
	 */
	public String toString()
	{
		return edges.stream().map(Edge::toString).collect(Collectors.joining(","));
	}
}
