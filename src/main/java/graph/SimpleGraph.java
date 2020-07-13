package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	private final AtomicInteger verticesCounter = new AtomicInteger();
	private final ArrayList<V> vertices;
	private final HashSet<T> edges;
	private final boolean directed;


	private SimpleGraph(int vertexCapacity, int edgeCapacity, boolean directed)
	{
     this.vertices = new ArrayList<>(vertexCapacity);
     this.edges = new HashSet<>(edgeCapacity);
     verticesCounter.set(0);
     this.directed = directed;
	}

	/**
	 * Returns an instance of empty directed graph
	 * @param vertexCapacity initial capacity of vertices
	 * @param edgeCapacity initial capacity of edges
	 * @return an instance of directed graph
	 */
	public static <V,T extends Edge> Graph<V,T> newDirected(int vertexCapacity, int edgeCapacity)
	{
		return new SimpleGraph<>(vertexCapacity, edgeCapacity, true);
	}

	/**
	 Returns an instance of empty undirected graph
	 * @param vertexCapacity initial capacity of vertices
	 * @param edgeCapacity initial capacity of edges
	 * @return an instance of undirected graph
	 */
	public static <V,T extends Edge> Graph<V,T> newUndirected(int vertexCapacity, int edgeCapacity)
	{
		return new SimpleGraph<>(vertexCapacity,edgeCapacity, false);
	}

	/**
	 * Adds a vertex to the graph, with an object of user-defined type assigned to the vertex.
	 * Returns a unique ID of the vertex.
	 * @param o an instance of object of type {@code V} assigned to the added vertex
	 * @return vertex ID
	 */
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
		synchronized(edges)
		{
			edges.add(edge);
		}
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
		List<V> verticesCopy;
		synchronized(vertices)
		{
			verticesCopy = vertices.stream().sequential().collect(Collectors.toList());
		}
		return Collections.unmodifiableList(verticesCopy);
	}

	@Override public void apply(UnaryOperator<V> function)
	{
		synchronized(vertices)
		{
			for(int id = 1; id <= vertices.size(); id++)
			{
				vertices.set(id - 1, function.apply(vertices.get(id - 1)));
			}
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
		List<T> copyOfEdges;

		synchronized(edges)
		{
			copyOfEdges = edges.stream().map(Edge::copy).map(edge -> (T) edge).collect(Collectors.toList());
		}

		if(!directed)
		{
				List<T> reversedEdges = copyOfEdges.stream().filter(edge -> edge.getTo() != edge.getFrom())
						.map(Edge::reverse)
						.map(edge -> (T) edge)
						.collect(Collectors.toList());
				copyOfEdges.addAll(reversedEdges);
		}

		return Collections.unmodifiableMap(copyOfEdges.stream().collect(Collectors.groupingBy(Edge::getFrom)));
	}

	/**
	 * Returns a string representation of the graph
	 * @return string representation of the graph
	 */
	public String toString()
	{
		String str;
		synchronized(edges)
		{
			str = edges.stream().map(Edge::toString).collect(Collectors.joining(","));
		}
		return str;
	}
}
