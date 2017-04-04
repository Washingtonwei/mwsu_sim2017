package constellation;

public class Satellite extends Entity {


	private Orbit orbit;
	
	public Satellite(double semiMajorAxis, double eccentricity, double inclination, double argOfPerigree, double RAAN, double trueAnomaly, String name, int id) {
		super(null, name, id);
		orbit = new Orbit(semiMajorAxis, eccentricity, inclination, argOfPerigree, RAAN, trueAnomaly);
		location = orbit.getLocation();
	}
	
	public void Propagate(double timeDiff) {
		orbit.propagateOrbit(timeDiff);
		location = orbit.getLocation();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Orbit getOrbit() {
		return orbit;
	}

	public void setOrbit(Orbit orbit) {
		this.orbit = orbit;
	}
	
}
