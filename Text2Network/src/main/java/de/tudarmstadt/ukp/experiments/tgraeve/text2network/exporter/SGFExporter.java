
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
package de.tudarmstadt.ukp.experiments.tgraeve.text2network.exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Relation;
import eu.sisob.api.parser.sisob.SGFParser;
import eu.sisob.api.visualization.format.graph.fields.Edge;
import eu.sisob.api.visualization.format.graph.fields.EdgeSet;
import eu.sisob.api.visualization.format.graph.fields.Node;
import eu.sisob.api.visualization.format.graph.fields.NodeSet;
import eu.sisob.api.visualization.format.metadata.Metadata;


/**
 * Exportiert Konzepte und Relationen als SGF-Format.
 * 
 * Output: /output
 * 
 * @author Tobias Graeve
 *
 */

public class SGFExporter extends JCasConsumer_ImplBase
{
	
	/**
	 * SPeicherort der Ausgabedatei.
	 */
	public static final String PARAM_OUTPUT_FILE = "outputFile";

	  @ConfigurationParameter(name = PARAM_OUTPUT_FILE, mandatory = true, defaultValue = "output/network.sgf")
	  private File outputFile;
	
	
	protected NodeSet nodeset = new NodeSet();
	protected EdgeSet edgeset = new EdgeSet();
	protected SGFParser parser = new SGFParser();
	int eId = 1;
	
	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException
	{	
		for(Concept concept : JCasUtil.select(jCas, Concept.class))
		{
			Node node = new Node(concept.getLabel(), concept.getLabel());
			if(!nodeset.contains(node))
			{
				nodeset.addNode(node);
			}
		}
		
		for(Relation relation : JCasUtil.select(jCas, Relation.class))
		{
			Node node1 = new Node(relation.getSource().getLabel(), relation.getSource().getLabel());
			if(!nodeset.contains(node1))
			{
				nodeset.addNode(node1);
			}
			Node node2 = new Node(relation.getTarget().getLabel(), relation.getTarget().getLabel());
			if(!nodeset.contains(node2))
			{
				nodeset.addNode(node2);
			}
			Edge edge;
			if(relation.getRelation() != null)
			{
				edge = new Edge(Integer.toString(eId), relation.getRelation().getLabel(), relation.getSource().getLabel(), relation.getTarget().getLabel());
			} else
			{
				edge = new Edge(Integer.toString(eId), relation.getSource().getLabel(), relation.getTarget().getLabel());
			}
			
			if(!edgeset.contains(edge))
			{
				edgeset.add(edge);
			}
			
			eId++;
		}
		
		parser.setParsingMetadata(new Metadata("Text2Network", "1 mode network", "false"));
		parser.setParsingNodeSet(nodeset);
		parser.setParsingEdgeSet(edgeset);
		
		FileWriter writer = null;
		
		try {
			writer = new FileWriter(outputFile);
			writer.write(parser.encode());
		} catch (IOException e) {
			throw new AnalysisEngineProcessException(e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				throw new AnalysisEngineProcessException(e);
			}
		}
	}
}
