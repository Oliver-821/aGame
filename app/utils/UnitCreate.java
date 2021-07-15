package utils;

import structures.BetterUnit;
import unitAbilities.AzureHerald;
import unitAbilities.AzuriteLion;
import unitAbilities.FireSpitter;
import unitAbilities.Flying;
import unitAbilities.Pyromancer;

/**
 * use factory to create two array. one for human another is for AI
 * unit create factory
 * add unit ability by using delegate pattern
 */
public class UnitCreate {
	/**
	 * @unit1  array to contain human unit 
	 * @unit2  array to contain AI unit
	 */
	private static BetterUnit[] unit1;
	private static BetterUnit[] unit2;
	// new unit for human 
	private static String[] deck1Units = {
			StaticConfFiles.u_azure_herald,
			StaticConfFiles.u_azurite_lion,
			StaticConfFiles.u_comodo_charger,
			StaticConfFiles.u_fire_spitter,
			StaticConfFiles.u_hailstone_golem,
			StaticConfFiles.u_ironcliff_guardian,
			StaticConfFiles.u_pureblade_enforcer,
			StaticConfFiles.u_silverguard_knight,	
			
			StaticConfFiles.u_azure_herald,
			StaticConfFiles.u_azurite_lion,
			StaticConfFiles.u_comodo_charger,
			StaticConfFiles.u_fire_spitter,
			StaticConfFiles.u_hailstone_golem,
			StaticConfFiles.u_ironcliff_guardian,
			StaticConfFiles.u_pureblade_enforcer,
			StaticConfFiles.u_silverguard_knight,
			StaticConfFiles.humanAvatar,
	};
	// new unit for AI 
	private static String[] deck2Units = {
			StaticConfFiles.u_blaze_hound,
			StaticConfFiles.u_bloodshard_golem,
			StaticConfFiles.u_hailstone_golemR,
			StaticConfFiles.u_planar_scout,
			StaticConfFiles.u_pyromancer,
			StaticConfFiles.u_rock_pulveriser,
			StaticConfFiles.u_serpenti,
			StaticConfFiles.u_windshrike,
			StaticConfFiles.u_blaze_hound,
			StaticConfFiles.u_bloodshard_golem,
			StaticConfFiles.u_hailstone_golemR,
			StaticConfFiles.u_planar_scout,
			StaticConfFiles.u_pyromancer,
			StaticConfFiles.u_rock_pulveriser,
			StaticConfFiles.u_serpenti,
			StaticConfFiles.u_windshrike,
			StaticConfFiles.aiAvatar,			
	};
	
	/**
	 * create unit of array for human and AI
	 * 
	 * @param name : human or AI
	 * @return : array contains all units of human or AI 
	 */
	public static BetterUnit[] unitCreate(String name) {
		BetterUnit[] temp = null;
		unit1 = new BetterUnit[deck1Units.length];
		for (int i = 0; i<deck1Units.length;i++) {
			String deck1CardFile = deck1Units[i];
			unit1[i] =  (BetterUnit) BasicObjectBuilders.loadUnit(deck1CardFile, i, BetterUnit.class);
			unit1[i].setId(i);
			if(i==0 || i ==8) {
				unit1[i].setManaCost(2);
				unit1[i].setAttack(1);
				unit1[i].setHealth(4);
				unit1[i].setPreHealth(4);
				unit1[i].setAbility( new AzureHerald());
			}else if(i==1|| i == 9 ) {
				unit1[i].setManaCost(3);
				unit1[i].setAttack(2);
				unit1[i].setHealth(3);
				unit1[i].setPreHealth(3);
				unit1[i].setAbility( new AzuriteLion());
			}else if(i==2|| i == 10 ) {
				unit1[i].setManaCost(1);
				unit1[i].setAttack(1);
				unit1[i].setHealth(3);
				unit1[i].setPreHealth(3);
			} else if(i==3|| i == 11 ) {
				unit1[i].setManaCost(4);
				unit1[i].setAttack(3);
				unit1[i].setHealth(2);
				unit1[i].setPreHealth(2);
				unit1[i].setAbility( new FireSpitter());
			} else if(i==4|| i == 12 ) {
				unit1[i].setManaCost(4);
				unit1[i].setAttack(4);
				unit1[i].setHealth(6);
				unit1[i].setPreHealth(6);
			} else if(i==5|| i == 13 ) {
				unit1[i].setManaCost(5);
				unit1[i].setAttack(3);
				unit1[i].setHealth(10);
				unit1[i].setPreHealth(10);
			}else if(i==6|| i == 14 ) {
				unit1[i].setManaCost(2);
				unit1[i].setAttack(1);
				unit1[i].setHealth(4);
				unit1[i].setPreHealth(4);
			}else if(i==7|| i == 15 ) {
				unit1[i].setManaCost(3);
				unit1[i].setAttack(1);
				unit1[i].setHealth(5);
				unit1[i].setPreHealth(5);
			}else if(i == 16 ) {
				unit1[i].setManaCost(0);
				unit1[i].setAttack(2);
			}
		}

		unit2 = new BetterUnit[deck2Units.length];
		for (int i = 0; i<deck2Units.length;i++) {
			String deck1CardFile = deck2Units[i];
			unit2[i] =  (BetterUnit)  BasicObjectBuilders.loadUnit(deck1CardFile, i + 30, BetterUnit.class);
			unit2[i].setId(i + 30);
			if(i==0 || i ==8) {
				unit2[i].setManaCost(3);
				unit2[i].setAttack(4);
				unit2[i].setHealth(3);
			}else if(i==1|| i == 9 ) {
				unit2[i].setManaCost(3);
				unit2[i].setAttack(4);
				unit2[i].setHealth(3);
			}else if(i==2|| i == 10 ) {
				unit2[i].setManaCost(4);
				unit2[i].setAttack(4);
				unit2[i].setHealth(6);
			} else if(i== 3 || i == 11 ) {
				unit2[i].setManaCost(1);
				unit2[i].setAttack(2);
				unit2[i].setHealth(1);
			} else if(i==4 || i == 12 ) {
				unit2[i].setManaCost(2);
				unit2[i].setAttack(2);
				unit2[i].setHealth(1);
				unit2[i].setAbility(new Pyromancer());
			} else if(i==5|| i == 13 ) {
				unit2[i].setManaCost(2);
				unit2[i].setAttack(1);
				unit2[i].setHealth(4);
			}else if(i==6|| i == 14 ) {
				unit2[i].setManaCost(6);
				unit2[i].setAttack(7);
				unit2[i].setHealth(4);
			}else if(i==7|| i == 15 ) {
				unit2[i].setManaCost(4);
				unit2[i].setAttack(4);
				unit2[i].setHealth(3);
				unit2[i].setAbility(new Flying());
			}else if(i== 16) {
				unit2[i].setManaCost(0);
				unit2[i].setAttack(2);
			}
		}
		if(name.equalsIgnoreCase("human")) {
			temp = unit1;
		} else if (name.equalsIgnoreCase("AI")) {
			temp = unit2;
		}
		return temp;
	}
	
}
