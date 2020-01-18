package util;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * [States.java]
 * Reading data of certain states from a text file and stores them in a hashset.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public class States {

  private static HashSet<String> 
  getStates(String filePath) throws IOException {

    HashSet<String> states = new HashSet<String>();
    File file = new File(filePath);

    Scanner fin = new Scanner(file);
    while (fin.hasNext()) {
      states.add(fin.next());
    }
    fin.close();
    return states;
  } 

  public static HashSet<String> attack;
  public static HashSet<String> special;

  
  static {
    try {
      attack = getStates("assets/states/attack-states.txt");
      special = getStates("assets/states/special-states.txt");
      special.addAll(attack);
    } catch (IOException e) {
      //not handled
      System.err.println(e);
    }
    
  }
  
  
}