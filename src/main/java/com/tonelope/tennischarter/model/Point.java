package com.tonelope.tennischarter.model;

import java.util.ArrayList;
import java.util.List;

import com.tonelope.tennischarter.utils.ListUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @ToString
public class Point extends Winnable {

	private Player server;
	private Player receiver;
	private final List<Stroke> strokes = new ArrayList<>();
	
	@Override
	public Player getWinningPlayer() {
		if (!this.isCompleted()) {
			return null;
		}
		
		Stroke stroke = ListUtils.getLast(this.strokes);
		
		// Winner
		if (!stroke.isOut()) {
			return stroke.getPlayer();
		}
		
		// Forced/Unforced Error
		if (!stroke.isServe()) {
			return this.strokes.get(this.strokes.size() - 2).getPlayer();
		} 

		// Double fault is only remaining option.
		return this.receiver;
	}

	@Override
	public void initialize() {
		
	}
}
