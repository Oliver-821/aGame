package cardAbilities;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.BetterUnit;
import structures.Board;
import structures.GameState;
import structures.HumanPlayer;
import structures.basic.Tile;
// If your avatar is dealt damage this unit gains +2 attack 
public class SilverguardKnight implements cardAbility{
	public void execute(int handPosition, GameState gameState, ActorRef out) {
		Board board;
		HumanPlayer human;
		BetterUnit betterUnit = gameState.getUnit();
		board = gameState.getBoard();
		human = gameState.getHuman();
		int humanMana;
		int mana =1;
		betterUnit.setAttack(3);
		betterUnit.setHealth(5);
		betterUnit.setManaCost(mana);
		//Randomly set, this needs to be changed
		betterUnit.setId(10);
		betterUnit.setName("Silverguard Knight");
		//??why use tile[][]
		Tile[][] tile = board.getTile();
		int[]pos = new int[2];
		pos = board.getPos(16);
		humanMana = human.getMana();
		if(humanMana<3) {
			BasicCommands.addPlayer1Notification(out, "mana not enough", 2);
		}else {
			BasicCommands.addPlayer1Notification(out, "human summon Silverguard Knight", 2);

			System.out.println("human mana before: "+human.getMana());
			BasicCommands.deleteUnit(out, tile[pos[0]][pos[1]].getUnit());
			tile[pos[0]][pos[1]].setUnit(null);
			//Why not modify mana in the game state
			human.setMana(humanMana-mana);
			System.out.println("human mana after: "+human.getMana());
			BasicCommands.setPlayer1Mana(out,human);
		}
		
		
		
	}
	
}
