package org.tim.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Pages {

	public static Pageable all() {
		return PageRequest.of(0, 10000);
	}

}
