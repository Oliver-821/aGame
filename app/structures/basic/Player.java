package structures.basic;

import java.util.ArrayList;

/**
 * A basic representation of of the Player. A player
 * has health and mana.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Player {


	int health;
	int mana;
	public ArrayList<Card> onhandCard;
	private int onhandCardNum = 0;
	
	public Player() {
		super();
		this.health = 20;
		this.mana = 0;
	}
	public Player(int health, int mana) {
		super();
		this.health = health;
		this.mana = mana;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getMana() {
		return mana;
	}
	public void setMana(int mana) {
		this.mana = mana;
	}
	public ArrayList<Card> getOnhandCard() {
		return onhandCard;
	}
	public void setOnhandCard(ArrayList<Card> onhandCard) {
		this.onhandCard = onhandCard;
	}
	public int getOnhandCardNum() {
		return onhandCardNum;
	}
	public void setOnhandCardNum(int onhandCardNum) {
		this.onhandCardNum = onhandCardNum;
	}

	
	
	
}
