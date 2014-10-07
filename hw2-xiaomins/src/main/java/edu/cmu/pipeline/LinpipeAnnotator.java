package edu.cmu.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.ConfidenceChunker;
import com.aliasi.util.AbstractExternalizable;

import edu.cmu.deiis.types.Sentence;

/**
 * @author Ryan Sun 
 *         This annotator is based on the algorithm provided by lingpipe it read the
 *         document from CollectionReader through JCas, find and locate the GeneTag. 
 *         if the confidence of this Tag is higher than 0.6, it will be passed to Consumer, 
 *         otherwise it will be ignored
 */
public class LinpipeAnnotator extends JCasAnnotator_ImplBase {

  // instance variable
  ConfidenceChunker chunker;

  int MAX_N_BEST_CHUNKS = 5; // the number of most similar words

  /**
   * initialize the chunker with model this modification can avoid initialize every time, which boosts the efficiency
   */
  public void initialize() {
    File modelFile = new File("./src/main/resources/ne-en-bio-genetag.HmmChunker");
    try {
      chunker = (ConfidenceChunker) AbstractExternalizable.readObject(modelFile);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * the process function is used to process JCas read from collection reader by using linpipe's chunking function. 
   * it can extract the useful information such as, gene name, the start and end position of genename, 
   * the whole sentence and the id from the sentence then it pass the JCas
   * into the CAS of consumer. if the confidence of this GeneTag is less than 0.6, it won't be accepted.
   */
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    //read information from CollectionReader
    FSIterator<Annotation> aIt = aJCas.getAnnotationIndex(Sentence.type).iterator();
    
    //initialize variables to avoid overhead
    Sentence sentence, temp;
    String id;
    String text;
    char[] chars;
    double confidence;
    
    while (aIt.hasNext()) {
      // read the information from the CollectionReader
      sentence = (Sentence) aIt.next();
      id = sentence.getID();
      text = sentence.getText();
      chars = text.toCharArray();

      // chunk the geneName with start and end positon by lingpipe chunk function
      Iterator<Chunk> chunkIt = chunker.nBestChunks(chars, 0, chars.length, MAX_N_BEST_CHUNKS);
      while (chunkIt.hasNext()) {
        Chunk chunk = chunkIt.next();
        // calculate the confidence of this GeneTag
        confidence = Math.pow(2.0, chunk.score());
        temp = new Sentence(aJCas);
        // only pass the GeneTag to Consumer if the confidence is greater than the threshold, say 0.6
        // write the information into temp variable and pass it to Comsumer in Sentence type
        if (confidence > 0.7) {
          temp.setStart(chunk.start());
          temp.setEnd(chunk.end());
          temp.setID(id);
          temp.setText(text);
          temp.addToIndexes();
        }
      }
    }
  }
}
