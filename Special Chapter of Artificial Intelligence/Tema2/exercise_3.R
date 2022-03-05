interval <- -7:7
mean <- 0
standard_deviation <- c(0.5, 1, 2)

normal_distribution_1 <- dnorm(x = interval, mean = mean, sd = standard_deviation[1])
normal_distribution_2 <- dnorm(x = interval, mean = mean, sd = standard_deviation[2])
normal_distribution_3 <- dnorm(x = interval, mean = mean, sd = standard_deviation[3])
matplot(interval, cbind(normal_distribution_1, normal_distribution_2, normal_distribution_3),
        type = 'l', lwd = 2, col = c("red", "blue", "green"), xlab = 'x', ylab = 'y',
        main = paste0("Normal distribution for mean = ", mean))
legend("topright", lty = 1, col = c("red", "blue", "green"),
       legend = c(paste0("standard deviation = ", standard_deviation[1]),
                  paste0("standard deviation = ", standard_deviation[2]),
                  paste0("standard deviation = ", standard_deviation[3])))
