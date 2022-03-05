CLT_uniform <- function(samples, n, min, max) {
  v <- c()
  for (i in 1:samples) {
    vi <- runif(n, min, max)
    v <- c(v, mean(vi))
  }
  v
}

CLT_binomial <- function(samples, n, size, probability) {
  v <- c()
  for (i in 1:samples) {
    vi <- rbinom(n, size, probability)
    v <- c(v, mean(vi))
  }
  v
}

samples <- 1000
n <- c(1, 5, 10, 100)
min <- 0
max <- 20
size <- 20
probability <- 0.1

par(mfrow = c(2, 4))
hist(CLT_uniform(samples, n[1], min, max), breaks = 100, col = "orange", xlab = 'x', ylab = 'y',
     main = paste0("Uniform distribution for n = ", n[1]))
hist(CLT_uniform(samples, n[2], min, max), breaks = 100, col = "violet", xlab = 'x', ylab = 'y',
     main = paste0("Uniform distribution for n = ", n[2]), breaks = 100)
hist(CLT_uniform(samples, n[3], min, max), breaks = 100, col = "green", xlab = 'x', ylab = 'y',
     main = paste0("Uniform distribution for n = ", n[3]), breaks = 100)
hist(CLT_uniform(samples, n[4], min, max), breaks = 100, col = "yellow", xlab = 'x', ylab = 'y',
     main = paste0("Uniform distribution for n = ", n[4]), breaks = 100)
hist(CLT_binomial(samples, n[1], size, probability), breaks = 100, col = "orange", xlab = 'x', ylab = 'y',
     main = paste0("Binomial distribution for n = ", n[1]), breaks = 100)
hist(CLT_binomial(samples, n[2], size, probability), breaks = 100, col = "violet", xlab = 'x', ylab = 'y',
     main = paste0("Binomial distribution for n = ", n[2]), breaks = 100)
hist(CLT_binomial(samples, n[3], size, probability), breaks = 100, col = "green", xlab = 'x', ylab = 'y',
     main = paste0("Binomial distribution for n = ", n[3]), breaks = 100)
hist(CLT_binomial(samples, n[4], size, probability), breaks = 100, col = "yellow", xlab = 'x', ylab = 'y',
     main = paste0("Binomial distribution for n = ", n[4]), breaks = 100)
