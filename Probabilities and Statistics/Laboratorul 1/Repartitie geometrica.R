Geometric = function(n, p) {
  x = seq(0, n, 1);
  y = (dgeom(x, p, log = FALSE));
  barplot(y, space = 0, main = 'barplot', sub = 'subtitlu', xlab = 'axa x', ylab = 'axa y')
}

Geometric(18, 0.25)