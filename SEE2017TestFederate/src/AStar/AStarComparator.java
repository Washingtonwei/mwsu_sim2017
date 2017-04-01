package AStar;

import java.util.Comparator;

public class AStarComparator implements Comparator<AStarNode> {

	@Override
	public int compare(AStarNode a, AStarNode b) {
		if (a.getPriority() > b.getPriority()) {
			return -1;
		}
		if (a.getPriority() < b.getPriority()) {
			return 1;
		}
		return 0;
	}
}