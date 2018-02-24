package com.tonelope.tennischarter.model;

import java.util.List;

import lombok.Data;

@Data
public class PlayerConfig {

	private List<Player> players;
	private Player startingServer;
	private Player startingReceiver;
}
