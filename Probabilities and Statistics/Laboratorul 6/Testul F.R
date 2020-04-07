#IV.1.
F_test = function(test_type, deviatie1, deviatie2, esantion1, esantion2, nivel_semnificatie) {
  F_score = deviatie1 ^ 2 / (deviatie2 ^ 2)
  print(paste("F_score = ", F_score))
  if (test_type == "dreapta") {
    critical_F = qf(1 - nivel_semnificatie, esantion1 - 1, esantion2 - 1)
    print(paste("critical_F = ", critical_F))
    if (F_score < critical_F) {
      print("Nu exista suficiente dovezi.")
    }
    else {
      print("Se respinge ipoteza nula.")
    }
  }
  if (test_type == "simetrica") {
    critical_F_s = qf(nivel_semnificatie / 2, esantion1 - 1, esantion2 - 1)
    print(paste("critical_F_s = ", critical_F_s))
    critical_F_d = qf(1 - nivel_semnificatie / 2, esantion1 - 1, esantion2 - 1)
    print(paste("critical_F_d = ", critical_F_d))
    if (F_score > critical_F_s && F_score < critical_F_d) {
      print("Nu exista suficiente dovezi.")
    }
    else {
      print("Se respinge ipoteza nula.")
    }
  }
}



#IV.2.
esantion1 = read.table("program.txt", header = TRUE)[['A']]
esantion2 = read.table("program.txt", header = TRUE)[['B']]
print("nivel de semnificatie = 1%")
F_test("simetrica", sd(esantion1), sd(esantion2), length(esantion1), length(esantion2), 0.01)
print("nivel de semnificatie = 5%")
F_test("simetrica", sd(esantion1), sd(esantion2), length(esantion1), length(esantion2), 0.05)



#IV.3.
esantion3 = c(12.512, 12.869, 19.098, 15.350, 13.297, 15.589)
esantion4 = c(11.074, 9.686, 12.164, 8.351, 12.182, 11.489)
F_test("simetrica", sd(esantion3), sd(esantion4), length(esantion3), length(esantion4), 0.05)



#V.1.
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



#V.2.
print("nivel de semnificatie = 1%")
T_test_means("simetrica", mean(esantion1), mean(esantion2), sd(esantion1), sd(esantion2), length(esantion1), length(esantion2), 0.01)
print("nivel de semnificatie = 5%")
T_test_means("simetrica", mean(esantion1), mean(esantion2), sd(esantion1), sd(esantion2), length(esantion1), length(esantion2), 0.05)



#V.3.
esantion5 = c(12.512, 12.869, 19.098, 15.350, 13.297, 15.589)
esantion6 = c(11.074, 9.686, 12.164, 8.351, 12.182, 11.489)
T_test_means("simetrica", mean(esantion5), mean(esantion6), sd(esantion5), sd(esantion6), length(esantion5), length(esantion6), 0.01)