print_exercise <- function(exercise, result) {
  cat("exercise", exercise, '-', result, '\n')
}

exercise_2_e <- function(x) {
  n <- length(x)
  c(mean(x), sd(x) * sqrt((n - 1) / n))
}

x <- c(1, 8, 2, 6, 2, 8, 8, 5, 5, 5)

print_exercise("2.a.", sum(x) / 10)
print_exercise("2.b.", log2(x))
print_exercise("2.c.", max(x) - min(x))
y <- (x - 5) / 2.624669
print_exercise("2.d.", y)
print_exercise("2.e.", exercise_2_e(y))
