def parse(file_name):
    with open(file_name) as file:
        equations = file.read()
    equations = equations.replace(" ", "").split("\n")
    coefficients_matrix = []
    for equation in equations:
        coefficients = []
        index_of_x = index_of_y = -1
        if "x" in equation:
            index_of_x = equation.index("x")
            if index_of_x == 0:
                coefficients.append(1)
            elif equation[index_of_x - 1] == '-':
                coefficients.append(-1)
            else:
                coefficients.append(int(equation[:index_of_x]))
        else:
            coefficients.append(0)
        if "y" in equation:
            index_of_y = equation.index("y")
            if index_of_y == 0 or equation[index_of_y - 1] == "+":
                coefficients.append(1)
            elif equation[index_of_y - 1] == '-':
                coefficients.append(-1)
            else:
                coefficients.append(int(equation[index_of_x + 1:equation.index("y")]))
        else:
            coefficients.append(0)
        if "z" in equation:
            index_of_z = equation.index("z")
            if index_of_z == 0 or equation[index_of_z - 1] == "+":
                coefficients.append(1)
            elif equation[index_of_z - 1] == '-':
                coefficients.append(-1)
            elif "y" in equation:
                coefficients.append(int(equation[index_of_y + 1:equation.index("z")]))
            else:
                coefficients.append(int(equation[index_of_x + 1:equation.index("z")]))
        else:
            coefficients.append(0)
        coefficients_matrix.append(coefficients)
    results = [int(equation.split("=")[-1]) for equation in equations]
    return coefficients_matrix, results
