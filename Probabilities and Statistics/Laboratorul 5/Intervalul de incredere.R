#III. 1
zconfidence_interval = function(nivel_incredere, medie_selectie, esantion, dispersie) {
  alpha = 1 - nivel_incredere;
  sample_mean = medie_selectie;
  n = esantion;
  sigma = sqrt(dispersie);
  critical_z = qnorm(1 - alpha / 2, 0, 1);
  a = sample_mean - critical_z * sigma / sqrt(n);
  b = sample_mean + critical_z * sigma / sqrt(n);
  interval = c(a, b);
  print(interval);
}

zconfidence_interval(0.9, 20, 100, 9)



#III. 2
zconfidence_interval(0.9, 67.53, 25, 100)



#III. 3
zconfidence_interval(0.95, 5, 50, 0.25)



#IV. 1
tconfidence_interval = function(nivel_incredere, medie_selectie, esantion, deviatie) {
  alpha = 1 - nivel_incredere;
  sample_mean = medie_selectie;
  n = esantion;
  s = deviatie;
  critical_t = qt(1 - alpha / 2, n - 1);
  a = sample_mean - critical_t * s / sqrt(n);
  b = sample_mean + critical_t * s / sqrt(n);
  interval = c(a, b);
  print(interval);
}

tconfidence_interval(0.95, 3.3, 60, 0.4)



#IV. 2
tconfidence_interval(0.99, 44.65, 196, 1.5)



#IV. 3. (a)
print("nivelul de incredere = 0.99")
tconfidence_interval(0.99, 12, 49, 1.75)
print("nivelul de incredere = 0.95")
tconfidence_interval(0.95, 12, 49, 1.75)



#IV. 3. (b)
tconfidence_interval(0.95, 13.5, 49, 1.25)