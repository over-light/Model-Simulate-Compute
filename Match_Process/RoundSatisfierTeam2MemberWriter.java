package Match_Process;

import java.io.FileWriter;
import java.io.IOException;

class RoundSatisfierTeam2MemberWriter {
  private static boolean writerCreated = false; 
  static FileWriter[] file_writers = new FileWriter[4]; 

  RoundSatisfierTeam2MemberWriter(String team, int memberNo, int round) {
    try {
      file_writers[memberNo-1] = Round_Solution.getFileWriter(team, memberNo, round); 
      RoundSatisfierTeam2MemberWriter.writerCreated = true; 
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static void get_file_writer(int round, int memberNo) {
    if(RoundSatisfierTeam2MemberWriter.writerCreated == true) {
      System.out.println("Calling round_satisfier constructor"); 
      new RoundSatisfierTeam2MemberWriter("t2", memberNo, round); 
    }
  }

  static void setWriterCreated(boolean var) {
    RoundSatisfierTeam2MemberWriter.writerCreated = true; 
  }
}
