import numpy
from matplotlib import pyplot


def add_ones(samples):
    """
    :param samples: array of input arrays
    :return: array of training arrays to which 1 was added at the end for bias
    """
    sample_number, inputs = numpy.shape(samples)
    ones = numpy.ones((sample_number, 1))
    return numpy.hstack((samples, ones))


def create_weights(inputs=2, hidden_layer_neurons=2, outputs=1):
    """
    :param inputs: number of input (default 2)
    :param hidden_layer_neurons: number of neurons in hidden layer (default 2)
    :param outputs: number of output (default 1)
    :return: randomly generated weights for hidden layer and output layer
    """
    hidden_layer_weights = numpy.random.uniform(0.1, 0.9, (inputs + 1, hidden_layer_neurons))
    output_layer_weights = numpy.random.uniform(0.1, 0.9, (hidden_layer_neurons + 1, outputs))
    return {"hidden": hidden_layer_weights, "output": output_layer_weights}


def sigmoid(inputs):
    """
    :param inputs: array of arrays
    :return: sigmoid value of each input
    """
    return 1 / (1 + numpy.exp(-inputs))


def derivative(inputs):
    """
    :param inputs: array of arrays
    :return: sigmoid derivative value of each input
    """
    return inputs * (1 - inputs)


def mean_squared_error(errors, samples=4, outputs=1):
    """
    :param errors: array of differences between expected output and computed output
    :param samples: number of samples (default 4)
    :param outputs: number of output values (default 1)
    :return: mean squared error value of each error
    """
    return numpy.sum(numpy.power(errors, 2)) / (samples * outputs)


def train(samples, labels, epochs=10000, learning_rate=0.5, minimum_loss=0.001, sample_number=4,
          input_number=2, hidden_layer_neurons=2, output_number=1):
    """
    :param samples: array of input arrays
    :param labels: expected output array
    :param epochs: number of iterations (default 10000)
    :param learning_rate: step size (default 0.5)
    :param minimum_loss: minimum mean squared error value (default 0.001)
    :param sample_number: number of samples
    :param input_number: number of inputs (default 2)
    :param hidden_layer_neurons: number of neurons in hidden layer (default 2)
    :param output_number: number of outputs (default 1)
    :return: mean squared error value for each iteration and computed output values
    """
    losses = list()
    outputs = None
    weights = create_weights(input_number, hidden_layer_neurons, output_number)
    for epoch in range(epochs):
        """
        Computes outputs and mean squared error (feed forward).
        """
        hidden_outputs = add_ones(sigmoid(numpy.dot(samples, weights["hidden"])))
        outputs = sigmoid(numpy.dot(hidden_outputs, weights["output"]))
        errors = labels - outputs
        losses.append(mean_squared_error(errors, sample_number, output_number))
        """
        Computes gradients and updates weights (backpropagation).
        """
        output_errors = derivative(outputs) * errors
        weights["output"] += numpy.transpose(numpy.sum(learning_rate * hidden_outputs * output_errors,
                                                       axis=0, keepdims=True))
        hidden_errors = derivative(hidden_outputs[:, :-1]) * output_errors * numpy.transpose(weights["output"][:-1, :])
        for neuron in range(hidden_layer_neurons):
            weights["hidden"][:, neuron:neuron + 1] += numpy.transpose(numpy.sum(learning_rate * samples *
                                                                                 hidden_errors[:, neuron:neuron + 1],
                                                                                 axis=0, keepdims=True))
        if losses[-1] < minimum_loss:
            break
    return losses, outputs


def show_performance(losses):
    """
    Plots loss versus epoch graph.
    :param losses: array of loss values
    """
    pyplot.figure()
    pyplot.plot(losses)
    pyplot.xlabel("Epochs")
    pyplot.ylabel("Loss")
    pyplot.show()


def main():
    """
    Reads information from file.
    Shows performance and result of algorithm.
    """
    with open("data.txt") as file:
        data = file.read()
    data = data.split('\n')
    epochs = int(data[0])
    learning_rate = float(data[1])
    samples = add_ones(numpy.array([numpy.array(list(map(int, line.split()))) for line in data[2:6]]))
    labels = numpy.array([numpy.array([int(label)]) for label in data[6].split()])
    losses, outputs = train(samples, labels, epochs, learning_rate)
    show_performance(losses)
    print(str(numpy.transpose(outputs)).replace('[', '').replace(']', ''))


if __name__ == '__main__':
    main()
