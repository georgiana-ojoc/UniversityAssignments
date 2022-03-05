# smaller
compute_residual_sum_of_squares <- function(output, model) {
  sum((output - fitted(model))^2)
}

compute_total_sum_of_squares <- function(output) {
  sum((output - mean(output))^2)
}

# bigger
# how close the points are to the line
compute_coefficient_of_determination <- function(residual_sum_of_squares, total_sum_of_squares) {
  1 - residual_sum_of_squares / total_sum_of_squares
}

# bigger
# does not necessarily increase with the number of variables
compute_adjusted_coefficient_of_determination <- function(coefficient_of_determination, observations, variables) {
  1 - (1 - coefficient_of_determination) * (observations - 1) / (observations - variables - 1)
}

compute_variance <- function(data_frame) {
  output_values <- data_frame[, 1]
  output_name <- names(data_frame)[1]
  model <- lm(as.formula(paste(output_name, '.', sep = " ~ ")), data = data_frame)
  observations <- nrow(data_frame)
  variables <- ncol(data_frame) - 1
  compute_residual_sum_of_squares(output_values, model) / (observations - variables - 1)
}

# closer to variables
# how unbiased is the small model, in comparison with the full model
compute_mallows_coefficient <- function(residual_sum_of_squares, variance, observations, variables) {
  residual_sum_of_squares / variance - observations + 2 * (variables + 1)
}

sort_models <- function(values) {
  values$score <- 0
  points <- 1
  for (position in order(values$residual_sum_of_squares)) {
    values$score[position] <- values$score[position] + points
    points <- points + 1
  }
  points <- 1
  for (position in order(-values$coefficient_of_determination)) {
    values$score[position] <- values$score[position] + points
    points <- points + 1
  }
  points <- 1
  for (position in order(-values$adjusted_coefficient_of_determination)) {
    values$score[position] <- values$score[position] + points
    points <- points + 1
  }
  points <- 1
  for (position in order(abs(values$mallows_coefficient -
                               values$variables -
                               1))) {
    values$score[position] <- values$score[position] + points
    points <- points + 1
  }
  values <- values[order(values$score),]
}

data_frame <- read.table("house.dat", header = TRUE)
observation_number <- nrow(data_frame)
variable_number <- ncol(data_frame) - 1
if (variable_number < 1 || observation_number < 1) {
  stop("Invalid dataset.")
}
output_values <- data_frame[, 1]
output_name <- names(data_frame)[1]
variable_names <- names(data_frame)[2:(variable_number + 1)]
total_sum_of_squares <- compute_total_sum_of_squares(output_values)
variance <- compute_variance(data_frame)

columns <- c("variables", "model", "residual_sum_of_squares", "coefficient_of_determination",
             "adjusted_coefficient_of_determination", "mallows_coefficient")
best_values <- data.frame(matrix(nrow = 0, ncol = 6))
colnames(best_values) <- columns

for (index in 1:variable_number) {
  values <- data.frame(matrix(nrow = 0, ncol = 6))
  colnames(values) <- columns

  for (combination in combn(variable_names, index, simplify = FALSE)) {
    combination <- paste(combination, collapse = " + ")
    model <- lm(as.formula(paste(output_name, combination, sep = " ~ ")), data = data_frame)

    residual_sum_of_squares <- compute_residual_sum_of_squares(output_values, model)
    coefficient_of_determination <- compute_coefficient_of_determination(residual_sum_of_squares, total_sum_of_squares)
    adjusted_coefficient_of_determination <- compute_adjusted_coefficient_of_determination(coefficient_of_determination,
                                                                                           observation_number, index)
    mallows_coefficient <- compute_mallows_coefficient(residual_sum_of_squares, variance, observation_number, index)

    current_values <- data.frame(index, combination, residual_sum_of_squares, coefficient_of_determination,
                                 adjusted_coefficient_of_determination, mallows_coefficient)
    colnames(current_values) <- columns
    values <- rbind(values, current_values)
  }

  values <- sort_models(values)
  best_values <- rbind(best_values, values[1, 1:6])
}

par(mfrow = c(2, 2))
plot(x = best_values$variables, y = best_values$residual_sum_of_squares, type = 'l', lwd = 2, col = "red",
     xlab = "Number of variables", ylab = "Residual sum of squares", main = "Residual sum of squares")
plot(x = best_values$variables, y = best_values$coefficient_of_determination, type = 'l', lwd = 2, col = "blue",
     xlab = "Number of variables", ylab = "Coefficient of determination", main = "Coefficient of determination")
plot(x = best_values$variables, y = best_values$adjusted_coefficient_of_determination, type = 'l', lwd = 2,
     col = "orange", xlab = "Number of variables", ylab = "Adjusted coefficient of determination",
     main = "Adjusted coefficient of determination")
plot(x = best_values$variables, y = best_values$mallows_coefficient, type = 'l', lwd = 2, col = "green",
     xlab = "Number of variables", ylab = "Mallows coefficient", main = "Mallows coefficient")

best_values <- sort_models(best_values)
best_values[1, 1:6]
