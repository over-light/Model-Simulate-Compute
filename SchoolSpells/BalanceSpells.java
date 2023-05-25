package SchoolSpells;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dataStructures.WizHeap;

public class BalanceSpells {
	
	private WizHeap hp = new WizHeap(); 
	
	public ArrayList<Spell> balanceSpells = new ArrayList<Spell>(); 
	
	BalanceSpells()
	{
		Connection conn1 = null;
        try {
            String url1 = "jdbc:mysql://localhost:3306/wizard_schema";
            String user = "srik6724";
            String password = "28892K0shair!";
 
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
            	Statement st1 = conn1.createStatement(); 
            	if(st1 != null)
            	{
            		ResultSet rs = st1.executeQuery("SELECT * FROM wizard_schema.balance_spells");
            		while(rs.next())
            		{
            			String name = rs.getString("name"); 
            			String level = rs.getString("level"); 
            			String description = rs.getString("description"); 
            			String pip_chance = rs.getString("pip_chance");
            			String pips = rs.getString("pips"); 
            			String balance_typeSpell = rs.getString("balance_typeSpell"); 
            			
            			Spell spell = new Spell(name, level, description, pip_chance, pips, balance_typeSpell); 
            			
            			balanceSpells.add(spell); 

									/*if(createdBalanceSpells != null)
									{
										System.out.println("Deck has been successfully created."); 
            				System.out.println("Here is the following information about your main Deck, tc Deck"); 
										deckInformation(createdBalanceSpells, givenBalanceSpells); 
									}*/
            		}
								conn1.close();
								List<List<Spell>> createdBalanceSpells = null; 
								List<Map<String, List<String>>> givenBalanceSpells = anotherDefaultDeck(balanceSpells); 
								if(givenBalanceSpells != null)
								{
									System.out.println("Deck has been successfully created."); 
										//System.out.println("Here is the following information about your main Deck, tc Deck"); 
										deckInformation(createdBalanceSpells, givenBalanceSpells); 
									}
            		System.out.println("Execution is done.");
            	}
            	else
            	{
            		System.out.println("Cannot execute database statement.");
            	}
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        }
	}
	
	public ArrayList<Spell> retrieveBalanceSpells()
	{
		return balanceSpells; 
	}
	
	/*public List<List<Spell>> defaultDeck(ArrayList<Spell> spells)
	{
		Spell[] collectSpells = new Spell[spells.size()]; 
		
		Spell[] orderedSpells = performHeapOperations(collectSpells); 
		
		List<List<Spell>> deckOfSpells = hp.buildDeckYESoption(orderedSpells, "balance"); 
		
		return deckOfSpells;
		
	}*/

	public List<Map<String, List<String>>> anotherDefaultDeck(ArrayList<Spell>spells)
	{
		List<Map<String,List<String>>> givenDeck = hp.selectNOoption("Balance", "NO"); 

		return givenDeck;
	}
	
	/*public Spell[] performHeapOperations(Spell[] spells)
	{
		Spell[] modifiedSpells = hp.buildHeap(spells); 
		
		return modifiedSpells;
	}*/
	
	public void deckInformation(List<List<Spell>> spells, List<Map<String, List<String>>> setOfSpells)
	{

		/*if(spells != null)
		{

			Set<String> TCInfo = computeTCInformation(spells, setOfSpells); 
			Set<String> mainDeckInfo = computeMainDeckInformation(spells, setOfSpells); 
		
			/*for(Spell spell: spells.get(0))
			{
				int capacity = 40; 
				countOfEachSpell(spell.getName(), spells.get(0), capacity); 
			}
		
			for(Spell spell: spells.get(1))
			{
				int capacity = 64;  
				countOfEachSpell(spell.getName(), spells.get(1), capacity); 
			}

		}*/
		if(setOfSpells != null)
		{
			computeMainDeckInformation(spells, setOfSpells); 
			computeTCInformation(spells, setOfSpells); 
		}
	}
	
	public Set<String> computeTCInformation(List<List<Spell>> spells, List<Map<String,List<String>>> setOfSpells)
	{
		if(spells != null)
		{
			Set<String> uniqueSpellsTC = new HashSet<String>(); 
		
			/*for(Spell spell: spells.get(0))
			{
				uniqueSpellsTC.add(spell.getName()); 
			}*/
		
			return uniqueSpellsTC;
		}
		else if(setOfSpells != null)
		{
			for(String spell: setOfSpells.get(0).keySet())
			{
				System.out.println("Spell Name: " + spell); 
				System.out.println("Pips of Spell: " + setOfSpells.get(0).get(spell).get(2)); 
				System.out.println("Count of Spell: " + setOfSpells.get(0).get(spell).get(0)); 
				System.out.println("---------------------------------------------------------"); 
			}
		
			return null;
		}
		return null; 
	}
	
	public Set<String> computeMainDeckInformation(List<List<Spell>> spells, List<Map<String,List<String>>> setOfSpells)
	{
		if(spells != null)
		{
			Set<String> mainDeckSpells = new HashSet<String>(); 
		
			/*for(Spell spell: spells.get(0))
			{
				uniqueSpellsTC.add(spell.getName()); 
			}*/
		
			return mainDeckSpells; 
		}
		else if(setOfSpells != null)
		{
		
			/*for(String spellName: setOfSpells.get(0).keySet())
			{
				System.out.println("Spell Name: " + spellName); 
				System.out.println("Count Of Spell: " + setOfSpells.get(0).get(spellName).get(0)); 
				System.out.println("Pips Of Spell: " + setOfSpells.get(0).get(spellName).get(2)); 
			}*/
		
			return null; 
		}
		return null; 
	}
	
	public void countOfEachSpell(String name, List<Spell> spells, int capacity)
	{
		int count = 0; 
		for(Spell spell: spells)
		{
			if(spell.getName().equals(name))
			{
				count = count + 1; 
			}
		}
		System.out.println(count + "/" + capacity + name + "found."); 
	}
	
	public static void main(String[]args)
	{
		//FireSpells spells = new FireSpells(); 
	}
	
	
}

