generate_linear_regression_samples <- function(n, a, b, x_min, x_max, sigma) {
  errors <- rnorm(n, mean = 0, sd = sigma)
  x <- runif(n, min = x_min, max = x_max)
  y <- a + b * x + errors

  list(x = x, y = y)
}