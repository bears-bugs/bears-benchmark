// Generated from ArgsLexer.g4 by ANTLR 4.7.1
package prompto.utils;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ArgsLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		STRING=1, EQUALS=2, DASH=3, WS=4, ELEMENT=5;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"STRING", "EscapeSequence", "EQUALS", "DASH", "WS", "ELEMENT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'='", "'-'", "' '"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "STRING", "EQUALS", "DASH", "WS", "ELEMENT"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public ArgsLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ArgsLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\7\60\b\1\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\3\2\3\2\7\2\23\n\2\f\2\16"+
		"\2\26\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\"\n\3\3\4\3\4\3"+
		"\5\3\5\3\6\3\6\3\6\3\6\3\7\6\7-\n\7\r\7\16\7.\2\2\b\3\3\5\2\7\4\t\5\13"+
		"\6\r\7\3\2\5\6\2\f\f\17\17$$^^\n\2$$))^^ddhhppttvv\b\2\13\f\17\17\"\""+
		"$$//??\2\64\2\3\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2"+
		"\2\3\17\3\2\2\2\5\31\3\2\2\2\7#\3\2\2\2\t%\3\2\2\2\13\'\3\2\2\2\r,\3\2"+
		"\2\2\17\24\7$\2\2\20\23\5\5\3\2\21\23\n\2\2\2\22\20\3\2\2\2\22\21\3\2"+
		"\2\2\23\26\3\2\2\2\24\22\3\2\2\2\24\25\3\2\2\2\25\27\3\2\2\2\26\24\3\2"+
		"\2\2\27\30\7$\2\2\30\4\3\2\2\2\31!\7^\2\2\32\"\t\3\2\2\33\34\4\62\65\2"+
		"\34\35\4\629\2\35\"\4\629\2\36\37\4\629\2\37\"\4\629\2 \"\4\629\2!\32"+
		"\3\2\2\2!\33\3\2\2\2!\36\3\2\2\2! \3\2\2\2\"\6\3\2\2\2#$\7?\2\2$\b\3\2"+
		"\2\2%&\7/\2\2&\n\3\2\2\2\'(\7\"\2\2()\3\2\2\2)*\b\6\2\2*\f\3\2\2\2+-\n"+
		"\4\2\2,+\3\2\2\2-.\3\2\2\2.,\3\2\2\2./\3\2\2\2/\16\3\2\2\2\7\2\22\24!"+
		".\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}