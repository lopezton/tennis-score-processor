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
package com.tonelope.tennis.scoreprocessor.model;

/**
 * <p>
 * Enum holder for describing events that occur within the score processing
 * framework.
 * </p>
 * 
 * <p>
 * The values stored here are purely descriptive, but may be used in several
 * different areas of the framework. For example, several of the
 * <tt>COMPLETION</tt> values defined within this object are used to register
 * client-side hooks to be executed on the completion of other events occurring
 * within the framework (e.g. register an event to occur after each set is
 * completed).
 * </p>
 * 
 * @author Tony Lopez
 *
 */
public enum MatchEventType {

	/**
	 * <p>
	 * Describes the event when a point has been completed. A point is
	 * considered to be completed when a player has won by either hitting a
	 * winner or a player has made an error.
	 * </p>
	 */
	ON_POINT_COMPLETION,

	/**
	 * <p>
	 * Describes the event when a game has been completed. A game is considered
	 * to be completed when a player has won the number of points required to
	 * win the game being played.
	 * </p>
	 */
	ON_GAME_COMPLETION,

	/**
	 * <p>
	 * Describes the event when a set has been completed. A set is considered to
	 * be completed when a player has won the number of games required to win
	 * the set being played.
	 * </p>
	 */
	ON_SET_COMPLETION,

	/**
	 * <p>
	 * Describes the event when a tiebreak has been completed. A tiebreak is
	 * considered to be completed when a player has won the number of points
	 * required to win the tiebreak being played.
	 * </p>
	 */
	ON_TIEBREAK_COMPLETION,

	/**
	 * <p>
	 * Describes the event when a match has been completed. A match is
	 * considered to be completed when a player has won the number of sets
	 * required to win the match being played.
	 * </p>
	 */
	ON_MATCH_COMPLETION;
}
