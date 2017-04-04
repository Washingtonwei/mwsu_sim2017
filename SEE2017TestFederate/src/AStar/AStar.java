package AStar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import constellation.Satellite;
import constellation.Vector3;

public class AStar {

	public ArrayList<Satellite> findPath(ArrayList<Satellite> satellites, int start, int end) {
		AStarNode current = null;
		AStarNode neighbor = null;
	
		//create a AStarNode comparator and the open priority queue
		Comparator<AStarNode> comparator = new AStarComparator();
		PriorityQueue<AStarNode> open = new PriorityQueue<AStarNode>(satellites.size(), comparator);
		PriorityQueue<AStarNode> closed = new PriorityQueue<AStarNode>(satellites.size(), comparator);
		
		//create AStarNodes holding our satellites
		ArrayList<AStarNode> available = new ArrayList<AStarNode>();
		for (int i = 0; i < satellites.size(); i++) {
			available.add(new AStarNode(satellites.get(i)));
		}
				
		open.add(available.get(start));
		
		//while there are still nodes in the open list
		while(open.size() != 0){
			current = open.remove();
			//if current is the target you're done
			if(current == available.get(end)){
				break;
			}
			//else move current to the closed list
			closed.add(current);
			//search the neighbors
			for (int i = 0; i < available.size(); i++) {
				if (i == start) {
					continue;
				}
				neighbor = available.get(i);
				double cost = distance(current, neighbor) + current.getCost();
				if (cost < neighbor.getCost()) {
					if (open.contains(neighbor)) {
						open.remove(neighbor);
					}
					if (closed.contains(neighbor)) {
						closed.remove(neighbor);
					}
				}
				if (!open.contains(neighbor) && !closed.contains(neighbor)) {
					neighbor.setCost(cost);
					neighbor.setParent(current);
					open.add(neighbor);
				}
			}
		}
		//if the targets parent is null no path was found
		if(available.get(end).getParent() == null){
			return null;
		}
		//else create the path by working back from the target and return it
		return constructPath(available.get(end));
	}
	
	public Boolean inLOS(AStarNode a, AStarNode b) {
		//Vector3 vec = new Vector3(a.getVector(), b.getVector());
		return distance(a, b) < 1.738E5;
	}
	
	//this distance between two 3d points is the square root of the sum of the difference of the components squared
	//distance = sqrt((x1-x2)^2+(y1-y2)^2+(z1-z2)^2)
	public double distance(AStarNode a, AStarNode b) {
		Vector3 locA = a.getVector();
		Vector3 locB = b.getVector();
		return Math.sqrt(Math.pow(locA.getX() - locB.getX(), 2) + Math.pow(locA.getY() - locB.getY(), 2) + Math.pow(locA.getZ() - locB.getZ(), 2));
	}
	
	//construct the path by starting at the end node and getting the parent nodes
	public ArrayList<Satellite> constructPath(AStarNode curr) {
		ArrayList<Satellite> path = new ArrayList<Satellite>();
		int steps = 0;
		while (curr.getParent() != null) {
			path.add((Satellite)curr.getEntity());
			curr = curr.getParent();
			steps++;
			if (steps > 100) {
				System.out.println("loop in construct path?");
				return null;
			}
		}
		
		return path;
	}
	
}
