read_number <- function(name) {
  repeat {
    cat("Enter ", name, " (", name, " > 0):\n", sep = '')
    value <- scan(n = 1)
    if (value > 0) {
      break
    }
  }
  value
}

logarithm <- function(x) {
  log2(x)
}

a <- read_number('a')
b <- read_number('b')
if (a > b) {
  a <- a + b
  b <- a - b
  a <- a - b
}
cat('a', '=', a, '\n')
cat('b', '=', b, '\n')

plot(logarithm(a:b), type = 'l', lwd = 2, col = "red", xlab = 'x', ylab = expression("log"[2] * "x"),
     main = expression("f(x) = log"[2] * "x, x in [a, b]"))
