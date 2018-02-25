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
package com.tonelope.tennischarter.processor;

import java.util.List;
import java.util.stream.Collectors;

import com.tonelope.tennischarter.model.Player;
import com.tonelope.tennischarter.model.ScoringObject;
import com.tonelope.tennischarter.model.Status;
import com.tonelope.tennischarter.processor.scoring.ScoreCompletionStrategyResolver;
import com.tonelope.tennischarter.utils.ListUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Tony Lopez
 *
 */
@RequiredArgsConstructor
@Getter
public abstract class AbstractMatchProcessor implements MatchProcessor {

	protected final ScoreCompletionStrategyResolver scoreCompletionStrategyResolver;
	
	protected Player getOpposingPlayer(Player server, List<Player> players) {
		return players.stream().filter(p -> !p.equals(server)).collect(Collectors.toList()).get(0);
	}
	
	protected <T extends ScoringObject> T getLastAndSetInProgress(List<T> list) {
		T scoringObject = ListUtils.getLast(list);
		if (scoringObject.isNotStarted()) {
			scoringObject.setStatus(Status.IN_PROGRESS);
		}
		return scoringObject;
	}
}
