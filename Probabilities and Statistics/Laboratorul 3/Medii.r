no_days_1 = function() {
  no_days = 2;
  last_errors = c(31, 27);
  no_errors = 27;
  while (no_errors > 0) {
    lambda = min(last_errors);
    no_errors = rpois(1, lambda);
    last_errors = c(last_errors[2], no_errors);
    no_days = no_days + 1;
  }
  return(no_days);
}

MC_no_days_1 = function(N) {
  s = 0;
  for (i in 1:N)
    s = s + no_days_1();
  print(s/N);
}

MC_no_days_1(10000)

III_1 = function() {
  no_days = 2;
  last_errors = c(15, 13, 9);
  no_errors = 9;
  while (no_errors > 0) {
    lambda = mean(last_errors);
    no_errors = rpois(1, lambda);
    last_errors = c(last_errors[2:3], no_errors);
    no_days = no_days + 1;
  }
  return(no_days);
}

MC_III_1 = function(N) {
  s = 0;
  for (i in 1:N)
      s = s + III_1();
  print(s/N);
}

MC_III_1(10)

no_days_2 = function() {
  no_days = 2;
  last_errors = c(28, 22, 18);
  no_errors = 18;
  while (no_errors > 0) {
    lambda = min(last_errors);
    no_errors = rpois(1, lambda);
    last_errors = c(last_errors[2:3], no_errors);
    no_days = no_days + 1;
  }
  return(no_days);
}

MC_no_days_2 = function(N) {
  s = 0;
  for (i in 1:N)
    if (no_days_2() > 21)
      s = s + 1;
  return(s/N);
}

MC_no_days_2_with_error = function() {
  alpha = 1 - 0.95;
  z = qnorm(alpha / 2);
  epsilon = 0.01;
  p = MC_no_days_2(10000);
  print(p);
  N_min = p * (1 - p) * (z / epsilon) ^ 2;
  print(N_min);
  MC_no_days_2(N_min + 1);
  N_min = 1 / 4 * (z / epsilon) ^ 2;
  print(N_min);
  MC_no_days_2(N_min + 1);
}

MC_no_days_2_with_error()

IV_1 = function(N) {
  x = (dgeom(seq(0, N, 1), 0.3, log = FALSE));
  y = (dgeom(seq(0, N, 1), 0.5, log = FALSE));
  s = 0;
  for (i in 1:N)
    if (x[i] > (y[i] * y[i]))
      s = s + 1;
  return(s / N);
}

IV_1_with_error = function() {
  alpha = 1 - 0.95;
  z = qnorm(alpha / 2);
  epsilon = 0.005;
  p = IV_1(10000);
  print(p);
  N_min = p * (1 - p) * (z / epsilon) ^ 2;
  print(N_min);
  IV_1(N_min + 1);
  N_min = 1 / 4 * (z / epsilon) ^ 2;
  print(N_min);
  IV_1(N_min + 1);
}

IV_1_with_error()

#IV.2.
computere = function() {
  no_infected = rbinom(1, 39, 0.2);
  no_uninfected = 39 - no_infected;
  no_days = 1;
  p = 0.2;
  while (p < 0.999) {
    p = 1 - 0.8 ^ (40 - no_infected);
    no_infected = rbinom(1, no_uninfected, p);
    no_uninfected = no_uninfected - no_infected + min(no_infected, 10);
    no_days = no_days + 1;
  }
  return(no_uninfected);
}

#IV.2.(a)
toate_computerele = function(N) {
  sum = 0;
  for(i in 1:N)
    if (computere() == 40)
      sum = sum + 1;
    print(paste("probabilitatea infectarii tuturor computerelor = ", sum / N));
}

toate_computerele(10000)

#IV.2.(b)
minim_15_computere = function(N) {
  sum = 0;
  for(i in 1:N)
    if (computere() >= 15)
      sum = sum + 1;
    print(paste("probabilitatea infectarii a cel putin 15 computere = ", sum / N));
}

minim_15_computere(10000)

#IV.2.(c)
N_cu_eroare = function() {
  alpha = 1 - 0.95;
  z = qnorm(alpha / 2);
  epsilon = 0.01;
  p = 0.2;
  N_min = p * (1 - p) * (z / epsilon) ^ 2;
  print(paste("numarul de simulari = ", N_min));
  N_min = 1 / 4 * (z / epsilon) ^ 2;
  print(paste("numarul de simulari = ", N_min));
}

N_cu_eroare()