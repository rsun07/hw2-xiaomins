package edu.cmu.pipeline;


import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import abner.Tagger;
import edu.cmu.deiis.types.Gene;
import edu.cmu.deiis.types.Sentence;

public class AbnerAnnotator extends JCasAnnotator_ImplBase {

  private Tagger tag;
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    //String model = ((String)aContext.getConfigParameterValue(PARAM_MODEL)).trim();
   super.initialize(aContext);
   tag = new Tagger(Tagger.NLPBA); 
  }
  
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
          
    FSIterator<Annotation> it = aJCas.getAnnotationIndex(Sentence.type).iterator();    
    while(it.hasNext()){
        //get sentence sentence from CAS
        Sentence sentence = (Sentence) it.next();
        String id = sentence.getID();
        String text = sentence.getText();
        
        //using Abner to process text in each sentence     
        String[][] ents = tag.getEntities(text);
        for (int i = 0; i < ents[0].length; i++) {
          int start = text.indexOf(ents[0][i]);
          //System.out.println(ents[0][i]);
          if(start != -1){
              int end = start + ents[0][i].length();
              start -= countSpace(text, start);
              end -= countSpace(text, end) + 1;         
              //create gene sentence CAS
              Gene gene = new Gene(aJCas);
              gene.setID(id);
              gene.setStart(start);
              gene.setEnd(end);
              gene.setGeneName(ents[0][i]);
              gene.addToIndexes();
          }
          }
          
         }
       }
  //count white space before the gene-token
  private int countSpace(String text, int start){
    int offset = 0;
    for(int i = 0; i < start; i++)
      if(text.charAt(i)==' ')
        offset++;
    return offset;
  }

    }

