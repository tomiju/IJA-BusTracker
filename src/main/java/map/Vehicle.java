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
	
	private VehicleView vehicleView;
	private TimetableEntry firstEntry;
	private TimetableEntry nextStop;
	private TimetableEntry previousStop;
	private int nextStopTime;
	private int previousStopTime;
	private List<Coordinate> vehiclePath;
	private int index = 0;
	private PathTransition transitionVehicle = new PathTransition();
	private PathTransition transitionText = new PathTransition();
	private int endTime;
	private boolean ended = false;
	
	// aktualni ulice se da dopocitat - aktualni koordinaty -> v lince jsou vsechny ulice po trase - spocitat na ktere primce lezi bod dany aktualnimi koordinaty
	
	public Vehicle(Timetable timetable, BusLine line, String id)
	{
		this.timetable = timetable;
		this.line = line;
		this.vehiclePath = new ArrayList<Coordinate>();
		this.id = id;
	}
	
	public Vehicle() 
	{
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
		
		this.endTime = Integer.parseInt(timetable.getEntries().get(timetable.getEntries().size() - 1).getTime().substring(3,5)) + (Integer.parseInt(timetable.getEntries().get(timetable.getEntries().size() - 1).getTime().substring(0,2)) * 60);
		
		this.previousStop = this.firstEntry;
		
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
	 * @return List aktualni trasa vozidla
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
	 * @param timeSpeed rychlost casu
	 */
	public void drive(String time, int timeSpeed)
	{
		if(!this.getLine().isEdited())
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
				
				coords.add(this.currentPosition.getX());
				coords.add(this.currentPosition.getY());
				coords2.add(this.currentPosition.getX() + 35);
				coords2.add(this.currentPosition.getY());
				
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
				transition.setDuration(Duration.millis(this.nextStopTime/timeSpeed*1000));
				transition.setPath(pathVehicle);
				transition.play();
				
				PathTransition transition2 = new PathTransition();
				transition2.setNode(this.vehicleView.getText());
				transition2.setDuration(Duration.millis(this.nextStopTime/timeSpeed*1000));
				transition2.setPath(pathName);
				transition2.play();
				
				this.transitionVehicle = transition;
				this.transitionText = transition2;
			}
			else if (this.endTime == inputTime)
			{
				this.transitionVehicle.stop();
				this.transitionText.stop();
				System.out.println(this.getId() + " finished travelling.");
				this.ended = true;
				
				this.getVehicleView().getCircle().setVisible(false);
				this.getVehicleView().getText().setVisible(false);
				
				return;
			}
			else if (inputTime == Integer.parseInt(this.previousStop.getTime().substring(3,5)) + (Integer.parseInt(this.previousStop.getTime().substring(0,2)) * 60) || inputTime == Integer.parseInt(this.firstEntry.getTime().substring(3,5)) + (Integer.parseInt(this.firstEntry.getTime().substring(0,2)) * 60))
			{
				this.nextStopTime = Integer.parseInt(this.nextStop.getTime().substring(3,5)) + (Integer.parseInt(this.nextStop.getTime().substring(0,2)) * 60) - this.previousStopTime;
				
				double nextStopX = (double)this.nextStop.getStop().getCoordinate().getX();
				double nextStopY = (double)this.nextStop.getStop().getCoordinate().getY();
				
				coords.add(this.currentPosition.getX());
				coords.add(this.currentPosition.getY());
				coords2.add(this.currentPosition.getX() + 35);
				coords2.add(this.currentPosition.getY());
				
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
				transition.setDuration(Duration.millis(this.nextStopTime/timeSpeed*1000));
				transition.setPath(pathVehicle);
				transition.play();
				
				PathTransition transition2 = new PathTransition();
				transition2.setNode(this.vehicleView.getText());
				transition2.setDuration(Duration.millis(this.nextStopTime/timeSpeed*1000));
				transition2.setPath(pathName);
				transition2.play();
				
				this.transitionVehicle = transition;
				this.transitionText = transition2;
			}
		}
		else
		{
			//todo jiny vypocet cesty
			System.out.println("DEBUG drive(): Trasa je editovana TODO funkce na novou animaci"); // debug
		}
		
	}
	
	/**
	 * Zrusi animaci cesty.
	 */
	public void cancelVehicle()
	{
		if (this.transitionVehicle != null && this.transitionText != null)
		{
			this.transitionVehicle.stop();
			this.transitionText.stop();			
		}
	}
	
	/**
	 * Pozastavi animaci cesty.
	 */
	public void pauseVehicle()
	{
		if(!this.isFinished() && this.transitionVehicle != null && this.transitionText != null)
		{
			this.transitionVehicle.pause();
			this.transitionText.pause();
		}
	}
	
	/**
	 * Znovu spusti animaci cesty.
	 */
	public void resumeVehicle()
	{	
		if(!this.isFinished() && this.transitionVehicle != null && this.transitionText != null)
		{
			this.transitionVehicle.play();
			this.transitionText.play();			
		}	
	}
	
	/**
	 * Zrusi animaci cesty a ulozi soucasne souradnice vozidla, nacte vozidlo na soucasne souradnice. (Pro edit mode a animaci nove trasy)
	 */
	public void cancelDriving()
	{
		if(!this.isFinished() && this.transitionVehicle != null && this.transitionText != null)
		{
			//System.out.println(this.getId()+" Current position: X" + this.getCurrentPosition().getX() + " Y " + this.getCurrentPosition().getY());
			
			// nastaveni nove soucasne pozice vozidla
			this.setCurrentPosition(new Coordinate(this.getVehicleView().getCircle().getCenterX() + this.getVehicleView().getCircle().getTranslateX(), this.getVehicleView().getCircle().getCenterY() + this.getVehicleView().getCircle().getTranslateY()));
			
			// zruseni animace (vrati objekt na zacatek, proto predtim ulozim pozici a objekt prekreslim na spravne misto
			this.transitionVehicle.stop();
			this.transitionVehicle = null;
			//System.out.println("Cancelled");
			
			// prekresleni na spravne misto
			//this.getVehicleView().getCircle().setCenterX(this.getCurrentPosition().getX());
			//this.getVehicleView().getCircle().setCenterY(this.getCurrentPosition().getY());
			//this.getVehicleView().getCircle().setFill(Color.YELLOW);
			
			//System.out.println(this.getId()+" Current position: X" + this.getCurrentPosition().getX() + " Y " + this.getCurrentPosition().getY());

			// nastaveni nove soucasne pozice popisku vozidla
			this.setCurrentPosition(new Coordinate(this.getVehicleView().getText().getX() + this.getVehicleView().getText().getTranslateX(), this.getVehicleView().getText().getY() + this.getVehicleView().getText().getTranslateY()));			
			this.transitionText.stop();
			this.transitionText = null;

			//this.getVehicleView().getText().xProperty().bind(this.getVehicleView().getCircle().centerXProperty().add(7));
			//this.getVehicleView().getText().yProperty().bind(this.getVehicleView().getCircle().centerYProperty());
		}	
	}
	
	/**
	 * Pomocna funkce pro restart simulace.
	 */
	public void resetIndex()
	{
		this.index = 0;
	}
	
	/**
	 * Pomocna funkce pro restart simulace.
	 */
	public void resetVehiclePath()
	{
		this.vehiclePath = new ArrayList<Coordinate>();
		this.ended = false;
		
		this.getVehicleView().getCircle().setVisible(true);
		this.getVehicleView().getText().setVisible(true);
	}
	
	/**
	 * Pomocna funkce, ktera zjisti, zda dotycne auto jiz dokoncilo cestu
	 * @return bool stav cesty
	 */
	public boolean isFinished()
	{
		return this.ended;
	}
	
	/**
	 * Vrati aktualne projetou zastavku.
	 * @return String nazev aktualni zastavky
	 */
	@JsonIgnore
	public String getCurrentStopId()
	{
		return this.previousStop.getStop().getId().concat("\n"+this.previousStop.getTime());
	}
}
