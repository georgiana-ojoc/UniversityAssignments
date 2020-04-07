#II. 1
selection_mean = function(file) {
  x = scan(file);
  m = mean(x);
  print(m);
}

selection_mean("history.txt")