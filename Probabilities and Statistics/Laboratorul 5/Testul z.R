#V. 1
test_proportion = function(alpha, n, succese, p0) {
  p_prim = succese / n
  z_score = (p_prim - p0) / sqrt(p0 * (1 - p0) / n)
  print(paste("z_score = ", z_score))
  if (p_prim < p0) {
    print("Ipoteza asimetrica la stanga.")
    critical_z = qnorm(alpha, 0, 1)
    print(paste("critical_z = ", critical_z))
    if (z_score < critical_z)
      print("Ipoteza nula este respinsa.")
    else
      print("Nu exista suficiente dovezi pentru a respinge ipoteza nula.")
  }
  else if (p_prim > p0) {
    print("Ipoteza asimetrica la dreapta.")
    critical_z = qnorm(1 - alpha, 0, 1)
    print(paste("critical_z = ", critical_z))
    if (z_score > critical_z)
      print("Ipoteza nula este respinsa.")
    else
      print("Nu exista suficiente dovezi pentru a respinge ipoteza nula.")
  }
  else {
    print("Ipoteza simetrica.")
    critical_z = qnorm(1 - alpha / 2, 0, 1)
    print(paste("critical_z = ", critical_z))
    if (abs(z_score) > abs(critical_z))
      print("Ipoteza nula este respinsa.")
    else
      print("Nu exista suficiente dovezi pentru a respinge ipoteza nula.")
  }
}

test_proportion(0.01, 100, 63, 0.6)



#V. 2
test_proportion(0.05, 150, 20, 0.1)



#V. 3
test_proportion(0.01, 42, 17, 0.25)