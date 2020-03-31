package map;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import drawable.StreetView;

/**
 * 
 * Trida reprezentujici ulici.
 * Ulice ma sve ID, ma promennou ukazujici, zda je otevrena, ci ne, obsahuje seznam zastavek a souradnice zacatku a konce, dale pak take uroven vytizeni.
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
 *
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Street
{

	private String id;
	private boolean open = true; // otevrena / uzavrena ulice
	private List<Stop> stops;
	private Coordinate start, end;
	private int busyness; // vytizeni
	
	@JsonIgnore
	private StreetView streetView;
	
	public Street(String name, Coordinate start, Coordinate end, List<Stop> stops)
	{
		this.id = name;
		this.stops = stops;
		this.start = start;
		this.end = end;
		this.busyness = 0;
		
		this.streetView = new StreetView();
	}
	
	public Street() {}
	
  /**
   * Vrati identifikator ulice.
   * @return String Identifikator ulice
   */
	public String getId()
	{
		return this.id;
	}
	
	/**
	 * Vrati pocatecni souradnice ulice.
	 * @return Coordinate souradnice ulice
	 */
	public Coordinate getStart()
	{	
		return this.start;
	}
	
	/**
	 * Vrati konecne souradnice ulice.
	 * @return Coordinate souradnice ulice
	 */
	public Coordinate getEnd()
	{
		return this.end;
	}

  /**
   * Vrati seznam zastavek na ulici.
   * @return Seznam zastavek na ulici. Pokud ulize nema zadnou zastavku, je seznam prazdny.
   */
	public List<Stop> getStops()
	{
		return this.stops;
	}
	
	/**
	 * Vrati status ulice.
	 * @return boolean status ulice (open/closed)
	 */
	@JsonIgnore
	public boolean getStatus()
	{
		return this.open;
	}
	
	/**
	 * Vrati promennou reprezentujici "zahlcenost" ulice
	 * @return int stupen zahlceni ulice
	 */
	@JsonIgnore
	public int getBusyness()
	{
		return this.busyness;
	}
	
	/**
	 * Nastavi zastavkam ulici, na ktere lezi.
	 */
	public void setStreetParameterToStop()
	{
		if (this.stops != null)
		{
			for (Stop stop : this.stops) 
			{
				stop.setStreet(this);
			}
		}
	}
	
	/**
	 * Zjisti, status ulice.
	 * @return status ulice
	 */
	public boolean isOpen()
	{
		return this.open;
	}

  /**
   * Prida do seznamu zastavek novou zastavku.
   * @param stop Nove pridavana zastavka.
   */
	public void addStop(Stop stop)
	{
		this.stops.add(stop);
		
		stop.setStreet(this);
	}
	
	/**
	 * Nastavi promennou obsahujici grafickou reprezentaci ulice.
	 * @param view Objekt obsahujici grafickou reprezentaci ulice.
	 */
	@JsonIgnore
	public void setStreetView(StreetView view)
	{
		this.streetView = view;
	}
	
	/**
	 * Vrati promennou obsahujici grafickou reprezentaci ulice.
	 * @return StreetView Objekt obsahujici grafickou reprezentaci ulice.
	 */
	@JsonIgnore
	public StreetView getStreetView()
	{
		return this.streetView;
	}
	
	/**
	 * Nastavi status ulice
	 * @param status status
	 */
	@JsonIgnore
	public void setOpen(boolean status)
	{
		this.open = status;
	}
}
