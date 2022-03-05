data_frame <- read.table("alcool.dat", header = TRUE)

# scatter plot
data_frame <- data.frame(data_frame[2], data_frame[3])
plot(data_frame, pch = 19)

# correlation coefficient: covariance of standardized data
# 1: for every positive increase in one variable, there is a positive increase of a fixed proportion in the other
# 0: for every increase, there isn’t a positive or negative increase (the two variables aren’t related)
# -1: for every positive increase in one variable, there is a negative decrease of a fixed proportion in the other
cor(data_frame[1], data_frame[2])