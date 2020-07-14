package graph;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

/** Interface for a Graph
 *  Defines adding a vertex, an edge,
 *  getting a path through the graph between a couple of vertices,
 *  gets a list of vertices for a snapshot.
 *
 */

public interface Graph<V, T extends Edge>
{
	/**
	 * Adds a vertex of type {@code V}
	 * @param o an instance of object of type {@code V} assigned to the added vertex
	 */
	void addVertex(int id, V o);

	/**
	 * Adds and edge. Each edge is an instance of either {@link Edge} or its descendants
	 * @param edge an instance of edge.
	 */
	void addEdge(T edge);

	/**
	 * Returns a list of edges forming a path between vertices with IDs {@code from} and {@code to}.
	 * @param from ID of the first vertex in the path
	 * @param to ID of the last vertex in the path
	 * @return the found path or empty list if no path is found.
	 */

	List<T> getPath(int from, int to);

	/**
	 * Does the same as method {@link #getPath(int from, int to) getPath}.
	 * Allows to specify a user-defined path finer.
	 * @param from ID of the first vertex in the path
	 * @param to ID of the last vertex in the path
	 * @param finder an instance of path finder
	 * @return a list of edges ordered along the path.
	 */
	List<T> getPath(int from, int to, GraphPathFinder<T> finder);

	/**
	 * Returns a snapshot of graphs' vertices collection.
	 * @return a collection of vertices as a map
	 */
	Map<Integer, Optional<V>> getVertices();

	/**
	 * Applies a function to each vertex and replaces it with the result
	 * @param function function that takes an object of type {@code V} and returns an object of the same type.
	 */
	void apply(UnaryOperator<V> function);
}
