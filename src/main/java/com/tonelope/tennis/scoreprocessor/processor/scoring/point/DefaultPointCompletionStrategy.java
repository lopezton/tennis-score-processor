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
package com.tonelope.tennis.scoreprocessor.processor.scoring.point;

import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Point;
import com.tonelope.tennis.scoreprocessor.model.Status;
import com.tonelope.tennis.scoreprocessor.model.Stroke;
import com.tonelope.tennis.scoreprocessor.model.Winnable;
import com.tonelope.tennis.scoreprocessor.utils.ListUtils;

/**
 * 
 * @author Tony Lopez
 *
 */
public class DefaultPointCompletionStrategy implements PointCompletionStrategy {

	@Override
	public boolean apply(Point scoringObject, Match match) {
		
		boolean isComplete = false;
		Stroke stroke = scoringObject.getCurrentStroke();
		
		if (stroke.isWinner() || stroke.isOutRallyShot() || stroke.isDoubleFault()) {
			isComplete = true;
			scoringObject.setStatus(Status.COMPLETE);
		}
		return isComplete;
	}

	@Override
	public boolean test(Winnable scoringObject, Match match) {
		return Point.class.isAssignableFrom(scoringObject.getClass());
	}

}
