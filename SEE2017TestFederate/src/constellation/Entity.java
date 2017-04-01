package constellation;

public class Entity {
	protected Vector3 location;
	protected double radius;
	protected String referenceFrame;
	protected String name;
	
	public Entity(String referenceFrame, String name) {
		this.referenceFrame = referenceFrame;
		this.name = name;
		location = new Vector3(0.0, 0.0, 0.0);
	}
	
	public Entity(String referenceFrame, String name, double x, double y, double z) {
		this.referenceFrame = referenceFrame;
		this.name = name;
		this.location = new Vector3(x, y, z);
	}
	
	public Entity(String referenceFrame, String name, double x, double y, double z, double radius) {
		this.referenceFrame = referenceFrame;
		this.name = name;
		this.location = new Vector3(x, y, z);
		this.radius = radius;
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
	
	public String getReferenceFrame() {
		return referenceFrame;
	}
	
	public String getName() {
		return name;
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
