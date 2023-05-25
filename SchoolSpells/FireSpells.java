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
import java.util.Set;

import dataStructures.WizHeap;

public class FireSpells {
	
	private WizHeap hp = new WizHeap(); 
	
	public ArrayList<Spell> fireSpells = new ArrayList<Spell>(); 
	
	FireSpells()
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
            		ResultSet rs = st1.executeQuery("SELECT * FROM wizard_schema.fire_spells");
            		while(rs.next())
            		{
            			String name = rs.getString("name"); 
            			String level = rs.getString("level"); 
            			String description = rs.getString("description"); 
            			String pip_chance = rs.getString("pip_chance");
            			String pips = rs.getString("pips"); 
            			String fire_typeSpell = rs.getString("fire_typeSpell"); 
            			
            			Spell spell = new Spell(name, level, description, pip_chance, pips, fire_typeSpell); 
            			
            			fireSpells.add(spell); 
            			 
            			//List<List<Spell>> updatedFireSpells = defaultDeck(fireSpells); 
            			
            			System.out.println("Deck has been successfully created."); 
            			System.out.println("Here is the following information about your main Deck, tc Deck"); 
            			
            			//deckInformation(updatedFireSpells); 
            			
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
	
	public ArrayList<Spell> retrieveFireSpells()
	{
		return fireSpells; 
	}
	
	/*public List<List<Spell>> defaultDeck(ArrayList<Spell> spells)
	{
		Spell[] collectSpells = new Spell[spells.size()]; 
		
		Spell[] orderedSpells = performHeapOperations(collectSpells); 
		
		List<List<Spell>> deckOfSpells = hp.buildDeckYESoption(orderedSpells, "fire"); 
		
		return deckOfSpells;
		
	}
	
	public Spell[] performHeapOperations(Spell[] spells)
	{
		Spell[] modifiedSpells = hp.buildHeap(spells); 
		
		return modifiedSpells;
	}*/
	
	public void deckInformation(List<List<Spell>> spells)
	{
		Set<String> TCInfo = computeTCInformation(spells); 
		Set<String> mainDeckInfo = computeMainDeckInformation(spells); 
		
		for(Spell spell: spells.get(0))
		{
			int capacity = 40; 
			countOfEachSpell(spell.getName(), spells.get(0), capacity); 
		}
		
		for(Spell spell: spells.get(1))
		{
			int capacity = 64;  
			countOfEachSpell(spell.getName(), spells.get(1), capacity); 
		}
		
	}
	
	public Set<String> computeTCInformation(List<List<Spell>> spells)
	{
		Set<String> uniqueSpellsTC = new HashSet<String>(); 
		
		for(Spell spell: spells.get(0))
		{
			uniqueSpellsTC.add(spell.getName()); 
		}
		
		return uniqueSpellsTC;
		
	}
	
	public Set<String> computeMainDeckInformation(List<List<Spell>> spells)
	{
		Set<String> uniqueSpellsMain = new HashSet<String>(); 
		
		for(Spell spell: spells.get(1))
		{
			uniqueSpellsMain.add(spell.getName()); 
		}
		
		return uniqueSpellsMain;
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

