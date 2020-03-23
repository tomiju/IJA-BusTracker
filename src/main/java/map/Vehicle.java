package map;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import drawable.VehicleView;
import javafx.animation.PathTransition;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;

/**
 * 
 * Trida reprezentujici vozidlo.
 * Sklada se z ID, linky, na ktere vozidlo jezdi, jizdniho radu, aktualni pozice a aktualni ulice, na ktere se vozidlo nachazi.
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
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
	private TimetableEntry previousStop;
	private int nextStopTime;
	private int previousStopTime;
	private List<Coordinate> vehiclePath;
	private int index = 0;
	
	// aktualni ulice se da dopocitat - aktualni koordinaty -> v lince jsou vsechny ulice po trase - spocitat na ktere primce lezi bod dany aktualnimi koordinaty
	
	public Vehicle(Timetable timetable, BusLine line, String id)
	{
		this.timetable = timetable;
		this.line = line;
		this.vehiclePath = new ArrayList<Coordinate>();
		//this.speed = 0;
		this.id = id;
	}
	
	public Vehicle() {
		this.vehiclePath = new ArrayList<Coordinate>();
	}
	
	/**
     * Nastavi nazev vozidla.
     * @param id nazev
     */
	public void setId(String id)
	{
		this.id = id;
	}
	
	/**
	 * Nastavi prvni zastavku a cas prijezdu
	 * @param timetable jizdni rad
	 */
	public void setFirstEntry(Timetable timetable)
	{
		this.firstEntry = timetable.getEntries().get(0);
		
		this.nextStopTime = Integer.parseInt(this.firstEntry.getTime().substring(3,5)) + (Integer.parseInt(this.firstEntry.getTime().substring(0,2)) * 60);
	
		this.previousStopTime = Integer.parseInt(this.firstEntry.getTime().substring(3,5)) + (Integer.parseInt(this.firstEntry.getTime().substring(0,2)) * 60);	
	}
	
	/**
     * Nastavi vozidlu linku.
     * @param line linka
     */
	public void setLine(BusLine line)
	{
		this.line = line;
	}
	
	/**
     * Nastavi grafickou reprezentaci vozidla.
     * @param vehicle Objekt obsahujici grafickou reprezentaci vozidla.
     */
	@JsonIgnore
	public void setVehicle(VehicleView vehicle)
	{
		this.vehicleView = vehicle;
	}
	
	/**
     * Nastavi aktualni ulici.
     * @param street ulice
     */
	@JsonIgnore
	public void setCurrentStreet(Street street)
	{
		this.currentStreet= street;
	}
	
	/**
     * Nastavi aktualni pozici (souradnice).
     * @param position souradnice
     */
	@JsonIgnore
	public void setCurrentPosition(Coordinate position)
	{
		this.currentPosition = position;
	}
	
	/**
	 * Vrati objekt reprezentujici graficke zobrazeni vozidla na mape
	 * @return VehicleView objekt reprezentujici graficke zobrazeni vozidla na mape
	 */
	@JsonIgnore
	public VehicleView getVehicleView()
	{
		return this.vehicleView;
	}
	
	/**
     * Ziska jizdni rad.
     * @return Timetable jizdni rad
     */
	public Timetable getTimetable()
	{
		return this.timetable;
	}
	
	/**
     * Ziska linku.
     * @return BusLine linka
     */
	public BusLine getLine()
	{
		return this.line;
	}
	
	/**
     * Ziska nazev vozidla.
     * @return String nazev
     */
	public String getId()
	{
		return this.id;
	}
	
	/**
     * Ziska aktualni pozici.
     * @return Coordinate aktualni pozice
     */
	@JsonIgnore
	public Coordinate getCurrentPosition()
	{
		return this.currentPosition;
	}
	
	/**
     * Ziska aktualni ulici.
     * @return Street aktualni ulice
     */
	@JsonIgnore
	public Street getCurrentStreet()
	{
		return this.currentStreet;
	}
	
	/**
     * Ziska objekt reprezentujici grafickou reprezentaci vozidla.
     * @return VehicleView graficka reprezentace vozidla
     */
	@JsonIgnore
	public VehicleView getVehicle()
	{
		return this.vehicleView;
	}
	
	/**
	 * Ziska aktualni trasu vozidla
	 * @return List<Coordinate> aktualni trasa vozidla
	 */
	@JsonIgnore
	public List<Coordinate> getVehiclePath()
	{
		return this.vehiclePath;
	}
	
	/**
     * Prida aktualni vozidlo v lince do jejiho seznamu.
     */
	public void addVehicleToLine()
	{
		this.line.addVehicle(this);
	}
	
	/**
	 * Vypocte kompletni drahu pro konkretni vozidlo, ulozi ji do listu.
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
					
					if (stop.getId() == this.getLine().getEnd().getId())
					{
						return;
					}
				}
			}
			this.vehiclePath.add(street.getEnd());
		}
	}
	
	/**
	 * Animace / prubeh cesty + vypocet trasy (aby vozidla jela podle jizdniho radu).
	 * @param time aktualni globalni cas
	 */
	public void drive(String time)
	{
		String hoursTmp = time.substring(0,2);
		int inputTime = (Integer.parseInt(time.substring(3,5)) + (Integer.parseInt(hoursTmp) * 60));
		
		List<Double> coords = new ArrayList<Double>();
		
		List<Double> coords2 = new ArrayList<Double>();
				
		if (inputTime == 0)
		{	
			this.computeFullPath();
			
			this.setFirstEntry(this.timetable);
			double nextStopX = (double)this.firstEntry.getStop().getCoordinate().getX();
			double nextStopY = (double)this.firstEntry.getStop().getCoordinate().getY();
			
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
			this.previousStop = this.firstEntry;
			this.nextStop = this.timetable.nextStop();
					
			Polyline pathVehicle = new Polyline();
			pathVehicle.getPoints().addAll(coords);
			
			Polyline pathName = new Polyline();
			pathName.getPoints().addAll(coords2);
			
			PathTransition transition = new PathTransition();
			transition.setNode(this.vehicleView.getCircle());
			transition.setDuration(Duration.seconds(this.nextStopTime));
			transition.setPath(pathVehicle);
			transition.play();
			
			PathTransition transition2 = new PathTransition();
			transition2.setNode(this.vehicleView.getText());
			transition2.setDuration(Duration.seconds(this.nextStopTime));
			transition2.setPath(pathName);
			transition2.play();
		}
		else if (this.firstEntry.getStop().getId() == this.nextStop.getStop().getId())
		{
			return;
		}
		else if (inputTime == Integer.parseInt(this.previousStop.getTime().substring(3,5)) + (Integer.parseInt(this.previousStop.getTime().substring(0,2)) * 60) || inputTime == Integer.parseInt(this.firstEntry.getTime().substring(3,5)) + (Integer.parseInt(this.firstEntry.getTime().substring(0,2)) * 60))
		{
			this.nextStopTime = Integer.parseInt(this.nextStop.getTime().substring(3,5)) + (Integer.parseInt(this.nextStop.getTime().substring(0,2)) * 60) - this.previousStopTime;
			
			double nextStopX = (double)this.nextStop.getStop().getCoordinate().getX();
			double nextStopY = (double)this.nextStop.getStop().getCoordinate().getY();
			
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
			
			this.previousStopTime = Integer.parseInt(this.nextStop.getTime().substring(3,5)) + (Integer.parseInt(this.nextStop.getTime().substring(0,2)) * 60);
			this.previousStop = this.nextStop;
			this.nextStop = this.timetable.nextStop();
			
			
			Polyline pathVehicle = new Polyline();
			pathVehicle.getPoints().addAll(coords);
			
			Polyline pathName = new Polyline();
			pathName.getPoints().addAll(coords2);
			
			PathTransition transition = new PathTransition();
			transition.setNode(this.vehicleView.getCircle());
			transition.setDuration(Duration.seconds(this.nextStopTime));
			transition.setPath(pathVehicle);
			transition.play();
			
			PathTransition transition2 = new PathTransition();
			transition2.setNode(this.vehicleView.getText());
			transition2.setDuration(Duration.seconds(this.nextStopTime));
			transition2.setPath(pathName);
			transition2.play();
		}
	}
}
