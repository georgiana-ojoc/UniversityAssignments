grafice = function(file)
  {
    z = read.table(file, header = T);
    x = z[['AA']];
    y = z[['BB']];
    plot(x, y, type = 'l', main = 'Grafic', sub = 'reprezentare', xlab = 'x', ylab = 'y', col = 'orange')
  }

grafice("test.txt")