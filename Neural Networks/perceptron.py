import gzip
import pickle
from matplotlib import pyplot
import numpy
import time


def get_datasets(file_name):
    file = gzip.open(file_name, "rb")
    training_set, validation_set, testing_set = pickle.load(file, encoding="latin")
    file.close()
    return training_set, validation_set, testing_set


def show_sample(digit):
    pyplot.imshow(digit.reshape(28, 28))
    pyplot.show()


def add_ones(samples):
    samples_number, _ = numpy.shape(samples)
    ones = numpy.ones((samples_number, 1))
    return numpy.hstack((samples, ones))


def one_hot_encode(labels):
    return numpy.eye(numpy.max(labels) + 1)[labels]


def create_weights(samples, labels):
    pixels = numpy.shape(samples)[1]
    digits = numpy.shape(labels)[1]
    return numpy.random.rand(pixels, digits)


def activate(values):
    return numpy.where(values > 0, 1, 0)


def update_weights(sample, label, weights, learning_rate=0.001):
    prediction = activate(numpy.dot(sample, weights))
    added_weights = sample.reshape(-1, 1) * (label - prediction) * learning_rate
    return weights + added_weights


def train_perceptrons(samples, labels, learning_rate=0.001, iterations=30):
    weights = create_weights(samples, labels)
    for iteration in range(iterations):
        for (sample, label) in zip(samples, labels):
            weights = update_weights(sample, label, weights, learning_rate)
    return weights


def classify(samples, weights):
    digits = numpy.dot(samples, weights)
    return numpy.argmax(digits, axis=1)


def test_perceptrons(samples, labels, weights):
    samples_number, = numpy.shape(labels)
    predictions = classify(samples, weights)
    correct = predictions == labels
    return numpy.sum(correct) / float(samples_number)


def perceptron(file_name, learning_rate=0.1, iterations=30):
    training_set, validation_set, testing_set = get_datasets(file_name)
    training_samples = add_ones(training_set[0])
    training_labels = one_hot_encode(training_set[1])
    testing_samples = add_ones(testing_set[0])
    testing_labels = testing_set[1]
    weights = train_perceptrons(training_samples, training_labels, learning_rate, iterations)
    return test_perceptrons(testing_samples, testing_labels, weights)


def print_accuracy(learning_rate=0.001, iterations=30):
    start_time = time.time()
    print("Accuracy for learning rate of {} and {} iterations on the testing set: {}."
          .format(learning_rate, iterations, perceptron("mnist.pkl.gz", learning_rate, iterations)))
    print("Elapsed time: {} seconds.".format(int(time.time() - start_time)))
    print()


def main():
    """
    Accuracy for learning rate of 0.001 and 30 iterations on the testing set : 0.8942.
    Elapsed time: 74 seconds.
    """
    print_accuracy()


if __name__ == '__main__':
    main()
