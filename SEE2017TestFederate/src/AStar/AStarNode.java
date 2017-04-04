package AStar;

import constellation.Entity;
import constellation.Vector3;

public class AStarNode {
	//cost is the distance from start
	private double cost;
	private Entity entity;
	private AStarNode parent;
	
	public AStarNode(Entity entity) {
		this.entity = entity;
		cost = Double.MAX_VALUE;
		parent = null;
	}
	
	public AStarNode(Entity entity, int priority) {
		this.entity = entity;
		this.cost = priority;
		parent = null;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public void setLowerCost(double cost) {
		if (this.cost > cost) {
			this.cost = cost;
		}
	}
	
	public double getCost() {
		return cost;
	}
	
	public void setVector(Vector3 vec) {
		entity.setLocation(vec);
	}
	
	public Vector3 getVector() {
		return entity.getLocation();
	}
	
	public void setParent(AStarNode parent) {
		this.parent = parent;
	}
	
	public AStarNode getParent() {
		return parent;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
}
