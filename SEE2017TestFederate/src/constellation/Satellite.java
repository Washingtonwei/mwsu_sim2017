package constellation;

public class Satellite {

	private Orbit orbit;
	
	public Satellite(double semiMajorAxis, double eccentricity, double inclination, double argOfPerigree, double RAAN, double trueAnomaly) {
		orbit = new Orbit(semiMajorAxis, eccentricity, inclination, argOfPerigree, RAAN, trueAnomaly);
	}
	
	public void Propagate(double timeDiff) {
		orbit.Propagate(timeDiff);
	}
			
	public double getRadius() {
		return orbit.getRadius();
	}
	
	public double getTrueAnomaly() {
		return orbit.getTrueAnomaly();
	}
	
}
