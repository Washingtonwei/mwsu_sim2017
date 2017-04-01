package AStar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import constellation.Entity;
import constellation.Satellite;
import constellation.Vector3;

public class AStar {

	public ArrayList<Satellite> findPath(ArrayList<Satellite> satellites, int start, int end) {
		//create node to represent moon and end point
		AStarNode moon = new AStarNode(new Entity("sun", "moon", 0, 0, 0, 1.738E6));
		AStarNode endNode = new AStarNode(satellites.get(end));
		
		//this will hold the path if there is one
		ArrayList<Satellite> path = null;
		
		//create AStarNodes holding our satellites
		ArrayList<AStarNode> available = new ArrayList<AStarNode>();
		for (int i = 0; i < satellites.size(); i++) {
			available.add(new AStarNode(satellites.get(i)));
		}
		
		//create a AStarNode comparator and the open priority queue
		Comparator<AStarNode> comparator = new AStarComparator();
		PriorityQueue<AStarNode> open = new PriorityQueue<AStarNode>(satellites.size(), comparator);
		
		//curr is the current node
		AStarNode curr = null;
		//checking is the node being checked against the current node
		AStarNode checking = null;
		
		//start with just the start node in the open queue
		open.add(new AStarNode(satellites.get(start)));
		
		int steps = 0;

		curr = open.remove();
		//loop until we reach the end node
		while (curr.getEntity() != endNode.getEntity() && steps < 100) {
			steps++;
			for (int i = 0; i < available.size(); i++) {
				checking = available.get(i);
				
				//only check it if it is not the current node
				if (checking.getEntity() != curr.getEntity()) {
					
					//if checking is within line of site, add it to open queue
					if (inLOS(curr, checking, moon)) {
						//the priority is not set if this distance is lower than the current one
						checking.setLowerPriority(distance(curr, checking));
						open.add(checking);
					}
				}
				
			}
			curr = open.remove();
		}
		
		if (curr.getEntity() == endNode.getEntity()) {
			System.out.println("we found a path!" + steps);
		}
		
		return path;
	}
	
	public Boolean inLOS(AStarNode a, AStarNode b, AStarNode c) {
		//Vector3 vec = new Vector3(a.getVector(), b.getVector());
		
		return true;
	}
	
	public double distance(AStarNode a, AStarNode b) {
		Vector3 locA = a.getVector();
		Vector3 locB = b.getVector();
		return Math.sqrt(Math.pow(locA.getX() - locB.getX(), 2) + Math.pow(locA.getY() - locB.getY(), 2) + Math.pow(locA.getZ() - locB.getZ(), 2));
	}
	
}
