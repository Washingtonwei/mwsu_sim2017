package constellation;

import java.text.DecimalFormat;

public class Orbit {
	
	 // Constants
	private static final Double RADIUS_MOON = 1737.10E3;                  //Radius of moon [m]
	private static final Double MASS_MOON = 7.36E22 ;			    	// kg
	
	private static final Double GRAVITATIONAL_CONSTANT = 6.67300E-11;   // m^3 kg^-1 s^-2

	// MU is the constant which, divided by the distance squared, gives the magnitude of the acceleration;
	// in the case of gravity this is the standard gravitational parameter
	// MU = GRAVITATIONAL_CONSTANT * MASS_MOON
	private static final Double MU_MOON = 4902.7779       ;  		// (km^3s^-2)
	
	//Meridian length = 1 deg of latitude/long on the surface of the moon
	private static final Double  MERIDAN_LENGTH = Math.toRadians(1)*RADIUS_MOON ; 	  // [m]
	
	

    private static final double  BASE_LATITUDE =  Math.toRadians(26.154461);   // [rad]
    private static final double  BASE_LONGITUDE = Math.toRadians(3.491947);    // [rad]

    // Convert the moon base lat and long cartesian coords, in meters
    private static final double BASE_X = RADIUS_MOON * Math.cos(BASE_LATITUDE) * Math.cos(BASE_LONGITUDE);
    private static final double BASE_Y = RADIUS_MOON * Math.cos(BASE_LATITUDE) * Math.sin(BASE_LONGITUDE);
    private static final double BASE_Z = RADIUS_MOON * Math.sin(BASE_LATITUDE);
    private static final double[] LUNAR_BASE = {BASE_X, BASE_Y, BASE_Z};
    //  Standard Equation of a Plane, used for Tangent plane Ax + By + Cz + D = 0
    private static final double D = RADIUS_MOON * RADIUS_MOON;



	

	private double a ;        // Semi-major axis
	private double e ;        // Orbital eccentricity
	private double T ;        // Time of perigee passage
	private double P ;        // Semi Latus Rectum
	private double r ;  	  // is the distance between the orbiting body and the central body
	private double altitude ; // distance from surface of moon to Spacecraft 
	private double x ;        // position x coordinate
	private double y ;        // position y coordinate
	private double z ;        // position z coordinate


	private double ascendingNode ;       // Ascending Node
	private double orbitalInclination ;       // Orbital inclination
	private double argumentOfPerigee ;       // Argument of perigee

	private double theta ;    // True anomaly

	private double distanceToLunarBase;  // how far is the item in orbit, at Theta, from Lunar Base
	private boolean inLOS;  //is the satellite in LOS with Lunar Base?

	// Constructor
	public Orbit () {
		SetDefaults();
	}
	
	public Orbit (double startTheta){
		SetDefaults();
		setTheta(startTheta);
	}
	
	public Orbit(double semiMajorAxis, double eccentricity, double inclination, double argOfPerigree, double RAAN, double trueAnomaly) {
		a = semiMajorAxis;
		e = eccentricity;
		orbitalInclination = inclination;
		argumentOfPerigee = argOfPerigree;
		ascendingNode = RAAN;
		T = 0;
		propagateOrbit(0);
	}
	
	
	private void SetDefaults()  {
		//---------- Set Constants and Inputs  -------------
		//http://en.wikipedia.org/wiki/Kepler's_laws_of_planetary_motion
		//http://en.wikipedia.org/wiki/Orbit_equation
		//http://en.wikipedia.org/wiki/Kepler_orbit

		a = RADIUS_MOON + 6E5; // meters
		ascendingNode = Math.toRadians(-90);               ;   // degrees, ascending node
		argumentOfPerigee = Math.toRadians(-90);              ;  // degrees, argument of perigee (30)
		orbitalInclination = BASE_LATITUDE;              ;  // degrees, orbital inclination (-30)
		T = 0                ;  				// initial time of perigee passage
		e = .25; // eccentricity (0 == circular)
		System.out.println("Ascending Node [rad]: "  + ascendingNode);
		System.out.println("Argument of Perigee [rad]: "  + argumentOfPerigee);
		System.out.println("Orbital Inclination [rad]: "  + orbitalInclination);
		// do one propagation without changing theta to get initial values
		propagateOrbit(0);
	}

	/**
	 * @param deltaTheta (RADIANS)
	 * increment theta by deltaTheta and recalculate all the other orbit properties 
	 * (x, y, z, r, distance to lunar base)
	 */
	public void propagateOrbit(double deltaTheta) {

		theta += deltaTheta    ;  // Advance around the orbit
		if( Math.toDegrees(theta) > 360 ) {
			theta = 0 ;	
		}
		
		P = a * (1 - e*e) ;     			// compute Semi Latus Rectum
		r = P / (1 + e * Math.cos(theta));  // radial distance
	    altitude = (r - RADIUS_MOON)  ;     // [m] altitude of the satellite
		
		// Compute position coordinates (updated 3/24/12 - y coordinate had sign error
		x = r * ( Math.cos(argumentOfPerigee + theta) * Math.cos(ascendingNode) - 
				Math.cos(orbitalInclination) * Math.sin(argumentOfPerigee + theta) * Math.sin(ascendingNode)) ;
		
		y = r * ( Math.cos(argumentOfPerigee + theta) * Math.sin(ascendingNode) + 
				Math.cos(orbitalInclination) * Math.sin(argumentOfPerigee + theta) * Math.cos(ascendingNode)) ;

		z = r * ( Math.sin(argumentOfPerigee + theta) * Math.sin(orbitalInclination) ) ;

		
		distanceToLunarBase = (x * BASE_X + y * BASE_Y + z * BASE_Z - RADIUS_MOON * RADIUS_MOON ) / RADIUS_MOON;
		inLOS =  (distanceToLunarBase > 0) ;
	}
	
	public String getDistanceToLunarBaseTangentPlane(){
		//http://mathworld.wolfram.com/Point-PlaneDistance.html
		String out;
		out = x + ", " +  y + ", " + z + ", " + r + ", " + distanceToLunarBase;
		return out;
	}
	
	public double[] getPosition(){
		return new double[] {x,y,z};
	}
        
	public String getLOSStatus()
	{
		if(inLOS)
		{
			return " IN LINE OF SIGHT";
		}
		else
		{
			return " NOT IN LINE OF SIGHT";
		}
	}

	public void PrintStatus(String owner) {
		String status ;
		status = "Owner" + owner + "| Angle: " + Math.toDegrees(theta) + 
				" pos: [" + x + ", " + y + ", " + z + "] ";
		System.out.println(status);
	}
	
	@Override
	public String toString() {
		String status ;
		DecimalFormat myFormatter = new DecimalFormat("#.#");
		status = "Angle: " + myFormatter.format(Math.toDegrees(theta))  + 
				" | radius: " + myFormatter.format(altitude) +
				" | LOS " + inLOS;
		return status;
	}
	
	public double getAscendingNode() {
		return ascendingNode;
	}

	public void setAscendingNode(double ascendingNode) {
		this.ascendingNode = ascendingNode;
	}

	public double getOrbitalInclination() {
		return orbitalInclination;
	}

	public void setOrbitalInclination(double orbitalInclination) {
		this.orbitalInclination = orbitalInclination;
	}

	public double getArgumentOfPerigee() {
		return argumentOfPerigee;
	}

	public void setArgumentOfPerigee(double argumentOfPerigee) {
		this.argumentOfPerigee = argumentOfPerigee;
	}

	public double getTheta() {
		return theta;
	}

	public void setTheta(double theta) {
		this.theta = theta;
	}

	public double getRadius() {
		return altitude;
	}

	public void setRadius(double radius) {
		this.altitude = radius;
	}

	public boolean isInLOS() {
		return inLOS;
	}

	public double[] getLunarBaseXYZ(){
		return LUNAR_BASE;
	}
	
	public Vector3 getLocation() {
		return new Vector3(x, y, z);
	}

}

//public class Orbit {
//
//	private final double G = 6.67E-11; // gravitational constant
//	private final double M = 7.34767309E22; // mass of moon
//	private final double MU = 4.9048695E12; // gravitational parameter (this is km^3 / day^2 right now)
//
//	private double time = 0; // epoch (variable will be used for last
//										// known time)
//	private double meanAnomaly = 326.2322; // degrees - mean anomaly at epoch (will be used for last known mean anomaly)
//	private double meanMotion = 3600; // mean motion (orbits per day right now)
//	private double eccentricAnomaly; // radians - eccentric anomaly
//	private double perigeeDistance; // perigee distance
//	private double radius; // the radius from the center of the earth
//
//	// orbital elements
//	private double semiMajorAxis; // semi-major axis
//	private double eccentricity = 0.7;// 0.0001492; //eccentricity
//	private double inclination = 051.9970; // inclination
//	private double argOfPerigee = 33.8641; // argument of perigee
//	private double RAAN = 251.0219; // R.A. of the ascending node
//	private double trueAnomaly; // degrees - true anomaly
//
//	public Orbit(double semiMajorAxis, double eccentricity, double inclination, double argOfPerigree, double RAAN, double trueAnomaly) {
//		this.semiMajorAxis = semiMajorAxis;
//		this.eccentricity = eccentricity;
//		this.inclination = inclination;
//		this.argOfPerigee = argOfPerigee;
//		this.RAAN = RAAN;
//		this.trueAnomaly = trueAnomaly;
//	}
//
//	public void Propagate(double dt) {
//		// double dt = t - t_0;
//		double nDtdiff = meanMotion * dt - (int) (meanMotion * dt);
//		double M_t = meanAnomaly + 360 * (nDtdiff - (int) (meanAnomaly + 360 * (nDtdiff)) / 360);
//		time += dt;
//		meanAnomaly = M_t;
//
//		// calculate the other values
//		EccentricAnomaly();
//		TrueAnomaly();
//		SemiMajor();
//		Perigee();
//		Radius();
//	}
//
//	public void EccentricAnomaly() {
//		double M_t = meanAnomaly * (Math.PI / 180);
//		eccentricAnomaly = 2 * Math.PI;
//		double E_1 = 0.0, diff = 2 * Math.PI;
//		double epsilon = 1.0E-5;
//
//		for (int i = 0; i < 30 && diff > epsilon; i++) {
//			E_1 = eccentricAnomaly - (eccentricAnomaly - eccentricity * Math.sin(eccentricAnomaly) - M_t) / (1 - eccentricity * Math.cos(eccentricAnomaly));
//			diff = Math.abs(E_1 - eccentricAnomaly);
//			eccentricAnomaly = E_1;
//		}
//		E_1 = E_1 * (180 / Math.PI);
//		eccentricAnomaly = E_1;
//	}
//
//	public void TrueAnomaly() {
//		trueAnomaly = Math.toDegrees(Math.acos((Math.cos(Math.toRadians(eccentricAnomaly)) - eccentricity) / (1 - eccentricity * Math.cos(Math.toRadians(eccentricAnomaly)))));
//	}
//
//	public void SemiMajor() {
//		semiMajorAxis = Math.pow(MU / Math.pow(2 * Math.PI * meanMotion, 2), 1.0 / 3.0);
//	}
//
//	public void Perigee() {
//		perigeeDistance = semiMajorAxis * (1 - eccentricity);
//	}
//
//	public void Radius() {
//		radius = (perigeeDistance * (1 + eccentricity)) / (1 + eccentricity * Math.pow(Math.cos(time), meanMotion));
//	}
//
//	public double getTrueAnomaly() {
//		return trueAnomaly;
//	}
//
//	public void setTrueAnomaly(double tE_0) {
//		trueAnomaly = tE_0;
//	}
//
//	public double getRadius() {
//		return radius;
//	}
//
//	public void setRadius(double r) {
//		this.radius = r;
//	}
//	
//	public double getX() {
//		return 0.0;
//	}
//	
//	public double getY() {
//		return 0.0;
//	}
//	
//	public double getZ() {
//		return 0.0;
//	}
//
//	public Vector3 getLocation() {
//		return new Vector3(0.0, 0.0, 0.0);
//	}
//
//}