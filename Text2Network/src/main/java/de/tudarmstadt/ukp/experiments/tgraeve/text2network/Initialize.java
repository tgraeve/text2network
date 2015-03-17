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

import java.io.IOException;

import org.apache.uima.UIMAException;

public class Initialize {

	public static void main(String[] args) throws UIMAException, IOException {
		
		String input = "input/";
		String output = "output/output.txt";
		
		
		ExtractionPipeline extractor = new ExtractionPipeline();
		
		extractor.startPipeline(input, output);
		
		
	}

}
