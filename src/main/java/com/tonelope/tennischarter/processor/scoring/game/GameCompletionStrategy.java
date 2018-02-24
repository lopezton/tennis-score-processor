package com.tonelope.tennischarter.processor.scoring.game;

import java.util.HashMap;
import java.util.Map;

import com.tonelope.tennischarter.model.Game;
import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Player;
import com.tonelope.tennischarter.model.Point;
import com.tonelope.tennischarter.model.Set;
import com.tonelope.tennischarter.model.Status;
import com.tonelope.tennischarter.processor.scoring.ScoreCompletionStrategy;

public abstract class GameCompletionStrategy<T extends Game> implements ScoreCompletionStrategy<T> {

	@Override
	public boolean apply(Game scoringObject, Match match) {
		boolean isComplete = false;
		Map<Player, Integer> playerPts = new HashMap<>();
		playerPts.put(scoringObject.getServer(), 0);
		playerPts.put(scoringObject.getReceiver(), 0);
		for(Point point : scoringObject.getPoints()) {
			Player winningPlayer = point.getWinningPlayer();
			
			if (null == winningPlayer) {
				return false;
			}
			
			playerPts.put(winningPlayer, playerPts.get(winningPlayer) + 1);
		}
		
		isComplete = this.isComplete(playerPts.get(scoringObject.getServer()), playerPts.get(scoringObject.getReceiver()));
		if (isComplete) {
			scoringObject.setStatus(Status.COMPLETE);
		}
		return isComplete;
	}
	
	protected boolean isFinalSetWinByTwo(Set currentSet, Match match) {
		return currentSet.getGames().size() >= 13 && 
				match.isCurrentlyInFinalSet() && 
				match.getMatchRules().isFinalSetTiebreakDisabled();
	}
	
	protected abstract boolean isComplete(Integer player1Pts, Integer player2Pts);
}
