package prompto.value;

import prompto.intrinsic.PromptoRange;
import prompto.type.CharacterType;


public class CharacterRange extends RangeBase<Character> {

	static class PromptoCharacterRange extends PromptoRange<Character> {

		public PromptoCharacterRange(prompto.value.Character low, prompto.value.Character high) {
			super(low, high);
		}
		
		@Override
		public prompto.value.Character getItem(long item) {
			char result = (char)(low.getValue() + item - 1);
			if(result>high.getValue())
				throw new IndexOutOfBoundsException();
			return new prompto.value.Character(result);
		}
		
		@Override
		public PromptoCharacterRange slice(long first, long last) {
			last = adjustLastSliceIndex(last);
			return new PromptoCharacterRange(getItem(first), getItem(last));
		}
	
		@Override
		public long getNativeCount() {
			return 1L + high.getValue() - low.getValue();
		}
		
		@Override
		public boolean contains(Object item) {
			if(!(item instanceof prompto.value.Character))
				return false;
			prompto.value.Character other = (prompto.value.Character)item;
			return other.compareTo(low)>=0 && high.compareTo(other)>=0;
		}

	}
	
	public CharacterRange(Character left, Character right) {
		this(new PromptoCharacterRange(left, right));
	}
	
	public CharacterRange(PromptoRange<Character> range) {
		super(CharacterType.instance(), range);
	}
	
	@Override
	public RangeBase<Character> newInstance(PromptoRange<Character> range) {
		return new CharacterRange(range);
	}


}
