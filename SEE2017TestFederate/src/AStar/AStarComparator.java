package AStar;

import java.util.Comparator;

public class AStarComparator implements Comparator<AStarNode> {

	@Override
	public int compare(AStarNode a, AStarNode b) {
		if (a.getCost() > b.getCost()) {
			return -1;
		}
		if (a.getCost() < b.getCost()) {
			return 1;
		}
		return 0;
	}
}