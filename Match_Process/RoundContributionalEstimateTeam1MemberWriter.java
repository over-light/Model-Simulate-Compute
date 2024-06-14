package Match_Process;

import java.io.FileWriter;
import java.io.IOException;

class RoundContributionalEstimateTeam1MemberWriter {
  private static boolean writerCreated = false; 
  static FileWriter[] file_writers = new FileWriter[4]; 

  RoundContributionalEstimateTeam1MemberWriter(String team, int memberNo, int round) {
    try {
      file_writers[memberNo-1] = Round_State.getFileWriter(team, memberNo, round); 
      RoundContributionalEstimateTeam1MemberWriter.writerCreated = true; 
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static void get_file_writer(int round, int memberNo) {
    if(RoundContributionalEstimateTeam1MemberWriter.writerCreated == false) {
      new RoundContributionalEstimateTeam1MemberWriter("t1", memberNo, round); 
    }
  }

  static void setWriterCreated(boolean var) {
    RoundContributionalEstimateTeam1MemberWriter.writerCreated = var; 
  }
}
