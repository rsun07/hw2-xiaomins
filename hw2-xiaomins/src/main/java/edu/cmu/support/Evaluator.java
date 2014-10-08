package edu.cmu.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This Evaluator class will calculate the precision, recall and F-score for CPE 
 */
public class Evaluator {
  private int correct_num = 0;

  private int answer_num = 0;

  private int golden_num = 0;

  private String goldenAnswer;

  /**
   * Constructor of the Evaluator class.
   * Read the golden standard from a file and initialize other variable
   * 
   * @param path
   * @throws IOException
   */
  public Evaluator(String path) throws IOException {
    File filename = new File(path);
    InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
    BufferedReader bufReader;
    bufReader = new BufferedReader(reader);
    StringBuffer sb = new StringBuffer();
    golden_num = 0;
    String line = "";
    line = bufReader.readLine();
    while (line != null) {
      line = bufReader.readLine(); 
      sb.append(line);
      golden_num++;
    }
    bufReader.close();
    reader.close();
    goldenAnswer = sb.toString();
  }

  /**
   * the judge() function will judge whether an answer is correct.
   * It also calculate the sum of answers given for later use.
   * @param ans
   */
  public void judge(String ans) {
    answer_num++;
    if (goldenAnswer.contains(ans)) {
      correct_num++;
    }
  }

  /**
   * The printReport() method will calculate and print the precision, recall and F-score for CPE at the end.
   */
  public void printReport() {
    double precision = (double) correct_num / answer_num;
    double recall = (double) correct_num / golden_num;
    System.out.println("it is the end of the cpe");
    System.out.println("Total Answer: " + answer_num);
    System.out.println("Correct Num: " + correct_num);
    System.out.println("Golden Answer Num: " + golden_num);
    System.out.println("Precision: " + precision);
    System.out.println("Recall: " + recall);
    System.out.println("F-socre:" + 2.0 * precision * recall / (precision + recall));
  }
}