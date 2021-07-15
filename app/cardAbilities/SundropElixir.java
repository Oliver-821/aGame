package cardAbilities;

import akka.actor.ActorRef;
import commands.AdvancedCommands;
import commands.BasicCommands;
import structures.BetterUnit;
import structures.Board;
import structures.GameState;
import structures.HumanPlayer;
import structures.basic.EffectAnimation;
import structures.basic.Tile;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * SundropElixir
 * add 5 health to one unit including avatar
 * the health shouldn't be more than the beginning health
 */
public class SundropElixir implements cardAbility {
		
		@Override
		public void execute(int handPosition, GameState gameState, ActorRef out) {
			HumanPlayer human = gameState.getHuman();;
			// determine whether Mana is enough
			int mana;
			mana = human.getMana();
			if(mana<1) {
				BasicCommands.addPlayer1Notification(out, "no enough mana", 2);
				return;	
			}else {
				highlight(handPosition, gameState, out);
			}
			
		}
		
		public void highlight(int handPosition, GameState gameState, ActorRef out ) {
			Board board = gameState.getBoard();
			Tile[][] tiles = board.getTile();
			for(int i = 0; i < 9 ; i++) {
				for(int j = 0; j < 5; j++) {
					if(tiles[i][j].getUnit() != null && tiles[i][j].getUnit().getId()< 30) {
						BasicCommands.drawTile(out, tiles[i][j], 1);
						tiles[i][j].setTileStatus(1);
						try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
					}
					
				}
			}
			BasicCommands.addPlayer1Notification(out, "add 5 health to unit", 2);
			gameState.setGameAction("clickCard");
			gameState.setHandPosition(handPosition);
		}
		
		
		/**
		 * use Sundrop Elixir 
		 */
		public static void useSundropElixir(int x, int y, GameState gameState, ActorRef out) {
			BetterUnit avatar = gameState.getBoard().unit1[16];
			int health = avatar.getHealth();
			Board board = gameState.getBoard();
			HumanPlayer human = gameState.getHuman();
			Tile[][] tiles = board.getTile();
			int handPosition = gameState.getHandPosition();
			int mana = human.getMana();
			if(tiles[x][y].getTileStatus() == 1) {
				BasicCommands.addPlayer1Notification(out, "Sundrop Elixir", 2);
				mana -= 1;
				human.setMana(mana);
				BasicCommands.setPlayer1Mana(out,human);
				BetterUnit unit = tiles[x][y].getUnit();		
				AdvancedCommands.clearHighlight(gameState, out);
				EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff);
				BasicCommands.playEffectAnimation(out, ef, tiles[x][y]);
				try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
				if(unit.getId() == 16) {
					health +=5;
					if(health <20) {
						BasicCommands.addPlayer1Notification(out, "update health", 2);
						human.setHealth(health);
						BasicCommands.setPlayer1Health(out, human);
						try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
						avatar.setHealth(health);
						BasicCommands.setUnitHealth(out, avatar, health);
					}else {
						BasicCommands.addPlayer1Notification(out, "update health", 2);
						human.setHealth(20);
						BasicCommands.setPlayer1Health(out, human);
						avatar.setHealth(20);
						try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
						BasicCommands.setUnitHealth(out, avatar, 20);
					}
				}else {
					int health2 = unit.getHealth() + 5;
					if(health2 <= unit.getPreHealth() ) {
						unit.setHealth(health2);
						BasicCommands.setUnitHealth(out, unit, health2);
						try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}	
					}else {
						unit.setHealth(unit.getPreHealth());
						BasicCommands.setUnitHealth(out, unit, unit.getHealth());
						try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
					}
				}
				// delete card
				human.getOnhandCard().remove(gameState.getHandPosition());
				int onhandCardNum = human.getOnhandCardNum()- 1;
				human.setOnhandCardNum(onhandCardNum);
				BasicCommands.deleteCard(out, handPosition);
				try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
				gameState.setHandPosition(-1);
				gameState.setGameAction("pending");
			} else {
				BasicCommands.addPlayer1Notification(out, "you couldn't put here", 2);
				AdvancedCommands.clearHighlight(gameState, out);
				gameState.setHandPosition(-1);
			}


		}

}