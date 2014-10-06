package edu.cmu.pipeline;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import edu.cmu.deiis.types.Sentence;
import edu.cmu.support.PosTagNamedEntityRecognizer;
  /**
   * @author Ryan Sun
   * This annotator based on PosTagNamedEntityRecognizer from Stanford Core NLP,
   * Aiming to find the GeneTag from sentences
   */
public class StanfordCoreNLPAnnotator extends JCasAnnotator_ImplBase {
  
    /**
     * The process function afford all the task of the annotator
     * it read a line of document from CollectionReader through JCas,
     * aiming to find the gene name from the sentences
     * and wirte the information into JCas for later Consumer usage.
     */
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
      
      //initialize variables to avoid overhead
      Sentence sentence, temp;
      String id;
      String text;
      //double confidence;
      PosTagNamedEntityRecognizer ptner;
      Iterator<Annotation> it = aJCas.getAnnotationIndex(Sentence.type).iterator();
      Map<Integer,Integer> map;
      
      while(it.hasNext()){
        //get sentence annotation from CAS
        sentence = (Sentence) it.next();
        id = sentence.getID();
        text = sentence.getText();
        map = new HashMap<Integer,Integer>();
        
        // find the position of the GeneTag using PosTagNamedEntityRecognizer() 
        // and store the begin & end position of the GeneTag in a HashMap
        try {
          ptner = new PosTagNamedEntityRecognizer();
          map = ptner.getGeneSpans(text);
        }catch (ResourceInitializationException e) {
          e.printStackTrace();
        }
          
        // read the start and end position of the GeneTag and pass it to the Consumer
        Iterator<Map.Entry<Integer, Integer>> mapIt = map.entrySet().iterator();   
        while(mapIt.hasNext()){
          Map.Entry<Integer, Integer> entry = mapIt.next();
          temp = new Sentence(aJCas);
          // write the information into temp variable and pass it to Consumer in Sentence type
          temp.setStart(entry.getKey());
          temp.setEnd(entry.getValue());
          temp.setID(id);
          temp.setText(text);
          temp.addToIndexes();
        }
     }
  }
}
