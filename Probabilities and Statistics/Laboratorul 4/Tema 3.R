#B2
x = "0100110"
y = "11000101001100"

numbers = function(string, first, last) {
  number = 0;
  for (index in first:last)
    number = number * 2 + string[index];
  return(number);
}

substring = function(x, y) {
  x = as.vector(unlist(strsplit(x, split="")), mode = "numeric");
  y = as.vector(unlist(strsplit(y, split="")), mode = "numeric");
  n = length(x);
  m = length(y);
  p = runif(1, 0, n ^ 2 * m * log(n ^ 2 * m));
  r = numbers(x, 1, n) %% p;
  for (i in 1:(m - n + 1))
    if (numbers(y, i, i + n - 1) %% p == r && identical(y[i:(i + n - 1)], x, n))
      return(i);
  return(-1);
}

print(paste("pozitia de start = ", substring(x, y)))



#B3
u = "10010"
U = c("001", "1100", "10010", "011100", "1001", "11")

element = function(u, U) {
  u = as.vector(unlist(strsplit(u, split="")), mode = "numeric");
  n = length(u);
  p = runif(1, 0, n ^ 2);
  r1 = numbers(u, 1, n) %% p;
  m = length(U);
  r2 = vector(mode = "numeric", length = m);
  for (i in 1:m) {
    vector = as.vector(unlist(strsplit(U[i], split="")), mode = "numeric");
    r2[i] = numbers(vector, 1, length(vector)) %% p;
    if (r2[i] == r1 && identical(vector, u, n))
      return("u apartine U");
  }
  return("u nu apartine U");
}

print(element(u, U))