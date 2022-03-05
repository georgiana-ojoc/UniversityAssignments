source("exercise_3.R")
source("exercise_4.R")

show_linear_regression <- function(exercise, n, a, b, x_min, x_max, sigma) {
  print(paste0("exercise ", exercise))

  # generated samples
  samples <- generate_linear_regression_samples(n, a, b, x_min, x_max, sigma)
  x <- samples$x
  y <- samples$y

  # estimated coefficients
  coefficients <- estimate_linear_regression_coefficients(x, y)
  estimated_a <- coefficients[1]
  estimated_b <- coefficients[2]

  # model goodness
  # increase n
  # reduce x range
  # reduce sigma
  pdf(paste0(exercise, "_n_", n, "_a_", a, "_b_", b, "_x_min_", x_min, "_x_max_", x_max, "_sigma_", sigma, ".pdf"))
  plot(x, a + b * x, ylab = 'y', type = 'l', col = "red")
  lines(x, estimated_a + estimated_b * x, col = "blue")
  legend("topleft", lty = 1, col = c("red", "blue"), legend = c("generated", "predicted"))
  dev.off()
}

show_linear_regression('a', 100, 10, 0.8, -200, 200, 1.5)
show_linear_regression('b', 10, 10, 0.8, -5, 5, 1)
show_linear_regression('c', 10000, 10, 0.8, -5, 5, 1)
show_linear_regression('d', 10, 10, 0.8, 5, 5.2, 1)
show_linear_regression('e', 10000, 10, 0.8, 5, 5.2, 1)
show_linear_regression('f', 10, 10, 0.8, 5, 5.2, 0.01)