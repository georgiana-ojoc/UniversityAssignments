from scipy.special import polygamma
import matplotlib.pyplot as plot
import numpy

with open("instances.txt") as file:
    instances = file.read().split()
instances = numpy.array(instances, dtype=numpy.float)
print(instances)

plot.plot(range(1, 101), instances, "violet", label="Instances")
plot.xlabel("Index")
plot.ylabel("Value")
plot.title("Instances")
plot.legend()
plot.savefig("Gamma instances.eps", dpi=300)
plot.show()

runs = 100
number = numpy.shape(instances)[0]
_sum = numpy.sum(instances)
logarithm_sum = numpy.sum(numpy.log(instances))
mean = numpy.mean(instances)


def first_derivative(_rate):
    return number * numpy.log(_rate / mean) - number * polygamma(0, _rate) + logarithm_sum


def second_derivative(_rate):
    return number / _rate - number * polygamma(1, _rate)


# Newton's method
rate = 1
newton_rates = []
newton_betas = []
for run in range(1, runs + 1):
    rate -= first_derivative(rate) / second_derivative(rate)
    newton_rates += [rate]
    newton_betas += [mean / rate]
newton_rates = numpy.array(newton_rates)
newton_betas = numpy.array(newton_betas)
print(newton_rates)
print(newton_betas)

# Gradient Ascent method
learning_rate = 0.01
rate = 1
gradient_ascent_rates = []
gradient_ascent_betas = []
for run in range(1, runs + 1):
    rate += learning_rate * first_derivative(rate)
    gradient_ascent_rates += [rate]
    gradient_ascent_betas += [mean / rate]
gradient_ascent_rates = numpy.array(gradient_ascent_rates)
gradient_ascent_betas = numpy.array(gradient_ascent_betas)
print(gradient_ascent_rates)
print(gradient_ascent_betas)

plot.plot(range(1, runs + 1), newton_rates, "green", label="Newton")
plot.plot(range(1, runs + 1), gradient_ascent_rates, "purple", label="Gradient Ascent")
plot.xlabel("Run")
plot.ylabel("Value")
plot.title("Estimated rate parameter of Gamma distribution using MLE")
plot.legend()
plot.savefig("Gamma rate.eps", dpi=300)
plot.show()

plot.plot(range(1, runs + 1), newton_betas, "orange", label="Newton")
plot.plot(range(1, runs + 1), gradient_ascent_betas, "blue", label="Gradient Ascent")
plot.xlabel("Run")
plot.ylabel("Value")
plot.title("Estimated beta parameter of Gamma distribution using MLE")
plot.legend()
plot.savefig("Gamma beta.eps", dpi=300)
plot.show()
