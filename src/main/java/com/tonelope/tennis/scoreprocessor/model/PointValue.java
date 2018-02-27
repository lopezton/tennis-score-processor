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

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * 
 * @author Tony Lopez
 *
 */
@Getter
public enum PointValue {

	LOVE("0"),
	FIFTEEN("15"),
	THIRTY("30"),
	FORTY("40"),
	ADVANTAGE("AD"),
	GAME("GAME");
	
	private String value;
	
	private static final PointValue[] values = values();
	
	private static Map<String, PointValue> mapByValue = new HashMap<>();

    static {
        for(PointValue pv : values) {
        	mapByValue.put(pv.value, pv);
        }
    }
	
	PointValue(String value) {
		this.value = value;
	}
	
	public PointValue next() {
		return values[(this.ordinal() + 1) % values.length];
	}
	
	public PointValue previous() {
		return values[(this.ordinal() - 1) % values.length];
	}
	
	public static PointValue getByValue(String value) {
		return mapByValue.get(value);
	}
}
