B = function(n, p) {
  x = seq(0, n, 1);
  y = (dbinom(x, n, p));
  print(max(y));
  barplot(y, space = 0, main = 'barplot', sub = 'subtitlu', xlab = 'axa x', ylab = 'axa y')
}

B(18, 0.25)
B(40, 0.5)
B(30, 0.8)