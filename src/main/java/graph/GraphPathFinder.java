package graph;

import java.util.List;
import java.util.Map;

/**
 * An interface for a graph path finder
 * @param <T> type of the edge; a subclass of {@link Edge}
 */
public interface GraphPathFinder<T extends Edge>
{
	/**
	 * Sets transition map
	 * @param transitionMap an instance of transition map
	 */
	void setTransitionMap(Map<Integer,List<T>> transitionMap);

	/**
	 * Finds the path connecting vertex with ID {@code from} and vertex with ID {@code to}
	 * @param from first vertex ID
	 * @param to last vertex ID
	 * @return a list of edges
	 */
	List<T> find(int from, int to);
}
