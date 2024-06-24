package Domain_Process;

import java.io.FileWriter;
import java.io.IOException;

interface Module_Interpretation_List {
  static FileWriter getFileWriter(String team, int round) throws IOException {
    return new FileWriter("module_" + round + "_interpretations_" + team + ".compute.txt"); 
  }

  static FileWriter getFileWriter(String team, int memberNo, int round) throws IOException {
    return new FileWriter("module_" + round + "_interpretations_" + team + "_member_" + memberNo + ".compute.txt"); 
  }
}
