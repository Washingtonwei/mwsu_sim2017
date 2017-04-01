package constellation;

public class Satellite extends Entity {

	private Orbit orbit;
	
	public Satellite(double semiMajorAxis, double eccentricity, double inclination, double argOfPerigree, double RAAN, double trueAnomaly, String name) {
		super("moon", name);
		orbit = new Orbit(semiMajorAxis, eccentricity, inclination, argOfPerigree, RAAN, trueAnomaly);
		setX(orbit.getX());
		setY(orbit.getY());
		setZ(orbit.getZ());
	}
	
	public void Propagate(double timeDiff) {
		orbit.Propagate(timeDiff);
		location = orbit.getLocation();
	}
			
//	public double getRadius() {
//		return orbit.getRadius();
//	}
//	
//	public double getTrueAnomaly() {
//		return orbit.getTrueAnomaly();
//	}
	
}
