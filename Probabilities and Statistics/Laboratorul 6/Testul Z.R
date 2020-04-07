#I.1.
Z_test = function(test_type, sample_mean, population_mean, dispersie, esantion, nivel_semnificatie) {
  z_score = (sample_mean - population_mean) / (sqrt(dispersie) / sqrt(esantion))
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



#I.2.
Z_test("stanga", 88, 90, 144, 49, 0.05)



#I.3.
Z_test("stanga", 75, 85, 17, 36, 0.01)



#III.1.
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



#III.2.
Z_test_means("simetrica", 160, 155, 3.24, 2.25, 80, 70, 0.01)



#III.3.
Z_test_means("simetrica", 22.8, 23.3, 1.3, 1.9, 100, 100, 0.01)