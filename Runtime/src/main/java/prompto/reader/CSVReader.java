package prompto.reader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import prompto.error.ReadWriteError;
import prompto.intrinsic.PromptoDict;
import prompto.intrinsic.PromptoDocument;
import prompto.intrinsic.PromptoList;

public abstract class CSVReader {
	
	public static PromptoList<PromptoDocument<String, Object>> read(String data, PromptoDict<String, String> columns, Character separator, Character encloser) throws IOException {
		try(StringReader reader = data==null ? null : new StringReader(data)) {
			return read(reader, columns, separator, encloser);
		}
	}
	
	public static PromptoList<PromptoDocument<String, Object>> read(final Reader buffered, PromptoDict<String, String> columns, Character separator, Character encloser) {
		PromptoList<PromptoDocument<String, Object>> list = new PromptoList<>(false);
		Iterator<PromptoDocument<String, Object>> iter = iterator(buffered, columns, separator, encloser);
		while(iter.hasNext())
			list.add(iter.next());
		return list;
	}
	
	static interface CSVIterable extends Iterable<PromptoDocument<String, Object>>, Iterator<PromptoDocument<String, Object>> {
	}
	
	public static CSVIterable iterator(String data, PromptoDict<String, String> columns, Character separator, Character encloser) throws IOException {
		Reader reader = data==null ? null : new StringReader(data);
		return iterator(reader, columns, separator, encloser);
	}
	
	public static CSVIterable iterator(final Reader _reader, PromptoDict<String, String> columns, Character separator, Character encloser) {
		
		char sep = separator==null ? ',' : separator.charValue();
		char quote = encloser==null ? '"' : encloser.charValue();
		
		return new CSVIterable() {
			
			Reader reader = _reader;
			ArrayList<String> headers = null;
			Integer peekedChar = null;
			int nextChar = 0;
			
			@Override
			public Iterator<PromptoDocument<String, Object>> iterator() {
				return this;
			}
			
			@Override
			public boolean hasNext() {
				if(nextChar==0)
					fetchChar(true);
				if(headers==null)
					parseHeaders(columns);
				return nextChar>0;
			}
			
			private void fetchChar() {
				fetchChar(false);
			}
			
			private void fetchChar(boolean eatNewLine) {
				if(reader==null)
					nextChar = -1; // EOF
				else if(peekedChar!=null) {
					int c = peekedChar.intValue();
					peekedChar = null;
					nextChar = c;
				} else try {
					int c = reader.read();
					if(c=='\r')
						fetchChar(eatNewLine);
					else if(eatNewLine && (c=='\n'))
						fetchChar(eatNewLine);
					else
						nextChar = c;
				} catch(IOException e) {
					throw new ReadWriteError(e.getMessage());
				}
			}
			
			private int peekChar() {
				if(peekedChar==null) {
					int oldChar = nextChar;
					fetchChar();
					peekedChar = nextChar;
					nextChar = oldChar;
				}
				return peekedChar.intValue();
			}



			private void parseHeaders(PromptoDict<String, String> columns) {
				headers = parseLine();
				if(columns!=null) {
					for(int i=0;i<headers.size(); i++) {
						String header = headers.get(i);
						String value = columns.get(header);
						if(value!=null)
							headers.set(i, value.toString());
					}
				}
			}

			private ArrayList<String> parseLine() {
				ArrayList<String> list = new ArrayList<>();
				while(parseValue(list))
					;
				if(nextChar=='\n')
					fetchChar();
				return list;
			}

			private boolean parseValue(ArrayList<String> list) {
				if(nextChar==sep)
					parseEmptyValue(list);
				else if(nextChar==quote)
					parseQuotedValue(list);
				else 
					parseUnquotedValue(list);
				return nextChar!=-1 && nextChar!='\n';
			}

			private void parseEmptyValue(ArrayList<String> list) {
				list.add(null);
				fetchChar();
			}

			private void parseQuotedValue(ArrayList<String> list) {
				fetchChar(); // consume the leading double quote
				parseValue(quote, list);
				// look for next sep
				while(nextChar!=sep && nextChar!=-1 && nextChar!='\n')
					fetchChar();
				if(nextChar==sep)
					fetchChar();
			}

			private void parseUnquotedValue(ArrayList<String> list) {
				parseValue(sep, list);
			}
			
			private void parseValue(char endChar, ArrayList<String> list) {
				StringBuilder sb = new StringBuilder();
				boolean exit = false;
				for(;;) {
					if(nextChar==-1)
						exit = handleEOF(sb, endChar, list);
					else if(nextChar=='\n')
						exit = handleNewLine(sb, endChar, list);
					else if(nextChar==endChar)
						exit = handleEndChar(sb, endChar, list);
					else if(nextChar=='\\')
						exit = handleEscape(sb, endChar, list);
					else
						exit = handleOtherChar(sb, endChar, list);
					if(exit) {
						if(sb.length()>0)
							list.add(sb.toString());
						return;
					}
				}
			}

			private boolean handleOtherChar(StringBuilder sb, char endChar, ArrayList<String> list) {
				sb.append((char)nextChar);
				fetchChar();
				return false;
			}

			private boolean handleEscape(StringBuilder sb, char endChar, ArrayList<String> list) {
				if(peekChar()!=-1) {
					sb.append((char)peekChar());
					fetchChar();
				}
				fetchChar();
				return false;
			}

			private boolean handleEOF(StringBuilder sb, char endChar, ArrayList<String> list) {
				return true;
			}

			private boolean handleEndChar(StringBuilder sb, char endChar, ArrayList<String> list) {
				if(endChar=='"' && peekChar()==endChar) {
					sb.append((char)nextChar);
					fetchChar();
					fetchChar();
					return false;
				} else {
					fetchChar();
					return true;
				}
			}

			private boolean handleNewLine(StringBuilder sb, char endChar, ArrayList<String> list) {
				if(endChar=='"') {
					sb.append((char)nextChar);
					fetchChar();
					return false;
				} else {
					return true;
				}
			}
			
			
			@Override
			public PromptoDocument<String, Object> next() {
				if(!hasNext())
					return null;
				ArrayList<String> values = parseLine();
				PromptoDocument<String, Object> doc = new PromptoDocument<>();
				for(int i=0;i<headers.size();i++) {
					if(i<values.size())
						doc.put(headers.get(i), values.get(i));
					else
						doc.put(headers.get(i), null);
				}
				return doc;
			}
		};
	}
}
