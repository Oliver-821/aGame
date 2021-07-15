package utils;

import java.util.ArrayList;

import cardAbilities.PlanarScout;
import cardAbilities.BlazeHound;
import cardAbilities.EntropicDecay;
import cardAbilities.StaffOfYKir;
import cardAbilities.SundropElixir;
import cardAbilities.TrueStrike;
import structures.basic.Card;

/**
 * card create 
 * add card spell by using delegate pattern
 */
public class deckFactory {
	
	private static ArrayList<Card> humanDeck;
	private static ArrayList<Card> AIDeck;
	
	private static String[] deck1Cards = {
			StaticConfFiles.c_azure_herald,
			StaticConfFiles.c_azurite_lion,
			StaticConfFiles.c_comodo_charger,
			StaticConfFiles.c_fire_spitter,
			StaticConfFiles.c_hailstone_golem,
			StaticConfFiles.c_ironcliff_guardian,
			StaticConfFiles.c_pureblade_enforcer,
			StaticConfFiles.c_silverguard_knight,
			StaticConfFiles.c_azure_herald,
			StaticConfFiles.c_azurite_lion,
			StaticConfFiles.c_comodo_charger,
			StaticConfFiles.c_fire_spitter,
			StaticConfFiles.c_hailstone_golem,
			StaticConfFiles.c_ironcliff_guardian,
			StaticConfFiles.c_pureblade_enforcer,
			StaticConfFiles.c_silverguard_knight,			
			StaticConfFiles.c_sundrop_elixir,
			StaticConfFiles.c_truestrike,
			StaticConfFiles.c_sundrop_elixir,
			StaticConfFiles.c_truestrike,
	};
	
	
	private static String[] deck2Cards = {
			StaticConfFiles.c_blaze_hound,
			StaticConfFiles.c_bloodshard_golem,
			StaticConfFiles.c_hailstone_golem,
			StaticConfFiles.c_planar_scout,
			StaticConfFiles.c_pyromancer,
			StaticConfFiles.c_rock_pulveriser,
			StaticConfFiles.c_serpenti,
			StaticConfFiles.c_windshrike,
			StaticConfFiles.c_blaze_hound,
			StaticConfFiles.c_bloodshard_golem,
			StaticConfFiles.c_hailstone_golem,
			StaticConfFiles.c_planar_scout,
			StaticConfFiles.c_pyromancer,
			StaticConfFiles.c_rock_pulveriser,
			StaticConfFiles.c_serpenti,
			StaticConfFiles.c_windshrike,
			StaticConfFiles.c_entropic_decay,
			StaticConfFiles.c_staff_of_ykir,
			StaticConfFiles.c_entropic_decay,
			StaticConfFiles.c_staff_of_ykir,

	};
	
	
	/**
	 * input string check to make deck for human player or AI player 
	 * 
	 * @param check : human or AI
	 * @return : List of deck 
	 */
	public static ArrayList<Card> makeDeck(String check){
		ArrayList<Card> temp = new ArrayList<Card>();
		if(check.equalsIgnoreCase("human")) {
			humanDeck = new ArrayList<Card>();
			for (int i =0; i < 20; i++) {
				String deck1CardFile =deck1Cards[i];
				humanDeck.add(BasicObjectBuilders.loadCard(deck1CardFile, i, Card.class));
			}
			temp = humanDeck;
			 humanDeck.get(5).setAbility( new PlanarScout());
			 humanDeck.get(13).setAbility( new PlanarScout());
			 humanDeck.get(17).setAbility( new TrueStrike());
			 humanDeck.get(19).setAbility( new TrueStrike());
			 humanDeck.get(16).setAbility( new SundropElixir());
			 humanDeck.get(18).setAbility( new SundropElixir());
		} else if(check.equalsIgnoreCase("AI")) {
			AIDeck = new ArrayList<Card>();
			for (int i =0; i < 20; i++) {
				String deckCardFile =deck2Cards[i];
				AIDeck.add(BasicObjectBuilders.loadCard(deckCardFile, i+30, Card.class));
			}
			temp = AIDeck;
			 AIDeck.get(17).setAbility(new StaffOfYKir());
			 AIDeck.get(19).setAbility(new StaffOfYKir());
			 AIDeck.get(16).setAbility(new EntropicDecay());
			 AIDeck.get(18).setAbility(new EntropicDecay());
			 AIDeck.get(3).setAbility(new PlanarScout());
			 AIDeck.get(11).setAbility(new PlanarScout());
			 AIDeck.get(0).setAbility(new BlazeHound());
			 AIDeck.get(8).setAbility(new BlazeHound());
		}
		
		return temp;
	}
	
}
