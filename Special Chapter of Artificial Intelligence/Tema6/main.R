data_frame <- read.table("swiss.txt", header = TRUE)
variables <- ncol(data_frame)
scaled_data_frame <- scale(data_frame, center = TRUE, scale = TRUE)
covariance_matrix <- cov(scaled_data_frame)
eigen <- eigen(covariance_matrix)
scores <- as.matrix(scaled_data_frame) %*% eigen$vectors
standard_deviations <- apply(scores, 2, sd)
standard_deviations
variances <- standard_deviations^2
proportions <- variances / sum(variances)
proportions
cumulative_proportions <- NULL
for (index in seq_along(proportions)) {
  cumulative_proportions <- c(cumulative_proportions, sum(proportions[1:index]))
}
cumulative_proportions

principal_component_analysis <- prcomp(data_frame, center = TRUE, scale = TRUE)
biplot(principal_component_analysis, scale = 0)