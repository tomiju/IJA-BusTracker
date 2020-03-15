package map;

public class Vehicle 
{
	private Line line;
	private Timetable timetable;
	private Coordinate currentPosition;
	private Street currentStreet;
	private int speed;
	
	// aktuální ulice se dá dopočítat - aktuální koordináty -> v lince jsou všechny ulice po trase - spočítat na které přímce leží bod daný aktuálními koordináty
	
	public Vehicle(Timetable timetable, Line line)
	{
		this.timetable = timetable;
		this.line = line;
		this.speed = 0;
		this.currentPosition = line.getStart().getCoordinate();
		this.currentStreet = line.getStart().onWhatStreet(); // počáteční ulice
		
		//this.addVehicleToLine();
	}
	
	public Vehicle() {}
	
	public Timetable getTimetable()
	{
		return this.timetable;
	}
	
	public Line getLine()
	{
		return this.line;
	}
	
	public Coordinate currentPosition()
	{
		return this.currentPosition;
	}
	
	public Street getCurrentStreet()
	{
		return this.currentStreet;
	}
	
	public void addVehicleToLine()
	{
		this.line.addVehicle(this);
	}
}
