import numpy

from equations_parser import parse
from python_solver import python_solve
from numpy_solver import numpy_solve


def main():
    coefficients, results = parse("equations.txt")
    print("Coefficients:")
    print(numpy.array(coefficients))
    print("Results:")
    print(numpy.array(results))

    print("\nPython solution:")
    python_solution = python_solve(coefficients, results)
    if python_solution is None:
        print("The system of equations does not have a unique solution.")
    else:
        print(numpy.array(python_solution))

    print("\nNumPy solution:")
    numpy_solution = numpy_solve(numpy.array(coefficients), numpy.array(results))
    if numpy_solution is None:
        print("The system of equations does not have a unique solution.")
    else:
        print(numpy_solution)

    if python_solution is not None and numpy_solution is not None:
        if numpy.array_equal(numpy.around(numpy.array(python_solution), 3), numpy.around(numpy_solution, 3)):
            print("\nBoth scripts give the same solution.")


if __name__ == '__main__':
    main()
