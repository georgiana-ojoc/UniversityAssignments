arie_disc = function(N) {
  MC = 0;
  for (i in 1:N) {
    x = runif(1, -1, 1);
    y = runif(1, -1, 1);
    if (x * x + y * y <= 1)
      MC = MC + 1;
  }
  print(4 * MC/N);
}

arie_disc(10000)

arie_parabola_integrare = function(x) {
  return(-2 * x ^ 3 / 3 + 5 * x ^ 2 / 2 - 2 * x);
}

print(arie_parabola_integrare(2))

arie_parabola = function(N) {
  MC = 0;
  for (i in 1:N) {
    x = runif(1, -1 / 2, 2);
    y = runif(1, 0, 9 / 8);
    if (y <= -2 * x * x + 5 * x - 2)
      MC = MC + 1;
  }
  return(45 / 16 * MC/N);
}

print(arie_parabola(10000))

erori_arie_parabola = function(actual, MC) {
  abs = abs(MC - actual);
  rel = abs / abs(actual);
  print(paste("eroarea absoluta = ", abs));
  print(paste("eroarea relativa = ", rel));
}

erori_arie_parabola(arie_parabola_integrare(2), arie_parabola(10000))