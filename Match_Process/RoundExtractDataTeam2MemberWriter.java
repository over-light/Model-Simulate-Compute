package Match_Process;

import java.io.FileWriter;
import java.io.IOException;

class RoundExtractDataTeam2MemberWriter {
  private static boolean writerCreated = false; 
  static FileWriter[] file_writers = new FileWriter[4]; 

  RoundExtractDataTeam2MemberWriter(String team, int memberNo, int round) {
    try {
      file_writers[memberNo-1] = Round_Statistics.getFileWriter(team, memberNo, round); 
      RoundExtractDataTeam2MemberWriter.writerCreated = true; 
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static void get_file_writer(int round, int memberNo) {
    if(RoundExtractDataTeam2MemberWriter.writerCreated == false) {
      new RoundExtractDataTeam2MemberWriter("t2", memberNo, round); 
    }
  }

  static void setWriterCreated(boolean var) {
    RoundExtractDataTeam2MemberWriter.writerCreated = var; 
  }
}
