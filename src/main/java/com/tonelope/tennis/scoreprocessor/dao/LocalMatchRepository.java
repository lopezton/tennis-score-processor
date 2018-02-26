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
package com.tonelope.tennis.scoreprocessor.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tonelope.tennis.scoreprocessor.model.Match;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Tony Lopez
 *
 */
@Repository
@Getter @Setter
public class LocalMatchRepository implements MatchRepository {

	private static Long matchCounter = 0L;
	private Map<String, Match> matches;
	
	public LocalMatchRepository() {
		this.matches = new HashMap<>();
	}

	@Override
	public Match findById(String id) {
		return Optional.ofNullable(this.matches.get(id)).orElseThrow(() -> 
			new RuntimeException("No match found for id=" + id));
	}
	
	@Override
	public Match save(Match match) {
		if (null == match) {
			return null;
		}
		
		if (null == match.getId()) {
			match.setId(String.valueOf(++matchCounter));
		}
		this.matches.put(match.getId(), match);
		return match;
	}
}
