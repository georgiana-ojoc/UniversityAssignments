MC_integration = function(N) {
  sum = 0;
  for (i in 1:N) {
    u = runif(1, 0, 10);
    sum = sum + exp(-u*u/2);
  }
  return(10*sum/N);
}

MC_integration_average = function(k, N) {
  estimates = vector();
  for (i in 1:k)
    estimates[i] = MC_integration(N);
  print(mean(estimates));
  print(sd(estimates));
}

MC_integration_average(30, 20000)

MC_improved_integration = function(N) {
  sum = 0;
  for(i in 1:N) {
    u = rexp(1, 1);
    sum = sum + exp(-u*u)*exp(u);
  }
  return(sum/N);
}

MC_improved_integration_average = function(k, N) {
  estimates = c();
  for (i in 1:k)
    estimates[i] = MC_improved_integration(N);
  print(mean(estimates));
  print(sd(estimates));
}

MC_improved_integration_average(30, 20000)

MC_II_1_b = function(N) {
  sum = 0;
  for(i in 1:N) {
    x = runif(1, 1, 4);
    sum = sum + exp(x);
  }
  return (3 * sum / N);
}

erori_MC_II_1_b = function(MC) {
  actual = 51.87987;
  abs = abs(MC - actual);
  rel = abs / abs(actual);
  print(paste("eroarea absoluta = ", abs));
  print(paste("eroarea relativa = ", rel));
}

erori_MC_II_1_b(MC_II_1_b(20000))

MC_II_1_d = function(N) {
  sum = 0;
  for(i in 1:N) {
    x = runif(1, 1);
    sum = sum + 1 / (4 * x * x - 1);
  }
  return (sum / N);
}

erori_MC_II_1_d = function(MC) {
  e = 2.718281828459045;
  actual = logb(3 / 4, e);
  print(actual);
  abs = abs(MC - actual);
  rel = abs / abs(actual);
  print(paste("eroarea absoluta = ", abs));
  print(paste("eroarea relativa = ", rel));
}

erori_MC_II_1_d(MC_II_1_d(20000))

MC_II_2 = function(N) {
  sum = 0;
  for(i in 1:N) {
    u = rexp(1, 3);
    sum = sum + exp(-2 * u * u) / (3 * exp(-3 * u));
  }
  return(sum/N);
}

erori_MC_II_2 = function(MC) {
  pi = 3.14159265359;
  actual = sqrt(pi / 8);
  print(actual);
  abs = abs(MC - actual);
  rel = abs / abs(actual);
  print(paste("eroarea absoluta = ", abs));
  print(paste("eroarea relativa = ", rel));
}

erori_MC_II_2(MC_II_2(50000))

average_MC_II_2 = function(k, N) {
  estimates = c();
  for (i in 1:k)
    estimates[i] = MC_II_2(N);
  print(mean(estimates));
  print(sd(estimates));
}

average_MC_II_2(3, 50000)