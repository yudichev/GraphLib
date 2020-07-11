package graph;

import java.util.List;
import java.util.function.Consumer;

public interface Graph<V, T>
{
	public int addVertex(V o);
	public void addEdge(T edge);

	public List<T> getPath(int from, int to);
	public void applyToVerticesOnThePath(Consumer<V> consumer, List<T> edges);
}
