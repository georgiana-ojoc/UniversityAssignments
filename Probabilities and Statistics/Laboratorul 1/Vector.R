vector = function(file) {
  x = scan("vector.txt");
  y = x / sum(x);
  print(y);
  z = (x - min(x)) / max(x);
  print(z);
}

vector("vector.txt")