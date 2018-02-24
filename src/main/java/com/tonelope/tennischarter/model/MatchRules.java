package com.tonelope.tennischarter.model;

import lombok.Data;

@Data
public class MatchRules {

	private int numberOfSets = 3;
	private int numberOfGamesPerSet = 6;
	private boolean finalSetTiebreakDisabled = false;
	private boolean noAdScoring = false;
}
