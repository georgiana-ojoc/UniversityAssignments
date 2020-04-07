b = c(46, 33, 39, 37, 46, 30, 48, 32, 49, 35, 30, 48)

facturi = function(b) {
  print(max(b))
  print(min(b))
  average = 0
  for (i in 1:length(b))
    average = average + b[i]
  average = average/length(b)
  print(average)
  print(sum(b))
  total = 0
  for (i in 1:length(b))
    if (b[i] < 35)
      total = total + 1
  print(total)
  total = 0
  for (i in 1:length(b))
    if (b[i] > 40)
      total = total + 1
  print(total*100/12)
}

facturi(b)