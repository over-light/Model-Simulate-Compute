package Match_Threading;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import Match_Documentation.MessageDirectory;
import Match_Process.Match;
import Match_Process.MatchWriter;

public class Team1Runnable implements Runnable {
  protected static boolean messagesCompleted; 
  protected static boolean team1Done = false; 

  public void run()
  {
    System.out.println("Accessing Team 1's run method."); 
    System.out.println("This will be the first team's thread in the 4v4 match."); 

    if(Team1Runnable.messagesCompleted == false)
    {
      new MessageDirectory(); 
    }
    if(Team1Runnable.messagesCompleted == true)
    {
      System.out.println("Starting round for team 1."); 
      try {
        FileWriter matchWriterFinalizer = MatchWriter.get_file_writer(); 
        FileReader roundReading = new FileReader("round_reading.txt");
        int roundNumber = 76; 
        for(int i = 0; i < roundNumber; i++) {
          Match.startRound(0, matchWriterFinalizer, roundReading);
        }
        roundReading.close(); 
        matchWriterFinalizer.close(); 
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }

  public static void main(String[]args)
  {
    new Thread(new Team1Runnable()).start(); 
  }
}
