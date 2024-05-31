package wizPackage;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

class FileOperation {

  private BufferedReader reader; 
  private FileWriter writer1;
  private FileWriter writer2; 
  private FileWriter writer3;  
  private FileWriter writerTemp;
  private FileReader reader1;
  private String label;
  private int round;
  private String line1; 
  private String line2; 
  private String line3;

  FileOperation(BufferedReader reader, FileWriter selectionWriter, FileWriter excessWriter, FileWriter remainsWriter, FileWriter combineWriter, FileReader processReader, String teamLabel, int round, String selectionStart, String discardStart, String remainingStart) throws IOException {
    this.reader = reader; 
    this.writer1 = selectionWriter; 
    this.writer2 = excessWriter; 
    this.writer3 = remainsWriter; 
    this.reader1 = processReader;
    this.writerTemp = combineWriter;
    this.label = teamLabel;
    this.round = round;
    this.line1 = selectionStart;
    this.line2 = discardStart;
    this.line3 = remainingStart;
    System.out.println("Calling file operation for a task.");
    System.out.println("Cross checking team-label: " + this.label); 
  }

  public void run() throws IOException {
    System.out.println("Read full-card-list or card-by-card for round?"); ; 
    BufferedReader round_reader = new BufferedReader(reader1); 
    String input = round_reader.readLine(); 
    round_reader.close(); 
    /*System.out.println("Line Read: " + reader.readLine()); 
    Scanner sc = new Scanner(System.in); 
    String input = sc.nextLine(); */
    if(input.equals("full-card-list")) {
      try {
        String line; 
        int number = 0;
        while((line = reader.readLine()) != null) {
          System.out.println(line); 
          if(line.contains("Card")) {
            JFrame frame = new JFrame("Card " + ++number + " display for " + label); 
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            System.out.println("Presenting card definitions on frame for better reference."); 
            frame.setLayout(new GridLayout(5, 1)); 
            final String nameOfSpell = reader.readLine(); 
            JLabel label_1 = new JLabel(nameOfSpell);
            JLabel new_line_1 = new JLabel("\n"); 
            final String pipsOfSpell = reader.readLine(); 
            JLabel label_2 = new JLabel(pipsOfSpell);
            JLabel new_line_2 = new JLabel("\n"); 
            final String pipChanceOfSpell = reader.readLine(); 
            JLabel label_3 = new JLabel(pipChanceOfSpell); 
            JLabel new_line_3 = new JLabel("\n"); 
            final String countOfSpell = reader.readLine();
            JLabel label_4 = new JLabel(countOfSpell); 
            JLabel new_line_4 = new JLabel("\n"); 
            final String descriptionOfSpell = reader.readLine();
            JLabel label_5 = new JLabel(descriptionOfSpell);  
            JLabel new_line_5 = new JLabel("\n"); 
            JCheckBox checkBox1 = new JCheckBox("Select Spell"); 
            JCheckBox checkBox2 = new JCheckBox("Discard Spell"); 

            ItemListener itemListener = new ItemListener() {
              @Override
              public void itemStateChanged(ItemEvent e) {
                  if (e.getSource() == checkBox1 && checkBox1.isSelected()) {
                      checkBox2.setSelected(false);
                  } else if (e.getSource() == checkBox2 && checkBox2.isSelected()) {
                      checkBox1.setSelected(false);
                  }
              }
          };
          
          checkBox1.addItemListener(itemListener);
          checkBox2.addItemListener(itemListener);
            
          frame.getContentPane().add(label_1);
          frame.getContentPane().add(new_line_1); 
          frame.getContentPane().add(label_2);
          frame.getContentPane().add(new_line_2); 
          frame.getContentPane().add(label_3); 
          frame.getContentPane().add(new_line_3); 
          frame.getContentPane().add(label_4); 
          frame.getContentPane().add(new_line_4); 
          frame.getContentPane().add(label_5); 
          frame.getContentPane().add(new_line_5); 
          frame.getContentPane().add(checkBox1); 
          frame.getContentPane().add(checkBox2);

          frame.addWindowListener(new WindowAdapter() {
            @Override 
            public void windowClosing(WindowEvent e) {
              if(checkBox1.isSelected() && !checkBox2.isSelected()) {
                System.out.println("Check-box selected on window-closing."); 
                try {
                  writer1.write(line1 + "\n");
                  writerTemp.write(line1 + "\n");
                  writer1.write("----------------------------\n");
                  writerTemp.write("----------------------------\n");
                  writer1.write(nameOfSpell + "\n"); 
                  writerTemp.write(nameOfSpell + "\n"); 
                  writer1.write(pipsOfSpell + "\n");
                  writerTemp.write(pipsOfSpell + "\n"); 
                  writer1.write(pipChanceOfSpell + "\n");
                  writerTemp.write(pipChanceOfSpell + "\n");
                  writer1.write(countOfSpell + "\n");
                  writerTemp.write(countOfSpell + "\n"); 
                  writer1.write(descriptionOfSpell + "\n");
                  writerTemp.write(descriptionOfSpell + "\n");
                  writer1.write("----------------------------\n");
                  writerTemp.write("----------------------------\n");
                  writer1.close(); 
                } catch (IOException e1) {
                  try {
                    if(label.equals("t1")) {
                      RoundSelectionLineTeam1Writer.setWriterCreated(false);
                      writer1 = RoundSelectionLineTeam1Writer.get_file_writer(round);
                      //writer1.write(line1 + "\n"); 
                      //writerTemp.write(line1 + "\n");
                      writer1.write("----------------------------\n");
                      writerTemp.write("----------------------------\n");
                      writer1.write(nameOfSpell + "\n"); 
                      writerTemp.write(nameOfSpell + "\n");
                      writer1.write(pipsOfSpell + "\n");
                      writerTemp.write(pipsOfSpell + "\n");
                      writer1.write(pipChanceOfSpell + "\n");
                      writerTemp.write(pipChanceOfSpell + "\n");
                      writer1.write(countOfSpell  + "\n");
                      writerTemp.write(countOfSpell + "\n");
                      writer1.write(descriptionOfSpell + "\n");
                      writerTemp.write(descriptionOfSpell + "\n");
                      writer1.write("----------------------------\n");
                      writerTemp.write("----------------------------\n");
                      writer1.close(); 
                    }
                    else if(label.equals("t2")) {
                      RoundSelectionLineTeam2Writer.setWriterCreated(false);
                      writer1 = RoundSelectionLineTeam2Writer.get_file_writer(round);
                      //writer1.write(line1 + "\n");
                      //writerTemp.write(line1 + "\n");
                      writer1.write("----------------------------\n");
                      writerTemp.write("----------------------------\n");
                      writer1.write(nameOfSpell + "\n"); 
                      writerTemp.write(nameOfSpell + "\n");
                      writer1.write(pipsOfSpell + "\n");
                      writerTemp.write(pipsOfSpell + "\n");
                      writer1.write(pipChanceOfSpell + "\n");
                      writerTemp.write(pipChanceOfSpell + "\n");
                      writer1.write(countOfSpell + "\n");
                      writerTemp.write(countOfSpell + "\n");
                      writer1.write(descriptionOfSpell + "\n");
                      writerTemp.write(descriptionOfSpell + "\n");
                      writer1.write("----------------------------\n");
                      writerTemp.write("----------------------------\n");
                      writer1.close(); 
                    }
                  } catch (IOException sube1) {
                    System.out.println("Will not reach unless problem reading file.");
                  }
                } 
              }
              else if(checkBox2.isSelected() && !checkBox1.isSelected()) {
                try {
                  System.out.println("Check-box not selected on window-closing."); 
                  writer2.write(line2 + "\n");
                  writerTemp.write(line2 + "\n");
                  writer2.write("----------------------------\n");
                  writerTemp.write("----------------------------\n");
                  writer2.write(nameOfSpell + "\n"); 
                  writerTemp.write(nameOfSpell + "\n");
                  writer2.write(pipsOfSpell + "\n"); 
                  writerTemp.write(pipsOfSpell + "\n");
                  writer2.write(pipChanceOfSpell + "\n"); 
                  writerTemp.write(pipChanceOfSpell + "\n");
                  writer2.write(countOfSpell + "\n"); 
                  writerTemp.write(countOfSpell + "\n");
                  writer2.write(descriptionOfSpell + "\n"); 
                  writerTemp.write(descriptionOfSpell + "\n");
                  writer2.write("----------------------------\n");
                  writerTemp.write("----------------------------\n");
                  writer2.close(); 
                } catch (IOException e2) {
                  try {
                    if(label.equals("t1")) {
                      RoundDiscardSpellsTeam1Writer.setWriterCreated(false);
                      writer2 = RoundDiscardSpellsTeam1Writer.get_file_writer(round); 
                      //writer2.write(line2 + "\n"); 
                      //writerTemp.write(line2 + "\n"); 
                      writer2.write("----------------------------\n");
                      writerTemp.write("----------------------------\n");
                      writer2.write(nameOfSpell + "\n"); 
                      writerTemp.write(nameOfSpell + "\n");
                      writer2.write(pipsOfSpell + "\n"); 
                      writerTemp.write(pipsOfSpell + "\n");
                      writer2.write(pipChanceOfSpell + "\n"); 
                      writerTemp.write(pipChanceOfSpell + "\n");
                      writer2.write(countOfSpell + "\n"); 
                      writerTemp.write(countOfSpell + "\n");
                      writer2.write(descriptionOfSpell + "\n"); 
                      writerTemp.write(descriptionOfSpell + "\n");
                      writer2.write("----------------------------\n");
                      writerTemp.write("----------------------------\n");
                      writer2.close(); 
                    }
                    else if(label.equals("t2")) {
                      RoundDiscardSpellsTeam2Writer.setWriterCreated(false);
                      writer2 = RoundDiscardSpellsTeam2Writer.get_file_writer(round); 
                      //writer2.write(line2 + "\n");
                      //writerTemp.write(line2 + "\n");
                      writer2.write("----------------------------\n");
                      writerTemp.write("----------------------------\n");
                      writer2.write(nameOfSpell + "\n"); 
                      writerTemp.write(nameOfSpell + "\n"); 
                      writer2.write(pipsOfSpell + "\n"); 
                      writerTemp.write(pipsOfSpell + "\n"); 
                      writer2.write(pipChanceOfSpell + "\n"); 
                      writerTemp.write(pipChanceOfSpell + "\n"); 
                      writer2.write(countOfSpell + "\n"); 
                      writerTemp.write(countOfSpell + "\n");
                      writer2.write(descriptionOfSpell + "\n"); 
                      writerTemp.write(descriptionOfSpell + "\n"); 
                      writer2.write("----------------------------\n");
                      writerTemp.write("----------------------------\n");
                      writer2.close(); 
                    }
                  } catch (IOException sube2) {
                    System.out.println("Will not reach unless problem reading file."); 
                  }
                }
              }
              else if(!checkBox1.isSelected() && !checkBox2.isSelected()) {
                try {
                  System.out.println("No boxes selected on window-closing."); 
                  writer3.write(line3 + "\n");
                  writerTemp.write(line3 + "\n");
                  writer3.write("----------------------------\n"); 
                  writerTemp.write("----------------------------\n");
                  writer3.write(nameOfSpell + "\n"); 
                  writerTemp.write(nameOfSpell + "\n"); 
                  writer3.write(pipsOfSpell + "\n"); 
                  writerTemp.write(pipsOfSpell + "\n");
                  writer3.write(pipChanceOfSpell + "\n"); 
                  writerTemp.write(pipChanceOfSpell + "\n"); 
                  writer3.write(countOfSpell + "\n"); 
                  writerTemp.write(countOfSpell + "\n");
                  writer3.write(descriptionOfSpell + "\n"); 
                  writerTemp.write(descriptionOfSpell + "\n");
                  writer3.write("----------------------------\n");
                  writerTemp.write("----------------------------\n");
                  writer3.close(); 
                } catch (IOException e3) {
                  try {
                    if(label.equals("t1")) {
                      RoundLeftOverSpellsTeam1Writer.setWriterCreated(false);
                      writer3 = RoundLeftOverSpellsTeam1Writer.get_file_writer(round); 
                      //writer3.write(line3 + "\n");
                      //writerTemp.write(line3 + "\n");
                      writer3.write("----------------------------\n"); 
                      writerTemp.write("----------------------------\n"); 
                      writer3.write(nameOfSpell + "\n"); 
                      writerTemp.write(nameOfSpell + "\n"); 
                      writer3.write(pipsOfSpell + "\n"); 
                      writerTemp.write(pipsOfSpell + "\n"); 
                      writer3.write(pipChanceOfSpell + "\n"); 
                      writerTemp.write(pipChanceOfSpell + "\n");
                      writer3.write(countOfSpell + "\n"); 
                      writerTemp.write(countOfSpell + "\n");
                      writer3.write(descriptionOfSpell + "\n"); 
                      writerTemp.write(descriptionOfSpell + "\n");
                      writer3.write("----------------------------\n");
                      writerTemp.write("----------------------------\n");
                      writer3.close(); 
                    }
                    else if(label.equals("t2")) {
                      RoundLeftOverSpellsTeam2Writer.setWriterCreated(false);
                      writer3 = RoundLeftOverSpellsTeam2Writer.get_file_writer(round); 
                      //writer3.write(line3 + "\n"); 
                      //writerTemp.write(line3 + "\n");
                      writer3.write("----------------------------\n"); 
                      writerTemp.write("----------------------------\n");
                      writer3.write(nameOfSpell + "\n"); 
                      writerTemp.write(nameOfSpell + "\n"); 
                      writer3.write(pipsOfSpell + "\n"); 
                      writerTemp.write(pipsOfSpell + "\n");
                      writer3.write(pipChanceOfSpell + "\n"); 
                      writerTemp.write(pipChanceOfSpell + "\n");
                      writer3.write(countOfSpell + "\n"); 
                      writerTemp.write(countOfSpell + "\n"); 
                      writer3.write(descriptionOfSpell + "\n"); 
                      writerTemp.write(descriptionOfSpell + "\n");
                      writer3.write("----------------------------\n");
                      writerTemp.write("----------------------------\n");
                      writer3.close(); 
                    }
                  } catch (IOException sube3) {
                    System.out.println("Will not reach unless problem reading file."); 
                  }
                }
              }
              }
            });
            frame.pack(); 
            frame.setLocationRelativeTo(null);
            frame.setVisible(true); 
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    else if(reader.readLine().equals("card-by-card")) {
      try {
        String line; 
        while((line = reader.readLine()) != null) {
          
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
