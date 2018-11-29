package com.github.julianps.modelmapper;

import io.vavr.collection.Array;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class StubFactory {

	// source inner
	@Data
	static class Y {
		Boolean status;
	}

	// dest inner
	@Data
	static class YInfo {
		Boolean status;
	}

	// source wrapper
	@Data
	static class X {
		Option<Y> y;
	}

	// dest wrapper
	@Data
	static class XInfo {
		Option<YInfo> y;
	}

	@Data
	static class Dest {
		Integer x;
	}


	@Data
	static class Source {
		Integer x;
	}


	// Array ---
	@Data
	static class SourceArray {
		Array<Source> array;
	}

	@Data
	static class DestArray {
		Array<Dest> array;
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	static class ExtendedDestArray extends DestArray {
	}

	// List ---
	@Data
	static class SourceList {
		List<Source> list;
	}

	@Data
	static class DestList {
		List<Dest> list;
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	static class ExtendedDestList extends DestList {
	}


	// Set ---

	@Data
	static class SourceSet {
		HashSet<Source> set;
	}

	@Data
	static class DestSet {
		HashSet<Dest> set;
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	static class ExtendedDestSet extends DestSet {
	}


	static SourceList stubSourceList() {
		final SourceList sourceList = new SourceList();
		sourceList.list = List.of(stubSource(2), stubSource(5));
		return sourceList;
	}

	static SourceList stubSourceListNull() {
		final SourceList sourceList = new SourceList();
		sourceList.list = null;
		return sourceList;

	}

	static SourceArray stubSourceArray() {
		final SourceArray sourceArray = new SourceArray();
		sourceArray.array = Array.of(stubSource(2), stubSource(5));
		return sourceArray;
	}

	static SourceSet stubSourceSet() {
		final SourceSet sourceList = new SourceSet();
		sourceList.set = HashSet.of(stubSource(2), stubSource(5));
		return sourceList;
	}


	static Source stubSource(int number) {
		final Source source = new Source();
		source.x = number;
		return source;
	}
}

