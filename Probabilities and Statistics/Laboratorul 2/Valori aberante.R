esantion = c(1, 91, 38, 72, 13, 27, 11, 85, 5, 22, 20, 19, 8, 17, 11, 15, 13, 23, 14, 17)

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

outliers_iqr(esantion)