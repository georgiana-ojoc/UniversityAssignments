#C1
a = 2
pi = 3.14159265358
volum_total = 8 * a * a
volum_exact = pi * a * a / 2
print(paste("Volumul exact = ", volum_exact))

volum_estimat = function(a, N) {
  MC = 0;
  for (i in 1:N) {
    u = runif(1, -sqrt(a), sqrt(a));
    v = runif(1, -sqrt(a), sqrt(a));
    w = runif(1, -a, a);
    if (u * u + v * v <= w)
      MC = MC + 1;
  }
  return(volum_total * MC/N);
}

erori_volum = function(volum_exact, volum_estimat) {
  absoluta = abs(volum_estimat - volum_exact);
  relativa = absoluta / abs(volum_exact);
  print(paste("eroarea absoluta = ", absoluta));
  print(paste("eroarea relativa = ", relativa));
}

print("N = 10000");
erori_volum(volum_exact, volum_estimat(a, 10000))
print("N = 20000");
erori_volum(volum_exact, volum_estimat(a, 20000))
print("N = 50000");
erori_volum(volum_exact, volum_estimat(a, 50000))



#C2
arie_totala = 18 / 5 * (3 / 4 + 12 / 5)
arie_exacta = 63 / 20
print(paste("aria exacta = ", arie_exacta))

arie_estimata = function(N) {
  MC = 0;
  for (i in 1:N) {
    x = runif(1, 0, 18 / 5);
    y = runif(1, -12 / 5, 3 / 4);
    if (x - 2 * y <= 0 & 2 * x - 3 * y >= 0 & 3 * x + 2 * y <= 6)
      MC = MC + 1;
  }
  return(arie_totala * MC / N);
}

print(paste("aria estimata = ", arie_estimata(10000)))



#C3. (a)
integrala_exacta = 3 * sqrt(3) / 8 + 13 / 24
print(paste("integrala exacta = ", integrala_exacta))

integrala_estimata = function(N) {
  sum = 0;
  for (i in 1:N) {
    x = runif(1, 0, pi / 3);
    sum = sum + sin(x) ^ 3 + cos(x) ^ 3;
  }
  return(pi / 3 * sum / N);
}

print(paste("integrala estimata = ", integrala_estimata(10000)))



#C3. (b)
integrala_exacta = pi / (2 * sqrt(2))
print(paste("integrala exacta = ", integrala_exacta))

integrala_estimata = function(N) {
  sum = 0;
  for (i in 1:N) {
    x = runif(1, 0, 100);
    sum = sum + 1 / (2 * x ^ 2  + 1);
  }
  return(100 * sum / N);
}

print(paste("integrala estimata = ", integrala_estimata(10000)))



#C4
timp_servire = function(N) {
  sum = 0;
  for(i in 1:N) {
    latenta = rexp(1,1);
    cerere1 = rgamma(1, shape = 6, scale = 4);
    cerere2 = rgamma(1, shape = 5, scale = 3);
    random = runif(1, 0, 1);
    if (random <= 0.35) {
      sum = sum + cerere1;
    }
    else {
      sum = sum + cerere2;
    }
    sum = sum + latenta;
  }
  return(sum / N);
}

print(paste("timpul mediu de servire = ", timp_servire(10000), "milisecunde"))



#C7. (a)
conturi = function() {
  no_infected = rbinom(1, 24, 0.3);
  no_uninfected = 24 - no_infected;
  no_days = 1;
  p = 0.3;
  while (p < 0.999) {
    p = 1 - 0.7 ^ (25 - no_infected);
    no_infected = rbinom(1, no_uninfected, p);
    no_uninfected = no_uninfected - no_infected + min(no_infected, 4);
    no_days = no_days + 1;
  }
  return(no_uninfected);
}

print(paste("probabilitatea infectarii conturilor cel putin o data = ", 1 - conturi() / 25))



#C7. (b)
toate_conturile = function(N) {
  sum = 0;
  for(i in 1:N)
    if (conturi() == 25)
      sum = sum + 1;
  print(paste("probabilitatea infectarii tuturor conturilor = ", sum / N));
}

toate_conturile(10000)



#C7. (c)
N_cu_eroare = function() {
  alpha = 1 - 0.99;
  z = qnorm(alpha / 2);
  epsilon = 0.01;
  p = 0.3;
  N_min = p * (1 - p) * (z / epsilon) ^ 2;
  print(paste("numarul de simulari = ", N_min));
  N_min = 1 / 4 * (z / epsilon) ^ 2;
  print(paste("numarul de simulari = ", N_min));
}

N_cu_eroare()