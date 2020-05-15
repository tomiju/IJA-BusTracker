package map;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import drawable.VehicleView;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;
import java.util.Random;

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
	private ArrayList<Coordinate> vehiclePath; // cesta vozidla
	private int index = 0;
	private PathTransition transitionVehicle = new PathTransition();
	private PathTransition transitionText = new PathTransition();
	private int endTime;
	private boolean ended = false;

	private Random random = new Random();
	private int delay = 0;
	private Coordinate editPreviousCoord = new Coordinate(0,0);
	private int editedPathStopsIndex = 0; // pomocny index
	private ArrayList<TimetableEntry> editedPathStops = new ArrayList<TimetableEntry>(); // editovana cesta vozidla

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
	 * @return ArrayList aktualni trasa vozidla
	 */
	@JsonIgnore
	public ArrayList<Coordinate> getVehiclePath()
	{
		return this.vehiclePath;
	}

	/**
	 * Ziska zpozdeni vozidla pri objizdke
	 * @return int Zpozdeni vozidla
	 */
	@JsonIgnore
	public int getDelay()
	{
		return this.delay;
	}

	/**
	 * Ziska editovany jizdni rad
	 * @return ArrayList editovany jizdni rad
	 */
	public ArrayList<TimetableEntry> getEditedPathStops()
	{
		return this.editedPathStops;
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
		this.vehiclePath = new ArrayList<Coordinate>();

		try
		{
			// zpatecni linka
			String str = this.getLine().getId();
			String result = str.substring(str.indexOf("(") + 1, str.indexOf(")")); // kdyz nazev obsahuje slovo "backwards", tak je to zpatecni linka

			if(result.equals("backwards"))
			{
				for (Street street: this.line.getStreets()) // projde cestu a postupne ulozi body
				{
					this.vehiclePath.add(street.getEnd());

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
					this.vehiclePath.add(street.getStart());
				}
			}
		}
		catch(Exception e)
		{
			// klasicky smer
			for (Street street: this.line.getStreets()) // projde cestu a postupne ulozi body, klasicka linka
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
	}

	/**
	 * Animace / prubeh cesty + vypocet trasy (aby vozidla jela podle jizdniho radu).
	 * @param time aktualni globalni cas
	 * @param timeSpeed rychlost casu
	 */
	public void drive(String time, int timeSpeed)
	{
		String hoursTmp = time.substring(0,2);
		int inputTime = (Integer.parseInt(time.substring(3,5)) + (Integer.parseInt(hoursTmp) * 60));

		if(!this.getLine().isEdited())
		{
			ArrayList<Double> coords = new ArrayList<Double>();
			ArrayList<Double> coords2 = new ArrayList<Double>();

			if (inputTime == 0) // prvni zastavka - nastaveni promennych
			{
				this.computeFullPath();
				this.setFirstEntry(this.timetable);
				double nextStopX = (double)this.firstEntry.getStop().getCoordinate().getX();
				double nextStopY = (double)this.firstEntry.getStop().getCoordinate().getY();

				coords.add(this.currentPosition.getX());
				coords.add(this.currentPosition.getY());
				coords2.add(this.currentPosition.getX() + 35);
				coords2.add(this.currentPosition.getY());

				for (; this.index < this.vehiclePath.size(); ++this.index)
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
				this.setCurrentStreet(this.firstEntry.getStop().getStreet());

				this.previousStop = this.firstEntry; // prvni zastavka
				this.nextStop = this.timetable.nextStop(); // druha zastavka

				Polyline pathVehicle = new Polyline();
				pathVehicle.getPoints().addAll(coords);

				Polyline pathName = new Polyline();
				pathName.getPoints().addAll(coords2);

				PathTransition transition = new PathTransition(); // nastaveni animace
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
			else if (this.endTime == inputTime) // konec jizdy
			{
				this.transitionVehicle.stop();
				this.transitionText.stop();

				this.ended = true;

				this.getVehicleView().getCircle().setVisible(false);
				this.getVehicleView().getText().setVisible(false);

				return;
			}
			else if (inputTime == Integer.parseInt(this.previousStop.getTime().substring(3,5)) + (Integer.parseInt(this.previousStop.getTime().substring(0,2)) * 60) || inputTime == Integer.parseInt(this.firstEntry.getTime().substring(3,5)) + (Integer.parseInt(this.firstEntry.getTime().substring(0,2)) * 60))
			{
				// po prujezdu prvni zastavkou se zviditelni vozidlo
				this.getVehicleView().getCircle().setVisible(true);
				this.getVehicleView().getText().setVisible(true);

				if(this.transitionVehicle != null)
				{
					this.transitionVehicle.stop();

					this.transitionText.stop();
				}

				// parsovani casu dalsi zastavky - kvuli delce jizdy
				this.nextStopTime = Integer.parseInt(this.nextStop.getTime().substring(3,5)) + (Integer.parseInt(this.nextStop.getTime().substring(0,2)) * 60) - this.previousStopTime;

				double nextStopX = (double)this.nextStop.getStop().getCoordinate().getX();
				double nextStopY = (double)this.nextStop.getStop().getCoordinate().getY();

				// nasklada se trasa k dalsi zastavce
				coords.add(this.currentPosition.getX());
				coords.add(this.currentPosition.getY());
				coords2.add(this.currentPosition.getX() + 35);
				coords2.add(this.currentPosition.getY());

				for (; this.index < this.vehiclePath.size(); ++this.index)
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

				// aktualni pozice je nasledujici zastavka
				this.setCurrentPosition(this.nextStop.getStop().getCoordinate());
				this.setCurrentStreet(this.previousStop.getStop().getStreet());

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
		{	// editovana trasa
			if(this.transitionVehicle == null && !this.ended)
			{
				hoursTmp = time.substring(0,2);
				inputTime = (Integer.parseInt(time.substring(3,5)) + (Integer.parseInt(hoursTmp) * 60));

				// delka cele jizdy, (pom - inputTime) je zbyvajici delka jizdy
				int pom = Integer.parseInt(this.getTimetable().getEntries().get(this.getTimetable().getEntries().size() - 1).getTime().substring(3,5)) + (Integer.parseInt(this.getTimetable().getEntries().get(this.getTimetable().getEntries().size() - 1).getTime().substring(0,2)) * 60);

				// nastaveni nove aktualni pozice s korekci
				this.setCurrentPosition(new Coordinate(this.getVehicleView().getCircle().getCenterX() + this.getVehicleView().getCircle().getTranslateX() - 10.0, this.getVehicleView().getCircle().getCenterY() + this.getVehicleView().getCircle().getTranslateY()));
				this.setCurrentPosition(new Coordinate(this.getVehicleView().getText().getX() + this.getVehicleView().getText().getTranslateX() - 10.0, this.getVehicleView().getText().getY() + this.getVehicleView().getText().getTranslateY()));
				this.vehiclePath = new ArrayList<Coordinate>();

				this.resetIndex();

				TimetableEntry nextStopBackup = null;

				for (Street street: this.line.getStreets())
				{
					// pomocne promenne pro vzdalenost
					double start_and_vehicle = round(this.distance(street.getStart().getX(), street.getStart().getY(), this.getCurrentPosition().getX(), this.getCurrentPosition().getY()), 0);
					double vehicle_and_end = round(this.distance(street.getEnd().getX(), street.getEnd().getY(), this.getCurrentPosition().getX(), this.getCurrentPosition().getY()), 0);
					double vehicle_distance = start_and_vehicle + vehicle_and_end;
					double street_size = round(this.distance(street.getEnd().getX(), street.getEnd().getY(), street.getStart().getX(), street.getStart().getY()), 0);

					// urceni pozice na aktualni ulici (
					if(vehicle_distance - street_size >= -10.0 && vehicle_distance - street_size <= 10.0)
					{
						try
						{
							// pripad, kdy je to zpatecni linka
							String str = this.getLine().getId();
							String result = str.substring(str.indexOf("(") + 1, str.indexOf(")")); // kdyz nazev obsahuje slovo "backwards", tak je to zpatecni linka

							if(result.equals("backwards"))
							{
								this.editPreviousCoord = street.getStart();

								if (inputTime <= Integer.parseInt(this.previousStop.getTime().substring(3,5)) + (Integer.parseInt(this.previousStop.getTime().substring(0,2)) * 60) || inputTime <= Integer.parseInt(this.firstEntry.getTime().substring(3,5)) + (Integer.parseInt(this.firstEntry.getTime().substring(0,2)) * 60))
								{ // pokud uz projel zastavkou na aktualni ulici, tak ji neulozim, jinak ano
									if(this.previousStop.getStop().getStreet().getId().equals(street.getId()))
									{
										this.vehiclePath.add(this.previousStop.getStop().getCoordinate());
										nextStopBackup = this.previousStop;
										this.editedPathStops.add(nextStopBackup);
										this.editedPathStopsIndex = this.editedPathStops.size() - 1;
									}
								}
								this.vehiclePath.add(street.getStart());
								this.setCurrentStreet(street);
								continue;
							}
						}
						catch(Exception e)
						{
							// pripad, kde je to klasicka linka smerem "dopredu"

							this.editPreviousCoord = street.getEnd();

							if (inputTime <= Integer.parseInt(this.previousStop.getTime().substring(3,5)) + (Integer.parseInt(this.previousStop.getTime().substring(0,2)) * 60) || inputTime <= Integer.parseInt(this.firstEntry.getTime().substring(3,5)) + (Integer.parseInt(this.firstEntry.getTime().substring(0,2)) * 60))
							{ // pokud uz projel zastavkou na aktualni ulici, tak ji neulozim, jinak ano
								if(this.previousStop.getStop().getStreet().getId().equals(street.getId()))
								{
									this.vehiclePath.add(this.previousStop.getStop().getCoordinate());
									nextStopBackup = this.previousStop;
									this.editedPathStops.add(nextStopBackup);
									this.editedPathStopsIndex = this.editedPathStops.size() - 1;
								}
							}
							this.vehiclePath.add(street.getEnd());
							this.setCurrentStreet(street);
							continue;
						}
					}

					if(street.getEnd().getX() == this.editPreviousCoord.getX() && street.getEnd().getY() == this.editPreviousCoord.getY()) // na ulici prijizdi zezadu
					{
						this.vehiclePath.add(street.getEnd());

						for(TimetableEntry timetable : this.getTimetable().getEntries()) // kdyz je ulice v puvodnim jizdnim radu, tak ji ulozim do trasy
						{
							if(timetable.getStop().getStreet().getId().equals(street.getId()))
							{
								this.vehiclePath.add(timetable.getStop().getCoordinate());
								this.editedPathStops.add(timetable);

								if(nextStopBackup == null)
								{
									nextStopBackup = timetable;
									this.editedPathStopsIndex = this.editedPathStops.size() - 1;
								}
							}
						}

						this.vehiclePath.add(street.getStart());

						this.editPreviousCoord = street.getStart();
					}
					else if(street.getStart().getX() == this.editPreviousCoord.getX() && street.getStart().getY() == this.editPreviousCoord.getY()) // na ulici prijizdi zepredu
					{
						this.vehiclePath.add(street.getStart());

						for(TimetableEntry timetable : this.getTimetable().getEntries()) // kdyz je ulice v puvodnim jizdnim radu, tak ji ulozim do trasy
						{
							if(timetable.getStop().getStreet().getId().equals(street.getId()))
							{
								this.vehiclePath.add(timetable.getStop().getCoordinate());
								this.editedPathStops.add(timetable);

								if(nextStopBackup == null)
								{
									nextStopBackup = timetable;
									this.editedPathStopsIndex = this.editedPathStops.size() - 1;
								}
							}
						}

						this.vehiclePath.add(street.getEnd());

						this.editPreviousCoord = street.getEnd();
					}
				}

				// osetreni prazdne cesty - vozidlo je na uzavrene ceste, apod...
				if(this.vehiclePath.isEmpty())
				{
					this.ended = true;

					Platform.runLater(() -> { // vypis informace o vozidle, ktere nemuze pokracovat v jizde

						Alert alert = new Alert(Alert.AlertType.WARNING);

						DialogPane dialogPane = alert.getDialogPane();
						dialogPane.getStylesheets().add("/style.css");

						alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
						alert.setTitle("Vehicle has no path");
						alert.setHeaderText("Warning\nVehicle: \"" + this.getId() + "\" doesn't know where to go :(");
						alert.setContentText("Stuck... Forever :(");
						alert.show();

		            });
					return;
				}

				ArrayList<Double> coords = new ArrayList<Double>();
				ArrayList<Double> coords2 = new ArrayList<Double>();

				coords.add(this.currentPosition.getX());
				coords.add(this.currentPosition.getY());
				coords2.add(this.currentPosition.getX() + 35);
				coords2.add(this.currentPosition.getY());

				for (; this.index < this.vehiclePath.size(); ++this.index)
				{
					if(nextStopBackup != null)
					{
						if (this.vehiclePath.get(this.index).getX() == nextStopBackup.getStop().getCoordinate().getX() && this.vehiclePath.get(this.index).getY() == nextStopBackup.getStop().getCoordinate().getY())
						{ // prvni animace po nasledujici zastavku
							coords.add(nextStopBackup.getStop().getCoordinate().getX());
							coords.add(nextStopBackup.getStop().getCoordinate().getY());
							coords2.add(nextStopBackup.getStop().getCoordinate().getX() + 35);
							coords2.add(nextStopBackup.getStop().getCoordinate().getY());

							pom = Integer.parseInt(nextStopBackup.getTime().substring(3,5)) + (Integer.parseInt(nextStopBackup.getTime().substring(0,2)) * 60);
							this.setCurrentPosition(nextStopBackup.getStop().getCoordinate());
							break;
						}
					}

					coords.add(this.vehiclePath.get(this.index).getX());
					coords.add(this.vehiclePath.get(this.index).getY());
					coords2.add(this.vehiclePath.get(this.index).getX() + 35);
					coords2.add(this.vehiclePath.get(this.index).getY());
				}

				Polyline pathVehicle = new Polyline();
				pathVehicle.getPoints().addAll(coords);

				Polyline pathName = new Polyline();
				pathName.getPoints().addAll(coords2);

				this.delay = random.nextInt(10) + 5; // nahodne zpozdeni
				int randomTimer = (pom - inputTime) + this.delay;

				PathTransition transition = new PathTransition();
				transition.setNode(this.vehicleView.getCircle());
				transition.setDuration(Duration.millis(randomTimer/timeSpeed*1000));
				transition.setPath(pathVehicle);
				transition.play();

				PathTransition transition2 = new PathTransition();
				transition2.setNode(this.vehicleView.getText());
				transition2.setDuration(Duration.millis(randomTimer/timeSpeed*1000));
				transition2.setPath(pathName);
				transition2.play();

				this.transitionVehicle = transition;
				this.transitionText = transition2;
				this.nextStop = nextStopBackup;
			}
			else if(!this.ended && this.nextStop != null && (inputTime) == (Integer.parseInt(this.nextStop.getTime().substring(3,5)) + (Integer.parseInt(this.nextStop.getTime().substring(0,2)) * 60) + this.delay))
			{

				if(this.transitionVehicle != null)
				{
					this.transitionVehicle.stop();

					this.transitionText.stop();
				}

				this.getVehicleView().getCircle().setVisible(true); // vozidlo je viditelne po prvni zastavce
				this.getVehicleView().getText().setVisible(true);

				if(this.editedPathStopsIndex + 1 < this.editedPathStops.size()) // pokud existuje jeste dalsi zastavka, tak je nasledujici, jinak dojel
				{
					this.nextStop = this.editedPathStops.get(this.editedPathStopsIndex + 1);
					this.editedPathStopsIndex++;
				}
				else
				{
					this.nextStop = null;
					this.ended = true;
					return;
				}

				int pom = (Integer.parseInt(this.nextStop.getTime().substring(3,5)) + (Integer.parseInt(this.nextStop.getTime().substring(0,2)) * 60) + this.delay);

				ArrayList<Double> coords = new ArrayList<Double>();
				ArrayList<Double> coords2 = new ArrayList<Double>();

				coords.add(this.currentPosition.getX());
				coords.add(this.currentPosition.getY());
				coords2.add(this.currentPosition.getX() + 35);
				coords2.add(this.currentPosition.getY());

				for (; this.index < this.vehiclePath.size(); ++this.index)
				{
					if(this.nextStop != null)
					{
						if (this.vehiclePath.get(this.index).getX() == this.nextStop.getStop().getCoordinate().getX() && this.vehiclePath.get(this.index).getY() == this.nextStop.getStop().getCoordinate().getY())
						{
							coords.add(this.nextStop.getStop().getCoordinate().getX());
							coords.add(this.nextStop.getStop().getCoordinate().getY());
							coords2.add(this.nextStop.getStop().getCoordinate().getX() + 35);
							coords2.add(this.nextStop.getStop().getCoordinate().getY());

							pom = Integer.parseInt(this.nextStop.getTime().substring(3,5)) + (Integer.parseInt(this.nextStop.getTime().substring(0,2)) * 60);
							this.setCurrentPosition(this.nextStop.getStop().getCoordinate());
							break;
						}
					}

					coords.add(this.vehiclePath.get(this.index).getX());
					coords.add(this.vehiclePath.get(this.index).getY());
					coords2.add(this.vehiclePath.get(this.index).getX() + 35);
					coords2.add(this.vehiclePath.get(this.index).getY());
				}


				Polyline pathVehicle = new Polyline();
				pathVehicle.getPoints().addAll(coords);

				Polyline pathName = new Polyline();
				pathName.getPoints().addAll(coords2);

				int randomTimer = (pom - inputTime) + this.delay;

				PathTransition transition = new PathTransition();
				transition.setNode(this.vehicleView.getCircle());
				transition.setDuration(Duration.millis(randomTimer/timeSpeed*1000));
				transition.setPath(pathVehicle);
				transition.play();

				PathTransition transition2 = new PathTransition();
				transition2.setNode(this.vehicleView.getText());
				transition2.setDuration(Duration.millis(randomTimer/timeSpeed*1000));
				transition2.setPath(pathName);
				transition2.play();

				this.transitionVehicle = transition;
				this.transitionText = transition2;
			}
			else if(this.transitionVehicle != null && !this.transitionVehicle.getStatus().equals(Animation.Status.RUNNING) && this.nextStop == null) // konec jizdy
			{
				this.ended = true;

				this.getVehicleView().getCircle().setVisible(false);
				this.getVehicleView().getText().setVisible(false);

				return;
			}
		}

	}

	/*
	 * Pomocna funkce pro zaokrouhleni vzdalenosti bodu
	 */
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

	/*
	 * Pomocna funkce pro vypocet vzdalenosti bodu
	 */
	private double distance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
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
	 * Zrusi animaci cesty a ulozi soucasne souradnice vozidla. (Pro edit mode a animaci nove trasy)
	 */
	public void cancelDriving()
	{
		if(!this.isFinished() && this.transitionVehicle != null && this.transitionText != null)
		{
			// nastaveni nove soucasne pozice vozidla (tam, kde skoncila animace)
			this.setCurrentPosition(new Coordinate(this.getVehicleView().getCircle().getCenterX() + this.getVehicleView().getCircle().getTranslateX(), this.getVehicleView().getCircle().getCenterY() + this.getVehicleView().getCircle().getTranslateY()));

			// zruseni animace (vrati objekt na zacatek, proto predtim ulozim pozici a objekt prekreslim na spravne misto
			this.transitionVehicle.stop();
			this.transitionVehicle = null;

			// nastaveni nove soucasne pozice popisku vozidla
			this.setCurrentPosition(new Coordinate(this.getVehicleView().getText().getX() + this.getVehicleView().getText().getTranslateX(), this.getVehicleView().getText().getY() + this.getVehicleView().getText().getTranslateY()));
			this.transitionText.stop();
			this.transitionText = null;
		}
	}

	/**
	 * Pomocna funkce pro restart simulace.
	 */
	public void resetIndex()
	{
		this.index = 0;
		this.editPreviousCoord = new Coordinate(0,0);
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
		if(this.getLine().isEdited())
		{
			if(this.nextStop != null)
			{
				return this.nextStop.getStop().getId().concat("\n" + this.nextStop.getTime() + "\n(+" + this.getDelay() + " min.)");
			}
			else
			{
				return this.editedPathStops.get(this.editedPathStops.size() - 1).getStop().getId().concat("\n" + editedPathStops.get(this.editedPathStops.size() - 1).getTime() + "\n(+" + this.getDelay() + " min.)");
			}
		}
		return this.previousStop.getStop().getId().concat("\n" + this.previousStop.getTime() + "\n(+" + this.getDelay() + " min.)");
	}

	/**
	 * Zmeni rychlost simulace, ze soucasne pozice vytvori animaci do dalsi zastavky, upravi delku a spusti znova animaci.
	 * @param time aktualni cas simulace
	 * @param timeSpeed aktualni rychlost simulace
	 */
	public void changeAnimationSpeed(String time, int timeSpeed)
	{
		if(!this.ended && !this.getLine().isEdited())
		{
			// zastavi animaci
			if(this.transitionVehicle != null)
			{
				this.transitionVehicle.stop();

				this.transitionText.stop();
			}

			String hoursTmp = time.substring(0,2);
			int inputTime = (Integer.parseInt(time.substring(3,5)) + (Integer.parseInt(hoursTmp) * 60));

			if(this.getLine().isEdited())
			{
				this.previousStop = this.nextStop;

				// parsovani casu dalsi zastavky - kvuli delce jizdy
				this.nextStopTime = (Integer.parseInt(this.nextStop.getTime().substring(3,5)) + (Integer.parseInt(this.nextStop.getTime().substring(0,2)) * 60) + this.delay);
				System.out.println(this.id+ ":");
				for(Coordinate test : this.vehiclePath)
				{
					System.out.println(test.getX());
					System.out.println(test.getY());
				}
			}
			else
			{
				this.nextStop = this.timetable.previousStop(); // nahraje zpet predchozi zastavku

				// parsovani casu dalsi zastavky - kvuli delce jizdy
				this.nextStopTime = Integer.parseInt(this.nextStop.getTime().substring(3,5)) + (Integer.parseInt(this.nextStop.getTime().substring(0,2)) * 60) - inputTime;
			}

			// nastaveni nove aktualni pozice s korekci
			this.setCurrentPosition(new Coordinate(this.getVehicleView().getCircle().getCenterX() + this.getVehicleView().getCircle().getTranslateX() - 10.0, this.getVehicleView().getCircle().getCenterY() + this.getVehicleView().getCircle().getTranslateY()));
			this.setCurrentPosition(new Coordinate(this.getVehicleView().getText().getX() + this.getVehicleView().getText().getTranslateX() - 10.0, this.getVehicleView().getText().getY() + this.getVehicleView().getText().getTranslateY()));

			ArrayList<Double> coords = new ArrayList<Double>();
			ArrayList<Double> coords2 = new ArrayList<Double>();

			boolean nextStopFound = false;

			double prevX = 0, prevY = 0;

			for (Street street: this.line.getStreets())
			{
				// pomocne promenne pro vzdalenost
				double start_and_vehicle = round(this.distance(street.getStart().getX(), street.getStart().getY(), this.getCurrentPosition().getX(), this.getCurrentPosition().getY()), 0);
				double vehicle_and_end = round(this.distance(street.getEnd().getX(), street.getEnd().getY(), this.getCurrentPosition().getX(), this.getCurrentPosition().getY()), 0);
				double vehicle_distance = start_and_vehicle + vehicle_and_end;
				double street_size = round(this.distance(street.getEnd().getX(), street.getEnd().getY(), street.getStart().getX(), street.getStart().getY()), 0);

				// urceni pozice na aktualni ulici
				if(vehicle_distance - street_size >= -10.0 && vehicle_distance - street_size <= 10.0)
				{
					try
					{
						// pripad, kdy je to zpatecni linka
						String str = this.getLine().getId();
						String result = str.substring(str.indexOf("(") + 1, str.indexOf(")")); // kdyz nazev obsahuje slovo "backwards", tak je to zpatecni linka

						if(result.equals("backwards"))
						{
							coords.add(this.currentPosition.getX());
							coords.add(this.currentPosition.getY());
							coords2.add(this.currentPosition.getX() + 35);
							coords2.add(this.currentPosition.getY());

							if (inputTime <= Integer.parseInt(this.previousStop.getTime().substring(3,5)) + (Integer.parseInt(this.previousStop.getTime().substring(0,2)) * 60) || inputTime <= Integer.parseInt(this.firstEntry.getTime().substring(3,5)) + (Integer.parseInt(this.firstEntry.getTime().substring(0,2)) * 60))
							{
								// pokud uz projel zastavkou na aktualni ulici, tak ji neulozim, jinak ano
								if(this.previousStop.getStop().getStreet().getId().equals(street.getId()))
								{
									coords.add(this.previousStop.getStop().getCoordinate().getX());
									coords.add(this.previousStop.getStop().getCoordinate().getY());
									coords2.add(this.previousStop.getStop().getCoordinate().getX() + 35);
									coords2.add(this.previousStop.getStop().getCoordinate().getY());
									nextStopFound = true;
								}
							}

							if(!nextStopFound)
							{
								coords.add(street.getStart().getX());
								coords.add(street.getStart().getY());
								coords2.add(street.getStart().getX() + 35);
								coords2.add(street.getStart().getY());
							}

							prevX = street.getStart().getX();
							prevY = street.getStart().getY();

							this.setCurrentStreet(street);
							break;
						}
					}
					catch(Exception e)
					{
						coords.add(this.currentPosition.getX());
						coords.add(this.currentPosition.getY());
						coords2.add(this.currentPosition.getX() + 35);
						coords2.add(this.currentPosition.getY());

						// pripad, kde je to klasicka linka smerem "dopredu"
						if (inputTime <= Integer.parseInt(this.previousStop.getTime().substring(3,5)) + (Integer.parseInt(this.previousStop.getTime().substring(0,2)) * 60) || inputTime <= Integer.parseInt(this.firstEntry.getTime().substring(3,5)) + (Integer.parseInt(this.firstEntry.getTime().substring(0,2)) * 60))
						{
							// pokud uz projel zastavkou na aktualni ulici, tak ji neulozim, jinak ano
							if(this.previousStop.getStop().getStreet().getId().equals(street.getId()))
							{
								coords.add(this.previousStop.getStop().getCoordinate().getX());
								coords.add(this.previousStop.getStop().getCoordinate().getY());
								coords2.add(this.previousStop.getStop().getCoordinate().getX() + 35);
								coords2.add(this.previousStop.getStop().getCoordinate().getY());
								nextStopFound = true;
							}
						}

						if(!nextStopFound)
						{
							coords.add(street.getEnd().getX());
							coords.add(street.getEnd().getY());
							coords2.add(street.getEnd().getX() + 35);
							coords2.add(street.getEnd().getY());
						}

						prevX = street.getEnd().getX();
						prevY = street.getEnd().getY();

						this.setCurrentStreet(street);
						break;
					}
				}
			}

			// nasklada se trasa k dalsi zastavce
			if(!nextStopFound)
			{
				double nextStopX = (double)this.previousStop.getStop().getCoordinate().getX();
				double nextStopY = (double)this.previousStop.getStop().getCoordinate().getY();

				int indexBackup = this.index;

				if(this.getLine().isEdited())
				{
					this.resetIndex();
				}
				else
				{
					for (; this.index > 0; this.index--)
					{
						if (this.vehiclePath.get(this.index).getX() == prevX && this.vehiclePath.get(this.index).getY() == prevY)
						{
							break;
						}
					}
				}

				if(this.getLine().isEdited())
				{
					for (; this.index > 0; this.index--)
					{
						if (this.vehiclePath.get(this.index).getX() == prevX && this.vehiclePath.get(this.index).getY() == prevY)
						{
							break;
						}
					}

					for (; this.index < this.vehiclePath.size(); ++this.index)
					{
						if(this.previousStop != null)
						{
							if (this.vehiclePath.get(this.index).getX() == this.previousStop.getStop().getCoordinate().getX() && this.vehiclePath.get(this.index).getY() == this.previousStop.getStop().getCoordinate().getY())
							{ // prvni animace po nasledujici zastavku
								coords.add(this.previousStop.getStop().getCoordinate().getX());
								coords.add(this.previousStop.getStop().getCoordinate().getY());
								coords2.add(this.previousStop.getStop().getCoordinate().getX() + 35);
								coords2.add(this.previousStop.getStop().getCoordinate().getY());

								this.nextStopTime = Integer.parseInt(this.previousStop.getTime().substring(3,5)) + (Integer.parseInt(this.previousStop.getTime().substring(0,2)) * 60);
								this.setCurrentPosition(this.previousStop.getStop().getCoordinate());
								break;
							}
						}

						coords.add(this.vehiclePath.get(this.index).getX());
						coords.add(this.vehiclePath.get(this.index).getY());
						coords2.add(this.vehiclePath.get(this.index).getX() + 35);
						coords2.add(this.vehiclePath.get(this.index).getY());
					}
				}
				else
				{
					for (; this.index < this.vehiclePath.size(); this.index++)
					{
						if (this.vehiclePath.get(this.index).getX() == nextStopX && this.vehiclePath.get(this.index).getY() == nextStopY)
						{
							break;
						}

						if(this.vehiclePath.get(this.index).getX() != prevX && this.vehiclePath.get(this.index).getY() != prevY)
						{
							coords.add(this.vehiclePath.get(this.index).getX());
							coords.add(this.vehiclePath.get(this.index).getY());
							coords2.add(this.vehiclePath.get(this.index).getX() + 35);
							coords2.add(this.vehiclePath.get(this.index).getY());
						}
					}

					this.index = indexBackup;

					coords.add(nextStopX);
					coords.add(nextStopY);
					coords2.add(nextStopX + 35);
					coords2.add(nextStopY);
				}
			}

			if(!this.getLine().isEdited())
			{
				this.setCurrentPosition(this.nextStop.getStop().getCoordinate());

				this.nextStop = this.timetable.nextStop();
			}
			else
			{
				this.setCurrentPosition(this.previousStop.getStop().getCoordinate());
			}


			Polyline pathVehicle = new Polyline();
			pathVehicle.getPoints().addAll(coords);

			Polyline pathName = new Polyline();
			pathName.getPoints().addAll(coords2);

			int randomTimer = 0;
			if(this.getLine().isEdited())
			{
				randomTimer = (nextStopTime - inputTime) + this.delay;
			}
			else
			{
				randomTimer = nextStopTime + this.delay;
			}

			PathTransition transition = new PathTransition();
			transition.setNode(this.vehicleView.getCircle());
			transition.setDuration(Duration.millis(randomTimer/timeSpeed*1000));
			transition.setPath(pathVehicle);
			transition.play();

			PathTransition transition2 = new PathTransition();
			transition2.setNode(this.vehicleView.getText());
			transition2.setDuration(Duration.millis(randomTimer/timeSpeed*1000));
			transition2.setPath(pathName);
			transition2.play();

			this.transitionVehicle = transition;
			this.transitionText = transition2;
		}
		else if(!this.ended)
		{
			// zastavi animaci
			if(this.transitionVehicle != null)
			{
				this.transitionVehicle.stop();

				this.transitionText.stop();
			}

			String hoursTmp = time.substring(0,2);
			int inputTime = (Integer.parseInt(time.substring(3,5)) + (Integer.parseInt(hoursTmp) * 60));

			// delka cele jizdy, (pom - inputTime) je zbyvajici delka jizdy
			int pom = Integer.parseInt(this.getTimetable().getEntries().get(this.getTimetable().getEntries().size() - 1).getTime().substring(3,5)) + (Integer.parseInt(this.getTimetable().getEntries().get(this.getTimetable().getEntries().size() - 1).getTime().substring(0,2)) * 60);

			// nastaveni nove aktualni pozice s korekci
			this.setCurrentPosition(new Coordinate(this.getVehicleView().getCircle().getCenterX() + this.getVehicleView().getCircle().getTranslateX() - 10.0, this.getVehicleView().getCircle().getCenterY() + this.getVehicleView().getCircle().getTranslateY()));
			this.setCurrentPosition(new Coordinate(this.getVehicleView().getText().getX() + this.getVehicleView().getText().getTranslateX() - 10.0, this.getVehicleView().getText().getY() + this.getVehicleView().getText().getTranslateY()));
			this.vehiclePath = new ArrayList<Coordinate>();

			this.resetIndex();

			TimetableEntry nextStopBackup = null;

			for (Street street: this.line.getStreets())
			{
				// pomocne promenne pro vzdalenost
				double start_and_vehicle = round(this.distance(street.getStart().getX(), street.getStart().getY(), this.getCurrentPosition().getX(), this.getCurrentPosition().getY()), 0);
				double vehicle_and_end = round(this.distance(street.getEnd().getX(), street.getEnd().getY(), this.getCurrentPosition().getX(), this.getCurrentPosition().getY()), 0);
				double vehicle_distance = start_and_vehicle + vehicle_and_end;
				double street_size = round(this.distance(street.getEnd().getX(), street.getEnd().getY(), street.getStart().getX(), street.getStart().getY()), 0);

				// urceni pozice na aktualni ulici (
				if(vehicle_distance - street_size >= -10.0 && vehicle_distance - street_size <= 10.0)
				{
					try
					{
						// pripad, kdy je to zpatecni linka
						String str = this.getLine().getId();
						String result = str.substring(str.indexOf("(") + 1, str.indexOf(")")); // kdyz nazev obsahuje slovo "backwards", tak je to zpatecni linka

						if(result.equals("backwards"))
						{
							this.editPreviousCoord = street.getStart();

							if (inputTime <= Integer.parseInt(this.previousStop.getTime().substring(3,5)) + (Integer.parseInt(this.previousStop.getTime().substring(0,2)) * 60) || inputTime <= Integer.parseInt(this.firstEntry.getTime().substring(3,5)) + (Integer.parseInt(this.firstEntry.getTime().substring(0,2)) * 60))
							{ // pokud uz projel zastavkou na aktualni ulici, tak ji neulozim, jinak ano
								if(this.previousStop.getStop().getStreet().getId().equals(street.getId()))
								{
									this.vehiclePath.add(this.previousStop.getStop().getCoordinate());
									nextStopBackup = this.previousStop;
									this.editedPathStops.add(nextStopBackup);
									this.editedPathStopsIndex = this.editedPathStops.size() - 1;
								}
							}
							this.vehiclePath.add(street.getStart());
							this.setCurrentStreet(street);
							continue;
						}
					}
					catch(Exception e)
					{
						// pripad, kde je to klasicka linka smerem "dopredu"

						this.editPreviousCoord = street.getEnd();

						if (inputTime <= Integer.parseInt(this.previousStop.getTime().substring(3,5)) + (Integer.parseInt(this.previousStop.getTime().substring(0,2)) * 60) || inputTime <= Integer.parseInt(this.firstEntry.getTime().substring(3,5)) + (Integer.parseInt(this.firstEntry.getTime().substring(0,2)) * 60))
						{ // pokud uz projel zastavkou na aktualni ulici, tak ji neulozim, jinak ano
							if(this.previousStop.getStop().getStreet().getId().equals(street.getId()))
							{
								this.vehiclePath.add(this.previousStop.getStop().getCoordinate());
								nextStopBackup = this.previousStop;
								this.editedPathStops.add(nextStopBackup);
								this.editedPathStopsIndex = this.editedPathStops.size() - 1;
							}
						}
						this.vehiclePath.add(street.getEnd());
						this.setCurrentStreet(street);
						continue;
					}
				}

				if(street.getEnd().getX() == this.editPreviousCoord.getX() && street.getEnd().getY() == this.editPreviousCoord.getY()) // na ulici prijizdi zezadu
				{
					this.vehiclePath.add(street.getEnd());

					for(TimetableEntry timetable : this.getTimetable().getEntries()) // kdyz je ulice v puvodnim jizdnim radu, tak ji ulozim do trasy
					{
						if(timetable.getStop().getStreet().getId().equals(street.getId()))
						{
							this.vehiclePath.add(timetable.getStop().getCoordinate());
							this.editedPathStops.add(timetable);

							if(nextStopBackup == null)
							{
								nextStopBackup = timetable;
								this.editedPathStopsIndex = this.editedPathStops.size() - 1;
							}
						}
					}

					this.vehiclePath.add(street.getStart());

					this.editPreviousCoord = street.getStart();
				}
				else if(street.getStart().getX() == this.editPreviousCoord.getX() && street.getStart().getY() == this.editPreviousCoord.getY()) // na ulici prijizdi zepredu
				{
					this.vehiclePath.add(street.getStart());

					for(TimetableEntry timetable : this.getTimetable().getEntries()) // kdyz je ulice v puvodnim jizdnim radu, tak ji ulozim do trasy
					{
						if(timetable.getStop().getStreet().getId().equals(street.getId()))
						{
							this.vehiclePath.add(timetable.getStop().getCoordinate());
							this.editedPathStops.add(timetable);

							if(nextStopBackup == null)
							{
								nextStopBackup = timetable;
								this.editedPathStopsIndex = this.editedPathStops.size() - 1;
							}
						}
					}

					this.vehiclePath.add(street.getEnd());

					this.editPreviousCoord = street.getEnd();
				}
			}

			// osetreni prazdne cesty - vozidlo je na uzavrene ceste, apod...
			if(this.vehiclePath.isEmpty())
			{
				this.ended = true;
				return;
			}

			ArrayList<Double> coords = new ArrayList<Double>();
			ArrayList<Double> coords2 = new ArrayList<Double>();

			coords.add(this.currentPosition.getX());
			coords.add(this.currentPosition.getY());
			coords2.add(this.currentPosition.getX() + 35);
			coords2.add(this.currentPosition.getY());

			for (; this.index < this.vehiclePath.size(); ++this.index)
			{
				if(nextStopBackup != null)
				{
					if (this.vehiclePath.get(this.index).getX() == nextStopBackup.getStop().getCoordinate().getX() && this.vehiclePath.get(this.index).getY() == nextStopBackup.getStop().getCoordinate().getY())
					{ // prvni animace po nasledujici zastavku
						coords.add(nextStopBackup.getStop().getCoordinate().getX());
						coords.add(nextStopBackup.getStop().getCoordinate().getY());
						coords2.add(nextStopBackup.getStop().getCoordinate().getX() + 35);
						coords2.add(nextStopBackup.getStop().getCoordinate().getY());

						pom = Integer.parseInt(nextStopBackup.getTime().substring(3,5)) + (Integer.parseInt(nextStopBackup.getTime().substring(0,2)) * 60);
						this.setCurrentPosition(nextStopBackup.getStop().getCoordinate());
						break;
					}
				}

				coords.add(this.vehiclePath.get(this.index).getX());
				coords.add(this.vehiclePath.get(this.index).getY());
				coords2.add(this.vehiclePath.get(this.index).getX() + 35);
				coords2.add(this.vehiclePath.get(this.index).getY());
			}

			Polyline pathVehicle = new Polyline();
			pathVehicle.getPoints().addAll(coords);

			Polyline pathName = new Polyline();
			pathName.getPoints().addAll(coords2);

			int randomTimer = (pom - inputTime) + this.delay;

			PathTransition transition = new PathTransition();
			transition.setNode(this.vehicleView.getCircle());
			transition.setDuration(Duration.millis(randomTimer/timeSpeed*1000));
			transition.setPath(pathVehicle);
			transition.play();

			PathTransition transition2 = new PathTransition();
			transition2.setNode(this.vehicleView.getText());
			transition2.setDuration(Duration.millis(randomTimer/timeSpeed*1000));
			transition2.setPath(pathName);
			transition2.play();

			this.transitionVehicle = transition;
			this.transitionText = transition2;
			this.nextStop = nextStopBackup;
		}
	}
}
