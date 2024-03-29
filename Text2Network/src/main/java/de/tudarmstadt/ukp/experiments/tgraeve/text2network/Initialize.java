/*******************************************************************************
 * Copyright 2010
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.tudarmstadt.ukp.experiments.tgraeve.text2network;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.IOException;

import org.apache.uima.UIMAException;

import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.NC;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SimpleConceptAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SlidingWindowRelationAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SpotlightAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SvoRelationClassifier;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.exporter.GraphMLExporter;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.exporter.SGFExporter;

/**
 * Dient der Initialisierung der Extractor zur Netzwerkextraktion.
 * 
 * @author Tobias Graeve
 *
 */
public class Initialize {

	public static void main(String[] args) throws UIMAException, IOException {
		
		Text2NetworkExtractor t2nExtractor = new Text2NetworkExtractor();	
		t2nExtractor.startPipeline(createEngineDescription(SpotlightAnnotator.class, SpotlightAnnotator.PARAM_CONFIDENCE, new Float(0.5),
																						SpotlightAnnotator.PARAM_RENAME_TO_URI, true),
								createEngineDescription(SlidingWindowRelationAnnotator.class, SlidingWindowRelationAnnotator.PARAM_WINDOW_SIZE, 7),
								createEngineDescription(SvoRelationClassifier.class),
									createEngineDescription(SGFExporter.class)
									);
		
//		t2nExtractor.startPipeline(createEngineDescription(SimpleConceptAnnotator.class, SimpleConceptAnnotator.PARAM_CONCEPT_TYPE, NC.class),
//				createEngineDescription(SlidingWindowRelationAnnotator.class, SlidingWindowRelationAnnotator.PARAM_BORDER_SENTENCE, false),
//					createEngineDescription(SGFExporter.class));

	}

}
