package SampleGame;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Castle extends Sprite{
	private String duke;
	private int treasure, level, door, health;
	private int[] troopsReserve; //allant de 0 à 2 (soit le nombre de type de soldat)
	//private Troop[][] troopsProduction; // [i][j] ==> i correspond aux types de soldat (0 à 2),
										// j correspond à la j-ième troupe d'un même type 				
	
	public Castle(Pane layer,Image image, double x, double y, int health, String duke,int treasure, int level, int door, Troop[][] troopsProduction, int[] troopsReserve) {
		super(layer,image,x,y,health);
		this.duke = duke;
		this.treasure = treasure;
		this.level = level;
		this.door = door;
		//this.troopsProduction = troopsProduction;
		this.troopsReserve = troopsReserve;
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
	public int getDoor() {
		return door;
	}
	public void setDoor(int door) {
		this.door = door;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public void treasureByLevel(int treasure, int level) {
		treasure = treasure + level*10; // le trésor gagne (niveau * 10 florins) par tour
	}
	
	/*public void EndProd(Troop[][] troopsProduction) { //teste toutes les troupes en cours de production
		for(int i = 0; i <= 2;i++) {
			for (int j = 0; j <= troopsProduction[i].length; j++) {
				if (troopsProduction[i][j].EndProd()) {
					troopsReserve[i] += troopsProduction[i][j].getNumberOfSoldier();
					/*lorsque la production de la troupe est fini,
					 *  on increment le nombre d'unité par le nombre de soldat de la troupe.
					 
				
				}
			}
			
		}
		
	}*/
	@Override
	public void checkRemovability() {
		// TODO Auto-generated method stub
		
	}
	
}