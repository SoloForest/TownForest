package com.ll.townforest.boundedContext.home.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SearchDTO {
	private String searchType;
	private String searchQuery;
}
