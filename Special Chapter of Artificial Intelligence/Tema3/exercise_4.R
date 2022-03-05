estimate_linear_regression_coefficients <- function(x, y) {
  # estimated coefficients
  mean_x <- mean(x)
  mean_y <- mean(y)
  mean_corrected_x <- x - mean_x
  mean_corrected_y <- y - mean_y
  b <- sum(mean_corrected_x * mean_corrected_y) / sum(mean_corrected_x^2)
  a <- mean_y - b * mean_x
  print(paste0("estimated a = ", a))
  print(paste0("estimated b = ", b))

  # confidence interval
  n <- length(x)
  t_critical_value <- qt(p = 0.025, df = n - 2)
  error <- y - (a + b * x)
  standard_deviation <- sqrt(sum(error^2) / (n - 2))
  a_standard_error <- standard_deviation * sqrt(1 / n + mean_x^2 / sum(mean_corrected_x^2))
  b_standard_error <- standard_deviation / sqrt(sum(mean_corrected_x^2))
  a_value <- t_critical_value * a_standard_error
  b_value <- t_critical_value * b_standard_error
  a_confidence_level <- sort(c(a - a_value, a + a_value))
  b_confidence_level <- sort(c(b - b_value, b + b_value))
  print(paste0(c("a 95% confidence level: ", a_confidence_level), collapse = ' '))
  print(paste0(c("b 95% confidence level: ", b_confidence_level), collapse = ' '))

  c(a, b)
}