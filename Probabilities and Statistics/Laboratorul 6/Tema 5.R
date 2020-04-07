#E1
t_test = function(test_type, sample_mean, population_mean, deviatie, esantion, nivel_semnificatie) {
  t_score = (sample_mean - population_mean) / (deviatie / sqrt(esantion))
  print(paste("t_score = ", t_score))
  if (test_type == "stanga") {
    critical_t = qt(nivel_semnificatie, esantion - 1)
    print(paste("critical_t = ", critical_t))
    if (t_score > critical_t) {
      print("Nu exista suficiente dovezi.")
    }
    else {
      print("Se respinge ipoteza nula.")
    }
  }
  if (test_type == "dreapta") {
    critical_t = qt(1 - nivel_semnificatie, esantion - 1)
    print(paste("critical_t = ", critical_t))
    if (t_score < critical_t) {
      print("Nu exista suficiente dovezi.")
    }
    else {
      print("Se respinge ipoteza nula.")
    }
  }
  if (test_type == "simetrica") {
    critical_t = qt(1 - nivel_semnificatie / 2, esantion - 1)
    print(paste("critical_t = ", critical_t))
    if (abs(t_score) < abs(critical_t)) {
      print("Nu exista suficiente dovezi.")
    }
    else {
      print("Se respinge ipoteza nula.")
    }
  }
}

esantion = c(1.64, 1.54, 1.56, 1.57, 1.44, 1.48, 1.56)
t_test("stanga", mean(esantion), 1.6, sd(esantion), length(esantion), 0.01)



#E3
Z_test_means = function(test_type, sample1_mean, sample2_mean, deviation1, deviation2, esantion1, esantion2, nivel_semnificatie) {
  combined_deviation = sqrt(deviation1 ^ 2 / esantion1 + deviation1 ^ 2 / esantion2)
  z_score = (sample1_mean - sample2_mean) / combined_deviation
  print(paste("z_score = ", z_score))
  if (test_type == "stanga") {
    critical_z = qnorm(nivel_semnificatie, 0, 1)
    print(paste("critical_z = ", critical_z))
    if (z_score > critical_z) {
      print("Nu exista suficiente dovezi.")
    }
    else {
      print("Se respinge ipoteza nula.")
    }
  }
  if (test_type == "dreapta") {
    critical_z = qnorm(1 - nivel_semnificatie, 0, 1)
    print(paste("critical_z = ", critical_z))
    if (z_score < critical_z) {
      print("Nu exista suficiente dovezi.")
    }
    else {
      print("Se respinge ipoteza nula.")
    }
  }
  if (test_type == "simetrica") {
    critical_z = qnorm(1 - nivel_semnificatie / 2, 0, 1)
    print(paste("critical_z = ", critical_z))
    if (abs(z_score) < abs(critical_z)) {
      print("Nu exista suficiente dovezi.")
    }
    else {
      print("Se respinge ipoteza nula.")
    }
  }
}

print("nivel de semnificatie = 1%")
Z_test_means("simetrica", 3, 3.5, 0.6, 0.4, 100, 100, 0.01)
print("nivel de semnificatie = 5%")
Z_test_means("simetrica", 3, 3.5, 0.6, 0.4, 100, 100, 0.05)



#E5
T_test_means = function(test_type, sample1_mean, sample2_mean, deviation1, deviation2, esantion1, esantion2, nivel_semnificatie) {
  F_score = deviation1 ^ 2 / (deviation2 ^ 2)
  critical_F_s = qf(nivel_semnificatie / 2, esantion1 - 1, esantion2 - 1)
  critical_F_d = qf(1 - nivel_semnificatie / 2, esantion1 - 1, esantion2 - 1)
  if (F_score < critical_F_s || F_score > critical_F_d) {
    freedom_degrees = min(esantion1 - 1, esantion2 - 1)
    combined_deviation = sqrt(deviation1 ^ 2 / esantion1 + deviation1 ^ 2 / esantion2)
  }
  else {
    freedom_degrees = esantion1 + esantion2 - 2
    combined_deviation = sqrt(deviation1 ^ 2 / (esantion1 - 1) + deviation1 ^ 2 / (esantion2 - 2)) * sqrt(1 / esantion1 + 1 / esantion2)
  }
  t_score = (sample1_mean - sample2_mean) / combined_deviation
  print(paste("t_score = ", t_score))
  if (test_type == "stanga") {
    critical_t = qt(nivel_semnificatie, freedom_degrees)
    print(paste("critical_t = ", critical_t))
    if (t_score > critical_t) {
      print("Nu exista suficiente dovezi.")
    }
    else {
      print("Se respinge ipoteza nula.")
    }
  }
  if (test_type == "dreapta") {
    critical_t = qt(1 - nivel_semnificatie, freedom_degrees)
    print(paste("critical_t = ", critical_t))
    if (t_score < critical_t) {
      print("Nu exista suficiente dovezi.")
    }
    else {
      print("Se respinge ipoteza nula.")
    }
  }
  if (test_type == "simetrica") {
    critical_t = qt(1 - nivel_semnificatie / 2, freedom_degrees)
    print(paste("critical_t = ", critical_t))
    if (abs(t_score) < abs(critical_t)) {
      print("Nu exista suficiente dovezi.")
    }
    else {
      print("Se respinge ipoteza nula.")
    }
  }
}

T_test_means("simetrica", 21, 20, 1.2, 1.1, 66, 68, 0.01) 