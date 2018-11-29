package com.github.julianps.modelmapper;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import io.vavr.control.Option;

public class ValueConverterTest {

	private ModelMapper modelMapper;

	@Before
	public void init() {
		modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.registerModule(new VavrModule());
	}

	@Test
	public void testOption() {
		StubFactory.X x = new StubFactory.X();
		StubFactory.Y y = new StubFactory.Y();
		y.status = Boolean.TRUE;
		x.y = Option.of(y);
		StubFactory.XInfo xinfo = modelMapper.map(x, StubFactory.XInfo.class);
		assertThat(xinfo).isNotNull();
		assertThat(xinfo.y).isNotNull();
		assertThat(xinfo.y.get().status).isEqualTo(Boolean.TRUE);
		assertThat(xinfo.y.get()).isInstanceOf(StubFactory.YInfo.class);
	}

	@Test
	public void testOptionNull() {
		StubFactory.X x = new StubFactory.X();
		StubFactory.Y y = new StubFactory.Y();
		y.status = null;
		x.y = Option.of(y);
		StubFactory.XInfo xinfo = modelMapper.map(x, StubFactory.XInfo.class);
		assertThat(xinfo).isNotNull();
		assertThat(xinfo.y).isNotNull();
		assertThat(xinfo.y.get().status).isNull();
	}

	@Test
	public void testNormalList() {
		final StubFactory.DestList destList = modelMapper.map(StubFactory.stubSourceList(), StubFactory.DestList.class);
		checkListResult(destList);
	}

	@Test
	public void testNormalListNull() {
		final StubFactory.DestList destList = modelMapper.map(StubFactory.stubSourceListNull(), StubFactory.DestList.class);
		assertThat(destList).isNotNull();
		assertThat(destList.list).isNull();
	}

	@Test
	public void testDerivedList() {
		final StubFactory.DestList destList = modelMapper.map(StubFactory.stubSourceList(), StubFactory.ExtendedDestList.class);
		checkListResult(destList);
	}

	private void checkListResult(StubFactory.DestList destList) {
		assertThat(destList.list.get(1))
				.isNotNull()
				.isInstanceOf(StubFactory.Dest.class);
		assertTrue(destList.list.get(1).x == 5);
	}


	@Test
	public void testNormalArray() {
		final StubFactory.DestArray destArray = modelMapper.map(StubFactory.stubSourceArray(), StubFactory.DestArray.class);
		checkArrayResult(destArray);
	}

	@Test
	public void testDerivedArray() {
		StubFactory.ExtendedDestArray extendedDestArray = modelMapper.map(StubFactory.stubSourceArray(), StubFactory.ExtendedDestArray.class);
		checkArrayResult(extendedDestArray);
	}

	private void checkArrayResult(final StubFactory.DestArray destArray) {
		assertThat(destArray.array.get(1))
				.isNotNull()
				.isInstanceOf(StubFactory.Dest.class);
		assertTrue(destArray.array.get(1).x == 5);
	}

	@Test
	public void testNormalSet() {
		final StubFactory.DestSet destSet = modelMapper.map(StubFactory.stubSourceSet(), StubFactory.DestSet.class);
		checkSetResult(destSet);
	}

	@Test
	public void testDerivedSet() {
		StubFactory.ExtendedDestSet destSet = modelMapper.map(StubFactory.stubSourceSet(), StubFactory.ExtendedDestSet.class);
		checkSetResult(destSet);
	}

	private void checkSetResult(final StubFactory.DestSet destSet) {
		assertThat(destSet.set.head())
				.isNotNull()
				.isInstanceOf(StubFactory.Dest.class);
		assertTrue(destSet.set.head().x == 5);
	}
}
