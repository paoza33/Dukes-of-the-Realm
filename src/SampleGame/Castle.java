package SampleGame;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * The castle class represents a castle of the kingdom
 * A castle is characterized by the following information :
 * 		Its x and y coordinates
 * 		One Duke
 * 		His treasure
 * 		His level
 * 		The orientation of its exit door
 * 		His troops
 * 
 * @author Kadri Mehdi
 * @version 8.0
 */

public class Castle extends SpriteCastle{
	/**
	 * The duke of the castle
	 */
	private String duke;
	
	/**
	 * the coordinates of the castle
	 */
	private double x, y;
	
	/**
	 * the treasure of the castle
	 * @see Castle#getTreasure()
	 * @see Castle#setTreasure(int)
	 */
	private int treasure;
	
	/**
	 * the level of the castle
	 * @see Castle#getLevel()
	 * @see Castle#setLevel(int)
	 */
	private int level;
	
	/**
	 * The orientation of its exit door (N, S, E, W)
	 * @see Castle#getDoor()
	 * @see Castle#setDoor(char)
	 */
	private char door;
	
	/**
	 * castle Onager's reserve
	 * 		index 0 represents the number of Onager in the castle
	 * 		index 1 represents the damage meter
	 * @see Castle#getTroopsReserveOnager()
	 * @see Castle#setTroopsReserveOnager(int[])
	 */
	private int[] troopsReserveOnager; //indice 0 -> nombres soldat indice 1 -> degats (vie d'une troupe)
	
	/**
	 * castle Knight's reserve
	 * 		index 0 represents the number of Knight in the castle
	 * 		index 1 represents the damage meter
	 * @see Castle#getTroopsReserveKnight()
	 * @see Castle#setTroopsReserveKnight(int[])
	 */
	private int[] troopsReserveKnight;
	
	/**
	 * castle Lancer's reserve
	 * 		index 0 represents the number of Lancer in the castle
	 * 		index 1 represents the damage meter
	 * @see Castle#getTroopsReserveLancer()
	 * @see Castle#setTroopsReserveLancer(int[])
	 */
	private int[] troopsReserveLancer;
	
	/**
	 * list of troops being produced
	 * @see Castle#getTroopsProduction()
	 * @see Castle#setTroopsProduction(int[])
	 */
	private int[] troopsProduction; // liste de troops en cours de productions
	
	
	public Castle(Pane layer, Image image, double x, double y, String duke,int treasure, int level, char door, int[] troopsProduction, int[] troopsReserveOnager, int[] troopsReserveKnight, int[] troopsReserveLancer) {
		super(layer,image,x,y);
		this.x = x;
		this.y = y;
		this.duke = duke;
		this.treasure = treasure;
		this.level = level;
		this.door = door;
		this.troopsProduction = troopsProduction;
		this.troopsReserveOnager = troopsReserveOnager;
		this.troopsReserveKnight = troopsReserveKnight;
		this.troopsReserveLancer = troopsReserveLancer;
	}
	
	/**
	 * The castle suffers the damage of the troops
	 * The number of troops (of the targeted type) is reduced by 1 once the damage counter drops to 0 or less.
	 * the target type is random
	 * @param troop
	 * 			The troop attacking the castle
	 */
	public void damagedBy( Troop troop) {
		// we manage the random targeting event of the type 
		ArrayList<String> string = new ArrayList<String>(3);
		int count =0;
		if(troopsReserveOnager.length > 0) {
			string.add("Onager");
			count++;
		}
		if(troopsReserveKnight.length > 0) {
			string.add("Knight");
			count++;
		}
		if(troopsReserveLancer.length > 0) {
			string.add("Lancer");
			count ++;
		}
		Random rand = new Random();
		int i = rand.nextInt(count);
		
		// The castle suffers damage
		if(string.get(i) == "Onager") {
			troopsReserveOnager[1] -= troop.getDamage();
			if(troopsReserveOnager[1] < 0) {
				troopsReserveOnager[0] -= 1;
				troopsReserveOnager[1] = Settings.ONAGER_HEALTH;
			}
		}
		else if(string.get(i) == "Knight") {
			troopsReserveKnight[1] -= troop.getDamage();
			if(troopsReserveKnight[1] < 0) {
				troopsReserveKnight[0] -= 1;
				troopsReserveKnight[1] = Settings.KNIGHT_HEALTH;
			}
		}
		else if(string.get(i) == "Lancer") {
			troopsReserveLancer[1] -= troop.getDamage();
			if(troopsReserveLancer[1] < 0) {
				troopsReserveLancer[0] -= 1;
				troopsReserveLancer[1] = Settings.LANCER_HEALTH;
			}
		}
	}
	
	public char getDoor() {
		return door;
	}
	
	public void setDoor(char door) {
		this.door = door;
	}
	
	public int[] getTroopsReserveOnager() {
		return troopsReserveOnager;
	}
	
	public double getX() {
		return x;
	}
	
	public int[] getTroopsReserveKnight() {
		return troopsReserveKnight;
	}
	
	public void setTroopsReserveKnight(int[] troopsReserveKnight) {
		this.troopsReserveKnight = troopsReserveKnight;
	}
	
	public int[] getTroopsReserveLancer() {
		return troopsReserveLancer;
	}
	
	public void setTroopsReserveLancer(int[] troopsReserveLancer) {
		this.troopsReserveLancer = troopsReserveLancer;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public int[] getTroopsProduction() {
		return troopsProduction;
	}
	
	public void setTroopsProduction(int[] troopsProduction) {
		this.troopsProduction = troopsProduction;
	}
	
	public void setTroopsReserveOnager(int[] troopsReserve) {
		this.troopsReserveOnager = troopsReserve;
	}
	
	public int getTreasure() {
		return treasure;
	}
	
	public void setTreasure(int treasure) {
		this.treasure = treasure;
	}
	
	public String getDuke() {
		return duke;
	}
	
	public void setDuke(String duke) {
		this.duke = duke;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * Treasure increases by 10* this level per turn.
	 */
	public void treasureByLevel() {
		this.treasure = this.treasure + this.level*10;
	}
	
	/**
	 * Returns true if the castle is defeated, false if not.
	 * If there are no more troops in the castle, the castle is defeated
	 * @return
	 */
	public boolean isAlive() {
		if(troopsReserveOnager[0] <1 && troopsReserveKnight[0] < 1 && troopsReserveLancer[0] < 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * The image of the enemy castle becomes that of the allied castle.
	 * @param image
	 * 			The new image
	 */
	public void becomeAllies(Image image) {
		ImageView imageView;
		imageView = new ImageView(image);
        imageView.relocate(x, y);
		setImageView(imageView);
	}
	/**
	 * The image of the allied castle becomes that of the enemy castle.
	 * @param image
	 * 			The new image
	 */
	public void becomeEnnemies(Image image) {
		ImageView imageView;
		imageView = new ImageView(image);
        imageView.relocate(x, y);
		setImageView(imageView);
	}

}