n <- 20
probabilities <- 1:9 / 10
successes <- 0:20

for (probability in probabilities) {
  png(filename = paste0("binomial_", probability, ".png"))
  plot(successes, dbinom(x = successes, size = n, prob = probability), type = 'l', xlab = 'x', ylab = "B(n, x, p)",
       main = paste0("Binomial distribution B(", n, ", x, ", probability, ')'))
  dev.off()
}
