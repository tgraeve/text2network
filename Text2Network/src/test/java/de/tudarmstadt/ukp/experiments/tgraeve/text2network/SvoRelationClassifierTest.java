package de.tudarmstadt.ukp.experiments.tgraeve.text2network;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.junit.Assert.*;

import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.NC;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpChunker;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.ChunkTagChanger;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SimpleConceptAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SlidingWindowRelationAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SvoRelationClassifier;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Relation;

public class SvoRelationClassifierTest {
	
	protected String text = "This is a test.";
	
	@Test
	public void testRelationAnnotation() throws UIMAException
	{
		JCas jcas = JCasFactory.createJCas();
		jcas.setDocumentText(text);
		jcas.setDocumentLanguage("en");
		
		//Segmenter
		AnalysisEngineDescription segmenter = createEngineDescription(BreakIteratorSegmenter.class);
		AnalysisEngine segEngine = createEngine(segmenter);
		segEngine.process(jcas);
		
		//POS
		AnalysisEngineDescription openPos = createEngineDescription(OpenNlpPosTagger.class);
		AnalysisEngine posEngine = createEngine(openPos);
		posEngine.process(jcas);
		
		//Chunker
		AnalysisEngineDescription openChunk = createEngineDescription(OpenNlpChunker.class);
		AnalysisEngine chunkEngine = createEngine(openChunk);
		chunkEngine.process(jcas);
		
		//ChangeChunker
		AnalysisEngineDescription changeChunker = createEngineDescription(ChunkTagChanger.class);
		AnalysisEngine changeEngine = createEngine(changeChunker);
		changeEngine.process(jcas);
		
		//ConceptAnnotation
		AnalysisEngineDescription conceptAnn = createEngineDescription(SimpleConceptAnnotator.class, SimpleConceptAnnotator.PARAM_CONCEPT_TYPE, NC.class);
		AnalysisEngine conceptEngine = createEngine(conceptAnn);
		conceptEngine.process(jcas);
		
		//RelationAnnotation
		AnalysisEngineDescription relationAnn = createEngineDescription(SlidingWindowRelationAnnotator.class);
		AnalysisEngine relationEngine = createEngine(relationAnn);
		relationEngine.process(jcas);
		
		//SVOClassifier
		AnalysisEngineDescription svoClass = createEngineDescription(SvoRelationClassifier.class);
		AnalysisEngine svoEngine = createEngine(svoClass);
		svoEngine.process(jcas);
		
		//Test
		Relation result = JCasUtil.selectSingle(jcas, Relation.class);
		assertEquals("This", result.getSource().getLabel());
		assertEquals("a test", result.getTarget().getLabel());
		assertEquals("is", result.getRelation().getLabel());
			
	}
}
