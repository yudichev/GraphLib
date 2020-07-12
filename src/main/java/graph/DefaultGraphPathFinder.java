package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implements default algorithm of finding a path between two vertices.
 * The length of the paths is limited by 255 in order to avoid stack overflow exception
 * as the algorithm uses recursive method invocation.
 */
final class DefaultGraphPathFinder implements GraphPathFinder<Edge>
{
	private final static int MAX_DEPTH = 255;
	private Map<Integer,List<Edge>> edgesMap;

	/**
	 * Accepts a transition map.
	 * The key of each map entry corresponds to a vertex ID.
	 * The value of each map entry is a list of edges connected to the vertex with the ID equal to the key.
	 * Each edge is directed 'out' from the vertex.
	 * For undirected graphs, each undirected edge is represented by a couple of directed edges with opposite
	 * orientation, except the case when the edge closes to the same vertex (from = to).
	 * @param transitionMap an instance of transition map
	 */
	@Override public void setTransitionMap(Map<Integer, List<Edge>> transitionMap)
	{
		this.edgesMap = transitionMap;
	}

	/**
	 * Returns the path connecting vertices with IDs {@code from} and {@code to}
	 * @param from first vertex ID
	 * @param to list vertex ID
	 * @return a list of edges
	 */
	public List<Edge> find(int from, int to)
	{
		Set<Integer> passedVertices = new HashSet<>();
		passedVertices.add(from);
		List<Edge> foundSubPath = findReversedSubPath(from, to, 1, passedVertices );
		Collections.reverse(foundSubPath);
		return Collections.unmodifiableList(foundSubPath);
	}

	/*
	Implements the main algorithm of path finding.
	Uses recursive invocation if itself.
	Returns the found path in a reversed order.
	 */
	private List<Edge> findReversedSubPath(int from, int to, int depth, Set<Integer> passedVertices)
	{
		// Avoid stack overflow
		if (depth == MAX_DEPTH) return Collections.emptyList();

		if(!this.edgesMap.containsKey(from)) return Collections.emptyList();

		Map<Boolean,List<Edge>> edges = this.edgesMap.get(from).stream().collect(Collectors.partitioningBy(edge-> edge.getTo() == to));
		List<Edge> lastEdges = edges.get(true);
		List<Edge> intermediateEdges = edges.get(false);
		if(lastEdges.isEmpty())
		{
			if(intermediateEdges.isEmpty())
				return Collections.emptyList();

			passedVertices.addAll(intermediateEdges.stream().map(Edge::getTo).collect(Collectors.toSet()));

			Optional<List<Edge>> reversedSubPath = edges.get(false).stream().map(edge ->
				{
					List<Edge> subPath = findReversedSubPath(edge.getTo(), to, depth + 1, passedVertices);
					if(!subPath.isEmpty()) subPath.add(edge);
					return subPath;
				}).filter(list -> !list.isEmpty()).findAny();

			return reversedSubPath.orElse(Collections.emptyList());
		}
		else
		{
			List<Edge> inversePath = new ArrayList<>();
			inversePath.add(lastEdges.get(0));
			return inversePath;
		}
	}
}
