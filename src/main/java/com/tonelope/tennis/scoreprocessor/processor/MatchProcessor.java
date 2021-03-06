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

import java.util.function.Consumer;

import com.tonelope.tennis.scoreprocessor.model.FrameworkException;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.MatchEventType;
import com.tonelope.tennis.scoreprocessor.model.Point;
import com.tonelope.tennis.scoreprocessor.model.Stroke;

import lombok.Getter;

/**
 * <p>
 * Serves as the main entry point to processing the provided <tt>match</tt>
 * object.
 * </p>
 * 
 * <p>
 * On creation, this object will determine the strategy and rules that will be
 * needed in order to process the provided <tt>match</tt> object. Which
 * particular strategy and rules that will be applied will be determined by the
 * appropriate configurations provided within the <tt>match</tt> object.
 * </p>
 * 
 * <p>
 * <tt>MatchProcessor</tt> follows the Facade pattern to abstract the details
 * away regarding which strategy or handlers are applied given a particular set
 * of match configurations. If greater control over which strategies or handlers
 * are needed, they may be used directly. (e.g. use
 * <tt>SinglesMatchStrategy</tt> or <tt>DoublesMatchStrategy</tt> directly.)
 * </p>
 * 
 * @author Tony Lopez
 *
 */
@Getter
public class MatchProcessor {

	private final Match match;
	private final MatchStrategy strategy;

	public MatchProcessor(Match match) {
		if (null == match) {
			throw new FrameworkException("match can not be null.");
		}
		// TODO validate match
		this.match = match;
		this.strategy = this.determineStrategy(match);
	}

	/**
	 * <p>
	 * Determines the match strategy to be used.
	 * </p>
	 * <ul>
	 * <li>If the number of players equals 2, then <tt>SinglesMatchStrategy</tt>
	 * will be used.</li>
	 * <li>If the number of players equals 4, then <tt>DoublesMatchStrategy</tt>
	 * will be used.</li>
	 * <li>If the number of players is not supported by the available
	 * strategies, then a <tt>FrameworkException</tt> will be thrown.</li>
	 * </ul>
	 * 
	 * @param match
	 *            the match object
	 * @return the strategy determined from <tt>match</tt>, or else a
	 *         <tt>FrameworkException</tt> is thrown.
	 */
	private MatchStrategy determineStrategy(Match match) {
		if (null == match.getPlayers() || match.getPlayers().isEmpty()) {
			throw new FrameworkException("match object contained no player information.");
		} else if (match.getPlayers().size() == 2) {
			return new SinglesMatchStrategy();
		} else if (match.getPlayers().size() == 4) {
			throw new UnsupportedOperationException("Doubles is not yet supported.");
		}
		throw new FrameworkException("Could not determine match strategy from given match information");
	}

	/**
	 * <p>
	 * Updates this <tt>match</tt> object with the provided <tt>stroke</tt>.
	 * </p>
	 * 
	 * @param stroke
	 *            the stroke object to update within <tt>match</tt>.
	 * @return the match object
	 */
	public Match update(Stroke stroke) {
		return this.strategy.update(this.match, stroke);
	}

	/**
	 * <p>
	 * Updates this <tt>match</tt> object with the provided <tt>point</tt>.
	 * </p>
	 * 
	 * @param point
	 *            the point object to update within <tt>match</tt>.
	 * @return the match object
	 */
	public Match update(Point point) {
		return this.strategy.update(this.match, point);
	}

	/**
	 * <p>
	 * Registers <tt>event</tt> to be executed following the completion of the
	 * event defined by <tt>eventType</tt>.
	 * </p>
	 * 
	 * @param eventType
	 *            the event descriptor for after which the provided
	 *            <tt>event</tt> should be executed. Whichever
	 *            <tt>MatchEventType</tt> is provided, <tt>event</tt> will
	 *            executed only after <tt>eventType</tt> has completed.
	 * @param event
	 *            the consuming method to be executed. The provided parameter
	 *            will be the updated <tt>match</tt> object.
	 */
	public void registerEvent(MatchEventType eventType, Consumer<Match> event) {
		this.strategy.registerEvent(eventType, event);
	}
}
