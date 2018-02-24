package com.tonelope.tennischarter.utils;

import java.util.List;

public final class ListUtils {

	private ListUtils() {}
	
	public static <T> T getLast(List<T> list) {
		return ListUtils.getLast(list, 1);
	}
	
	public static <T> T getLast(List<T> list, int index) {
		if (null == list || list.isEmpty() || list.size() - index < 0) {
			return null;
		}
		return list.get(list.size() - index);
	}
}
