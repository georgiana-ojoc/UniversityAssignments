import copy
import gzip
import pickle
from matplotlib import pyplot
import numpy
import time


def get_datasets(file_name):
    file = gzip.open(file_name, "rb")
    _training_set, _validation_set, _testing_set = pickle.load(file, encoding="latin")
    file.close()
    return _training_set, _validation_set, _testing_set


def add_ones(samples):
    samples_number, _ = numpy.shape(samples)
    ones = numpy.ones((samples_number, 1))
    return numpy.hstack((samples, ones))


def one_hot_encode(labels):
    return numpy.eye(numpy.max(labels) + 1)[labels]


def create_weights(samples, labels, hidden_neurons=100):
    pixels = numpy.shape(samples)[1]
    digits = numpy.shape(labels)[1]
    hidden_weights = numpy.random.randn(pixels, hidden_neurons) / numpy.sqrt(pixels)
    output_weights = numpy.random.randn(hidden_neurons + 1, digits) / numpy.sqrt(hidden_neurons + 1)
    return {"hidden": hidden_weights, "output": output_weights}


def create_batches(samples, labels, batch_size=10):
    sample_number = numpy.shape(samples)[0]
    batch_number = sample_number / batch_size
    permutation = numpy.random.permutation(sample_number)
    shuffled_samples = samples[permutation, :]
    shuffled_labels = labels[permutation, :]
    return zip(numpy.vsplit(shuffled_samples, batch_number), numpy.vsplit(shuffled_labels, batch_number))


def initialize_gradients(weights):
    return {"hidden": numpy.zeros(numpy.shape(weights["hidden"])),
            "output": numpy.zeros(numpy.shape(weights["output"]))}


def activate(values):
    return 1 / (1 + numpy.exp(-values))


def derive(values):
    return values * (1 - values)


def softmax(values):
    exponentials = numpy.exp(values)
    return exponentials / numpy.sum(exponentials, axis=1, keepdims=True)


def feed_forward(samples, weights):
    hidden_activations = activate(numpy.dot(samples, weights["hidden"]))
    output_activations = softmax(numpy.dot(add_ones(hidden_activations), weights["output"]))
    return {"hidden": hidden_activations, "output": output_activations}


def back_propagate(samples, labels, weights, activations):
    output_errors = activations["output"] - labels
    output_gradients = numpy.dot(numpy.transpose(add_ones(activations["hidden"])), output_errors)
    hidden_errors = derive(activations["hidden"]) * numpy.dot(output_errors, numpy.transpose(weights["output"][:-1, :]))
    hidden_gradients = numpy.dot(numpy.transpose(samples), hidden_errors)
    return {"hidden": hidden_gradients, "output": output_gradients}


def train(samples, labels, iterations, batch_size, learning_rate, momentum, regularization):
    sample_number = numpy.shape(samples)[0]
    weights = create_weights(samples, labels)
    iterations_weights = list()
    start_time = time.time()
    for iteration in range(iterations):
        for sample_batch, label_batch in create_batches(samples, labels, batch_size):
            added_gradients = initialize_gradients(weights)
            for batch in range(batch_size):
                sample = sample_batch[batch:batch + 1, :]
                label = label_batch[batch:batch + 1, :]
                activations = feed_forward(sample, weights)
                gradients = back_propagate(sample, label, weights, activations)
                for layer in ["hidden", "output"]:
                    added_gradients[layer] = momentum * added_gradients[layer] - learning_rate * gradients[layer]
            for layer in ["hidden", "output"]:
                weights[layer] = (1 - learning_rate * regularization / sample_number) * weights[layer] + \
                                 added_gradients[layer] / batch_size
        iterations_weights.append(copy.deepcopy(weights))
        # end_time = time.time()
        # print("Iteration {}: {} cost ({} seconds)"
        #       .format(iteration + 1, compute_cost(labels, feed_forward(samples, weights)["output"]),
        #               end_time - start_time))
        # start_time = end_time
    return iterations_weights


def compute_cost(labels, activations):
    return -numpy.mean(labels * numpy.log(activations) + (1 - labels) * numpy.log(1 - activations))


def classify(samples, weights):
    digits = feed_forward(samples, weights)["output"]
    return numpy.argmax(digits, axis=1)


def compute_accuracy(samples, labels, weights):
    samples_number, = numpy.shape(labels)
    predictions = classify(samples, weights)
    correct = predictions == labels
    return numpy.sum(correct) / float(samples_number)


def show_accuracies(accuracies):
    pyplot.figure()
    training, = pyplot.plot(accuracies["training"], color="blue", label="training")
    validation, = pyplot.plot(accuracies["validation"], color="green", label="validation")
    testing, = pyplot.plot(accuracies["testing"], color="violet", label="testing")
    pyplot.legend(handles=[training, validation, testing])
    pyplot.xlabel("Iterations")
    pyplot.ylabel("Accuracies")
    pyplot.show()


def test(file_name="mnist.pkl.gz", iterations=30, batch_size=10, learning_rate=0.01, momentum=0.9, regularization=0.1):
    training_set, validation_set, testing_set = get_datasets(file_name)
    training_samples = add_ones(training_set[0])
    training_labels = training_set[1]
    validation_samples = add_ones(validation_set[0])
    validation_labels = validation_set[1]
    testing_samples = add_ones(testing_set[0])
    testing_labels = testing_set[1]
    iterations_weights = train(training_samples, one_hot_encode(training_labels), iterations, batch_size, learning_rate,
                               momentum, regularization)
    accuracies = {"training": [], "validation": [], "testing": []}
    for iteration_weights in iterations_weights:
        accuracies["training"].append(compute_accuracy(training_samples, training_labels, iteration_weights))
        accuracies["validation"].append(compute_accuracy(validation_samples, validation_labels, iteration_weights))
        accuracies["testing"].append(compute_accuracy(testing_samples, testing_labels, iteration_weights))
    print("{} iterations, {} batch size, {} learning rate, {} momentum, {} regularization:"
          .format(iterations, batch_size, learning_rate, momentum, regularization))
    print("{} training accuracy".format(max(accuracies["training"])))
    print("{} validation accuracy".format(max(accuracies["validation"])))
    print("{} testing accuracy".format(max(accuracies["testing"])))
    show_accuracies(accuracies)


def main():
    """
    30 iterations, 10 batch size, 0.01 learning rate, 0.9 momentum, 0.1 regularization:
    0.9488 training accuracy
    0.952 validation accuracy
    0.9478 testing accuracy
    """
    test()


if __name__ == '__main__':
    main()
