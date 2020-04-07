variabila_aleatoare = function(matrice) {
  random = runif(1, 0, 1);
  first = 0;
  index = 1;
  while (random >= first & random >= (first + matrice[2, index]) & index < ncol(matrice)) {
    index = index + 1;
    first = first + matrice[2, index];
  }
  return (matrice[1, index]);
}

matrice = matrix(c(2, 6, 4, 9, 5, 0.25, 0.30, 0.15, 0.2, 0.1), nrow = 2, ncol = 5, byrow = T);
variabila_aleatoare(matrice)