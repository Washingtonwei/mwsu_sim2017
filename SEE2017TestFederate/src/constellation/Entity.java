package constellation;

import siso.smackdown.frame.ReferenceFrame;

public class Entity {
	protected Vector3 location;
	protected double radius;
	protected String name;
	protected int id;
	protected String status;
	protected ReferenceFrame parentReferenceFrame;
	
	public Entity(ReferenceFrame parentReferenceFrame, String name, int id) {
		this.parentReferenceFrame = parentReferenceFrame;
		this.name = name;
		location = new Vector3(0.0, 0.0, 0.0);
		this.id = id;
	}
	
	public Entity(ReferenceFrame parentReferenceFrame, String name, double x, double y, double z, int id) {
		this.parentReferenceFrame = parentReferenceFrame;
		this.name = name;
		this.location = new Vector3(x, y, z);
		this.id = id;
	}
	
	public Entity(ReferenceFrame parentReferenceFrame, String name, double x, double y, double z, double radius, int id) {
		this.parentReferenceFrame = parentReferenceFrame;
		this.name = name;
		this.location = new Vector3(x, y, z);
		this.radius = radius;
		this.id = id;
	}
	
	public void setX(double x) {
		location.setX(x);
	}
	
	public void setY(double y) {
		location.setY(y);
	}
	
	public void setZ(double z) {
		location.setZ(z);
	}
	
	public double getX() {
		return location.getX();
	}
	
	public double getY() {
		return location.getY();
	}
	
	public double getZ() {
		return location.getZ();
	}

	public ReferenceFrame getParentReferenceFrame() {
		return parentReferenceFrame;
	}

	public void setParentReferenceFrame(ReferenceFrame parentReferenceFrame) {
		this.parentReferenceFrame = parentReferenceFrame;
	}
	
	public String getName() {
		return name;
	}
	
	public int getID() {
		return id;
	}
	
	public void setLocation(Vector3 location) {
		this.location = location;
	}
	
	public Vector3 getLocation() {
		return location;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public double getRadius() {
		return radius;
	}
	
}
