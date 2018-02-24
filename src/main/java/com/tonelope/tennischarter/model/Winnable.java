package com.tonelope.tennischarter.model;

import java.util.List;

import com.tonelope.tennischarter.utils.ListUtils;

public abstract class Winnable extends ScoringObject {

	public abstract Player getWinningPlayer();

	public abstract void initialize();

	protected <T extends Winnable> Player getWinningPlayer(List<T> list) {
		if (!this.isCompleted()) {
			return null;
		}
		
		return ListUtils.getLast(list).getWinningPlayer();
	}
}
