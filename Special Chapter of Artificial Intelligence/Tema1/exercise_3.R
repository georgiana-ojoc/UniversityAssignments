library("FSA")
library("scales")

bills <- c(46, 33, 39, 37, 36, 30, 48, 32, 49, 35, 30, 48)

cat("sum", '=', sum(bills), '\n')
cat("smallest bill", '=', min(bills), '\n')
cat("biggest bill", '=', max(bills), '\n')
cat("number of bills > 40", '=', length(bills[bills > 40]), '\n')
cat("percentage of bills > 40", '=', label_percent(accuracy = 0.01)(length(bills[bills > 40]) / length(bills)), '\n')
cat("percentage of bills > 40", '=', paste0(round(perc(bills, 40, "gt"), digits = 2), '%'), '\n')
