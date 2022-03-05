# p-value: low chances predicted value was obtained randomly

get_forward_combination <- function(data_frame, output_name, variable_names, alpha = 0.05) {
  final_combination <- NULL
  while (length(variable_names)) {
    minimum_p_value <- alpha * 2
    final_index <- NULL
    for (index in seq_along(variable_names)) {
      combination <- c(final_combination, variable_names[index])
      combination <- paste(combination, collapse = " + ")
      model <- lm(as.formula(paste(output_name, combination, sep = " ~ ")), data = data_frame)
      p_value <- summary(model)$coefficients[nrow(summary(model)$coefficients), 4]
      if (p_value < minimum_p_value) {
        minimum_p_value <- p_value
        final_index <- index
      }
    }
    if (minimum_p_value <= alpha) {
      final_combination <- c(final_combination, variable_names[final_index])
      variable_names <- variable_names[-final_index]
    } else {
      return(paste(final_combination, collapse = " + "))
    }
  }
  paste(final_combination, collapse = " + ")
}

get_backward_combination <- function(data_frame, output_name, variable_names, alpha = 0.05) {
  combination <- variable_names
  while (length(combination)) {
    model <- lm(as.formula(paste(output_name, paste(combination, collapse = " + "), sep = " ~ ")), data = data_frame)
    maximum_p_value <- alpha / 2
    final_index <- NULL
    for (index in seq_along(combination)) {
      p_value <- summary(model)$coefficients[index + 1, 4]
      if (p_value > maximum_p_value) {
        maximum_p_value <- p_value
        final_index <- index
      }
    }
    if (maximum_p_value > alpha) {
      combination <- combination[-final_index]
    } else {
      return(paste(combination, collapse = " + "))
    }
  }
  paste(combination, collapse = " + ")
}

get_stepwise_combination <- function(data_frame, output_name, variable_names, alpha = 0.05) {
  final_combination <- NULL
  while (length(variable_names)) {
    minimum_p_value <- alpha * 2
    final_index <- NULL
    for (index in seq_along(variable_names)) {
      combination <- c(final_combination, variable_names[index])
      combination <- paste(combination, collapse = " + ")
      model <- lm(as.formula(paste(output_name, combination, sep = " ~ ")), data = data_frame)
      p_value <- summary(model)$coefficients[nrow(summary(model)$coefficients), 4]
      if (p_value < minimum_p_value) {
        minimum_p_value <- p_value
        final_index <- index
      }
    }
    if (minimum_p_value <= alpha) {
      final_combination <- c(final_combination, variable_names[final_index])
      variable_names <- variable_names[-final_index]
      while (length(final_combination)) {
        model <- lm(as.formula(paste(output_name, paste(final_combination, collapse = " + "), sep = " ~ ")),
                    data = data_frame)
        maximum_p_value <- alpha / 2
        final_index <- NULL
        for (index in seq_along(final_combination)) {
          p_value <- summary(model)$coefficients[index + 1, 4]
          if (p_value > maximum_p_value) {
            maximum_p_value <- p_value
            final_index <- index
          }
        }
        if (maximum_p_value > alpha) {
          variable_names <- c(variable_names, final_combination[final_index])
          final_combination <- final_combination[-final_index]
        } else {
          break
        }
      }
    } else {
      return(paste(final_combination, collapse = " + "))
    }
  }
  paste(final_combination, collapse = " + ")
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
exhaustive_combination <- "FLR + ST + LOT + CON + GAR + L2"
exhaustive_model <- lm(as.formula(paste(output_name, exhaustive_combination, sep = " ~ ")), data = data_frame)
summary(exhaustive_model)
forward_combination <- get_forward_combination(data_frame, output_name, variable_names, alpha = 0.05)
forward_model <- lm(as.formula(paste(output_name, forward_combination, sep = " ~ ")), data = data_frame)
summary(forward_model)
backward_combination <- get_backward_combination(data_frame, output_name, variable_names, alpha = 0.05)
backward_model <- lm(as.formula(paste(output_name, backward_combination, sep = " ~ ")), data = data_frame)
summary(backward_model)
stepwise_combination <- get_stepwise_combination(data_frame, output_name, variable_names, alpha = 0.05)
stepwise_model <- lm(as.formula(paste(output_name, stepwise_combination, sep = " ~ ")), data = data_frame)
summary(stepwise_model)
matplot(output_values, cbind(output_values, fitted(exhaustive_model), fitted(forward_model), fitted(backward_model),
                             fitted(stepwise_model)), type = 'p', pch = 19, col = c("red", "purple", "orange", "blue",
                                                                                    "green"), xlab = 'expected y',
        ylab = 'predicted y', main = "Variable selection methods")
legend("topleft", lty = 3, col = c("red", "purple", "orange", "blue", "green"),
       legend = c("Expected values",
                  paste0("Exhaustive search: ", exhaustive_combination),
                  paste0("Forward selection: ", forward_combination),
                  paste0("Backward selection: ", backward_combination),
                  paste0("Stepwise selection: ", stepwise_combination)))