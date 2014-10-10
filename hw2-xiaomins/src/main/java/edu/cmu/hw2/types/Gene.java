

/* First created by JCasGen Tue Oct 07 18:15:55 EDT 2014 */
package edu.cmu.hw2.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Oct 09 21:09:24 EDT 2014
 * XML source: /Users/mac/git/hw2-xiaomins/hw2-xiaomins/src/main/resources/AggregateAnnotatorDescriptor.xml
 * @generated */
public class Gene extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Gene.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Gene() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Gene(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Gene(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Gene(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: GeneName

  /** getter for GeneName - gets 
   * @generated
   * @return value of the feature 
   */
  public String getGeneName() {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_GeneName == null)
      jcasType.jcas.throwFeatMissing("GeneName", "edu.cmu.hw2.types.Gene");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Gene_Type)jcasType).casFeatCode_GeneName);}
    
  /** setter for GeneName - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setGeneName(String v) {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_GeneName == null)
      jcasType.jcas.throwFeatMissing("GeneName", "edu.cmu.hw2.types.Gene");
    jcasType.ll_cas.ll_setStringValue(addr, ((Gene_Type)jcasType).casFeatCode_GeneName, v);}    
   
    
  //*--------------*
  //* Feature: ID

  /** getter for ID - gets 
   * @generated
   * @return value of the feature 
   */
  public String getID() {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_ID == null)
      jcasType.jcas.throwFeatMissing("ID", "edu.cmu.hw2.types.Gene");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Gene_Type)jcasType).casFeatCode_ID);}
    
  /** setter for ID - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setID(String v) {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_ID == null)
      jcasType.jcas.throwFeatMissing("ID", "edu.cmu.hw2.types.Gene");
    jcasType.ll_cas.ll_setStringValue(addr, ((Gene_Type)jcasType).casFeatCode_ID, v);}    
   
    
  //*--------------*
  //* Feature: start

  /** getter for start - gets 
   * @generated
   * @return value of the feature 
   */
  public int getStart() {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_start == null)
      jcasType.jcas.throwFeatMissing("start", "edu.cmu.hw2.types.Gene");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Gene_Type)jcasType).casFeatCode_start);}
    
  /** setter for start - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setStart(int v) {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_start == null)
      jcasType.jcas.throwFeatMissing("start", "edu.cmu.hw2.types.Gene");
    jcasType.ll_cas.ll_setIntValue(addr, ((Gene_Type)jcasType).casFeatCode_start, v);}    
   
    
  //*--------------*
  //* Feature: end

  /** getter for end - gets 
   * @generated
   * @return value of the feature 
   */
  public int getEnd() {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_end == null)
      jcasType.jcas.throwFeatMissing("end", "edu.cmu.hw2.types.Gene");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Gene_Type)jcasType).casFeatCode_end);}
    
  /** setter for end - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEnd(int v) {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_end == null)
      jcasType.jcas.throwFeatMissing("end", "edu.cmu.hw2.types.Gene");
    jcasType.ll_cas.ll_setIntValue(addr, ((Gene_Type)jcasType).casFeatCode_end, v);}    
   
    
  //*--------------*
  //* Feature: casProcessorId

  /** getter for casProcessorId - gets 
   * @generated
   * @return value of the feature 
   */
  public String getCasProcessorId() {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_casProcessorId == null)
      jcasType.jcas.throwFeatMissing("casProcessorId", "edu.cmu.hw2.types.Gene");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Gene_Type)jcasType).casFeatCode_casProcessorId);}
    
  /** setter for casProcessorId - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setCasProcessorId(String v) {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_casProcessorId == null)
      jcasType.jcas.throwFeatMissing("casProcessorId", "edu.cmu.hw2.types.Gene");
    jcasType.ll_cas.ll_setStringValue(addr, ((Gene_Type)jcasType).casFeatCode_casProcessorId, v);}    
   
    
  //*--------------*
  //* Feature: confidence

  /** getter for confidence - gets 
   * @generated
   * @return value of the feature 
   */
  public double getConfidence() {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_confidence == null)
      jcasType.jcas.throwFeatMissing("confidence", "edu.cmu.hw2.types.Gene");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Gene_Type)jcasType).casFeatCode_confidence);}
    
  /** setter for confidence - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setConfidence(double v) {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_confidence == null)
      jcasType.jcas.throwFeatMissing("confidence", "edu.cmu.hw2.types.Gene");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Gene_Type)jcasType).casFeatCode_confidence, v);}    
   
    
  //*--------------*
  //* Feature: Text

  /** getter for Text - gets 
   * @generated
   * @return value of the feature 
   */
  public String getText() {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_Text == null)
      jcasType.jcas.throwFeatMissing("Text", "edu.cmu.hw2.types.Gene");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Gene_Type)jcasType).casFeatCode_Text);}
    
  /** setter for Text - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setText(String v) {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_Text == null)
      jcasType.jcas.throwFeatMissing("Text", "edu.cmu.hw2.types.Gene");
    jcasType.ll_cas.ll_setStringValue(addr, ((Gene_Type)jcasType).casFeatCode_Text, v);}    
  }

    