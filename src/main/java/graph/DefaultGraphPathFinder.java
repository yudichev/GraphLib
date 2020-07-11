package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

final class DefaultGraphPathFinder implements GraphPathFinder<Edge>
{
	private static int MAX_DEPTH = 255;
	private Map<Integer,List<Edge>> edgesMap;


	@Override public void setEdgesMap(Map<Integer, List<Edge>> graphMap)
	{
		this.edgesMap = graphMap;
	}

	public List<Edge> find(int from, int to)
	{
		Set<Integer> passedVertices = new HashSet<>();
		passedVertices.add(Integer.valueOf(from));
		List foundSubPath = findSubPath(from, to, 1, passedVertices );
		Collections.reverse(foundSubPath);
		return Collections.unmodifiableList(foundSubPath);
	}

	private List<Edge> findSubPath(int from, int to, int depth, Set<Integer> passedVertices)
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

			passedVertices.addAll(intermediateEdges.stream().map(edge -> edge.getTo()).collect(Collectors.toSet()));

			Optional<List<Edge>> reversedSubPath = edges.get(false).stream().map(edge ->
				{
					List<Edge> subPath = findSubPath(edge.getTo(), to, depth + 1, passedVertices);
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
