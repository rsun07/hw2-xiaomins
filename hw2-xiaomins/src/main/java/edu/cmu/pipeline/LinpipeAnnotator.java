package edu.cmu.pipeline;

import java.io.IOException;
import java.util.Iterator;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.ConfidenceChunker;
import com.aliasi.util.AbstractExternalizable;

import edu.cmu.hw2.types.Gene;
import edu.cmu.hw2.types.Sentence;

/**
 * @author Ryan Sun 
 * This annotator is based on the algorithm provided by lingpipe it read the
 * document from CollectionReader through JCas, find and locate the GeneTag. 
 * if the confidence of this Tag is higher than 0.6, it will be passed to Consumer, 
 * otherwise it will be ignored
 *         
 * Licensed to the Apache Software Foundation (ASF)
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 */
public class LinpipeAnnotator extends JCasAnnotator_ImplBase {

  // instance variable
  private ConfidenceChunker chunker;
  private static final double THRESHOLD = 0.6;
  //the number of most similar words
  private static final int MAX_N_BEST_CHUNKS = 5; 
  private static final String CAS_PROCESSOR_ID = "Lingpipe";
  private static final String LINGPIPE_MODEL = "model";
  /**
   * initialize the chunker with model this modification can avoid initialize every time, which boosts the efficiency
   */
  public void initialize(UimaContext context) throws ResourceInitializationException {
    chunker = null;  
    String modelPath = (String) context.getConfigParameterValue(LINGPIPE_MODEL);
    try {
      chunker = (ConfidenceChunker) AbstractExternalizable.readResourceObject(LinpipeAnnotator.class, modelPath);
      System.out.println("point");
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
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
    FSIterator<Annotation> it = aJCas.getAnnotationIndex(Sentence.type).iterator();
    
    if (it.hasNext()) {
      // read the information from the CollectionReader
      Sentence sentence = (Sentence) it.next();
      String id = sentence.getID();
      String text = sentence.getText();
      char[] cs = text.toCharArray();

      // chunk the geneName with start and end positon by lingpipe chunk function
      Iterator<Chunk> chunkIt = chunker.nBestChunks(cs, 0, cs.length, MAX_N_BEST_CHUNKS);
      while (chunkIt.hasNext()) {
        Chunk chunk = chunkIt.next();
        
        // calculate the confidence of this GeneTag
        double confidence = Math.pow(2.0, chunk.score());
        
        // only pass the GeneTag to Consumer if the confidence is greater than the threshold, say 0.6
        // write the information into gene variable and pass it to Comsumer in Sentence type
        if (confidence > THRESHOLD) {
          Gene gene = new Gene(aJCas);
          int start = chunk.start();
          int end = chunk.end();
          String name = text.substring(start, end);
          // pass documents to consumer
          gene.setStart(start);
          gene.setEnd(end);
          gene.setID(id);
          gene.setGeneName(name);
          gene.setText(text);
          gene.setCasProcessorId(CAS_PROCESSOR_ID);
          gene.setConfidence(confidence);
          gene.addToIndexes();
        }
      }
    }
  }
}
