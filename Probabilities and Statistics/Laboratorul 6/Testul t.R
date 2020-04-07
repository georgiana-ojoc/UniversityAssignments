#II.1.
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



#II.2.
esantion = c(36, 32, 28, 33, 41, 28, 31, 26, 29, 34)
t_test("simetrica", mean(esantion), 34, sd(esantion), length(esantion), 0.01)



#II.3.
t_test("dreapta", 11.9, 11.4, 0.25, 100, 0.05)