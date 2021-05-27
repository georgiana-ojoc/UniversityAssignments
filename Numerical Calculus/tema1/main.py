from decimal import Decimal

import math
import numpy as np
import random
import time
import tkinter as tk


class GUI(tk.Tk):

    def __init__(self):
        super().__init__()
        self.title("Tema 1")
        self.grid_rowconfigure(index=0, weight=1)
        self.grid_columnconfigure(index=0, weight=1)

        container = tk.Frame(self)
        container.grid()
        container.grid_rowconfigure(index=0, weight=1)
        container.grid_columnconfigure(index=0, weight=1)

        self.frames = {}

        for name in (Menu, Exercise1, Exercise2, Exercise3):
            frame = name(container, self)
            self.frames[name] = frame
            frame.grid(row=0, column=0, sticky="NSEW")
            frame.grid_columnconfigure(index=0, weight=1)

        self.show_frame(Menu)

    def show_frame(self, name):
        frame = self.frames[name]
        frame.tkraise()


class Menu(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Meniu")
        Button(master=self, text="Exercițiul 1", command=lambda: window.show_frame(Exercise1))
        Button(master=self, text="Exercițiul 2", command=lambda: window.show_frame(Exercise2))
        Button(master=self, text="Exercițiul 3", command=lambda: window.show_frame(Exercise3))


class Exercise1(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Exercițiul 1")
        SubTitle(master=self, text="Precizia mașină")
        Text(master=self, text=problem_1())
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


class Exercise2(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Exercițiul 2")
        SubTitle(master=self, text="Asociativitatea adunării")
        Text(master=self, text=problem_2_1())

        SubTitle(master=self, text="Asociativitatea înmulțirii")
        Text(master=self, text=problem_2_2())
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


class Exercise3(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        problem_3 = Problem3()

        Title(master=self, text="Exercițiul 3")
        SubTitle(master=self, text="Aproximarea funcției tangentă folosind fracții continue")
        name = tk.Label(master=self, text="Precizie", font=10, foreground="darkcyan")
        name.grid(ipadx=10)

        results = tk.Label(master=self, text=problem_3.solve(problem_3.our_tan_1), font=10, foreground="teal")
        results.grid(row=5, ipadx=10, ipady=5, pady=5)

        value = tk.Text(master=self, height=1, width=10, font=10, foreground="darkcyan")
        value.bind("<Return>", lambda event: on_enter(results, problem_3, problem_3.our_tan_1,
                                                      Decimal(value.get("1.0", "end"))))
        value.grid(row=3, ipadx=10)
        value.insert(tk.END, problem_3.epsilon)

        button = Button(master=self, text="Aproximează",
                        command=lambda: change_text(results, problem_3, problem_3.our_tan_1,
                                                    Decimal(value.get("1.0", "end"))))
        button.grid(row=4, ipadx=10)

        SubTitle(master=self, text="Aproximarea funcției tangentă folosind polinoame")
        Text(master=self, text=problem_3.solve(problem_3.our_tan_2))
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


def change_text(label, problem, method, constant=None):
    if constant:
        problem.epsilon = constant
    label["text"] = problem.solve(method)


def on_enter(label, problem, method, constant=None):
    change_text(label, problem, method, constant)
    return "break"


class Button(tk.Button):

    def __init__(self, **kw):
        super().__init__(**kw)
        self["activebackground"] = "peachpuff"
        self["cursor"] = "hand2"
        self.background = self["background"]
        self.bind("<Enter>", self.on_enter)
        self.bind("<Leave>", self.on_leave)
        self.grid(ipadx=10, ipady=5, pady=5)

    def on_enter(self, _):
        self["background"] = self["activebackground"]

    def on_leave(self, _):
        self["background"] = self.background


class Title(tk.Label):
    def __init__(self, **kw):
        super().__init__(**kw)
        self["font"] = 12
        self.grid(ipadx=10, ipady=5, pady=5)


class SubTitle(tk.Label):
    def __init__(self, **kw):
        super().__init__(**kw)
        self["font"] = 11
        self["foreground"] = "slateBLue"
        self.grid(ipadx=10, ipady=5)


class Text(tk.Label):
    def __init__(self, **kw):
        super().__init__(**kw)
        self["font"] = 10
        self["foreground"] = "teal"
        self.grid(ipadx=10, ipady=5, pady=5)


def addition_precision():
    m = 1
    while 1.0 + 10 ** -m != 1.0:
        m += 1
    return 10 ** (1 - m)


def problem_1():
    return "u = {}".format(addition_precision())


def problem_2_1():
    x = 1.0
    y = z = addition_precision() / 10
    return "a = {}\nb = {}\nc = {}\n(a + b) + c = {}\na + (b + c) = {}".format(x, y, z, (x + y) + z, x + (y + z))


def multiplication_precision():
    m = 1
    while 1.0 * 10 ** -m != 0.0:
        m += 1
    return 10 ** (1 - m)


def problem_2_2():
    x = 0.1
    y = multiplication_precision()
    z = 10
    return "a = {}\nb = {}\nc = {}\n(a × b) × c = {}\na × (b × c) = {}".format(x, y, z, (x * y) * z, x * (y * z))


def pi_over_2_reduction(x):
    if x == -math.pi / 2:
        return -1, math.pi / 2
    if x == math.pi / 2:
        return 1, math.pi / 2
    sign = 1
    if x < -math.pi / 2:
        sign = -1
        x = -x
    while x > math.pi / 2:
        x -= math.pi
    return sign, x


def pi_over_4_reduction(sign, x):
    inverse = 1
    if x <= - math.pi / 4:
        sign = -sign
        x = -x
    if x >= math.pi / 4:
        x = math.pi / 2 - x
        inverse = -1
    return inverse, sign, x


def a(i, x):
    if i == 0:
        return 0
    if i == 1:
        return x
    return -x ** 2


def b(i):
    if i == 0:
        return 0
    return (i - 1) * 2 + 1


class Problem3:
    def __init__(self):
        self.epsilon = addition_precision()
        self.c1 = 0.33333333333333333
        self.c2 = 0.133333333333333333
        self.c3 = 0.053968253968254
        self.c4 = 0.0218694885361552

        self.numbers = []
        self.py_results = []
        self.py_time = []
        for _ in range(10_000):
            while True:
                sign, x = pi_over_2_reduction(random.uniform(-100, 100))
                if x != math.pi / 2:
                    break
            self.numbers += [(sign, x)]

            start_time = time.time()
            self.py_results += [sign * math.tan(x)]
            end_time = time.time()
            self.py_time += [end_time - start_time]

    def p(self, x):
        x_2 = x * x
        x_3 = x_2 * x
        return self.c1 + self.c2 * x + self.c3 * x_2 + self.c4 * x_3

    def our_tan_2(self, number):
        sign, x = number
        inverse, sign, x = pi_over_4_reduction(sign, x)
        x_2 = x * x
        x_3 = x_2 * x
        return sign * (x + self.p(x_2) * x_3) ** inverse

    def our_tan_1(self, number):
        sign, x = number
        f = b(0)
        tiny = 10 ** -12
        if f == 0:
            f = tiny
        c = f
        d = 0
        i = 1
        while True:
            d = b(i) + a(i, x) * d
            if d == 0:
                d = tiny
            c = b(i) + a(i, x) / c
            if c == 0:
                c = tiny
            d = 1 / d
            delta = c * d
            f = delta * f
            i += 1
            if abs(delta - 1) < self.epsilon:
                break
        return sign * f

    def solve(self, our_tan):
        our_results = []
        our_time = []
        for i in range(len(self.numbers)):
            start_time = time.time()
            our_results += [our_tan(self.numbers[i])]
            end_time = time.time()
            our_time += [end_time - start_time]

        results_error = abs(np.subtract(self.py_results, our_results))

        return "average error = {}\nminimum error = {}\nmaximum error = {}\n\n" \
               "python average time = {}\n python minimum time = {}\npython maximum time = {}\n\n" \
               "our average time = {}\n our minimum time = {}\nour maximum time = {}" \
            .format(np.mean(results_error), np.min(results_error), np.max(results_error),
                    np.mean(self.py_time), np.min(self.py_time), np.max(self.py_time),
                    np.mean(our_time), np.min(our_time), np.max(our_time))


def main():
    window = GUI()
    window.mainloop()


if __name__ == '__main__':
    main()
