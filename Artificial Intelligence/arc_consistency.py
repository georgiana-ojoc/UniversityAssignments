from collections import deque


def satisfies_constraint(start_value, end_values):
    """
    :param start_value: color of start node
    :param end_values: color list of end node
    :return: True if end node has another color, False otherwise
    """
    for end_value in end_values:
        if start_value != end_value:
            return True
    return False


def remove_inconsistent_values(arc, domain):
    """
    :param arc: current arc
    :param domain: list of color lists for each node
    :return: True if at least one color was removed from color list of start node, False otherwise
    """
    start, end = arc
    removed = False
    for value in domain[start]:
        if not satisfies_constraint(value, domain[end]):
            domain[start].remove(value)
            removed = True
    return removed


def get_entering_arcs(end_node, graph):
    """
    :param end_node: start node of current arc
    :param graph: list of neighbor lists for each node
    :return: list of arcs that enter in specified node
    """
    next_list = []
    for start_node in graph:
        if start_node == end_node:
            continue
        for neighbor in graph.get(start_node):
            if neighbor == end_node:
                next_list += [(start_node, end_node)]
    return next_list


def is_inconsistent(arc, domain):
    """
    :param arc: current arc
    :param domain: color lists
    :return: True if start node or end node has no color, False otherwise
    """
    start, end = arc
    if len(domain[start]) == 0 or len(domain[end]) == 0:
        return True
    return False


def arc_consistency(graph, domain):
    """
    :param graph: list of neighbor lists for each node
    :param domain: list of color lists for each node
    :return: (True, graph coloring) if exists, (False, first inconsistent arc) otherwise
    """
    queue = deque([(start, end) for start in graph for end in graph.get(start)])
    while len(queue):
        arc = queue.popleft()
        if remove_inconsistent_values(arc, domain):
            queue += get_entering_arcs(arc[0], graph)
        if is_inconsistent(arc, domain):
            return False, arc
    return True, domain


def first_example():
    """
    Applies algorithm on valid graph.
    :return: (True, graph coloring) if exists, (False, first inconsistent arc) otherwise
    """
    graph = {
        "WA": ["SA", "NT"],
        "SA": ["WA", "NT"],
        "NT": ["WA", "SA"]
    }
    domain = {
        "WA": ["red", "green", "blue"],
        "SA": ["red", "green"],
        "NT": ["green"]
    }
    return arc_consistency(graph, domain)


def second_example():
    """
    Applies algorithm on invalid graph.
    :return: (True, graph coloring) if exists, (False, first inconsistent arc) otherwise
    """
    graph = {
        "T": ["V"],
        "WA": ["NT", "SA"],
        "NT": ["WA", "Q", "SA"],
        "SA": ["WA", "NT", "Q", "NSW", "V"],
        "Q": ["NT", "SA", "NSW"],
        "NSW": ["Q", "SA", "V"],
        "V": ["SA", "NSW", "T"]
    }
    domain = {
        "WA": ["red"],
        "NT": ["red", "blue", "green"],
        "SA": ["red", "blue", "green"],
        "Q": ["green"],
        "NSW": ["red", "blue", "green"],
        "V": ["red", "blue", "green"],
        "T": ["red", "blue", "green"]
    }
    return arc_consistency(graph, domain)


def test(result):
    """
    Prints the result.
    :param result: (True, graph coloring) if exists, (False, first inconsistent arc) otherwise
    """
    if result[0]:
        print("The coloring of the map should be: {}.".format(result[1]))
    else:
        print("Failed to remove inconsistent values, the first inconsistency found is: {}.".format(result[1]))


def main():
    """
    Exemplifies functionality of program.
    """
    test(first_example())
    test(second_example())


if __name__ == '__main__':
    main()
