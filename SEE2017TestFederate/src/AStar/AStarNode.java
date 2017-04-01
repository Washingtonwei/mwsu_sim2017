package AStar;

import constellation.Entity;
import constellation.Vector3;

public class AStarNode {

	private double priority;
	private Entity entity;
	private AStarNode parent;
	
	public AStarNode(Entity entity) {
		this.entity = entity;
		priority = Double.MAX_VALUE;
	}
	
	public AStarNode(Entity entity, int priority) {
		this.entity = entity;
		this.priority = priority;
	}
	
	public void setPriority(double priority) {
		this.priority = priority;
	}
	
	public void setLowerPriority(double priority) {
		if (this.priority < priority) {
			this.priority = priority;
		}
	}
	
	public double getPriority() {
		return priority;
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
