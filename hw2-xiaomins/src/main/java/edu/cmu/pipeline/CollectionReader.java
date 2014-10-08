package edu.cmu.pipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;

import edu.cmu.hw2.types.Sentence;

/**
 * 
 * @author Ryan Sun
 * 
 *         This Collection Reader is responsible for obtaining documents from the collection and
 *         returning each document as a CAS. More specifically, it read document line by line from
 *         ./src/main/resources/data/hw2.in
 */

public class CollectionReader extends CollectionReader_ImplBase {

  /**
   * Name of configuration parameter that must be set to the path of a directory containing input
   * files.
   */
  public static final String PARAM_INPUTDIR = "InputDirectory";

  // instance variables, a BufferedReader
  private BufferedReader bufReader;

  /**
   * The initialize() method is called by the framework when the Collection Reader is first created.
   * In this method, the Collection Reader class can access the document and read it line by line,
   * store each line into an ArrayList for later use.
   * 
   * @see org.apache.uima.collection.CollectionReader#getNext(org.apache.uima.cas.CAS)
   */

  public void initialize() throws ResourceInitializationException {
    File directory = new File(((String) getConfigParameterValue(PARAM_INPUTDIR)));
    try {
      bufReader = new BufferedReader(new FileReader(directory));
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * The getNext() method reads the next document from the collection and populates a CAS. It read
   * each line from the ArrayList and pass the information of this line to each annotator.
   */
  @Override
  public void getNext(CAS aCAS) throws IOException, CollectionException {
    // TODO Auto-generated method stub
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException ex) {
      throw new CollectionException(ex);
    }
    // read the id and sentence, and store them in different strings
    String line = bufReader.readLine().trim();
    Sentence temp = new Sentence(jcas);
    // get the sentence(information) from collection reader
    // store the id and sentence for later write
    String id = line.substring(0, 14).trim();
    String text = line.substring(15).trim();
    temp.setID(id);
    temp.setText(text);
    temp.addToIndexes();
  }

  /**
   * The close method is called when the Collection Reader is no longer needed.
   */
  @Override
  public void close() throws IOException {
    // TODO Auto-generated method stub
    try {
      bufReader.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * It should return how much of the collection has been read thus far and how much remains to be read.
   * But in this case, we only read one document. It is not necessary any more.
   */
  @Override
  public Progress[] getProgress() {
    // TODO Auto-generated method stub
    return new Progress[0];
  }

  /**
   * The hasNext() method returns whether or not there are any documents remaining to be read from
   * the collection
   */
  @Override
  public boolean hasNext() throws IOException, CollectionException {
    // TODO Auto-generated method stub
    return bufReader != null && bufReader.ready();
  }

}
