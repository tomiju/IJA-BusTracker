package map;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import drawable.VehicleView;
import gui.Drawable;
import gui.SceneController;
import javafx.animation.PathTransition;
import javafx.scene.shape.Circle;
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
	
	// aktuální ulice se dá dopočítat - aktuální koordináty -> v lince jsou všechny ulice po trase - spočítat na které přímce leží bod daný aktuálními koordináty
	
	public Vehicle(Timetable timetable, BusLine line, String id)
	{
		this.timetable = timetable;
		this.line = line;
		//this.speed = 0;
		this.id = id;
		
		this.setCurrentStreet(currentStreet);
		this.setCurrentPosition(line.getStart().getCoordinate());
		
		this.addVehicleToLine();
	}
	
	public Vehicle() {}
	
	/**
     * Nastaví název vozidla.
     * @param id název
     */
	public void setId(String id)
	{
		this.id = id;
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
	
	/**
     * Přidá aktuální vozidlo v lince do jejího seznamu.
     */
	public void addVehicleToLine()
	{
		this.line.addVehicle(this);
	}
	
	public void drive() // TODO: cesta po zastávku - podle časového údaje - (argument nové zastávky?)
	{
		//double[] coords = new double[this.line.getStreets().size()];
		List<Double> coords = new ArrayList<Double>();
		
		List<Double> coords2 = new ArrayList<Double>();
		//int i = this.line.getStreets().size();
		
		for (Street street : this.line.getStreets())
		{
			coords.add((double)street.getStart().getX());
			coords.add((double)street.getStart().getY());
			
			coords.add((double)street.getEnd().getX());
			coords.add((double)street.getEnd().getY());
			
			coords2.add((double)street.getStart().getX() + 35);
			coords2.add((double)street.getStart().getY());
			
			coords2.add((double)street.getEnd().getX() + 35);
			coords2.add((double)street.getEnd().getY());
		}
		
		Polyline pathVehicle = new Polyline();
		pathVehicle.getPoints().addAll(coords);
		
		Polyline pathName = new Polyline();
		pathName.getPoints().addAll(coords2);
		
		PathTransition transition = new PathTransition();
		transition.setNode(vehicleView.getCircle());
		transition.setDuration(Duration.seconds(5));
		transition.setPath(pathVehicle);
		transition.setCycleCount(PathTransition.INDEFINITE);
		transition.play();
		
		PathTransition transition2 = new PathTransition();
		transition2.setNode(vehicleView.getText());
		transition2.setDuration(Duration.seconds(5));
		transition2.setPath(pathName);
		transition2.setCycleCount(PathTransition.INDEFINITE);
		transition2.play();
		
		//polyline.getPoints().addall
	}
}
