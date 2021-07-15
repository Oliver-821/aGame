package structures;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import akka.actor.ActorRef;
import structures.basic.Unit;
import unitAbilities.Ability;

/**
 * Better unit extends Unit to add more parameters
 */
@JsonIgnoreProperties(value = {"ability" } )
public class BetterUnit extends Unit  {
	
	/**
	 * @id : id of unit 
	 * @attack : attack of unit 
	 * @health : health of unit 
	 * @manaCost : mana cost to summon this unit
	 * @attackNum : the times of unit attack
	 * @moveNum : the times of unit move 
	 * @ability : Ability of unit 
	 * @turnNum : the turn num of unit since unit summoned on the board( first summon turn num = 0)
	 * @preHealth : the beginning health of unit 
	 */
	private int id;
	private int attack;
	private int health;
	private int manaCost;
	int attackNum = 0;
	private int moveNum = 0;
	private Ability ability;
	private int turnNum = 0;
	private int preHealth;
	
	public BetterUnit(){
		super();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}
	
	public int getAttackNum() {
		return attackNum;
	}

	public void setAttackNum(int attackNum) {
		this.attackNum = attackNum;
	}

	public int getMoveNum() {
		return moveNum;
	}

	public void setMoveNum(int moveNum) {
		this.moveNum = moveNum;
	}

	public int getManaCost() {
		return manaCost;
	}

	public void setManaCost(int manaCost) {
		this.manaCost = manaCost;
	}
	
	public Ability getAbility() {
		return ability;
	}

	public void setAbility(Ability ability) {
		this.ability = ability;
	}
	
	public void executeAbility(int x, int y, GameState gameState, ActorRef out) {
		this.ability.execute(x, y, gameState, out);
	}


	public int getTurnNum() {
		return turnNum;
	}

	public void setTurnNum(int turnNum) {
		this.turnNum = turnNum;
	}
	
	
	public int getPreHealth() {
		return preHealth;
	}

	public void setPreHealth(int preHealth) {
		this.preHealth = preHealth;
	}

	// clear attack times and move times once have new Turn 
	public void clearAllNum() {
		setAttackNum(0);
		setMoveNum(0);
	}
	
	


}
