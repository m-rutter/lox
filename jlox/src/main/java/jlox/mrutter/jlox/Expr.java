package jlox.mrutter.jlox;

abstract class Expr {
	static class Binary extends Expr {
		Binary(Expr left, Token operator, Expr right) {
			this.left = left;
			this.operator = operator;
			this.right = right;

		}

		final Expr left;
		final Token operator;
		final Expr right;
	}

	static class Unary extends Expr {
		Unary(Token operator, Expr expression) {
			this.operator = operator;
			this.expression = expression;
		}

		final Token operator;
		final Expr expression;
	}

	static class Grouping {
		Grouping(Expr expression) {
			this.expression = expression;
		}

		final Expr expression;
	}
}
