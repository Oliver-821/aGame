package cardAbilities;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.BetterUnit;
import structures.Board;
import structures.GameState;
import structures.HumanPlayer;
import structures.basic.Tile;

public class IroncliffGuardian implements cardAbility{
	public void execute(int handPosition, GameState gameState, ActorRef out) {
		Board board;
		HumanPlayer human;
		BetterUnit betterUnit = gameState.getUnit();
		board = gameState.getBoard();
		human = gameState.getHuman();
		int humanMana;
		int mana =5;
		betterUnit.setAttack(3);
		betterUnit.setHealth(10);
		betterUnit.setManaCost(mana);
		betterUnit.setName("Ironcliff Guardian");
		Tile[][] tile = board.getTile();
		int[]pos = new int[2];
		pos = board.getPos(16);
		humanMana = human.getMana();
		if(humanMana<3) {
			BasicCommands.addPlayer1Notification(out, "mana not enough", 2);
		}else {
			BasicCommands.addPlayer1Notification(out, "human summon Ironcliff Guardian", 2);

			System.out.println("human mana before: "+human.getMana());
			BasicCommands.deleteUnit(out, tile[pos[0]][pos[1]].getUnit());
			tile[pos[0]][pos[1]].setUnit(null);
			
			human.setMana(humanMana-mana);
			System.out.println("human mana after: "+human.getMana());
			BasicCommands.setPlayer1Mana(out,human);
		}
		
	}
}
