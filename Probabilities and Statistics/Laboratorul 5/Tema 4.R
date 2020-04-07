#D1
zconfidence_interval = function(nivel_incredere, medie_selectie, esantion, deviatie) {
  nivel_semnificatie = 1 - nivel_incredere;
  sample_mean = medie_selectie;
  n = esantion;
  sigma = deviatie;
  critical_z = qnorm(1 - nivel_semnificatie / 2, 0, 1);
  a = sample_mean - critical_z * sigma / sqrt(n);
  b = sample_mean + critical_z * sigma / sqrt(n);
  interval = c(a, b);
  print(interval);
}

zconfidence_interval(0.95, 140, 8, 10)



#D2
tconfidence_interval = function(nivel_incredere, medie_selectie, esantion, dispersie) {
  nivel_semnificatie = 1 - nivel_incredere;
  sample_mean = medie_selectie;
  n = esantion;
  s = sqrt(dispersie);
  critical_t = qt(1 - nivel_semnificatie / 2, n - 1);
  a = sample_mean - critical_t * s / sqrt(n);
  b = sample_mean + critical_t * s / sqrt(n);
  interval = c(a, b);
  print(interval);
}

tconfidence_interval(0.99, 35, 195, 16)



test_proportion = function(nivel_semnificatie, n, succese, p0) {
  p_prim = succese / n
  z_score = (p_prim - p0) / sqrt(p0 * (1 - p0) / n)
  print(paste("z_score = ", z_score))
  if (p_prim < p0) {
    print("Ipoteza asimetrica la stanga.")
    critical_z = qnorm(nivel_semnificatie, 0, 1)
    print(paste("critical_z = ", critical_z))
    if (z_score < critical_z)
      print("Ipoteza nula este respinsa.")
    else
      print("Nu exista suficiente dovezi pentru a respinge ipoteza nula.")
  }
  else if (p_prim > p0) {
    print("Ipoteza asimetrica la dreapta.")
    critical_z = qnorm(1 - nivel_semnificatie, 0, 1)
    print(paste("critical_z = ", critical_z))
    if (z_score > critical_z)
      print("Ipoteza nula este respinsa.")
    else
      print("Nu exista suficiente dovezi pentru a respinge ipoteza nula.")
  }
  else {
    print("Ipoteza simetrica.")
    critical_z = qnorm(1 - nivel_semnificatie / 2, 0, 1)
    print(paste("critical_z = ", critical_z))
    if (abs(z_score) > abs(critical_z))
      print("Ipoteza nula este respinsa.")
    else
      print("Nu exista suficiente dovezi pentru a respinge ipoteza nula.")
  }
}



#D3
print("nivel de semnificatie = 1%")
test_proportion(0.01, 100, 32, 0.3)

print("nivel de semnificatie = 5%")
test_proportion(0.05, 100, 32, 0.3)



#D4
print("nivel de semnificatie = 1%")
test_proportion(0.01, 100, 48, 0.509)

print("nivel de semnificatie = 5%")
test_proportion(0.05, 100, 48, 0.509)