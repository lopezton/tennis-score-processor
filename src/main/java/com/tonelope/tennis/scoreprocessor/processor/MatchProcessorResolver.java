/**
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tonelope.tennis.scoreprocessor.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.tonelope.tennis.scoreprocessor.model.FrameworkException;
import com.tonelope.tennis.scoreprocessor.model.Match;

/**
 * 
 * @author Tony Lopez
 *
 */
@Component
public class MatchProcessorResolver {

	@Autowired
	private ApplicationContext context;
	
	public MatchProcessor get(Match match) {
		if (match.getPlayers().size() == 2) {
			return context.getBean(SinglesMatchProcessor.class);
		}
		throw new FrameworkException("Unable to identify match processor for " + match);
	}
}
