"Enter 6 real numbers:"
x <- scan(n = 6)
x

cat("max", '=', max(x), '\n')
cat("min", '=', min(x), '\n')
mean <- mean(x)
cat("mean", '=', mean, '\n')
cat("median", '=', median(x), '\n')
n <- length(x)
standardDeviation <- sd(x) * sqrt((n - 1) / n)
cat("standard deviation", '=', standardDeviation, '\n')
cat("sorted array:", sort(x), '\n')
cat("standardized array:", (x - mean) / standardDeviation, '\n')
