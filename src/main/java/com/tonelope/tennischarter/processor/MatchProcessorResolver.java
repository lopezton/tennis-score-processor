package com.tonelope.tennischarter.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.tonelope.tennischarter.model.FrameworkException;
import com.tonelope.tennischarter.model.Match;

@Component
public class MatchProcessorResolver {

	@Autowired
	private ApplicationContext context;
	
	public MatchProcessor get(Match match) {
		if (match.getPlayers().size() == 2) {
			return context.getBean(SinglesMatchProcessor.class);
		}
		throw new FrameworkException("Unable to identify match processor for " + match);
	}
}
