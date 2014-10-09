package edu.cmu.pipeline;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;

import edu.cmu.hw2.types.Gene;
/*
 * import edu.cmu.support.Evaluator;
 */

/**
 * @author Ryan Sun 
 *         
 *         The CASConsumer receives each CAS after it has been analyzed by the Analysis Engine.
 *         The CASConsumer will do filter, which ignore the less important GeneName.
 *         After that, it will write the data in required formation to the output document.
 */
public class GeneCASConsumer extends CasConsumer_ImplBase {

  
  /**
   * Name of configuration parameter that  be set to the path of output file(optional)
   */  
  File outFile;
  FileWriter fileWriter;
  /*
   * private Evaluator evaluator;
   */
  
  private HashMap<String, String> genes;
  
  public GeneCASConsumer() {
  }
  
  /**
   * The initialize() method is called by the framework when the CAS Consumer is first created.
   * Consumer will implement this method to obtain parameter values and perform various initialization steps.
   * It will also initialize the evaluator for evaluating the performance of the CPE.
   * 
   * @throws ResourceInitializationException
   *         when encounter errors during initializing
   */
  public void initialize() throws ResourceInitializationException {   
    
    initOutput();
   /*
    * try {
    * initEvaluator();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    */
     genes = new HashMap<String, String>();
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
    

   /* 
    private void initEvaluator() throws IOException{
      String path = "./src/main/resources/data/GENE.eval"; 
      evaluator = new Evaluator(path);
    }
    */
    
    /**
     * The processCas function will process the data from the aggregated annotator,
     * Filter the gene that are not suitable, write the correct answer to the output document.
     */
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
      filter(gene);
    }
  }

    private void filter(Gene gene){
      // get the variables from the annotator
      String name = gene.getGeneName();
      if(name.length() < 3)
        return;
      String id = gene.getID();    
      String processorID = gene.getCasProcessorId();
      double confidence = gene.getConfidence();
      // filter the unimportant gene name
      if(confidence > 0.6){
        if(processorID.equals("Lingpipe")){
          if(genes.containsKey(id)){
            String existName = genes.get(id);
            if(! (existName.contains(name) || name.contains(existName)) ){
              genes.put(id, name);
              writeOutput(gene);
            }
          }else{
            genes.put(id, name);
            writeOutput(gene);
          }
        }
      }
    }
    
    private void writeOutput(Gene gene){
      int start = gene.getStart();
      int end = gene.getEnd();
      String text = gene.getText();
      // calculate the required start and end which don't have white space
      start -= countSpace(text, start);
      end -= countSpace(text, end) + 1;
      String id = gene.getID();
      String name = gene.getGeneName();
      /*
       * evaluator.judge(name);
       */
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
  /**
   * the destroy() function will close the fileWriter
   * It will also call the method in Eluator class to print the final report of precision, recall and F-score
   */
  public void destroy() {
    if (fileWriter != null) {
      // print the evaluation report
      /*
       * evaluator.printReport();
       */
      try {
        fileWriter.close();
      } catch (IOException e) {
        // it does not matter during destroy
      }
    }
  }
  
  /*
   * this function will count the white space before the gene-token
   * @param text
   * @param start
   * @return
   */
  private int countSpace(String text, int start){
    int offset = 0;
    for(int i = 0; i < start; i++)
      if(text.charAt(i) == ' ')
        offset++;
    return offset;
  }
}
