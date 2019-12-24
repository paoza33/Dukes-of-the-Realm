package SampleGame;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Castle extends SpriteCastle{
	private String duke;
	Image image;
	double x, y;
	private int treasure, level, health;
	private char door;
	private int[] troopsReserve; //allant de 0 à 2 (soit le nombre de type de soldat)
								 //0->Lancer ; 1->Knight ; 2->Onager 
	private int[] troopsProduction; // contiendra le nombre de troupes a produire
									// indice 0->Lancer ; 1->Knight ; 2->Onager
	
	public Castle(Pane layer, Image image, double x, double y, int health, String duke,int treasure, int level, char door, int[] troopsProduction, int[] troopsReserve) {
		super(layer,image,x,y,health);
		this.duke = duke;
		this.treasure = treasure;
		this.level = level;
		this.door = door;
		this.troopsProduction = troopsProduction;
		this.troopsReserve = troopsReserve.clone();
	}
	
	public char getDoor() {
		return door;
	}

	public void setDoor(char door) {
		this.door = door;
	}

	public int[] getTroopsReserve() {
		return troopsReserve;
	}

	public double getX() {
		return x;
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

	public void setTroopsReserve(int[] troopsReserve) {
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
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public void treasureByLevel() {
		this.treasure = this.treasure + this.level*10; // le trésor gagne (niveau * 10 florins) par tour
	}
	
	public boolean EndProd(int[] troopsProduction, int time) { //teste toutes les troupes en cours de production
		if(troopsProduction[2] > 0 && (Settings.ONAGER_TIME - time) <=0) {	//les troupes les plus lentes representent le temps de depart de l'ost
			return true;
		}
		else if(troopsProduction[1] > 0 && !(troopsProduction[2] > 0) &&(Settings.KNIGHT_TIME - time) <=0) {
			return true;
		}
		else if(troopsProduction[0] > 0 && !(troopsProduction[2] > 0)&& !(troopsProduction[1] > 0) &&(Settings.LANCER_TIME - time) <=0) {
			return true;
		}
		return false;
	}
	
}