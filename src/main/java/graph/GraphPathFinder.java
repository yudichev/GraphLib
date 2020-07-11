package graph;

import java.util.List;
import java.util.Map;

public interface GraphPathFinder<T extends Edge>
{
	public void setEdgesMap(Map<Integer,List<T>> graphMap);
	public List<T> find(int from, int to);
}
