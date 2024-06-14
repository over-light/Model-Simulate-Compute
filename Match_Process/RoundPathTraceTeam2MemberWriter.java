package Match_Process;

import java.io.FileWriter;
import java.io.IOException;

class RoundPathTraceTeam2MemberWriter {
  private static boolean writerCreated = false; 
  static FileWriter[] file_writers = new FileWriter[4]; 

  RoundPathTraceTeam2MemberWriter(String team, int memberNo, int round) {
    try {
      file_writers[memberNo-1] = Round_Transducer.getFileWriter(team, memberNo, round); 
      RoundPathTraceTeam2MemberWriter.writerCreated = true; 
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static void get_file_writer(int round, int memberNo) {
    if(RoundPathTraceTeam2MemberWriter.writerCreated == false) {
      new RoundPathTraceTeam2MemberWriter("t2", memberNo, round); 
    }
  }

  static void setWriterCreated(boolean var) {
    RoundPathTraceTeam2MemberWriter.writerCreated = var; 
  }
}
