package edu.cmu.pipeline;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;

import edu.cmu.deiis.types.Sentence;

/**
 * @author Ryan Sun 
 *         The CAS consumer receives each CAS after it has been analyzed by the Analysis
 *         Engine they typically extract data from the CAS and persist selected information to
 *         aggregate data structures such as search engine indexes or databases.
 */
public class GeneCASConsumer extends CasConsumer_ImplBase {

  
  /**
   * Name of configuration parameter that  be set to the path of output file(optional)
   */  
  File outFile;
  FileWriter fileWriter;
  
  public GeneCASConsumer() {
  }
  
  /**
   * The initialize() method is called 
   * by the framework when the CAS Consumer is first created.
   * Consumer will implement this method to obtain parameter values 
   * and perform various initialization steps.
   * 
   * @throws ResourceInitializationException
   *         when encounter errors during initializing
   */
  public void initialize() throws ResourceInitializationException {
    
    //get configuration parameter
    String oPath = (String) getUimaContext().getConfigParameterValue("outputFile");
    
    // check validation of output directory
    if (oPath == null) {
      throw new ResourceInitializationException(
              ResourceInitializationException.CONFIG_SETTING_ABSENT, new Object[] { "outputFile" });
    }
    // create output directory
    outFile = new File(oPath.trim());    
    try {
      fileWriter = new FileWriter(outFile);
    } catch (IOException e) {
      throw new ResourceInitializationException(e);
    }
  }
  
  public void processCas(CAS aCAS) throws ResourceProcessException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }
    
    // traversal the CAS file and find the id and gene name, then output it in regular formation
    FSIterator<Annotation> it = jcas.getAnnotationIndex(Sentence.type).iterator();
    while(it.hasNext()){
      //read and store the related information from jcas
      Sentence thisType = (Sentence) it.next();
      String id = thisType.getID();
      String geneName = thisType.getGeneName();
      System.out.println(geneName);
      int start = thisType.getStart();
      int end = thisType.getEnd();
      //count the white space before gene type and find & set the output start 
      start -= countSpace(thisType.getText(), start);
      end -= countSpace(thisType.getText(), end) + 1;
      // output the geneTag in regular formation
      try {
          fileWriter.write(id + "|" + start + " " + end + "|" + geneName + "\n\r");
          fileWriter.flush();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }   
  }
  //count white space before the gene-token
  private int countSpace(String sentence, int start){
    int offset = 0;
    for(int i = 0; i < start; i++)
      if(sentence.charAt(i)==' ')
        offset++;
    return offset;
  }
  @Override
  public void destroy() {
    if (fileWriter != null) {
      try {
        fileWriter.close();
      } catch (IOException e) {
        // it does not matter during destroy
      }
    }
  }
}
