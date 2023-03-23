package jlox.mrutter.jlox;

public sealed interface Expr permits Expr.Binary, Expr.Unary, Expr.Grouping,
    Expr.Literal {
  public record Binary(Expr left, Token operator, Expr right) implements Expr {}

  public record Unary(Token operator, Expr expression) implements Expr {}

  public record Grouping(Expr expression) implements Expr {}

  public record Literal(Object value) implements Expr {}
}
