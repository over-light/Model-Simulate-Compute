package Match_Process;

import java.io.FileWriter;
import java.io.IOException;

class RoundDFATeam2MemberWriter {
  private static boolean writerCreated = false; 
  static FileWriter[] file_writers = new FileWriter[4];

  RoundDFATeam2MemberWriter(String team, int memberNo, int round) {
    try {
      file_writers[memberNo-1] = Round_DFA.getFileWriter(team, memberNo, round);
      RoundDFATeam2MemberWriter.writerCreated = true; 
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static void get_file_writer(int round, int memberNo) {
    if(RoundDFATeam2MemberWriter.writerCreated == false) {
      System.out.println("Calling round_combine constructor");
      new RoundDFATeam2MemberWriter("t2",  memberNo, round);
    }
  }

  static void setWriterCreated(boolean var) {
    RoundDFATeam2MemberWriter.writerCreated = var; 
  }
}
