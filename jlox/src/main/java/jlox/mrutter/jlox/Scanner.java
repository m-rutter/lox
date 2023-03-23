package jlox.mrutter.jlox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
	private final String source;
	private final List<Token> tokens = new ArrayList<>();
	private int start = 0;
	private int current = 0;
	private int line = 1;
	private static final Map<String, TokenType> keywords;

	static {
		keywords = new HashMap<>();

		keywords.put("and", TokenType.AND);
		keywords.put("class", TokenType.CLASS);
		keywords.put("else", TokenType.ELSE);
		keywords.put("false", TokenType.FALSE);
		keywords.put("true", TokenType.TRUE);
		keywords.put("for", TokenType.FOR);
		keywords.put("fun", TokenType.FUN);
		keywords.put("if", TokenType.IF);
		keywords.put("nil", TokenType.NIL);
		keywords.put("or", TokenType.OR);
		keywords.put("print", TokenType.PRINT);
		keywords.put("return", TokenType.RETURN);
		keywords.put("super", TokenType.SUPER);
		keywords.put("var", TokenType.VAR);
		keywords.put("while", TokenType.WHILE);
	}

	Scanner(String source) {
		this.source = source;
	}

	public List<Token> scanTokens() {
		while (!isAtEnd()) {
			start = current;
			scanToken();
		}

		tokens.add(new Token(TokenType.EOF, "", null, line));
		return tokens;
	}

	private void scanToken() {
		var c = advance();

		switch (c) {
			case '(':
				addToken(TokenType.LEFT_PAREN);
				break;
			case ')':
				addToken(TokenType.LEFT_PAREN);
				break;
			case '{':
				addToken(TokenType.LEFT_BRACE);
				break;
			case '}':
				addToken(TokenType.RIGHT_BRACE);
				break;
			case ',':
				addToken(TokenType.COMMA);
				break;
			case '.':
				addToken(TokenType.DOT);
				break;
			case '-':
				addToken(TokenType.MINUS);
				break;
			case '+':
				addToken(TokenType.PLUS);
				break;
			case ';':
				addToken(TokenType.SEMICOLON);
				break;
			case '*':
				addToken(TokenType.STAR);
				break;
			case '!':
				addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
				break;
			case '=':
				addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
				break;
			case '<':
				addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
				break;
			case '>':
				addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
				break;
			case '/':
				if (match('/')) {
					while (peak() != '\n' && !isAtEnd()) {
						advance();
					}
				} else {
					addToken(TokenType.SLASH);
				}
				break;
			case ' ':
			case '\r':
			case '\t':
				// ignore whitespace
				break;
			case '\n':
				line++;
				break;

			case '"':
				string();
				break;

			default:
				if (isDigit(c)) {
					number();
				} else if (isAlpah(c)) {
					identifer();
				} else {
					JLox.error(line, "Unexpected character");
				}
				break;
		}
	}

	private void identifer() {
		while (isAlpahNumeric(peak())) {
			advance();
		}

		var text = source.substring(start, current);
		var type = keywords.get(text);

		if (type == null) {
			type = TokenType.IDENTIFER;
		}

		addToken(type);
	}

	private boolean isAlpah(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
	}

	private boolean isAlpahNumeric(char c) {
		return isAlpah(c) || isDigit(c);
	}

	private void number() {
		while (isDigit(peak())) {
			advance();
		}

		if (peak() == '.' && isDigit(peakNext())) {
			advance();
			while (isDigit(peak())) {
				advance();
			}
		}

		addToken(TokenType.NUMBER,
				Double.parseDouble(source.substring(start, current)));
	}

	private char peakNext() {
		if (current + 1 >= source.length()) {
			return '\0';
		}
		return source.charAt(current + 1);
	}

	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private void string() {
		while (peak() != '"' && !isAtEnd()) {
			if (peak() == '\n') {
				line++;
			}
			advance();
		}

		if (isAtEnd()) {
			JLox.error(line, "Unterminated string");
			return;
		}
		advance();

		var value = source.substring(start + 1, current - 1);

		addToken(TokenType.STRING, value);
	}

	private char peak() {
		if (isAtEnd()) {
			return '\0';
		}

		return source.charAt(current);
	}

	private boolean match(char expected) {
		if (isAtEnd()) {
			return false;
		}
		if (source.charAt(current) != expected) {
			return false;
		}

		current++;
		return true;
	}

	private boolean isAtEnd() {
		return current >= source.length();
	}

	private void addToken(TokenType type) {
		addToken(type, null);
	}

	private void addToken(TokenType type, Object literal) {
		var text = source.substring(start, current);
		tokens.add(new Token(type, text, literal, line));
	}

	private char advance() {
		return source.charAt(current++);
	}
}
