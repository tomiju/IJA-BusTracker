package map;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import drawable.VehicleView;
import gui.SceneController;
import javafx.animation.PathTransition;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;

/**
 * 
 * Třída reprezentující vozidlo.
 * Skládá se z ID, linky, na které vozidlo jezdí, jízdního řádu, aktuální pozice a aktuální ulice, na které se vozidlo nachází.
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Vehicle 
{
	private String id;
	private BusLine line;
	private Timetable timetable;
	private Coordinate currentPosition;
	private Street currentStreet;
	//private int speed;
	
	private VehicleView vehicleView;
	private TimetableEntry firstEntry;
	private TimetableEntry nextStop;
	private String nextStopTime;
	private String previousStopTime;
	private List<Coordinate> vehiclePath;
	private int index = 0;
	
	// aktuální ulice se dá dopočítat - aktuální koordináty -> v lince jsou všechny ulice po trase - spočítat na které přímce leží bod daný aktuálními koordináty
	
	public Vehicle(Timetable timetable, BusLine line, String id)
	{
		this.timetable = timetable;
		this.line = line;
		this.vehiclePath = new ArrayList<Coordinate>();
		//this.speed = 0;
		this.id = id;	
		
		this.addVehicleToLine();
	}
	
	public Vehicle() {
		this.vehiclePath = new ArrayList<Coordinate>();
	}
	
	/**
     * Nastaví název vozidla.
     * @param id název
     */
	public void setId(String id)
	{
		this.id = id;
	}
	
	public void setFirstEntry(Timetable timetable)
	{
		this.firstEntry = timetable.getEntries().get(0);
		
		this.nextStopTime = this.firstEntry.getTime().substring(3,5);
		
		this.previousStopTime = this.firstEntry.getTime().substring(3,5);
		
		System.out.println(this.nextStopTime);	
	}
	
	/**
     * Nastaví vozidlu linku.
     * @param line linka
     */
	public void setLine(BusLine line)
	{
		this.line = line;
	}
	
	/**
     * Nastaví grafickou reprezentaci vozidla.
     * @param VehicleView vehicle
     */
	@JsonIgnore
	public void setVehicle(VehicleView vehicle)
	{
		this.vehicleView = vehicle;
	}
	
	/**
     * Nastaví aktuální ulici.
     * @param street ulice
     */
	@JsonIgnore
	public void setCurrentStreet(Street street)
	{
		this.currentStreet= street;
	}
	
	/**
     * Nastaví aktuální pozici (souřadnice).
     * @param position souřadnice
     */
	@JsonIgnore
	public void setCurrentPosition(Coordinate position)
	{
		this.currentPosition = position;
	}
	
	/**
     * Získá jízdní řád.
     * @return Timetable jízdní řád
     */
	public Timetable getTimetable()
	{
		return this.timetable;
	}
	
	/**
     * Získá linku.
     * @return BusLine linka
     */
	public BusLine getLine()
	{
		return this.line;
	}
	
	/**
     * Získá název vozidla.
     * @return String název
     */
	public String getId()
	{
		return this.id;
	}
	
	/**
     * Získá aktuální pozici.
     * @return Coordinate aktuální pozice
     */
	@JsonIgnore
	public Coordinate getCurrentPosition()
	{
		return this.currentPosition;
	}
	
	/**
     * Získá aktuální ulici.
     * @return Street aktuální ulice
     */
	@JsonIgnore
	public Street getCurrentStreet()
	{
		return this.currentStreet;
	}
	
	/**
     * Získá objekt reprezentující grafickou reprezentaci vozidla.
     * @return VehicleView grafická reprezentace vozidla
     */
	@JsonIgnore
	public VehicleView getVehicle()
	{
		return this.vehicleView;
	}
	
	@JsonIgnore
	public List<Coordinate> getVehiclePath()
	{
		return this.vehiclePath;
	}
	
	/**
     * Přidá aktuální vozidlo v lince do jejího seznamu.
     */
	public void addVehicleToLine()
	{
		this.line.addVehicle(this);
	}
	
	/**
	 * Vypočte kompletní dráhu pro konkrétní vozidlo, uloží ji do listu.
	 */
	@JsonIgnore
	public void computeFullPath()
	{
		for (Street street: this.line.getStreets()) 
		{
			this.vehiclePath.add(street.getStart());
			
			if (street.getStops() != null)
			{
				for (Stop stop : street.getStops())
				{
					this.vehiclePath.add(stop.getCoordinate());
				}
			}
			
			this.vehiclePath.add(street.getEnd());
		}
	}
	
	
	/**
	 * Animace / průběh cesty + výpočet trasy (aby vozidla jela podle jízdního řádu).
	 * @param time aktuální globální čas
	 */
	public void drive(String time)
	{
		String inputTime = time.substring(3,5);
		
		List<Double> coords = new ArrayList<Double>();
		
		List<Double> coords2 = new ArrayList<Double>();
		
			
		if (inputTime.equals("00"))
		{	
			this.computeFullPath();
			

			this.setFirstEntry(this.timetable);
			double nextStopX = (double)this.firstEntry.getStop().getCoordinate().getX();
			double nextStopY = (double)this.firstEntry.getStop().getCoordinate().getY();
			
			coords.add((double)this.currentPosition.getX());
			coords.add((double)this.currentPosition.getY());
			coords2.add((double)this.currentPosition.getX() + 35);
			coords2.add((double)this.currentPosition.getY());
			
			for (; this.index < this.vehiclePath.size(); this.index++) 
			{
				if (this.vehiclePath.get(this.index).getX() == nextStopX && this.vehiclePath.get(this.index).getY() == nextStopY)
				{
					break;
				}
				
				coords.add(this.vehiclePath.get(this.index).getX());
				coords.add(this.vehiclePath.get(this.index).getY());
				coords2.add(this.vehiclePath.get(this.index).getX() + 35);
				coords2.add(this.vehiclePath.get(this.index).getY());
			}
			
			coords.add(nextStopX);
			coords.add(nextStopY);
			coords2.add(nextStopX + 35);
			coords2.add(nextStopY);
			
			this.setCurrentPosition(this.firstEntry.getStop().getCoordinate());
			this.nextStop = this.timetable.nextStop();
			
			
			Polyline pathVehicle = new Polyline();
			pathVehicle.getPoints().addAll(coords);
			
			Polyline pathName = new Polyline();
			pathName.getPoints().addAll(coords2);
			
			PathTransition transition = new PathTransition();
			transition.setNode(this.vehicleView.getCircle());
			transition.setDuration(Duration.seconds(Integer.parseInt(this.nextStopTime)));
			transition.setPath(pathVehicle);
			transition.play();
			
			PathTransition transition2 = new PathTransition();
			transition2.setNode(this.vehicleView.getText());
			transition2.setDuration(Duration.seconds(Integer.parseInt(this.nextStopTime)));
			transition2.setPath(pathName);
			transition2.play();
		}
		else if (this.firstEntry.getStop().getId() == this.nextStop.getStop().getId())
		{
			System.out.println("DEBUG: konec jízdy vozidla: " + this.getId());
			return;
		}
		else if (inputTime.equals(this.nextStop.getTime().substring(3,5)))
		{
			this.nextStopTime = Integer.toString((Integer.parseInt(this.nextStop.getTime().substring(3,5)) - Integer.parseInt(this.previousStopTime)));
			
			double nextStopX = (double)this.nextStop.getStop().getCoordinate().getX();
			double nextStopY = (double)this.nextStop.getStop().getCoordinate().getY();
			
			coords.add((double)this.currentPosition.getX());
			coords.add((double)this.currentPosition.getY());
			coords2.add((double)this.currentPosition.getX() + 35);
			coords2.add((double)this.currentPosition.getY());
			
			for (; this.index < this.vehiclePath.size(); this.index++) 
			{
				if (this.vehiclePath.get(this.index).getX() == nextStopX && this.vehiclePath.get(this.index).getY() == nextStopY)
				{
					break;
				}
				
				coords.add(this.vehiclePath.get(this.index).getX());
				coords.add(this.vehiclePath.get(this.index).getY());
				coords2.add(this.vehiclePath.get(this.index).getX() + 35);
				coords2.add(this.vehiclePath.get(this.index).getY());
			}
			
			coords.add(nextStopX);
			coords.add(nextStopY);
			coords2.add(nextStopX + 35);
			coords2.add(nextStopY);
			
			this.setCurrentPosition(this.nextStop.getStop().getCoordinate());
			
			this.previousStopTime = this.nextStopTime;
			this.nextStop = this.timetable.nextStop();
			
			
			Polyline pathVehicle = new Polyline();
			pathVehicle.getPoints().addAll(coords);
			
			Polyline pathName = new Polyline();
			pathName.getPoints().addAll(coords2);
			
			PathTransition transition = new PathTransition();
			transition.setNode(this.vehicleView.getCircle());
			transition.setDuration(Duration.seconds(Integer.parseInt(this.nextStopTime)));
			transition.setPath(pathVehicle);
			transition.play();
			
			PathTransition transition2 = new PathTransition();
			transition2.setNode(this.vehicleView.getText());
			transition2.setDuration(Duration.seconds(Integer.parseInt(this.nextStopTime)));
			transition2.setPath(pathName);
			transition2.play();
		}
	}
}
