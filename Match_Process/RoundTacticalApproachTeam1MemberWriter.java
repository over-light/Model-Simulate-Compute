package Match_Process;

import java.io.FileWriter;
import java.io.IOException;

class RoundTacticalApproachTeam1MemberWriter {
  private static boolean writerCreated = false; 
  static FileWriter[] file_writers = new FileWriter[4]; 

  RoundTacticalApproachTeam1MemberWriter(String team, int memberNo, int round) {
    try {
      file_writers[memberNo-1] = Round_Strategy.getFileWriter(team, memberNo, round);
      RoundTacticalApproachTeam1MemberWriter.writerCreated = true; 
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static void get_file_writer(int round, int memberNo) {
    if(RoundTacticalApproachTeam1MemberWriter.writerCreated == false) {
      new RoundTacticalApproachTeam1MemberWriter("t1", memberNo, round); 
    }
  }

  static void setWriterCreated(boolean var) {
    RoundTacticalApproachTeam1MemberWriter.writerCreated = var; 
  }
}
