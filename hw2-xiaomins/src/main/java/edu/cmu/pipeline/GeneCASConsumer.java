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

import edu.cmu.deiis.types.Gene;
import edu.cmu.support.Evaluator;

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
  Evaluator evaluator;
  
  public GeneCASConsumer() {
  }
  
  /**
   * The initialize() method is called 
   * by the framework when the CAS Consumer is first created.
   * Consumer will implement this method to obtain parameter values 
   * and perform various initialization steps.
   * It will also initialize the evaluator.
   * 
   * @throws ResourceInitializationException
   *         when encounter errors during initializing
   */
  public void initialize() throws ResourceInitializationException {   
    initOutput();
    try {
      initEvaluator();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
    private void initOutput() throws ResourceInitializationException{
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
    
    private void initEvaluator() throws IOException{
      String path = "./src/main/resources/data/sample.out"; 
      evaluator = new Evaluator(path);
    }
  public void processCas(CAS aCAS) throws ResourceProcessException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }
    
    // traversal the CAS file and find the id and gene name, then output it in regular formation
    FSIterator<Annotation> it = jcas.getAnnotationIndex(Gene.type).iterator();
    while(it.hasNext()){
      //read and store the related information from jcas
      Gene gene = (Gene) it.next();
      int start = gene.getStart();
      int end = gene.getEnd();
      String id = gene.getID();
      String name = gene.getGeneName();
      evaluator.judge(name);
      //System.out.println(id + "|" + start + " " + end + "|" + name + "\n\r");
      //count the white space before gene type and find & set the output start 
      // output the geneTag in regular formation
      try {
          fileWriter.write(id + "|" + start + " " + end + "|" + name + "\n\r");
          fileWriter.flush();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  @Override
  public void destroy() {
    if (fileWriter != null) {
      // print the evaluation report
      evaluator.printReport();
      try {
        fileWriter.close();
      } catch (IOException e) {
        // it does not matter during destroy
      }
    }
  }
}
