volum_sfera = function(N) {
  MC = 0;
  for (i in 1:N) {
    x = runif(1, -1, 1);
    y = runif(1, -1, 1);
    z = runif(1, -1, 1);
    if (x * x + y * y + z * z <= 1)
      MC = MC + 1;
  }
  return(8 * MC/N);
}

pi = 3.14159265358;
volum_sfera_unitate = 4 * pi / 3;

erori_volum_sfera = function(actual, MC) {
  abs = abs(MC - actual);
  rel = abs / abs(actual);
  print(paste("eroarea absoluta = ", abs));
  print(paste("eroarea relativa = ", rel));
}

erori_volum_sfera(volum_sfera_unitate, volum_sfera(10000))
