package Match_Process;

import java.io.FileWriter;
import java.io.IOException;

interface Match_Transducer {
  static FileWriter getFileWriter(String team) throws IOException {
    return new FileWriter("match_transducer_" + team + "_compute.txt");
  }
}