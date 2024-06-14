package Match_Process;

import java.io.FileWriter;
import java.io.IOException;

class RoundRunTimeTeam1MemberWriter {
  private static boolean writerCreated = false; 
  static FileWriter[] file_writers = new FileWriter[4]; 

  RoundRunTimeTeam1MemberWriter(String team, int memberNo, int round) {
    try {
      file_writers[memberNo-1] = Round_Process.getFileWriter(team, memberNo, round); 
      RoundRunTimeTeam1MemberWriter.writerCreated = true; 
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static void get_file_writer(int round, int memberNo) {
    if(RoundRunTimeTeam1MemberWriter.writerCreated == true) {
      System.out.println("Calling round_run_time constructor"); 
      new RoundRunTimeTeam1MemberWriter("t1", memberNo, round); 
    }
  }

  static void setWriterCreated(boolean var) {
    RoundRunTimeTeam1MemberWriter.writerCreated = true; 
  }
}
