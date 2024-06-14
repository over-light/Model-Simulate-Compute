package Match_Process;

import java.io.FileWriter;
import java.io.IOException;

class RoundPrGraphTeam1Writer {
  private static boolean writerCreated = false; 
  private static FileWriter file_writer; 
  

  RoundPrGraphTeam1Writer(int round, String team) {
    try {
      file_writer = Round_pyreason_graph.getFileWriter(team, round);
      RoundPrGraphTeam1Writer.writerCreated = true; 
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static FileWriter get_file_writer(int round) {
    if(RoundPrGraphTeam1Writer.writerCreated == false) {
      System.out.println("Calling round_pr_graph constructor."); 
      new RoundPrGraphTeam1Writer(round, "t1"); 
    }
    return file_writer;
  }

  static void setWriterCreated(boolean var) {
    RoundPrGraphTeam1Writer.writerCreated = var; 
  }
}
