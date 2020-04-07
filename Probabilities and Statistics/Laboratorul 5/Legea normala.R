#I. 1
normal_density_rule = function(limit, sigma, micro) {
  t = seq(-limit, limit, length = 400)
  f = 1 / (sigma * sqrt(2*pi)) * exp(-(t - micro) ^ 2 / ( 2 * sigma ^ 2))
  plot(t, f, type = "l", lwd = 1)
}

normal_density_rule(6, 1, 0)