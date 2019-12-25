package SampleGame;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Castle extends SpriteCastle{
	private String duke;
	Image image;
	double x, y;
	private int treasure, level, health;
	private char door;
	private int[] troopsReserveOnager;	//indice 0 -> nombres soldat  indice 1 -> degats
	private int[] troopsReserveKnight;
	private int[] troopsReserveLancer;
	private int[] troopsProduction;	// liste de troops en cours de productions
	private ArrayList<Troop> ost;	// ost de 3 membres max
	private ArrayList<Troop> osts;  // liste de tout les osts
	
	
	public Castle(Pane layer, Image image, double x, double y, String duke,int treasure, int level, char door, int[] troopsProduction, int[] troopsReserveOnager, int[] troopsReserveKnight, int[] troopsReserveLancer) {
		super(layer,image,x,y);
		this.duke = duke;
		this.treasure = treasure;
		this.level = level;
		this.door = door;
		this.troopsProduction = troopsProduction;
		this.troopsReserveOnager = troopsReserveOnager;
		this.troopsReserveKnight = troopsReserveKnight;
		this.troopsReserveLancer = troopsReserveLancer;
	}
	
	private void buildOsts(ArrayList<Troop> troopsAttack) {  // construit liste osts
		for(Troop troop : troopsAttack) {
			troop.setX(this.x);
			troop.setY(this.y);
			if(troop instanceof Onager) {
				troop.setSpeed(Settings.ONAGER_SPEED);
				troop.setTimeProd(Settings.ONAGER_TIME);
				troop.setHealth(Settings.ONAGER_HEALTH);
				troop.setCostProd(Settings.ONAGER_COST);
				troop.setDamage(Settings.ONAGER_DAMAGE);
			}
			else if(troop instanceof Knight) {
				troop.setSpeed(Settings.KNIGHT_SPEED);
				troop.setTimeProd(Settings.KNIGHT_TIME);
				troop.setHealth(Settings.KNIGHT_HEALTH);
				troop.setCostProd(Settings.KNIGHT_COST);
				troop.setDamage(Settings.KNIGHT_DAMAGE);
			}
			else {
				troop.setSpeed(Settings.LANCER_SPEED);
				troop.setTimeProd(Settings.LANCER_TIME);
				troop.setHealth(Settings.LANCER_HEALTH);
				troop.setCostProd(Settings.LANCER_COST);
				troop.setDamage(Settings.LANCER_DAMAGE);
			}
			osts.add(troop);
		}
	}
	
	public void buildOst(ArrayList<Troop> troopsAttack) {	// limite l'ost à 3 membres
		buildOsts(troopsAttack);
		if(!(osts.size() < 4)) {
			ArrayList<Troop> subList = new ArrayList<>();
			for(int i = 0; i< osts.size(); i+= 3) { 
				subList = (ArrayList<Troop>) osts.subList(i, i+3);
				ost.addAll(subList);
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
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public void treasureByLevel() {
		this.treasure = this.treasure + this.level*10; // le trésor gagne (niveau * 10 florins) par tour
	}
	
	public boolean isAlive() {
		if(troopsReserveOnager[0] <1 && troopsReserveKnight[0] < 1 && troopsReserveLancer[0] < 1) {
			return false;
		}
		return true;
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