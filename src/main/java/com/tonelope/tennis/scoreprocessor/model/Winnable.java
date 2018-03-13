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

import java.util.List;

import com.tonelope.tennis.scoreprocessor.utils.ListUtils;

/**
 * 
 * @author Tony Lopez
 *
 */
public abstract class Winnable extends ScoringObject {

	public abstract Player getWinningPlayer();

	public abstract void initialize();

	public abstract Score getScore();

	/**
	 * <p>
	 * Returns the winning player of this object, if the current point is
	 * considered to be complete. If it is not complete, <tt>null</tt> will be
	 * returned.
	 * </p>
	 * <p>
	 * Unless overridden, the winning player is determined by retrieving the
	 * last element in the provided <tt>list</tt> object and executes it's
	 * <tt>getWinningPlayer</tt> method (<tt>list</tt> contains only types of
	 * <tt>Winnable</tt>).
	 * </p>
	 * 
	 * @param list
	 *            the list of objects from which to retrieve the winning player
	 *            from.
	 * @param <T>
	 * 			  the <tt>Winnable</tt> object.
	 * @return the winning player object.
	 */
	protected <T extends Winnable> Player getWinningPlayer(List<T> list) {
		if (!this.isCompleted()) {
			return null;
		}

		return ListUtils.getLast(list).getWinningPlayer();
	}
}
