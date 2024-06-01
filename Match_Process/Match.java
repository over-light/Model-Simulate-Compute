package Match_Process;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import Credentials.WizCredentials;

import AeonStats.AeonAmuletStats;
import AeonStats.AeonAthameStats;
import AeonStats.AeonBootsStats;
import AeonStats.AeonClass;
import AeonStats.AeonDeckStats;
import AeonStats.AeonHatStats;
import AeonStats.AeonRingStats;
import AeonStats.AeonRobeStats;
import AeonStats.AeonWandStats;
import Arena_Container_Systems.*;
import CustomExceptions.TypeException;
import DragoonStats.DragoonClass;
import EternalStats.EternalClass;
import Gear.Amulet;
import Gear.Athame;
import Gear.Boot;
import Gear.Deck;
import Gear.Gear;
import Gear.Hat;
import Customizations.*; 
import Gear.Ring;
import Gear.Robe;
import Gear.Wand;
import JadeStats.JadeClass;
import Input_Logging.LoggingStorage;

import java.util.logging.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import NightMireStats.NightMireClass;
import Match_Threading.*;
import PlayerStats.Player;
import SchoolSpells.Spell;
import SchoolSpells.schoolSpells;
import Model_Extensions.Socket;
import SpookyStats.SpookyClass;
import Jar_Executable.*;
import State_Management.*;
import Input_Variables.*;
import Data_Structures.Deck.Element;
import Deck_Build.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class Match extends Arena_Default_System implements Round, Match_Singleton, Match_Recorder {

	private static File initialFile; 

	Match(File file) {
		Match.initialFile = file;
	}

	// Putting match_writer here for now
	Scanner sc = new Scanner(System.in); 

	// Store application states
	private static Queue<ApplicationState<Object>> q = new LinkedList<>(); 

	// Read from the files team_1_information.txt and team_2_information.txt
	String line_curr = ""; 
	File team1 = new File("team_1_information.txt"); 
	File team2 = new File("team_2_information.txt");



	// round_reading file

	// Arrays to store information about the firstTeam and secondTeam. 
	int teamSize; 
	private static String[] team;
	private static String[] teamLevels; 
	private static String[] teamSchools;
	private static List<List<String>> teamPlayers = new ArrayList<List<String>>(); 
	private static List<List<String>> schoolsOfTeam = new ArrayList<List<String>>(); 
	private static HashMap<String, String> playerAssociationToSchool = new HashMap<String, String>(); 
	private static HashMap<String, List<String>> gearSets = new HashMap<String, List<String>>(); 

	private String gameModePath = "gameMode.ser"; 
	private String nameOfTeamPath = "nameOfTeam.ser"; 
	private String playerNamesPath = "playerNames.ser"; 
	private String playerLevelsPath = "playerLevels.ser"; 
	private String playerSchoolsPath = "playerSchools.ser";
	private String playerDecksPath = "playerDecks.ser"; 
	private String playerStatsPath = "playerStats.ser"; 
	private String playerOrderPath = "playerOrder.ser"; 
	private String arenaSelectionPath = "arenaSelection.ser"; 
	private String matchCountDownPath = "matchCountDown.ser"; 
	private String matchBeginsPath = "matchBegins.ser"; 
	private String bothTeamsRegistered = "bothTeamsRegistered.ser";

	// Setting application states here for readability purposes
	private ApplicationState<Object> gameModeState; 
	private ApplicationState<Object> nameOfTeamState; 
	private ApplicationState<Object> playerNamesState; 
	private ApplicationState<Object> playerLevelsState; 
	private ApplicationState<Object> playerSchoolsState; 
	private ApplicationState<Object> playerDecksState; 
	private ApplicationState<Object> playerStatsState; 
	private ApplicationState<Object> playerOrderState; 
	private ApplicationState<Object> arenaSelectionState; 
	private ApplicationState<Object> matchCountDownState; 
	private ApplicationState<Object> matchBeginsState; 
	private ApplicationState<Object> bothTeamsRegisteredState; 
	
	
	private StopWatch watch = new StopWatch(); 
	public Proposition statement = new Proposition(); 
	public String inputReceived = null; 
	
	private static ArrayList<Spectator> spectators; 
	
	private String[] wizSchools = {"Balance", "Death", "Storm", "Myth", "Ice", "Fire", "Life"}; 
	
	private String[] firstTeamOrder = new String[4]; 
	private String[] secondTeamOrder = {}; 
	
	public static HashMap<String, List<List<Element>>> decks = new HashMap<String, List<List<Element>>>(); 
	
	int startCountDown; 
	
	public static int spectatorCount = 0; 
	public static int countDownNumber = 10; 
	
	private String temp = ""; 
	private String retrieveFirstTeamName = ""; 
	private String retrieveSecondTeamName = "";

	public char[] randomVariable;

	private static int countTeamsRegistered = 1; 
	private static int firstIteration = 1; 

	// Creating the buffer reader
	private static BufferedReader reader; 

	private void setBufferReader(File teamFile) throws IOException {
		reader = new BufferedReader(new FileReader(teamFile)); 
	}

	public static BufferedReader getBufferReader() {
		return reader;
	}
	
	public void enroll2Teams(String[]args) throws InterruptedException, IOException
	{
		//launch(args);
		String[] teamStr = {"First", "Second"};  
		for(int i = 0; i < 2; i++)
		{
			if(i == 0) {
				setBufferReader(team1);
				enrollTeamPlayers(teamStr[i], firstIteration, countTeamsRegistered);
				retrieveFirstTeamName = temp; 
			}
			else if(i == 1) {
				setBufferReader(team2);
				enrollTeamPlayers(teamStr[i], firstIteration, countTeamsRegistered);
				retrieveSecondTeamName = temp; 
				//Thread.sleep(2000); 
				System.out.println("Team Name Of Second Team: " + retrieveSecondTeamName);
			}
		}
		System.out.println(retrieveFirstTeamName + " will be competing against " + retrieveSecondTeamName); 
		if(BreakpointVariables.getArenaSelection() == true) {
			System.out.println("Re-execute arena selection again? Y or N"); 
			String input = sc.nextLine(); 
			if(input.equals("Y")) {
				randomizeArenaSelection(); 
			}
		}
		else {
			randomizeArenaSelection();
		}

		if(BreakpointVariables.getMatchCountDown() == true) {
			System.out.println("Re-execute match countdown? Y or N"); 
			String input = sc.nextLine(); 
			if(input.equals("Y")) {
				randomizeArenaSelection();
			}
		}
		else {
			matchCountDown(retrieveFirstTeamName, retrieveSecondTeamName); 
		}

		if(BreakpointVariables.getMatchBegins() == true) {
			System.out.println("Re-execute match countdown? Y or N"); 
			String input = sc.nextLine(); 
			if(input.equals("Y")) {
				sc.close();
				beginMatch();
			}
		}
		else {
			beginMatch();
		}
	}

	private void setPlayerOrder() {
		System.out.println("Enter the order your team would like to be placed during the match.");
		System.out.println("Here is a list of potential orders you could be interested in for ordering your team.");

		
	}

	private void setPlayerStats() throws NumberFormatException, IOException {
		System.out.println("The next step will involve player stats to be recorded of every individual."); 
		System.out.println("This is expected to take around 10-15 minutes in total, so please be patient."); 
		int iter = 4; 
		int place = 0; 
		firstIteration = 1; 
		String saveDuplicateKey = ""; 
		LinkedListTeam1 list1 = new LinkedListTeam1(); 
		LinkedListTeam2 list2 = new LinkedListTeam2(); 
		HashMap<Integer, List<String>> keywords = new HashMap<Integer, List<String>>(); 
		for(int i = 0; i < team.length; i++)
		{
			System.out.println(teamSchools[i]); 
			//The full gear string is in the format gear1, gear2, gear3, gear4, gear5, gear6, gear7
			String fullGearString = computePlayerInformation(team[i], teamSchools[i], Integer.parseInt(teamLevels[i]), keywords, iter);
			iter--; 
			ArrayList<String> storeGearPieces = new ArrayList<String>(); 
			String gearToAdd = ""; 
			int start = 1; 
			while(start <= 9)
			{
				for(int j = place; j < fullGearString.indexOf(',', 1); j++)
				{
					gearToAdd += fullGearString.substring(j, j+1); 
				}

				if(start == 9)
				{
					for(int x = 0; x < fullGearString.length(); x++)
					{
						gearToAdd += fullGearString.substring(x, x+1); 
					}
				}		
				System.out.println("Gear to add: " + gearToAdd); 
				storeGearPieces.add(gearToAdd); 
				if(start < 9)
				{
					gearToAdd += ","; 
				}
				fullGearString = cutPartOfString(gearToAdd, fullGearString); 
				System.out.println("Full Gear String: " + fullGearString); 
				gearToAdd = ""; 
				start = start + 1; 
			}
			System.out.println(team[i]); 
			
			if(countTeamsRegistered == 1)
			{
				LinkedListTeam1.Node node = new LinkedListTeam1.Node(team[i], storeGearPieces); 
				list1.insert(node); 
				System.out.println("Inside storeGearPieces method."); 
				System.out.println(LinkedListTeam1.head); 
			}
			else 
			{
				LinkedListTeam2.Node node = new LinkedListTeam2.Node(team[i], storeGearPieces); 
				list2.insert(node); 
			}
		}
		LinkedListTeam1.Node current1;
		LinkedListTeam2.Node current2;
		if(countTeamsRegistered == 1)
		{
			current1 = LinkedListTeam1.head; 
			int count = 1; 
			String modified_wizardName = ""; 
			while(current1.next != null)
			{
				modified_wizardName = current1.wizard + " " + count; 
				gearSets.put(modified_wizardName, current1.data); 
				current1 = current1.next; 
				count = count + 1; 
			}
			modified_wizardName = current1.wizard + " " + count;
			gearSets.put(modified_wizardName, current1.data); 
		}
		else
		{
			list2.printNodeData();
			current2 = LinkedListTeam2.head; 
			//System.out.println(current2); 
			int count = 1; 
			String modified_wizardName = ""; 
			while(current2.next != null)
			{
				modified_wizardName = current2.wizard + " " + count; 
				gearSets.put(modified_wizardName, current2.data); 
				current2 = current2.next; 
				count = count + 1; 
			}
			modified_wizardName = current2.wizard + " " + count;
			gearSets.put(modified_wizardName, current2.data); 
		}
		
		for(String wizard: gearSets.keySet())
		{
			System.out.println(wizard); 
			System.out.println(gearSets.get(wizard).get(0)); 
			System.out.println(gearSets.get(wizard).get(1)); 
			System.out.println(gearSets.get(wizard).get(2)); 
			System.out.println(gearSets.get(wizard).get(3)); 
			System.out.println(gearSets.get(wizard).get(4)); 
			System.out.println(gearSets.get(wizard).get(5)); 
			System.out.println(gearSets.get(wizard).get(6)); 
			System.out.println(gearSets.get(wizard).get(7)); 
		}
		System.out.println("Computing stats information now."); 
		for(int number: keywords.keySet())
		{
			System.out.println(keywords.get(number).get(0)); 
			System.out.println(keywords.get(number).get(1)); 
			System.out.println(keywords.get(number).get(2)); 
			System.out.println(keywords.get(number).get(3)); 
			System.out.println(keywords.get(number).get(4)); 
			System.out.println(keywords.get(number).get(5)); 
			System.out.println(keywords.get(number).get(6)); 
			System.out.println(keywords.get(number).get(7)); 
		}
		computeStatsInformation(gearSets, keywords);
		LoggingStorage.getLogger().log(Level.INFO, "Player stats completed for each player on team."); 
		BreakpointVariables.setPlayerStats(true);
		playerStatsState = new ApplicationState<Object>(Match.gearSets, playerStatsPath); 
		q.add(playerStatsState); 
	}

	void createPlayerDecks(boolean flag) throws IOException {
		System.out.println("You are now clear to now create a deck for each player on your team."); 
		if(flag == false) {
			for(int i = 0; i < teamSize; i++)
			{
				createDeck(teamSchools[i], (i+1)); 
			}
		}
		else {
			boolean iterate = true; 
			List<String> players = new ArrayList<String>(); 
			int maxLimit = 4; 
			while(iterate)
			{
				System.out.println("Which player do you want to reset the deck for? Options are player 1, player 2, player 3, or player 4");
				String input = sc.nextLine(); 
				if(input.equals("1")){
					players.add(input); 
					maxLimit--;
				}
				else if(input.equals("2")){
					players.add(input); 
					maxLimit--;
				}
				else if(input.equals("3")) {
					players.add(input); 
					maxLimit--;
				}
				else if(input.equals("4")) {
					players.add(input); 
					maxLimit--;
				}
				System.out.println("Are you done or more?"); 
				String isFinished = sc.nextLine(); 
				if(isFinished.equals("NO")) {
					iterate = true;
				}
				else if(maxLimit == 0 || isFinished.equals("YES")) {
					iterate = false; 
				}
			}
		}

		System.out.println("The decks are now created for all schools specified.");
		System.out.println(); 
		System.out.println(); 

		System.out.println("The files are created for all decks specified."); 

		LoggingStorage.getLogger().log(Level.INFO, "Player Decks created for each player on team");
		BreakpointVariables.setPlayerDecks(true);
		playerDecksState = new ApplicationState<Object>(Match.decks, playerDecksPath);
		q.add(playerDecksState); 
	}

	void selectPlayerIdentitiesForTeam() throws InterruptedException {
		System.out.println("Success, now proceed to write down each of the school identities of the players."); 
		System.out.println(); 
		System.out.println(); 
		System.out.println(); 
		System.out.println(); 
		System.out.println("Just a note. The following school names allowed to be entered are: "
				+ "Ice, Fire, Death, Balance, Life, Myth, and Storm."
				+ "Any other schools will not be accepted.");
		
		Thread.sleep(1000); 

		System.out.println("Reading the player identities.");

		for(int i = 0; i < teamSchools.length; i++) {
			System.out.println("Player " + (i+1) + " School: " + teamSchools[i]); 
		}
		Match.schoolsOfTeam.add(Arrays.asList(teamSchools)); 
	}

	void selectPlayerLevelsForTeam() {
		for(int i = 0; i < teamLevels.length; i++) {
			System.out.println("Added " + teamLevels[i] + " level of " + "player " + team[i]); 
		}
		LoggingStorage.getLogger().log(Level.INFO, "Levels for each player on team selected."); 
		BreakpointVariables.setPlayerLevels(true);  
		playerLevelsState = new ApplicationState<Object>(Match.teamLevels, playerLevelsPath); 
		q.add(playerLevelsState);
	}

	void selectPlayerNamesForTeam() {
		boolean checkPlayerNames = true; 
		
		System.out.println("INSTRUCTIONS FOR REGISTERING A TEAM. Follow carefully."); 
		System.out.println("First, please write down the names of your players."
				+ "After this step is completed, write down the corresponding levels of each player." + "Then proceed"
						+ "to write down the school identities of every player."); 
		System.out.println("We humbly request it is done in this order as we would hope to avoid any technical "
				+ "issues on our end for registering all teams. Thank you! "); 
		
		List<String> playersOnTeam = new ArrayList<String>(); 
		System.out.println("Team Length: " + team.length); 
		for(int i = 0; i < team.length; i++) {
			System.out.println(team[i]);
			playersOnTeam.add(team[i]); 
		}
		teamPlayers.add(playersOnTeam); 
		playerNamesState = new ApplicationState<Object>(teamPlayers, playerNamesPath);
		q.add(playerNamesState); 
		LoggingStorage.getLogger().log(Level.INFO, "Player Names selected for players on team");
		BreakpointVariables.setPlayerNames(true); 
	}

	String selectTeamName(String str) {
		System.out.println(str + " Team");
		System.out.println("-------------------"); 
		System.out.println("What is the name of your team?"); 
		String teamName = sc.nextLine();
		LoggingStorage.getLogger().log(Level.INFO, "Name selected for team.");
		BreakpointVariables.setNameOfTeam(true); 
		nameOfTeamState = new ApplicationState<Object>(teamName, nameOfTeamPath); 
		q.add(nameOfTeamState); 
		return teamName;
	}
	
	void gameModeSelection(String line_curr) throws InterruptedException {
		boolean iterate = true; 
		String gameMode = line_curr; 
		while(iterate)
		{
			System.out.println("Select the game mode you intend to play."); 
			Thread.sleep(1000); 
			System.out.println("--------------OPTIONS-------------------"); 
			Thread.sleep(1000); 
			System.out.println("----------------1v1---------------------"); 
			Thread.sleep(1000); 
			System.out.println("----------------2v2---------------------");
			Thread.sleep(1000); 
			System.out.println("----------------3v3----------------------"); 
			Thread.sleep(1000); 
			System.out.println("----------------4v4----------------------");
			Thread.sleep(1000);  
			System.out.println("Choose which one to play based on those options."); 
			if(gameMode.contains("1v1"))
			{
				teamSize = _1v1_.teamSize(); 
				team = new String[teamSize]; 
				teamLevels = new String[teamSize]; 
				teamSchools = new String[teamSize]; 
				System.out.println("Team Size Value Set Here: " + teamSize); 
			}
			else if(gameMode.contains("2v2"))
			{
				teamSize = _2v2_.teamSize(); 
				team = new String[teamSize]; 
				teamLevels = new String[teamSize]; 
				teamSchools = new String[teamSize]; 
			}
			else if(gameMode.contains("3v3"))
			{
				teamSize = _3v3_.teamSize(); 
				team = new String[teamSize]; 
				teamLevels = new String[teamSize]; 
				teamSchools = new String[teamSize];
			}
			else if(gameMode.contains("4v4"))
			{
				teamSize = _4v4_.teamSize(); 
				team = new String[teamSize]; 
				teamLevels = new String[teamSize]; 
				teamSchools = new String[teamSize]; 
			}
			break; 
		}
		System.out.println("Game-mode selection chosen is: " + gameMode); 
		LoggingStorage.getLogger().log(Level.INFO, "Game-mode selection completed."); 
		BreakpointVariables.setGameModeSelection(true);  
		gameModeState = new ApplicationState<Object>(gameMode, gameModePath); 
		StatePersistence gameModePersistence = new StatePersistence(gameModeState); 
		gameModePersistence.saveState();
		q.add(gameModeState); 
	}
	
	public void enrollTeamPlayers(String str, int firstIteration, int countTeamsRegistered) throws InterruptedException, IOException
	{
		if(BreakpointVariables.getGameModeSelection() == true && BreakpointVariables.getBothTeamsRegistered() == true) {
			System.out.println("Re-execute game mode selection again? Y or N"); 
			String input = sc.nextLine(); 
			if(input.equals("Y")) {
				BreakpointVariables.setGameModeSelection(false);
				line_curr = Match.getBufferReader().readLine();
				gameModeSelection(line_curr);
			}
			else {
				System.out.println("Testing the load state function. Accessing the queue");
				gameModeState = pollQueue(gameModePath); 
			}
		} else {
			line_curr = Match.getBufferReader().readLine();
			gameModeSelection(line_curr);
		} 

		if(BreakpointVariables.getNameOfTeam() == true && BreakpointVariables.getBothTeamsRegistered() == true) {
			System.out.println("Switch team name? Y or N"); 
			String input = sc.nextLine(); 
			if(input.equals("Y")) {
				BreakpointVariables.setNameOfTeam(false);
				temp = selectTeamName(str); 
				System.out.println("Team Name chosen is: " + temp); 
			}
		}
		else {
			System.out.println("Select your team name."); 
			temp = reader.readLine(); 
			temp = temp.replace("Team Name:", "");
			System.out.println("Team Name chosen is:" + temp); 
		}

		if(BreakpointVariables.getPlayerNames() == true && BreakpointVariables.getBothTeamsRegistered() == true) {
			System.out.println("Change player names? Y or N"); 
			String input = sc.nextLine(); 
			if(input.equals("Y")) {
				BreakpointVariables.setPlayerNames(false);
				selectPlayerNamesForTeam(); 
			}
		}
		else {
			// Collecting the player names
			System.out.println("Team Size: " + teamSize); 
			for(int i = 0; i < teamSize; i++) {
				String playerName = reader.readLine();
				System.out.println("Player Name Read: " + playerName); 
				if(playerName.contains("Player " + (i+1) + " Name: ")) {
					String x = "Player " + (i+1) + " Name: "; 
					playerName = playerName.replace(x, ""); 
					//System.out.println("Player Name: " + playerName);
					team[i] = playerName; 
				}
			}
			selectPlayerNamesForTeam();
		}

		if(BreakpointVariables.getPlayerLevels() == true && BreakpointVariables.getBothTeamsRegistered() == true) {
			System.out.println("Change player levels? Y or N"); 
			String input = sc.nextLine(); 
			if(input.equals("Y")) {
				BreakpointVariables.setPlayerLevels(false);
				selectPlayerLevelsForTeam(); 
			}
		}
		else {
			for(int i = 0; i < teamSize; i++) {
				String levelOfPlayer = reader.readLine();
				if(levelOfPlayer.contains("Player " + (i+1) + " Level: ")){
					String x = "Player " + (i+1) + " Level: "; 
					levelOfPlayer = levelOfPlayer.replace(x, ""); 
					teamLevels[i] = levelOfPlayer;
				}
			}
			selectPlayerLevelsForTeam();
		}

		if(BreakpointVariables.getPlayerSchools() == true && BreakpointVariables.getBothTeamsRegistered() == true) {
			System.out.println("Change player identities? Y or N"); 
			String input = sc.nextLine(); 
			if(input.equals("Y")) {
				BreakpointVariables.setPlayerSchools(false);
				selectPlayerIdentitiesForTeam(); 
			}
		} else {
			for(int i = 0; i < teamSize; i++) {
				String schoolOfPlayer = reader.readLine(); 
				if(schoolOfPlayer.contains("Player " + (i+1) + " School: ")) {
					String x = "Player " + (i+1) + " School: "; 
					schoolOfPlayer = schoolOfPlayer.replace(x, ""); 
					teamSchools[i] = schoolOfPlayer;
				}
			}
			selectPlayerIdentitiesForTeam(); 
		}

		if(BreakpointVariables.getPlayerDecks() == true && BreakpointVariables.getBothTeamsRegistered() == true) {
			System.out.println("Change player decks? Y or N"); 
			String input = sc.nextLine(); 
			if(input.equals("Y")) {
				BreakpointVariables.setPlayerDecks(false);
				boolean flag = BreakpointVariables.getPlayerDecks(); 
				createPlayerDecks(flag); 
			}
		} else {
			BreakpointVariables.setPlayerDecks(false);
			boolean flag = BreakpointVariables.getPlayerDecks(); 
			createPlayerDecks(flag); 
		}

		System.out.println();
		System.out.println(); 
		System.out.println(); 
		System.out.println(); 

		if(BreakpointVariables.getPlayerStats() == true && BreakpointVariables.getBothTeamsRegistered() == true) {
			System.out.println("Redo player stats for players? Y or N"); 
			String input = sc.nextLine(); 
			if(input.equals("Y")) {
				gearSets = new HashMap<String, List<String>>(); 
				setPlayerStats(); 
			}
		}
		else {
			setPlayerStats(); 
		}

		if(BreakpointVariables.getPlayerOrder() == true) {
			System.out.println("Revamp player order for team? Y or N"); 
			String input = sc.nextLine(); 
			if(input.equals("Y")) {
				setPlayerOrder(); 
			}
		}
		else {
			setPlayerOrder(); 
		}
	} 
	private ApplicationState<Object> pollQueue(String pathName) {
		return q.stream()
					 .filter(state -> state.getFilePath().equals(pathName))
					 .findFirst()
					 .orElse(null); 
	}

	private void generatePossibleOrders(int left, int right, int length, int firstIteration, String[]orderCreated, List<String> firstTeamSchools, HashMap<Integer, List<String>> orderDetail) {
		// Implement a backtracking algorithm that generates all the possible combinations of orders that one can look at.
		// Use hashmap to store the information of each order. 
		// Make sure to terminate the base case if there are duplicates and no other potential combinations can be created. 
		// Use a randomization metric from 0-3 to decide which two elements to swap

		if(firstIteration == 1)
		{
			orderCreated = firstTeamSchools.toArray(orderCreated); 
			firstIteration = 0; 
		}

		if(orderDetail.size() == 0)
		{
			orderDetail.put(left+1, Arrays.asList(orderCreated)); 
		}
		else 
		{
			orderDetail.put(left+1, Arrays.asList(orderCreated));
		}

		if(left < length)
		{
			int firstRandomNumber = (int)(Math.random() * orderCreated.length-1);
			int secondRandomNumber = (int)(Math.random() * orderCreated.length-1); 

			String temp = orderCreated[firstRandomNumber]; 
			orderCreated[firstRandomNumber] = orderCreated[secondRandomNumber];
			orderCreated[secondRandomNumber] = temp; 

			generatePossibleOrders(left+1, right, length, firstIteration, Arrays.copyOf(orderCreated, orderCreated.length), Arrays.asList(orderCreated), orderDetail);
		}

		if(right < length)
		{
			int firstRandomNumber = (int)(Math.random() * orderCreated.length-1); 
			int secondRandomNumber = (int)(Math.random() * orderCreated.length-1); 

			String temp = orderCreated[firstRandomNumber]; 
			orderCreated[firstRandomNumber] = orderCreated[secondRandomNumber]; 
			orderCreated[secondRandomNumber] = temp; 
		
			generatePossibleOrders(left, right+1, length, firstIteration, Arrays.copyOf(orderCreated, orderCreated.length), Arrays.asList(orderCreated), orderDetail);
		}
		
	}

	private List<String> retrieveDuplicateKeyInfo(HashMap<String, List<String>> gearSets, String string) {
		List<String> gearItems = new ArrayList<String>(); 
		for(String wizard: gearSets.keySet())
		{
			if(wizard.equals(string))
			{
				gearItems.add(gearSets.get(wizard).get(0)); //hat
				gearItems.add(gearSets.get(wizard).get(1)); //robe
				gearItems.add(gearSets.get(wizard).get(2)); //boots
				gearItems.add(gearSets.get(wizard).get(3)); //wand
				gearItems.add(gearSets.get(wizard).get(4)); //athame
				gearItems.add(gearSets.get(wizard).get(5)); //amulet
				gearItems.add(gearSets.get(wizard).get(6)); //ring
				gearItems.add(gearSets.get(wizard).get(7)); //deck
				break;
			}
		}
		return gearItems;
	}

	private HashMap<String, List<List<Element>>> createDeck(String str, int deckNo) throws IOException {
		
		new schoolSpells(str); 
		
		switch(str.toLowerCase()) {
		case "balance": 
			int mainDeckCardIndex = 0; 
			int tcDeckCardIndex = 0; 

			decks.put("balance", schoolSpells.allSchoolSpells.get(schoolSpells.balanceIndex)); 

			for(String school: decks.keySet())
			{
				if(school.equals("balance"))
				{
					boolean iterate = true; 
					while(iterate && mainDeckCardIndex < 64 && tcDeckCardIndex < 40)
					{
						System.out.println("Main Deck Card Name: " + decks.get(school).get(0).get(mainDeckCardIndex).getSpellName()); 
						System.out.println("TC Deck Card Name: " + decks.get(school).get(1).get(tcDeckCardIndex).getSpellName());  
						mainDeckCardIndex++; 
						tcDeckCardIndex++;
					}
				}
			}

			System.out.println("Main Deck Card No: " + (mainDeckCardIndex + 1)); 
			System.out.println("TC Deck Card No: " + (tcDeckCardIndex + 1)); 

			System.out.println("Success, balance deck is in!"); 
			break;
		
		case "fire": 
			mainDeckCardIndex = 0; 
			tcDeckCardIndex = 0; 

			decks.put("fire", schoolSpells.allSchoolSpells.get(schoolSpells.fireIndex));

			for(String school: decks.keySet())
			{
				if(school.equals("fire"))
				{
					boolean iterate = true; 
					while(iterate && mainDeckCardIndex < 64 && tcDeckCardIndex < 40)
					{
						System.out.println("Main Deck Card Name: " + decks.get(school).get(0).get(mainDeckCardIndex).getSpellName()); 
						System.out.println("TC Deck Card Name: " + decks.get(school).get(1).get(tcDeckCardIndex).getSpellName());  
						mainDeckCardIndex++; 
						tcDeckCardIndex++;
					}
				}
			}

			System.out.println("Main Deck Card No: " + (mainDeckCardIndex + 1)); 
			System.out.println("TC Deck Card No: " + (tcDeckCardIndex + 1)); 

			System.out.println("Success, fire deck is in!"); 
			break;
			
		case "ice": 
			mainDeckCardIndex = 0; 
			tcDeckCardIndex = 0; 

			decks.put("ice", schoolSpells.allSchoolSpells.get(schoolSpells.stormIndex));

			for(String school: decks.keySet())
			{
				if(school.equals("ice"))
				{
					boolean iterate = true; 
					while(iterate)
					{
						System.out.println("Main Deck Card Name: " + decks.get(school).get(0).get(mainDeckCardIndex).getSpellName()); 
						System.out.println("TC Deck Card Name: " + decks.get(school).get(1).get(tcDeckCardIndex).getSpellName());  
						mainDeckCardIndex++; 
						tcDeckCardIndex++;
					}
				}
			}

			System.out.println("Main Deck Card No: " + (mainDeckCardIndex + 1)); 
			System.out.println("TC Deck Card No: " + (tcDeckCardIndex + 1)); 

			System.out.println("Success, ice deck is in!"); 
			break;
		
		case "life": 
			mainDeckCardIndex = 0; 
			tcDeckCardIndex = 0; 

			decks.put("life", schoolSpells.allSchoolSpells.get(schoolSpells.lifeIndex)); 

			for(String school: decks.keySet())
			{
				if(school.equals("life"))
				{
					boolean iterate = true; 
					while(iterate && mainDeckCardIndex < 64 && tcDeckCardIndex < 40)
					{
						System.out.println("Main Deck Card Name: " + decks.get(school).get(0).get(mainDeckCardIndex).getSpellName()); 
						System.out.println("TC Deck Card Name: " + decks.get(school).get(1).get(tcDeckCardIndex).getSpellName());  
						mainDeckCardIndex++; 
						tcDeckCardIndex++;
					}
				}
			}

			System.out.println("Main Deck Card No: " + (mainDeckCardIndex + 1)); 
			System.out.println("TC Deck Card No: " + (tcDeckCardIndex + 1)); 

			System.out.println("Success, life deck is in!"); 
			break;
		
		case "death": 
			mainDeckCardIndex = 0; 
			tcDeckCardIndex = 0; 

			decks.put("death", schoolSpells.allSchoolSpells.get(schoolSpells.deathIndex)); 

			for(String school: decks.keySet())
			{
				if(school.equals("death"))
				{
					boolean iterate = true; 
					while(iterate && mainDeckCardIndex < 64 && tcDeckCardIndex < 40)
					{
						System.out.println("Main Deck Card Name: " + decks.get(school).get(0).get(mainDeckCardIndex).getSpellName()); 
						System.out.println("TC Deck Card Name: " + decks.get(school).get(1).get(tcDeckCardIndex).getSpellName());  
						mainDeckCardIndex++; 
						tcDeckCardIndex++;
					}
				}
			}

			System.out.println("Main Deck Card No: " + (mainDeckCardIndex + 1)); 
			System.out.println("TC Deck Card No: " + (tcDeckCardIndex + 1)); 

			System.out.println("Success, death deck is in!"); 
			break;
		
		case "storm": 
			mainDeckCardIndex = 0; 
			tcDeckCardIndex = 0; 

			decks.put("storm", schoolSpells.allSchoolSpells.get(schoolSpells.stormIndex));

			for(String school: decks.keySet())
			{
				if(school.equals("storm"))
				{
					boolean iterate = true; 
					while(iterate && mainDeckCardIndex < 64 && tcDeckCardIndex < 40)
					{
						System.out.println("Main Deck Card Name: " + decks.get(school).get(0).get(mainDeckCardIndex).getSpellName()); 
						System.out.println("TC Deck Card Name: " + decks.get(school).get(1).get(tcDeckCardIndex).getSpellName());  
						mainDeckCardIndex++; 
						tcDeckCardIndex++;
					}
				}
			}

			System.out.println("Main Deck Card No: " + (mainDeckCardIndex + 1)); 
			System.out.println("TC Deck Card No: " + (tcDeckCardIndex + 1)); 

			System.out.println("Success, storm deck is in!"); 
			break;
			
		case "myth": 
			mainDeckCardIndex = 0; 
			tcDeckCardIndex = 0; 

			decks.put("myth", schoolSpells.allSchoolSpells.get(schoolSpells.mythIndex)); 

			for(String school: decks.keySet())
			{
				if(school.equals("myth"))
				{
					boolean iterate = true; 
					while(iterate && mainDeckCardIndex < 64 && tcDeckCardIndex < 40)
					{
						System.out.println("Main Deck Card Name: " + decks.get(school).get(0).get(mainDeckCardIndex).getSpellName()); 
						System.out.println("TC Deck Card Name: " + decks.get(school).get(1).get(tcDeckCardIndex).getSpellName());  
						mainDeckCardIndex++; 
						tcDeckCardIndex++;
					}
				}
			}

			System.out.println("Main Deck Card No: " + (mainDeckCardIndex + 1)); 
			System.out.println("TC Deck Card No: " + (tcDeckCardIndex + 1)); 

			System.out.println("Success, myth deck is in!"); 
			break;

		default: 
			TypeException ex = new TypeException(); 
			ex.message(str); 
		}

		Thread th = new Thread(new SpringBootExecutable()); 
		th.start();

		return decks;
		
	}
	
	public boolean isNumeric(String str)
	{
		if(str == null)
		{
			return false; 
		}
		
		try {
			Double.parseDouble(str); 
			return true; 
		} catch(NumberFormatException exception)
		{
			return false; 
		}
	}
	
	public boolean findSchool(String[]arr, String schoolEntered)
	{
		boolean check = true; 
		
		for(int i = 0; i < arr.length; i++)
		{
			if(arr[i].toLowerCase().equals(schoolEntered.toLowerCase()))
			{
				check = true; 
				break; 
			}
			else
			{
				check = false; 
			}
		}
		return check; 
	}
	
	public void randomizeArenaSelection()
	{
		int ArenaNumber = Arena_Default.ArenaNumber();
		int AvalonArenaNumber = Arena_1.returnAvalonArenaNumber(); 
		int DragonSpyreArenaNumber = DragonSpyreArena.DragonSpyreArenaNumber(); 
		int GrizzleheimArenaNumber = GrizzleheimArena.GrizzleheimArenaNumber(); 
		int HeapArenaNumber = HeapArena.HeapArenaNumber(); 
		
		int MooshuArenaNumber = MooshuArena.MooshuaArenaNumber(); 
		
		String arenaName = " "; 
		int ArenaNumberSelected = (int)(Math.random() * 6) + 1; 
		
		int[] arenaNumbers = {ArenaNumber, AvalonArenaNumber, DragonSpyreArenaNumber, GrizzleheimArenaNumber, HeapArenaNumber, MooshuArenaNumber};
		/*String[] arenaNames = {ArenaName(), AvalonArenaName(), DragonSpyreArenaName(), GrizzleheimArenaName(), HeapArenaName(), MooshuArenaName()}; 
		
		
		boolean matchArenaNumber = true; 
	
		
		for(int i = 0; i < arenaNumbers.length; i++)
		{
			if(arenaNumbers[i] == ArenaNumberSelected)
			{
				arenaName = arenaNames[i]; 
				ArenaNumberSelected = arenaNumbers[i]; 
				break; 
			}
		}
		
		System.out.println("The chosen Arena Number is " + ArenaNumberSelected);
		System.out.println("This corresponds to the arena of " + arenaName);

		LoggingStorage.getLogger().log(Level.INFO, "Arena selection has been made."); 
		BreakpointVariables.setArenaSelection(true);
		// Storing arenaName
		arenaSelectionState = new ApplicationState<Object>(arenaName, arenaSelectionPath);
		q.add(arenaSelectionState); */
	}
	
	public void matchCountDown(String firstTeamName, String secondTeamName)
	{	
		System.out.println("It is official."); 
		System.out.println("Team " + firstTeamName + " will be versing " + secondTeamName); 
		
		System.out.println("Setting thread here for the countdown from 15 to 1.");
		System.out.println("Just for personal note, potential data analysis will be conducted there.");
		System.out.println("Also look into separate terminals for threading."); 
		
		Thread th = new Thread(new Team1vsTeam2());
    th.start(); 

    try {
      th.join(); 
    }catch(InterruptedException e)
    {
      //System.out.println("Execution will now resume in the main thread."); 
    }

		int numberOfSpectators = countSpectators(); 
		System.out.println("We will have " + numberOfSpectators + " spectators " + "watching the match. "); 
		
		String[] checkIfSorted = sortTeamsAlphabetically(team); 
		for(int i = 0; i < checkIfSorted.length; i++)
		{
			System.out.println("Sorted element " + i + ": " + checkIfSorted[i]); 
		}
		//watch.startWatch(); 
		LoggingStorage.getLogger().log(Level.INFO, "Match countdown for both teams versing one another completed."); 
		BreakpointVariables.setMatchCountDown(true);
		matchCountDownState = new ApplicationState<Object>("Place-holder to store match state", matchCountDownPath);
		q.add(matchCountDownState);
	}
	
	public int countSpectators()
	{
		return spectatorCount; 
	}
	
	
	public void addSpectator(int randomNumberGenerated)
	{
		int randomNumber = (int) ((Math.random() * 6) + 1);
		
		if(randomNumber == randomNumberGenerated)
		{
			spectatorCount = spectatorCount + 1; 
			System.out.println("Spectator " + spectatorCount + " randomly generated."); 
		}
		
	}
	
	public boolean placeBets()
	{
		String isRightInput = statement.establishStatement(retrieveFirstTeamName, retrieveSecondTeamName);
		
		if(isRightInput == "")
		{
			return false; 
		}

		inputReceived = isRightInput; 
		return true; 
	}
	
	public Spectator[] sortedArray()
	{
		return null; 
	}
	
	public String[] sortTeamsAlphabetically(String[]firstTeam)
	{
		// Before Insertion Sort Algorithm 

		for(int i = 0; i < firstTeam.length; i++)
		{
			System.out.println("Player Element " + i + ": " + firstTeam[i]); 
		}
		
		// Insertion Sort algorithm 
		// We are going to sort the two teams alphabetically in order. 
		// First Team: Cowan Shadowsteed, Miguel Pearlhunter, Travis Waterblood, Anthony Firestone
		
		for(int i = 1; i < firstTeam.length; i++)
		{
			String retrieveKey = firstTeam[i]; // Cowan Shadowsteed
			
			// Store the index i in variable j
			int j = i - 1;  // 0
			
			// Compare the first character to the second character
			while(j >= 0 && retrieveKey.compareTo(firstTeam[j]) < 0) 
			{
				firstTeam[j+1] = firstTeam[j]; 
				j = j - 1; 
			}

			firstTeam[j+1] = retrieveKey; 
		}

		return firstTeam; 
	}
	
	public String returnFirstLetter(String retrieveKey)
	{
		return retrieveKey.substring(0,1); 
	}

	private Set<String> extractUniqueElementsTeam(List<String> players) {
		Set<String> uniquePlayerNames = new HashSet<String>(); 

		for(String player: players) {
			uniquePlayerNames.add(player);
		}

		return uniquePlayerNames;
	}

	private void setTeamAttributes() {
		int playerIndex = 0; 
		int schoolIndex = 0;
		String playerPerson = ""; 


		System.out.println("-------------------------------------"); 
		System.out.println("#####################################"); 

		int loop = 1; 
		for(List<String> team: teamPlayers) {

			List<String> sortedTeam = new ArrayList<>(team); 
			Collections.sort(sortedTeam); 
			// Sorted Team, Mark Labels on unique entries
			Set<String> uniquePlayerNames = extractUniqueElementsTeam(sortedTeam);
			System.out.println("UNIQUE PLAYER NAMES SIZE: " + uniquePlayerNames.size()); 

			for(String uniquePlayer: uniquePlayerNames) {
				System.out.println("UNIQUE PLAYER NAME: " + uniquePlayer); 
				int counter = 0;
				for(int i = 0; i < team.size(); i++) {
					if(team.get(i).equals(uniquePlayer)) {
						team.set(i, uniquePlayer +  " MEMBER "  + String.valueOf(++counter) + " --> (TEAM " + loop + ")");
					}
				}
				counter = 0; 
			}
			loop++;
		}

		System.out.println("#####################################"); 
		System.out.println("-------------------------------------"); 

		boolean iterationTeamDone = false; 
		for(List<String> players: teamPlayers) {
			for(int i = 0; i < players.size(); i++) {
				if(i == playerIndex) {
					playerPerson = players.get(i); 
					System.out.println("Player to be inserted is: " + playerPerson); 
					for(List<String> schools: schoolsOfTeam) { 
						if(iterationTeamDone == true) {
							iterationTeamDone = false; 
							continue; 
						}
						boolean breakLoop = false; 
						for(int j = 0; j < schools.size(); j++) { 
							if(j == schoolIndex) {  
								String school = schools.get(j); 
								System.out.println("School to be inserted is: " + school); 
								playerAssociationToSchool.put(playerPerson, school); 
								breakLoop = true; 
							}
						}

						if(breakLoop == true) {
							break;
						}
	
					}
				}
				playerIndex++;
				schoolIndex++; 
			}
			iterationTeamDone = true; 
			playerIndex = 0; 
			schoolIndex = 0; 
		}
	}
	
	public void beginMatch()
	{
		// Placing team members with their respective schools depending by the team they belong to
		setTeamAttributes();

		System.out.println("TEAM ATTRIBUTES SET FOR MEMBERS OF TEAMS."); 

		System.out.println("Match between " + retrieveFirstTeamName + " and " + retrieveSecondTeamName + " has begun."); 
		System.out.println("We now spin a wheel to decide who starts first. We will do this "
				+ "the traditional way.");
		System.out.println("Heads or Tails Style."); 
		System.out.println("TEAM" + retrieveFirstTeamName + ": " + "Heads or Tails");
		String input1 = sc.nextLine(); 
		System.out.println("TEAM" + retrieveSecondTeamName + ": " + "Heads or Tails");
		String input2 = sc.nextLine(); 
		String inputAnswer = randomizeHeadsOrTails(); 

		if(inputAnswer.equals(input1)) 
		{
			System.out.println("TEAM " + retrieveFirstTeamName + " is starting first."); 

			Thread th1 = new Thread(new Team1Runnable()); 

			try {
				th1.start(); 
				th1.join(); 
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		else if(inputAnswer.equals(input2))
		{
			System.out.println("TEAM " + retrieveFirstTeamName + " is starting first."); 
			
			Thread th2 = new Thread(new Team2Runnable()); 

			try {
				th2.start(); 
				th2.join(); 
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		LoggingStorage.getLogger().log(Level.INFO, "The match has begun."); 
		BreakpointVariables.setMatchBegins(true); 
	}

	public FileWriter getMatchRecorderInstance() {
		FileWriter matchRecorder = null;
		try {
			matchRecorder = Match_Recorder.getFileWriter(); 
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		return matchRecorder;
	}

	
	
	public static void startRound(int index, FileWriter matchWriterFinalizer, FileReader roundReading) throws IOException, InterruptedException
	{
		FileWriter roundTeam1SpellsWriter = null;
		FileWriter roundTeam2SpellsWriter = null;
		FileWriter matchCombineWriter = null;
		FileWriter matchRoundByRoundWriter = null;
		FileWriter matchExcessSpellsWriter = null;
		FileWriter matchExcessSpellsTeam1Writer = null;
		FileWriter matchExcessSpellsTeam2Writer = null;
		FileWriter matchRemainingSpellsWriter = null;
		FileWriter matchRemainingSpellsTeam1Writer = null;
		FileWriter matchRemainingSpellsTeam2Writer = null;
		FileWriter matchSelectionLineWriter = null;
		FileWriter matchSelectionLineTeam1Writer = null;
		FileWriter matchSelectionLineTeam2Writer = null;
		FileWriter roundCombineWriter = null;
		FileWriter roundDefaultWriter = null;
		FileWriter roundSelectionLineWriter = null; 
		FileWriter roundSelectionLineTeam1Writer = null;
		FileWriter roundSelectionLineTeam2Writer = null;
		FileWriter roundRemainingSpellsWriter = null;
		FileWriter roundRemainingTeam1SpellsWriter = null;
		FileWriter roundRemainingTeam2SpellsWriter = null;
		FileWriter roundExcessSpellsWriter = null;
		FileWriter roundExcessSpellsTeam1Writer = null;
		FileWriter roundExcessSpellsTeam2Writer = null;

		int round = Round.get_current_number(); 
		System.out.println("PLAYER ASSOCIATION TO SCHOOL SIZE: " + playerAssociationToSchool.size()); 

		try {
			if(playerAssociationToSchool.size() == 2) {
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 1); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 1); 
			}
			else if(playerAssociationToSchool.size() == 4) {
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 1); 
				RoundTeam1MemberSpellsWriter.file_writers[0].write(""); 
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 2); 
				RoundTeam1MemberSpellsWriter.file_writers[1].write(""); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 1);
				RoundTeam2MemberSpellsWriter.file_writers[0].write(""); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 2); 
				RoundTeam2MemberSpellsWriter.file_writers[1].write(""); 
			}
			else if(playerAssociationToSchool.size() == 6) {
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 1); 
				RoundTeam1MemberSpellsWriter.file_writers[0].write(""); 
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 2); 
				RoundTeam1MemberSpellsWriter.file_writers[1].write(""); 
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 3); 
				RoundTeam1MemberSpellsWriter.file_writers[2].write(""); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 1); 
				RoundTeam2MemberSpellsWriter.file_writers[0].write(""); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 2); 
				RoundTeam2MemberSpellsWriter.file_writers[1].write(""); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 3); 
				RoundTeam2MemberSpellsWriter.file_writers[2].write(""); 
			}
			else if(playerAssociationToSchool.size() == 8) {
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 1); 
				RoundTeam1MemberSpellsWriter.file_writers[0].write(""); 
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 2); 
				RoundTeam1MemberSpellsWriter.file_writers[1].write(""); 
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 3); 
				RoundTeam1MemberSpellsWriter.file_writers[2].write(""); 
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 4); 
				RoundTeam1MemberSpellsWriter.file_writers[3].write(""); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 1); 
				RoundTeam2MemberSpellsWriter.file_writers[0].write(""); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 2); 
				RoundTeam2MemberSpellsWriter.file_writers[1].write(""); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 3); 
				RoundTeam2MemberSpellsWriter.file_writers[2].write(""); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 4); 
				RoundTeam2MemberSpellsWriter.file_writers[3].write(""); 
			}
			matchCombineWriter = MatchCombineWriter.get_file_writer();
			matchCombineWriter.write(""); 
			matchRoundByRoundWriter = RoundByRoundWriter.get_file_writer();
			matchRoundByRoundWriter.write(""); 
			matchExcessSpellsWriter = MatchDiscardSpellsWriter.get_file_writer();
			matchExcessSpellsWriter.write(""); 
			matchExcessSpellsTeam1Writer = MatchDiscardSpellsTeam1Writer.get_file_writer();
			matchExcessSpellsTeam1Writer.write(""); 
			matchExcessSpellsTeam2Writer = MatchDiscardSpellsTeam2Writer.get_file_writer();
			matchExcessSpellsTeam2Writer.write("");
			matchRemainingSpellsWriter = MatchLeftOverSpellsWriter.get_file_writer();
			matchRemainingSpellsWriter.write(""); 
			matchRemainingSpellsTeam1Writer = MatchLeftOverSpellsTeam1Writer.get_file_writer();
			matchRemainingSpellsTeam1Writer.write(""); 
			matchRemainingSpellsTeam2Writer = MatchLeftOverSpellsTeam2Writer.get_file_writer();
			matchRemainingSpellsTeam2Writer.write(""); 
			matchSelectionLineWriter = MatchSelectionLineWriter.get_file_writer(); 
			matchSelectionLineWriter.write(""); 
			matchSelectionLineTeam1Writer = MatchSelectionLineTeam1Writer.get_file_writer();
			matchSelectionLineTeam1Writer.write(""); 
			matchSelectionLineTeam2Writer = MatchSelectionLineTeam2Writer.get_file_writer();
			matchSelectionLineTeam2Writer.write(""); 
			roundCombineWriter = RoundCombineWriter.get_file_writer(round); 
			roundCombineWriter.write(""); 
			roundTeam1SpellsWriter = RoundTeam1SpellsWriter.get_file_writer(round); 
			roundTeam1SpellsWriter.write(""); 
			roundTeam2SpellsWriter = RoundTeam2SpellsWriter.get_file_writer(round); 
			roundTeam2SpellsWriter.write(""); 
			roundDefaultWriter = RoundOfSpellsWriter.get_file_writer(round);
			roundDefaultWriter.write(""); 
			roundSelectionLineWriter = RoundSelectionLineWriter.get_file_writer(round); 
			roundSelectionLineWriter.write(""); 
			roundSelectionLineTeam1Writer = RoundSelectionLineTeam1Writer.get_file_writer(round); 
			roundSelectionLineTeam1Writer.write(""); 
			roundSelectionLineTeam2Writer = RoundSelectionLineTeam2Writer.get_file_writer(round); 
			roundSelectionLineTeam2Writer.write(""); 
			roundRemainingSpellsWriter = RoundLeftOverSpellsWriter.get_file_writer(round);
			roundRemainingSpellsWriter.write(""); 
			roundRemainingTeam1SpellsWriter = RoundLeftOverSpellsTeam1Writer.get_file_writer(round); 
			roundRemainingTeam1SpellsWriter.write(""); 
			roundRemainingTeam2SpellsWriter = RoundLeftOverSpellsTeam2Writer.get_file_writer(round); 
			roundRemainingTeam2SpellsWriter.write(""); 
			roundExcessSpellsWriter = RoundDiscardSpellsWriter.get_file_writer(round); 
			roundExcessSpellsWriter.write(""); 
			roundExcessSpellsTeam1Writer = RoundDiscardSpellsTeam1Writer.get_file_writer(round); 
			roundExcessSpellsTeam1Writer.write(""); 
			roundExcessSpellsTeam2Writer = RoundDiscardSpellsTeam2Writer.get_file_writer(round); 
			roundExcessSpellsTeam2Writer.write(""); 
		} catch (Exception e) {
			if(playerAssociationToSchool.size() == 2) {
				RoundTeam1MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 1); 
				RoundTeam2MemberSpellsWriter.setWriterCreated(false);
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 1); 
			}
			else if(playerAssociationToSchool.size() == 4) {
				RoundTeam1MemberSpellsWriter.setWriterCreated(false);
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 1); 
				RoundTeam1MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 2); 
				RoundTeam2MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 1);
				RoundTeam2MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 2); 
			}
			else if(playerAssociationToSchool.size() == 6) {
				RoundTeam1MemberSpellsWriter.setWriterCreated(false);
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 1); 
				RoundTeam1MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 2); 
				RoundTeam1MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 3); 
				RoundTeam2MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 1); 
				RoundTeam2MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 2); 
				RoundTeam2MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 3); 
			}
			else if(playerAssociationToSchool.size() == 8) {
				RoundTeam1MemberSpellsWriter.setWriterCreated(false);
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 1); 
				RoundTeam1MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 2); 
				RoundTeam1MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 3); 
				RoundTeam1MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam1MemberSpellsWriter.get_file_writer(round, 4); 
				RoundTeam2MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 1); 
				RoundTeam2MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 2); 
				RoundTeam2MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 3); 
				RoundTeam2MemberSpellsWriter.setWriterCreated(false); 
				RoundTeam2MemberSpellsWriter.get_file_writer(round, 4); 
			}
			System.out.println("Caught Stream Closed Exception"); 
			MatchCombineWriter.setWriterCreated(false); 
			matchCombineWriter = MatchCombineWriter.get_file_writer();
			RoundByRoundWriter.setWriterCreated(false);
			matchRoundByRoundWriter = RoundByRoundWriter.get_file_writer();
			MatchDiscardSpellsWriter.setWriterCreated(false);
			matchExcessSpellsWriter = MatchDiscardSpellsWriter.get_file_writer();
			MatchDiscardSpellsTeam1Writer.setWriterCreated(false); 
			matchExcessSpellsTeam1Writer = MatchDiscardSpellsTeam1Writer.get_file_writer();
			MatchDiscardSpellsTeam2Writer.setWriterCreated(false);
			matchExcessSpellsTeam2Writer = MatchDiscardSpellsTeam2Writer.get_file_writer();
			MatchLeftOverSpellsWriter.setWriterCreated(false); 
			matchRemainingSpellsWriter = MatchLeftOverSpellsWriter.get_file_writer();
			MatchLeftOverSpellsTeam1Writer.setWriterCreated(false); 
			matchRemainingSpellsTeam1Writer = MatchLeftOverSpellsTeam1Writer.get_file_writer();
			MatchLeftOverSpellsTeam2Writer.setWriterCreated(false); 
			matchRemainingSpellsTeam2Writer = MatchLeftOverSpellsTeam2Writer.get_file_writer();
			MatchSelectionLineWriter.setWriterCreated(false);
			matchSelectionLineWriter = MatchSelectionLineWriter.get_file_writer();
			MatchSelectionLineTeam1Writer.setWriterCreated(false); 
			matchSelectionLineTeam1Writer = MatchSelectionLineTeam1Writer.get_file_writer();
			MatchSelectionLineTeam2Writer.setWriterCreated(false);
			matchSelectionLineTeam2Writer = MatchSelectionLineTeam2Writer.get_file_writer();
			RoundCombineWriter.setWriterCreated(false); 
			roundCombineWriter = RoundCombineWriter.get_file_writer(round); 
			RoundTeam1SpellsWriter.setWriterCreated(false);
			roundTeam1SpellsWriter = RoundTeam1SpellsWriter.get_file_writer(round);
			RoundTeam2SpellsWriter.setWriterCreated(false);
			roundTeam2SpellsWriter = RoundTeam2SpellsWriter.get_file_writer(round);
			RoundOfSpellsWriter.setWriterCreated(false); 
			roundDefaultWriter = RoundOfSpellsWriter.get_file_writer(round); 
			RoundSelectionLineWriter.setWriterCreated(false);
			roundSelectionLineWriter = RoundSelectionLineWriter.get_file_writer(round); 
			RoundSelectionLineTeam1Writer.setWriterCreated(false);
			roundSelectionLineTeam1Writer = RoundSelectionLineTeam1Writer.get_file_writer(round); 
			RoundSelectionLineTeam2Writer.setWriterCreated(false); 
			roundSelectionLineTeam2Writer = RoundSelectionLineTeam2Writer.get_file_writer(round); 
			RoundLeftOverSpellsWriter.setWriterCreated(false); 
			roundRemainingSpellsWriter = RoundLeftOverSpellsWriter.get_file_writer(round); 
			RoundLeftOverSpellsTeam1Writer.setWriterCreated(false); 
			roundRemainingTeam1SpellsWriter = RoundLeftOverSpellsTeam1Writer.get_file_writer(round); 
			RoundLeftOverSpellsTeam2Writer.setWriterCreated(false); 
			roundRemainingTeam2SpellsWriter = RoundLeftOverSpellsTeam2Writer.get_file_writer(round); 
			RoundDiscardSpellsWriter.setWriterCreated(false);
			roundExcessSpellsWriter = RoundDiscardSpellsWriter.get_file_writer(round);
			RoundDiscardSpellsTeam1Writer.setWriterCreated(false); 
			roundExcessSpellsTeam1Writer = RoundDiscardSpellsTeam1Writer.get_file_writer(round); 
			RoundDiscardSpellsTeam2Writer.setWriterCreated(false); 
			roundExcessSpellsTeam2Writer = RoundDiscardSpellsTeam2Writer.get_file_writer(round); 
		}

		System.out.println("This is a round casted of our match between team 1 and team 2"); 
		System.out.println("Team Players Size inclusive of both team 1 and team 2: " + teamPlayers.size()); 
		
		matchRoundByRoundWriter.write("\n"); 
		matchRoundByRoundWriter.write("ROUND # " + round + " OF SPELLS" + "\n");
		matchWriterFinalizer.write("ROUND # " + round + " OF SPELLS" + "\n"); 
		roundDefaultWriter.write("\n"); 
		roundDefaultWriter.write("ROUND # " + round + " OF SPELLS" + "\n"); 
		roundCombineWriter.write("ROUND # " + round + " OF SPELLS" + "\n");
		System.out.println("Size of player assocation to school: " + playerAssociationToSchool.size());
		// 2 players <-> 1:1
		// 4 players <-> 2:2
		// 6 players <-> 3:3
		// 8 players <-> 4:4
		for(String player: playerAssociationToSchool.keySet()) {
			System.out.println("Player " + player + ": Select a card."); 
			matchRoundByRoundWriter.write("PLAYER-" + player + "-SPELLS SELECTION\n"); 
			matchWriterFinalizer.write("PLAYER-" + player + "-SPELLS SELECTION\n"); 
			roundDefaultWriter.write("PLAYER-" + player + "-SPELLS SELECTION\n");	
			roundCombineWriter.write("PLAYER-" + player + "-SPELLS SELECTION\n"); 
			System.out.println("The following seven cards have been generated."); 
			Element[] sevenCards = generateSevenCards(playerAssociationToSchool.get(player).toLowerCase(), index); 
			System.out.println("Printing out the seven cards.");
			for(int z = 0; z < sevenCards.length; z++)
			{
				matchRoundByRoundWriter.write("----------------------------\n");
				matchWriterFinalizer.write("----------------------------\n"); 
				roundDefaultWriter.write("----------------------------\n"); 
				roundCombineWriter.write("----------------------------\n");
				System.out.println("Card " + (z+1) + ": " + "{ "); 
				matchRoundByRoundWriter.write("Card " + (z+1) + ": " + "{\n "); 
				matchWriterFinalizer.write("Card " + (z+1) + ": " + "{\n "); 
				roundDefaultWriter.write("Card " + (z+1) + ": " + "{\n ");
				roundCombineWriter.write("Card " + (z+1) + ": " + "{\n ");
				System.out.println("Name Of Spell: " + sevenCards[z].getSpellName()); 
				matchRoundByRoundWriter.write("Name Of Spell: " + sevenCards[z].getSpellName() + "\n"); 
				matchWriterFinalizer.write("Name Of Spell: " + sevenCards[z].getSpellName() + "\n"); 
			 	roundDefaultWriter.write("Name Of Spell: " + sevenCards[z].getSpellName() + "\n");
				roundCombineWriter.write("Name Of Spell: " + sevenCards[z].getSpellName() + "\n"); 
				System.out.println("Pips Of Spell: " + sevenCards[z].getPips()); 
				matchRoundByRoundWriter.write("Pips Of Spell: " + sevenCards[z].getPips() + "\n"); 
				matchWriterFinalizer.write("Pips Of Spell: " + sevenCards[z].getPips() + "\n"); 
				roundDefaultWriter.write("Pips Of Spell: " + sevenCards[z].getPips() + "\n");
				roundCombineWriter.write("Pips Of Spell: " + sevenCards[z].getPips() + "\n"); 
				System.out.println("Pip Chance Of Spell: " + sevenCards[z].getPipChance()); 
				matchRoundByRoundWriter.write("Pip Chance Of Spell: " + sevenCards[z].getPipChance() + "\n"); 
				matchWriterFinalizer.write("Pip Chance Of Spell: " + sevenCards[z].getPipChance() + "\n"); 
				roundDefaultWriter.write("Pip Chance Of Spell: " + sevenCards[z].getPipChance() + "\n");
				roundCombineWriter.write("Pip Chance Of Spell: " + sevenCards[z].getPipChance() + "\n"); 
				System.out.println("Type Of Spell: " + sevenCards[z].getTypeSpell()); 
				matchRoundByRoundWriter.write("Type Of Spell: " + sevenCards[z].getTypeSpell() + "\n"); 
				matchWriterFinalizer.write("Type Of Spell: " + sevenCards[z].getTypeSpell() + "\n"); 
				roundDefaultWriter.write("Type Of Spell: " + sevenCards[z].getTypeSpell() + "\n"); 
				roundCombineWriter.write("Type Of Spell: " + sevenCards[z].getTypeSpell() + "\n"); 
				System.out.println("Count Of Spell: " + sevenCards[z].getCount()); 
				matchRoundByRoundWriter.write("Count Of Spell: " + sevenCards[z].getCount() + "\n"); 
				matchWriterFinalizer.write("Count Of Spell: " + sevenCards[z].getCount() + "\n"); 
				roundDefaultWriter.write("Count Of Spell: " + sevenCards[z].getCount() + "\n");
				roundCombineWriter.write("Count Of Spell: " + sevenCards[z].getCount() + "\n"); 
				System.out.println("Description Of Spell: " + sevenCards[z].getDescription()); 
				matchRoundByRoundWriter.write("Description Of Spell: " + sevenCards[z].getDescription() + "\n"); 
				matchWriterFinalizer.write("Description Of Spell: " + sevenCards[z].getDescription() + "\n");
				roundDefaultWriter.write("Description Of Spell: " + sevenCards[z].getDescription() + "\n"); 
				roundCombineWriter.write("Description Of Spell: " + sevenCards[z].getDescription() + "\n"); 
				System.out.println("}");
				matchRoundByRoundWriter.write("}\n"); 
				matchWriterFinalizer.write("}\n"); 
				roundDefaultWriter.write("}\n"); 
				roundCombineWriter.write("}\n"); 
				System.out.println("Spell added to the Matchwriter."); 
			}
			matchRoundByRoundWriter.write("----------------------------\n");
			matchWriterFinalizer.write("----------------------------\n"); 
			roundDefaultWriter.write("----------------------------\n"); 
			roundCombineWriter.write("----------------------------\n");
		}
		matchRoundByRoundWriter.write("END OF ROUND\n");
		matchWriterFinalizer.write("END OF ROUND\n"); 
		matchWriterFinalizer.write("############################\n"); 
		roundDefaultWriter.write("END OF ROUND\n"); 
		roundCombineWriter.write("END OF ROUND\n"); 
		System.out.println("Reading round of spells using reader. Decide between reading team 1 or team 2 first."); 

		matchRoundByRoundWriter.close(); 
		roundDefaultWriter.close(); 
		BufferedReader roundComputeReader = null;
		try {
			roundComputeReader = new BufferedReader(RoundOfSpellsReader.get_file_reader()); 
			roundComputeReader.readLine(); 
		} catch (Exception e) {
			RoundOfSpellsReader.setReaderCreated(false); 
			roundComputeReader = new BufferedReader(RoundOfSpellsReader.get_file_reader()); 
		}
		roundTeam1SpellsWriter.write("\n"); 
		roundTeam2SpellsWriter.write("\n"); 
		String line; 
		boolean readFirstTeam = false; 
		boolean readSecondTeam = false; 
		String selectionStmtLine_t1 = null;
		String discardStmtLine_t1 = null;
		String remainingStmtLine_t1 = null;
		String selectionStmtLine_t2 = null;
		String discardStmtLine_t2 = null;
		String remainingStmtLine_t2 = null;
		while((line = roundComputeReader.readLine()) != null) { 
			if(line.contains("(TEAM 1)")) {
				System.out.println("CONTAINS TEAM 1"); 
				readFirstTeam = true; 
				readSecondTeam = false; 
				
				line = line.replace("SPELLS SELECTION", "SPELL SELECTED"); 
				roundSelectionLineWriter.write(line + "\n"); 
				roundSelectionLineTeam1Writer.write(line + "\n"); 
				//roundCombineWriter.write(line + "\n"); 
				selectionStmtLine_t1 = line;
				String discardLine = line; 
				discardLine = discardLine.replace("SPELL SELECTED", "SPELLS DISCARDED"); 
				roundExcessSpellsWriter.write(discardLine + "\n"); 
				roundExcessSpellsTeam1Writer.write(discardLine + "\n"); 
				discardStmtLine_t1 = discardLine;
				//roundCombineWriter.write(discardLine + "\n"); 
				String remainingLine = line; 
				remainingLine = remainingLine.replace("SPELL SELECTED", "SPELLS REMAINING"); 
				roundRemainingSpellsWriter.write(remainingLine + "\n"); 
				roundRemainingTeam1SpellsWriter.write(remainingLine + "\n"); 
				roundRemainingTeam2SpellsWriter.write(remainingLine + "\n"); 
				remainingStmtLine_t1 = remainingLine;
				//roundCombineWriter.write(remainingLine + "\n"); 
			}
			if(line.contains("(TEAM 2)")) {
				readSecondTeam = true; 
				readFirstTeam = false;
				line = line.replace("SPELLS SELECTION", "SPELL SELECTED");
				roundSelectionLineWriter.write(line + "\n"); 
				roundSelectionLineTeam2Writer.write(line + "\n");  
				selectionStmtLine_t2 = line;
				//roundCombineWriter.write(line + "\n"); 
				String discardLine = line; 
				discardLine = discardLine.replace("SPELL SELECTED", "SPELL DISCARDED"); 
				roundExcessSpellsWriter.write(discardLine + "\n"); 
				roundExcessSpellsTeam2Writer.write(discardLine + "\n"); 
				discardStmtLine_t2 = discardLine;
				//roundCombineWriter.write(discardLine + "\n"); 
				String remainingLine = line; 
				remainingLine = remainingLine.replace("SPELL SELECTED", "SPELL REMAINING"); 
				roundRemainingSpellsWriter.write(remainingLine + "\n"); 
				roundRemainingTeam1SpellsWriter.write(remainingLine + "\n"); 
				roundRemainingTeam2SpellsWriter.write(remainingLine + "\n"); 
				remainingStmtLine_t2 = remainingLine;
				//roundCombineWriter.write(remainingLine + "\n"); 
			}
			if(readFirstTeam == true) {
				System.out.println("WRITING LINE: " + line + " FOR TEAM 1"); 
				roundTeam1SpellsWriter.write(line + "\n"); 
				System.out.println("Extracting member_no information"); 
				if(line.contains("MEMBER")) {
					int memberCharLoc = line.indexOf("M");
					int stringNoLoc = line.indexOf(" ", memberCharLoc) + 1; 
					int memberNoLoc = Integer.parseInt(line.substring(stringNoLoc, stringNoLoc+1)); 
					System.out.println("memberNoLoc index: " + memberNoLoc); 
					RoundTeam1MemberSpellsWriter.file_writers[memberNoLoc-1].write(line + "\n"); 
				}
			}
			if(readSecondTeam == true) {
				System.out.println("WRITING LINE: " + line + " FOR TEAM 2");
				roundTeam2SpellsWriter.write(line + "\n"); 
				System.out.println("Extracting member_no information"); 
				if(line.contains("MEMBER")) {
					int memberCharLoc = line.indexOf("M"); 
					int stringNoLoc = line.indexOf(" ", memberCharLoc) + 1; 
					int memberNoLoc = Integer.parseInt(line.substring(stringNoLoc, stringNoLoc+1)); 
					System.out.println("memberNoLoc index: " + memberNoLoc); 
 					RoundTeam2MemberSpellsWriter.file_writers[memberNoLoc-1].write(line + "\n");
				}
			}
		}
		roundTeam1SpellsWriter.close(); 
		try {
			RoundTeam1MemberSpellsWriter.file_writers[0].close(); 
			RoundTeam1MemberSpellsWriter.file_writers[1].close(); 
			RoundTeam1MemberSpellsWriter.file_writers[2].close(); 
			RoundTeam1MemberSpellsWriter.file_writers[3].close(); 
		} catch (Exception e) {
			System.out.println("A member writer for team 1 failed to close"); ; 
		}
		roundTeam2SpellsWriter.close();  
		try {
			RoundTeam2MemberSpellsWriter.file_writers[0].close(); 
			RoundTeam2MemberSpellsWriter.file_writers[1].close(); 
			RoundTeam2MemberSpellsWriter.file_writers[2].close(); 
			RoundTeam2MemberSpellsWriter.file_writers[3].close(); 
		} catch (Exception e) {
			System.out.println("A member writer for team 2 failed to close"); 
		}
		System.out.println("Creating two readers for selection_line of team 1 and team 2 round of spells."); 
		BufferedReader readerTeam1Compute = null;
		BufferedReader readerTeam2Compute = null;
		try {
			readerTeam1Compute = new BufferedReader(RoundSelectionLineReader.get_file_reader("t1", round)); 
			readerTeam1Compute.readLine(); 
			System.out.println("Line Read By Team 1 Reader: " + line); 
			readerTeam2Compute = new BufferedReader(RoundSelectionLineReader.get_file_reader("t2", round)); 
			readerTeam2Compute.readLine(); 
			System.out.println("Line Read By Team 2 Reader: " + line); 
		} catch (Exception e) {
			RoundSelectionLineReader.setReaderCreated(false);
			readerTeam1Compute = new BufferedReader(RoundSelectionLineReader.get_file_reader("t1", round)); 
			RoundSelectionLineReader.setReaderCreated(false); 
			readerTeam2Compute = new BufferedReader(RoundSelectionLineReader.get_file_reader("t2", round)); 
		}
		new FileOperation(readerTeam1Compute, roundSelectionLineTeam1Writer, roundExcessSpellsTeam1Writer, roundRemainingTeam1SpellsWriter, roundCombineWriter, new FileReader(Match.initialFile), "t1", round, selectionStmtLine_t1, discardStmtLine_t1, remainingStmtLine_t1).run(); 
		//Thread.sleep(30000);
		new FileOperation(readerTeam2Compute, roundSelectionLineTeam2Writer, roundExcessSpellsTeam2Writer, roundRemainingTeam2SpellsWriter, roundCombineWriter, new FileReader(Match.initialFile), "t2", round, selectionStmtLine_t2, discardStmtLine_t2, remainingStmtLine_t2).run(); 
		//Thread.sleep(30000); 
		roundComputeReader.close(); 
		readerTeam1Compute.close(); 
		readerTeam2Compute.close(); 
		roundSelectionLineWriter.close();
		roundSelectionLineTeam1Writer.close(); 
		roundSelectionLineTeam2Writer.close(); 
		roundExcessSpellsWriter.close(); 
		roundExcessSpellsTeam1Writer.close(); 
		roundExcessSpellsTeam2Writer.close(); 
		roundRemainingSpellsWriter.close(); 
		roundRemainingTeam1SpellsWriter.close(); 
		roundRemainingTeam2SpellsWriter.close(); 
		roundCombineWriter.close(); 
}
	
private static Element[] generateSevenCards(String school, int index) {
	Element[] sevenCards = new Element[7]; 

	System.out.println("School: " + school); 
	System.out.println("Index: " + index); 

	int number = 0; 
	while (number < 7) {
		int randomIndex = (int) (Math.random() * decks.get(school).get(index).size());

		if(!(decks.get(school).get(index).get(randomIndex).getSpellName().equals("X"))) {
			// Figure out a way to change the element retrieved 
			sevenCards[number] = decks.get(school).get(index).get(randomIndex); 
			number++; 
		}
	}

	return sevenCards;
}


public String randomizeHeadsOrTails()
{
	String[] inputs = {"Heads", "Tails"}; 
		
	int number = (int) ( (Math.random() * team.length) + 1);
		
	return inputs[number]; 
}
	
	
	public boolean validatePlayer(String playerName, int startIndex)
	{
		if(playerName instanceof String)
		{
			boolean check = false; 
			for(int i = 0; i < team.length; i++)
			{
				if(playerName.equals(team[i]))
				{
					team[startIndex] = ""; 
					check = true; 
					break;
				}
			}
			return true; 
		}
		return false; 
	}
	
	public String computePlayerInformation(String wizard, String school, int level, HashMap<Integer, List<String>> keywords, int count) throws IOException
	{
		System.out.println("Inside computePlayerInformation Method."); 

		StringBuilder gearType1 = new StringBuilder(""); 
		StringBuilder gearType2 = new StringBuilder(""); 
		StringBuilder gearType3 = new StringBuilder(""); 
		StringBuilder gearType4 = new StringBuilder(""); 
		StringBuilder gearType5 = new StringBuilder(""); 
		StringBuilder gearType6 = new StringBuilder(""); 
		StringBuilder gearType7 = new StringBuilder(""); 
		StringBuilder gearType8 = new StringBuilder(""); 

		String[] listGear = {"hat", "robe", "boot", "wand", "athame", "amulet", "ring", "deck", "pet"};  

		Hat hat = (Hat)instantiateGearPiece(listGear[0], school, level, gearType1); 
		System.out.println("Hat gearType: " + gearType1);  
		Robe robe = (Robe)instantiateGearPiece(listGear[1], school, level, gearType2); 
		System.out.println("Robe gearType: " + gearType2); 
		Boot boot = (Boot)instantiateGearPiece(listGear[2], school, level, gearType3);
		System.out.println("Boot gearType: " + gearType3); 
		Wand wand = (Wand)instantiateGearPiece(listGear[3], school, level, gearType4); 
		System.out.println("Wand gearType: " + gearType4); 
		Athame athame = (Athame)instantiateGearPiece(listGear[4], school, level, gearType5);
		System.out.println("Athame gearType: " + gearType5); 
		Amulet amulet = (Amulet)instantiateGearPiece(listGear[5], school, level, gearType6); 
		System.out.println("Amulet gearType: " + gearType6); 
		Ring ring = (Ring)instantiateGearPiece(listGear[6], school, level, gearType7); 
		System.out.println("Ring gearType: " + gearType7); 
		
		Deck deck = (Deck)instantiateGearPiece(listGear[7], school, level, gearType8); 
		System.out.println("Deck gearType: " + gearType8); 
		Pet pet = (Pet)instantiateGearPiece(listGear[8], school, level, gearType8);
		
		keywords.put(count, Arrays.asList(gearType1.toString(), gearType2.toString(), gearType3.toString(), gearType4.toString(), gearType5.toString(), gearType6.toString(), gearType7.toString(), gearType8.toString())); 

		Gear gear = new Gear(hat, robe, boot, wand, athame, amulet, ring, deck, pet); 

		System.out.println("The gear information for player "  + wizard + " is now completed."); 
		System.out.println(gear.toString()); 

		return gear.toString();  
	}

	public boolean verifyHealthRequirements(int health, int level)
	{
		return true; 
	}

	public void computeStatsInformation(HashMap<String, List<String>> gearSets, HashMap<Integer, List<String>> keywords) throws IOException
	{
		System.out.println("Before the loop.");
		String[] listItems = {"hat", "robe", "boots", "wand", "athame", "amulet", "ring", "deck"};

		Iterator<Integer> numberIterator = keywords.keySet().iterator(); 
		Iterator<String> wizardIterator = gearSets.keySet().iterator(); 
		int number = 0; 
		String wizard = ""; 

		while(numberIterator.hasNext() && wizardIterator.hasNext())
		{
				number = numberIterator.next();
				wizard = wizardIterator.next(); 
				String word1 = keywords.get(number).get(0); 
				String gearName1 = gearSets.get(wizard).get(0); 
				String word2 = keywords.get(number).get(1); 
				String gearName2 = gearSets.get(wizard).get(1); 
				String word3 = keywords.get(number).get(2); 
				String gearName3 = gearSets.get(wizard).get(2); 
				String word4 = keywords.get(number).get(3); 
				String gearName4 = gearSets.get(wizard).get(3); 
				String word5 = keywords.get(number).get(4); 
				String gearName5 = gearSets.get(wizard).get(4); 
				String word6 = keywords.get(number).get(5); 
				String gearName6 = gearSets.get(wizard).get(5); 
				String word7 = keywords.get(number).get(6); 
				String gearName7 = gearSets.get(wizard).get(6); 
				String word8 = keywords.get(number).get(7); 
				String gearName8 = gearSets.get(wizard).get(7); 

				findGearClass(word1, gearName1, listItems[0]); 
				findGearClass(word2, gearName2, listItems[1]); 
				findGearClass(word3, gearName3, listItems[2]); 
				findGearClass(word4, gearName4, listItems[3]); 
				findGearClass(word5, gearName5, listItems[4]); 
				findGearClass(word6, gearName6, listItems[5]); 
				findGearClass(word7, gearName7, listItems[6]); 
				findGearClass(word8, gearName8, listItems[7]); 
		}
	}

	private void findGearClass(String word, String gearName, String pieceOfGear) throws IOException {
				if(word.toLowerCase().equals("aeon"))
				{
					new AeonClass(gearName, pieceOfGear); 
				}
				else if(word.toLowerCase().equals("eternal"))
				{
					new EternalClass(gearName, pieceOfGear); 
				}
				else if(word.toLowerCase().equals("dragoon"))
				{
					new DragoonClass(gearName, pieceOfGear); 
				}	
				else if(word.toLowerCase().equals("spooky"))
				{
					new SpookyClass(gearName, pieceOfGear); 
				}
				else if(word.toLowerCase().equals("night mire"))
				{
					new NightMireClass(gearName, pieceOfGear); 
				}
				else if(word.toLowerCase().equals("jade"))
				{
					new JadeClass(gearName, pieceOfGear);
				}
	}


	public String cutPartOfString(String str, String gearString)
	{
		int index = 0; 
		String createNewString = ""; 
		for(int i = 0; i < gearString.length(); i++)
		{
			if(index < str.length())
			{
				if(gearString.substring(i, i+1).equals(str.substring(index, index+1)))
				{
					createNewString += ""; 
				}
				index = index + 1; 
			}
			else 
			{
				createNewString += gearString.substring(i, i+1); 
			}
		}
		return createNewString; 
	}

	public Object instantiateGearPiece(String gearName, String school, int level, StringBuilder extractGearType) throws IOException
	{
		Option.scannerOrfileOption();

		String hatName = new String(); 
		String robeName = new String(); 
		String bootName = new String(); 
		String wandName = new String(); 
		String athameName = new String(); 
		String amuletName = new String(); 
		String ringName = new String(); 
		String deckName = new String(); 
		String petName = new String(); 

		final String temp; 

		String[] jadeGear = {"Sword Of Kings", "Stone Of The Other Side", "Pepper Grass Blade", "Celestian Sliver Of Power", "Ring Of The Dying Star"}; 
	
		System.out.println("Please try to space out each word to ensure name of gear inputted can be fixed if written incorrectly."); 
		System.out.println("If you happen to fail to follow these instructions, you will be redirected back to the same page."); 
		System.out.println("Thank you for this understanding."); 
		System.out.println("Enter the name of your " + gearName); 

		switch(gearName) 
		{
			case "hat": 
				if(Option.getScannerInUse() == true) {
					hatName = sc.nextLine();
					if(!(sc.hasNextLine()))
					{
						sc.close(); 
					}
				}
				else if(Option.getFileInUse() == true) {
					Match.getBufferReader().readLine();
					Match.getBufferReader().readLine(); 
					hatName = Match.getBufferReader().readLine(); 
					hatName = hatName.trim(); 
					System.out.println("Hat Name Read: " + hatName); 
				}
				temp = hatName;
				boolean isJadeGear = Arrays.stream(jadeGear).anyMatch(gear->gear.equals(temp)); 
				boolean check1 = hatName.contains("Aeon"); 
				boolean check2 = hatName.contains("Eternal"); 
				boolean check3 = hatName.contains("Dragoon"); 
				boolean check4 = hatName.contains("Spooky"); 
				boolean check5 = hatName.contains("Night Mire"); 
				if(isJadeGear == true)
				{
					extractGearType.append("jade"); 
				}
				else 
				{
					extractGearType = filterByKeyword(check1, check2, check3, check4, check5, hatName, extractGearType); 
					System.out.println("Gear Type here: " + extractGearType);
				}
				boolean res1 = checkGearName(hatName, gearName, extractGearType, school, level); 
				if(res1 == true)
				{
					Hat hat = new Hat(hatName); 
					System.out.println("Hat: " + hatName + " created."); 
					return hat; 
				}
				extractGearType = new StringBuilder(""); 
				Hat hat = (Hat)instantiateGearPiece(gearName, school, level, extractGearType);
				return hat;
			case "robe": 
				if(Option.getScannerInUse() == true) {
					robeName = sc.nextLine();
					if(!(sc.hasNextLine()))
					{
						sc.close(); 
					}
				}
				else {
					robeName = Match.getBufferReader().readLine(); 
					robeName = robeName.trim(); 
					System.out.println("Robe Name Read: " + robeName);  
				}
				temp = robeName; 
				isJadeGear = Arrays.stream(jadeGear).anyMatch(gear->gear.equals(temp)); 
				check1 = robeName.contains("Aeon"); 
				check2 = robeName.contains("Eternal"); 
				check3 = robeName.contains("Dragoon"); 
				check4 = robeName.contains("Spooky");
				check5 = robeName.contains("Night Mire"); 
				if(isJadeGear == true)
				{
					extractGearType.append("jade"); 
				}
				else 
				{
					extractGearType = filterByKeyword(check1, check2, check3, check4, check5, robeName, extractGearType); 
					System.out.println("Gear Type here: " + extractGearType);
				} 
				boolean res2 = checkGearName(robeName, gearName, extractGearType, school, level); 
				if(res2 == true)
				{
					Robe robe = new Robe(robeName); 
					System.out.println("Robe: " + robeName + " created."); 
					return robe;
				}
				extractGearType = new StringBuilder(""); 
				Robe robe = (Robe)instantiateGearPiece(gearName, school, level,  extractGearType);
				return robe;
			case "boot": 
				if(Option.getScannerInUse() == true) {
					bootName = sc.nextLine();
					if(!(sc.hasNextLine()))
					{
						sc.close(); 
					}
				}
				else {
					bootName = Match.getBufferReader().readLine();
					bootName = bootName.trim(); 
					System.out.println("Boot Name Read: " + bootName); 
				}
				temp = bootName;
				isJadeGear = Arrays.stream(jadeGear).anyMatch(gear->gear.equals(temp)); 
				check1 = bootName.contains("Aeon"); 
				check2 = bootName.contains("Eternal"); 
				check3 = bootName.contains("Dragoon"); 
				check4 = bootName.contains("Spooky"); 
				check5 = bootName.contains("Night Mire"); 
				if(isJadeGear == true)
				{
					extractGearType.append("jade");  
				}
				else 
				{
					extractGearType = filterByKeyword(check1, check2, check3, check4, check5, bootName, extractGearType); 
					System.out.println("Gear Type here: " + extractGearType);
				}
				boolean res3 = checkGearName(bootName, gearName, extractGearType, school, level); 
				if(res3 == true)
				{
					Boot boot = new Boot(bootName); 
					System.out.println("Boot: " + bootName + " created."); 
					return boot;
				}
				extractGearType = new StringBuilder(""); 
				Boot boot = (Boot)instantiateGearPiece(gearName, school, level, extractGearType);
				return boot;
			case "wand": 
				if(Option.getScannerInUse() == true) {
					wandName = sc.nextLine();
					if(!(sc.hasNextLine()))
					{
						sc.close(); 
					}
				}
				else {
					wandName = Match.getBufferReader().readLine(); 
					wandName = wandName.trim(); 
					System.out.println("Wand Name Read: " + wandName); 
				}
				temp = wandName; 
				isJadeGear = Arrays.stream(jadeGear).anyMatch(gear->gear.equals(temp)); 
				check1 = wandName.contains("Aeon"); 
				check2 = wandName.contains("Eternal"); 
				check3 = wandName.contains("Dragoon"); 
				check4 = wandName.contains("Spooky"); 
				check5 = wandName.contains("Night Mire"); 
				if(isJadeGear == true)
				{
					extractGearType.append("jade"); 
				}
				else 
				{
					extractGearType = filterByKeyword(check1, check2, check3, check4, check5, wandName, extractGearType); 
					System.out.println("Gear Type here: " + extractGearType);
				}
				boolean res4 = checkGearName(wandName, gearName, extractGearType, school, level); 
				if(res4 == true)
				{
					Wand wand = new Wand(wandName); 
					System.out.println("Boot: " + wandName + " created."); 
					return wand;
				}
				extractGearType = new StringBuilder(""); 
				Wand wand = (Wand)instantiateGearPiece(gearName, school, level, extractGearType);
				return wand;
			case "athame": 
				if(Option.getScannerInUse() == true) {
					athameName = sc.nextLine();
					if(!(sc.hasNextLine()))
					{
						sc.close(); 
					}
				}
				else {
					athameName = Match.getBufferReader().readLine();
					athameName = athameName.trim(); 
					System.out.println("Athame Name Read: " + athameName); 
				}
				temp = athameName; 
				isJadeGear = Arrays.stream(jadeGear).anyMatch(gear->gear.equals(temp)); 
				check1 = athameName.contains("Aeon"); 
				check2 = athameName.contains("Eternal"); 
				check3 = athameName.contains("Dragoon"); 
				check4 = athameName.contains("Spooky"); 
				check5 = athameName.contains("Night Mire"); 
				if(isJadeGear == true)
				{
					extractGearType.append("jade");  
				}
				else 
				{
					extractGearType = filterByKeyword(check1, check2, check3, check4, check5, athameName, extractGearType); 
					System.out.println("Gear Type here: " + extractGearType);
				}
				boolean res5 = checkGearName(athameName, gearName, extractGearType, school, level); 
				if(res5 == true)
				{
					Athame athame = new Athame(athameName); 
					System.out.println("Athame: " + athameName + " created."); 
					return athame;
				}
				extractGearType = new StringBuilder(""); 
				Athame athame = (Athame)instantiateGearPiece(gearName, school, level, extractGearType);
				return athame;
			case "amulet": 
				if(Option.getScannerInUse() == true) {
					amuletName = sc.nextLine();
					if(!(sc.hasNextLine()))
					{
						sc.close(); 
					}
				}
				else {
					amuletName = Match.getBufferReader().readLine(); 
					amuletName = amuletName.trim(); 
					System.out.println("Amulet Name Read: " + amuletName); 
				}
				temp = amuletName;
				isJadeGear = Arrays.stream(jadeGear).anyMatch(gear->gear.equals(temp)); 
				check1 = amuletName.contains("Aeon"); 
				check2 = amuletName.contains("Eternal"); 
				check3 = amuletName.contains("Dragoon"); 
				check4 = amuletName.contains("Spooky"); 
				check5 = amuletName.contains("Night Mire"); 
				if(isJadeGear == true)
				{
					extractGearType.append("jade"); 
				}
				else 
				{
					extractGearType = filterByKeyword(check1, check2, check3, check4, check5, amuletName, extractGearType); 
					System.out.println("Gear Type here: " + extractGearType);
				}
				boolean res6 = checkGearName(amuletName, gearName, extractGearType, school, level); 
				if(res6 == true)
				{
					Amulet amulet = new Amulet(amuletName); 
					System.out.println("Amulet: " + amuletName + " created."); 
					return amulet;
				}
				extractGearType = new StringBuilder(""); 
				Amulet amulet = (Amulet)instantiateGearPiece(gearName, school, level, extractGearType);
				return amulet;
			case "ring": 
				if(Option.getScannerInUse() == true) {
					ringName = sc.nextLine();
					if(!(sc.hasNextLine()))
					{
						sc.close(); 
					}
				}
				else {
					ringName = Match.getBufferReader().readLine(); 
					ringName = ringName.trim(); 
					System.out.println("Ring Name: " + ringName);
				}
				temp = ringName;
				isJadeGear = Arrays.stream(jadeGear).anyMatch(gear->gear.equals(temp)); 
				check1 = ringName.contains("Aeon"); 
				check2 = ringName.contains("Eternal"); 
				check3 = ringName.contains("Dragoon"); 
				check4 = ringName.contains("Spooky"); 
				check5 = ringName.contains("Night Mire"); 
				if(isJadeGear == true)
				{
					extractGearType.append("jade");  
				}
				else 
				{
					extractGearType = filterByKeyword(check1, check2, check3, check4, check5, ringName, extractGearType); 
					System.out.println("Gear Type here: " + extractGearType);
				}
				boolean res7 = checkGearName(ringName, gearName, extractGearType, school, level); 
				if(res7 == true)
				{
					Ring ring = new Ring(ringName); 
					System.out.println("Ring: " + ringName + " created.");
					return ring;
				}
				extractGearType = new StringBuilder(""); 
				Ring ring = (Ring)instantiateGearPiece(gearName, school, level, extractGearType);
				return ring;
			case "deck": 
				if(Option.getScannerInUse() == true) {
					deckName = sc.nextLine(); 
					if(!(sc.hasNextLine()))
					{
						sc.close(); 
					}
				}
				else {
					deckName = Match.getBufferReader().readLine(); 
					deckName = deckName.trim(); 
					System.out.println("Deck Name Read: " + deckName); 
				}
				temp = deckName;
				isJadeGear = Arrays.stream(jadeGear).anyMatch(gear->gear.equals(temp)); 
				check1 = deckName.contains("Aeon"); 
				check2 = deckName.contains("Eternal"); 
				check3 = deckName.contains("Dragoon"); 
				check4 = deckName.contains("Spooky"); 
				check5 = deckName.contains("Night Mire"); 
				if(isJadeGear == true)
				{
					extractGearType.append("jade"); 
				}
				else 
				{
					extractGearType = filterByKeyword(check1, check2, check3, check4, check5, deckName, extractGearType); 
					System.out.println("Gear Type here: " + extractGearType);
				}
				boolean res8 = checkGearName(deckName, gearName, extractGearType, school, level);
				if(res8 == true)
				{
					Deck deck = new Deck(deckName); 
					System.out.println("Deck: " + deckName + " created.");  
					return deck;
				}
				extractGearType = new StringBuilder(""); 
				Deck deck = (Deck)instantiateGearPiece(gearName, school, level, extractGearType);
				return deck;
			case "pet": 
				if(Option.getScannerInUse() == true) {
					petName = sc.nextLine(); 
					if(!(sc.hasNextLine()))
					{
						sc.close(); 
					}
				}
				else {
					petName = Match.getBufferReader().readLine(); 
					petName = petName.trim(); 
					System.out.println("Pet Name Read: " + petName); 
				}
				boolean res9 = checkGearName(petName, gearName, null, school, level); 
				if(res9 == true)
				{
					Pet pet = new Pet(petName); 
					System.out.println("Pet: " + petName + " created."); 
					return pet; 
				}
				Pet pet = (Pet)instantiateGearPiece(gearName, school, level, extractGearType); 
				return pet; 
		}
		sc.close();
		TypeException ex = new TypeException();
		ex.message(gearName); 
		return null;
	}

	public boolean checkGearName(String gearName, String pieceOfGear, StringBuilder gearType, String school, int level) throws IOException
	{
		// Open connection to database 
		try {
			Connection conn1 = null; 
			String url1 = "jdbc:mysql://localhost:3306/wizard_schema";
			String user = "srik6724";
			String password = "28892K0shair!"; 

			if (WizCredentials.authenticate(user, password)) {
				System.out.println("Authentication successful");
			} else {
				System.out.println("Authentication failed");
			}

			conn1 = DriverManager.getConnection(url1, user, password); 

			if(conn1 != null) 
			{
				if(pieceOfGear.equals("pet"))
				{
					Option.scannerOrfileOption();
					Scanner sc = new Scanner(System.in); 
					String petType = new String(); 
					System.out.println("Enter the type of your pet here. Make sure to spell it correctly. Otherwise, an error will occur."); 
					if(Option.getScannerInUse() == true) {
						petType = sc.nextLine(); 
						if(!(sc.hasNextLine()))
						{
							sc.close();
						}		
					}
					else {
						petType = Match.getBufferReader().readLine(); 
						System.out.println("Pet Type Read: " + petType);
						Match.getBufferReader().readLine(); 
					}
					//Create a sql string
					ResultSet rs = null;
					try {
						String sqlToExecute = "SELECT typeName, school FROM wizard_schema.pets WHERE typename = ?";
						//Execute the sql string above, but create a statement first.
						PreparedStatement createStatement = conn1.prepareStatement(sqlToExecute); 
						createStatement.setString(1, petType.trim()); 
						//Store the result inside a result set to access the database column's data
						rs = createStatement.executeQuery();  
					} catch(SQLException e) {
						e.printStackTrace();
					}

					boolean iterate = false; 
					
					while(rs.next())
					{
						//Retrieve the typename of the pet
						String typeName = rs.getString("typeName"); 
						System.out.println("Type Name: " + typeName);
						if(petType.trim().equals(typeName.trim()))
						{
							Pet.typeName = typeName.trim(); 
						}
						else  
						{
							System.out.println("Sorry name in database for pet type: " + typeName + " does not match " + petType);
							break;
						}
						//Retrieve the school of the pet 
						String schoolOfPet = rs.getString("school"); 
						System.out.println("School Of Pet: " + schoolOfPet); 
						Pet.school = schoolOfPet; 

						iterate = true; 
					}
					conn1.close(); 
					System.out.println("Iterate bool value: " + iterate); 
					return iterate; 
				}
				else 
				{
					CallableStatement stmt = null;
					System.out.println(gearType); 
					if(gearType.toString().toLowerCase().equals("aeon"))
					{
						stmt = (CallableStatement) conn1.prepareCall("{call extractgear(?,?,?,?,?)}"); 
					}
					else if(gearType.toString().toLowerCase().equals("eternal"))
					{
						stmt = (CallableStatement) conn1.prepareCall("{call extractEternalGear(?,?,?,?,?)}"); 
					}
					else if(gearType.toString().toLowerCase().equals("dragoon"))
					{
						stmt = (CallableStatement) conn1.prepareCall("{call extractDragoonGear(?,?,?,?,?)}"); 
						level = 130; 
					}
					else if(gearType.toString().toLowerCase().equals("spooky"))
					{
						stmt = (CallableStatement) conn1.prepareCall("{call extractSpookyGear(?,?,?,?,?)}"); 
						level = 130; 
					}
					else if(gearType.toString().toLowerCase().equals("night mire"))
					{
						stmt = (CallableStatement) conn1.prepareCall("{call extractNightMireGear(?,?,?,?,?)}"); 
						level = 160;
					}
					else if(gearType.toString().toLowerCase().equals("jade"))
					{
						stmt = (CallableStatement) conn1.prepareCall("{call extractJadeGear(?,?,?,?,?)}"); 
						if(gearName.equals("Stone Of The Other Side") || gearName.equals("Pepper Grass Blade") || gearName.equals("Celestian Sliver Of Power") || gearName.equals("Ring Of The Dying Star"))
						{
							school = "Any";
						}
						level = 160; 
					}
					System.out.println(school); 
					System.out.println(pieceOfGear); 
					System.out.println(String.valueOf(level)); 
					System.out.println(gearName); 
					stmt.setString(1, gearName); 
			 		stmt.setString(2, school); 
					stmt.setString(3, pieceOfGear);
					stmt.setString(4, String.valueOf(level)); 
					stmt.registerOutParameter(5, Types.VARCHAR); 

					stmt.execute(); 
				
					String databaseGearName = stmt.getString(5); 

					if(databaseGearName == null)
					{
						System.out.println("Gear Name entered " + gearName + " does not match " + "database gearName " + databaseGearName + " of given school " + school); 
						conn1.close(); 
						return false;
					}

			 		if(gearName.equals(databaseGearName))
					{
						conn1.close(); 
						return true; 
					}
				}
			}
			return false; 
		}
		catch(SQLException e)
		{
			System.out.println("Invalid username/password"); 
			return false;
		}
	}

	public boolean searchInGearSets(HashMap<String, List<String>> gearSets, String person)
	{
		System.out.println(gearSets.size()); 
		System.out.println(person); 
		boolean var = false;
		for(String wizard: gearSets.keySet())
		{
			System.out.println("Wizard: " + wizard);
			if(wizard.equals(person))
			{
				var = true; 
				break;
			}
			else 
			{
				var = false; 
			}
		}
		return var; 
	}

	public StringBuilder filterByKeyword(boolean check1, boolean check2, boolean check3, boolean check4, boolean check5, String name, StringBuilder extractGearType)
	{
		String toCheckfor = ""; 
		String stopCharacter = ""; 
		if(check1 == true)
				{
					toCheckfor = "Aeon"; 
					stopCharacter = "n"; 
				}
				else if(check2 == true)
				{
					toCheckfor = "Eternal"; 
					stopCharacter = "l"; 
				}
				else if(check3 == true)
				{
					toCheckfor = "Dragoon"; 
					stopCharacter = "n"; 
				}
				else if(check4 == true)
				{
					toCheckfor = "Spooky"; 
					stopCharacter = "y"; 
				}
				else if(check5 == true)
				{
					toCheckfor = "Night Mire"; 
					stopCharacter = "e"; 
				}
				int findWordIndex = name.indexOf(toCheckfor); 
				if(findWordIndex != -1)
				{
					int findNextSpace = name.indexOf(stopCharacter, findWordIndex); 
					if(findNextSpace != -1)
					{
						extractGearType.append(name.substring(findWordIndex, findNextSpace+1)); 
						return extractGearType;
					}
					return null;
				}
				System.out.println(extractGearType); 
				return null;
	}
}