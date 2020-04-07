#A1
B = function(n, p) {
  x = seq(0, n, 1);
  y = (dbinom(x, n, p));
  barplot(y, space = 0, main = 'Repartitia binomiala', xlab = 'axa x', ylab = 'axa y', col = 'green')
}

B(18, 0.25)

Poisson = function(n, lambda) {
  x = seq(0, n, 1);
  y = (dpois(x, lambda, log = FALSE));
  barplot(y, space = 0, main = 'Repartitia Poisson', xlab = 'axa x', ylab = 'axa y', col = 'yellow')
}

Poisson(3, 0.5)

Geometric = function(k, p) {
  x = seq(0, k, 1);
  y = (dgeom(x, p, log = FALSE));
  barplot(y, space = 0, main = 'Repartitia geometrica', xlab = 'axa x', ylab = 'axa y', col = 'lightblue')
}

Geometric(18, 0.25)



#A2
esantion = c(79, 71, 89, 57, 76, 64, 82, 82, 67, 80, 81, 65, 73, 79, 79, 60, 58, 83, 74, 68, 78, 80, 78, 81, 76, 65, 70, 76, 58, 82, 59, 73, 72, 79, 87, 63, 74, 90, 69, 70, 83, 76, 61, 66, 71, 60, 57, 81, 57, 65, 81, 78, 77, 81, 81, 63, 71, 66, 56, 62, 75, 64, 74, 74, 70, 71, 56, 69, 63, 72, 81, 54, 72, 91, 92)

print(paste("media = ", mean(esantion)))
print(paste("mediana = ", median(esantion)))
print(paste("deviatia standard = ", sd(esantion)))
print(summary(esantion))

outliers_mean = function(esantion) {
  medie = mean(esantion);
  dev_st = sd(esantion);
  esantion_nou = vector();
  j = 1;
  for (i in 1:length(esantion))
    if (esantion[i] >= medie - 2 * dev_st & esantion[i] <= medie + 2 * dev_st) {
      esantion_nou[j] = esantion[i];
      j = j + 1;
    }
  print(esantion_nou)
}

print("Metoda cu media si deviatia standard:")
outliers_mean(esantion)

outliers_iqr = function(esantion) {
  Q1 = as.vector(quantile(esantion))[2];
  Q3 = as.vector(quantile(esantion))[4];
  IQR = Q3 - Q1;
  esantion_nou = vector();
  j = 1;
  for (i in 1:length(esantion))
    if (esantion[i] >= Q1 - 1.5 * IQR & esantion[i] <= Q3 + 1.5 * IQR) {
      esantion_nou[j] = esantion[i];
      j = j + 1;
    }
  print(esantion_nou)
}

print("Metoda cu quartilele si intervalul interquartilic")
outliers_iqr(esantion)

min = min(esantion)
max = max(esantion)
intervale = seq(40, 100, 10)
hist(esantion, breaks = intervale, right = F, freq = T, main = 'Mase', xlab = 'axa x', ylab = 'axa y', col = 'lightpink')