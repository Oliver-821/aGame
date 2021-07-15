package cardAbilities;

import akka.actor.ActorRef;
import commands.AdvancedCommands;
import commands.BasicCommands;
import structures.Board;
import structures.GameState;
import structures.HumanPlayer;
import structures.BetterUnit;
import structures.basic.Card;
import structures.basic.EffectAnimation;
import structures.basic.Tile;
import unitAbilities.Ability;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class AzureHerald implements cardAbility {
	@Override
	public void execute(int handPosition, GameState gameState, ActorRef out) {
		// TODO Auto-generated method stub
		Board board = gameState.getBoard();
		Tile[][] tile = board.getTile();
		AdvancedCommands.clearHighlight(gameState, out);
		HumanPlayer human = gameState.getHuman();
		BetterUnit[] avatar = board.getUnit1();
		BetterUnit avatar1 = avatar[16];
		int[]pos = new int[2];
		pos = board.getPos(16);
		Tile tile111 = board.tile[pos[0]][pos[1]];
		for(int i = 0; i < 9 ; i++) {
			for(int j = 0; j < 5; j++) {
				if(tile[i][j].getUnit() != null) {
					if(tile[i][j].getUnit().getId() == 0 || tile[i][j].getUnit().getId() == 8) {
						if(avatar1.getHealth() <=17 ) {
							int health = avatar1.getHealth() +3;
							avatar1.setHealth(health);
							gameState.getHuman().setHealth(health);
							BasicCommands.addPlayer1Notification(out, "health +3", 2);
							AdvancedCommands.clearHighlight(gameState, out);
							EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff);
							BasicCommands.playEffectAnimation(out, ef, tile111);
							try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
							BasicCommands.setUnitHealth(out, avatar1, health);//which one
							try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
							BasicCommands.setPlayer1Health(out, gameState.getHuman());//which one
							try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
						}else if(avatar1.getHealth() <=20  && avatar1.getHealth() >17) {
							avatar1.setHealth(20);
							gameState.getHuman().setHealth(20);
							BasicCommands.addPlayer1Notification(out, "health +3", 2);
							AdvancedCommands.clearHighlight(gameState, out);
							EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff);
							BasicCommands.playEffectAnimation(out, ef, tile111);
							try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
							BasicCommands.setUnitHealth(out, avatar1, 20);
							try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
							BasicCommands.setPlayer1Health(out, gameState.getHuman());//which one
							try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
						}
					}
				}
			}
		}
						int mana = human.getMana() - 2;
						BasicCommands.setPlayer1Mana(out,human);			

		//test**************************************
		

		
		
}}

