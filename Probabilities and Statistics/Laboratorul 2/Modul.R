esantion = c(5, 3, 5, 5, 7, 3, 3, 7)

modul = function(esantion) {
  freq = unique(esantion);
  tab = tabulate(match(esantion, freq));
  print(max(tab));
  print(freq[tab == max(tab)])
}

modul(esantion)