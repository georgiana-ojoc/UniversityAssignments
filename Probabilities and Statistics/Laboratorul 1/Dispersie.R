dispersie = function(x, p) {
medie = sum(p*x);
dispersie = sum(p*(x - medie)^2);
print (dispersie)
}

x = c(23, 32, 31, 27, 27, 33, 25, 21)
p = c(1/8, 1/16, 1/8, 1/16, 1/8, 1/16, 1/8, 5/16)
dispersie(x, p)