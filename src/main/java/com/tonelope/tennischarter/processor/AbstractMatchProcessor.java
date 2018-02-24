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
