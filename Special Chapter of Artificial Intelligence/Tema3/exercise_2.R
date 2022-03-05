data_frame <- read.table("iq.dat", header = TRUE)

# scatter plot
data_frame <- data.frame(data_frame[2], data_frame[3])
plot(data_frame, pch = 19)

# linear regression model
model <- lm(Nota ~ IQ, data = data_frame)
summary(model)

coefficients <- coef(model)
abline(coefficients[1], coefficients[2], lwd = 3, col = "red")

predict(model, newdata = data.frame(IQ = c(115, 130)))