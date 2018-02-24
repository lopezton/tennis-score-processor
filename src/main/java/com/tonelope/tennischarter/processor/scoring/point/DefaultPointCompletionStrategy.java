package com.tonelope.tennischarter.processor.scoring.point;

import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Point;
import com.tonelope.tennischarter.model.Status;
import com.tonelope.tennischarter.model.Stroke;
import com.tonelope.tennischarter.model.Winnable;
import com.tonelope.tennischarter.utils.ListUtils;

public class DefaultPointCompletionStrategy implements PointCompletionStrategy {

	@Override
	public boolean apply(Point scoringObject, Match match) {
		
		boolean isComplete = false;
		Stroke stroke = ListUtils.getLast(scoringObject.getStrokes());
		
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
