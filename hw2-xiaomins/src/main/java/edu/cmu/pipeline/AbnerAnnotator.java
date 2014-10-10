package edu.cmu.pipeline;

import java.util.regex.Pattern;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import abner.Tagger;
import edu.cmu.hw2.types.Gene;
import edu.cmu.hw2.types.Sentence;

/**
 * @author Ryan Sun
 * 
 *         This annotator is based on the algorithm provided by Abner. it read the document from
 *         CollectionReader through JCas, find and locate the GeneTag. if the gene name suit the
 *         regular expression, it will be passed to Consumer, otherwise it will be ignored.
 * 
 *         Common Public License Version 1.0 (CPL) http://pages.cs.wisc.edu/~bsettles/abner
 */
public class AbnerAnnotator extends JCasAnnotator_ImplBase {

  private Tagger tag;

  private static final String CAS_PROCESSOR_ID = "Abner";

  /**
   * The initialize() will initialize the Abner Tagger with NLPBA corpora
   */
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    tag = new Tagger(Tagger.NLPBA);
  }

  /**
   * the process() function is used to process JCas read from collection reader by using Abner's
   * getEntities method. it can extract gene name from the text, and we can get the start and end
   * from it after calculation There is a regular expression to filter the gene name, those with
   * unstructured symbles will be ignored. The whole sentence and the id from the sentence then it
   * pass the JCas into the CAS of consumer.
   * 
   */
  public void process(JCas aJCas) throws AnalysisEngineProcessException {

    FSIterator<Annotation> it = aJCas.getAnnotationIndex(Sentence.type).iterator();
    while (it.hasNext()) {
      // get sentence sentence from CAS
      Sentence sentence = (Sentence) it.next();
      String id = sentence.getID();
      String text = sentence.getText();

      // using Abner to process text in each sentence
      String[][] ents = tag.getEntities(text);
      for (int i = 0; i < ents[0].length; i++) {
        int start = text.indexOf(ents[0][i]);
        // System.out.println(ents[0][i]);
        if (start != -1) {
          int end = start + ents[0][i].length();
          // create gene sentence CAS
          Gene gene = new Gene(aJCas);
          gene.setID(id);
          gene.setStart(start);
          gene.setEnd(end);
          gene.setText(text);
          gene.setGeneName(ents[0][i]);

          // use a regular expression to filter the gene name
          double confidence = 0.81;
          if (Pattern.matches("[-\\sa-z0-9A-Z]+", ents[0][i]) == false) {
            confidence -= 0.3;
          }
          gene.setCasProcessorId(CAS_PROCESSOR_ID);
          gene.setConfidence(confidence);
          gene.addToIndexes();
        }
      }
    }
  }
}
