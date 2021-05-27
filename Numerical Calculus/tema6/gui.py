from functions import *
import tkinter as tk

h = 10 ** -5


class GUI(tk.Tk):
    def __init__(self):
        super().__init__()

        self.title("Tema 6")
        self.grid_rowconfigure(index=0, weight=1)
        self.grid_columnconfigure(index=0, weight=1)

        container = tk.Frame(self)
        container.grid()
        container.grid_rowconfigure(index=0, weight=1)
        container.grid_columnconfigure(index=0, weight=1)

        self.frames = {}

        for name in (Menu, Example1, Example2, Example3):
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
        Button(master=self, text="Exemplul 1", command=lambda: window.show_frame(Example1))
        Button(master=self, text="Exemplul 2", command=lambda: window.show_frame(Example2))
        Button(master=self, text="Exemplul 3", command=lambda: window.show_frame(Example3))


class Example1(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Exemplul 1")
        i = 1
        x_0, x_n = x_0_x_n(i)
        x = np.random.uniform(x_0, x_n)
        Text(master=self, text="x = {}".format(x))
        xs = generate_xs(x_0, x_n)
        ys = compute_ys(xs, i)
        SubTitle(master=self, text="Aproximare polinomială")
        p = compute_polynomial(xs, ys)
        y = compute_y(x, p)
        Text(master=self, text="rezultat = {}".format(y))
        Text(master=self, text="prima norma = {}".format(compute_x_norm(x, y, i)))
        Text(master=self, text="a doua norma = {}".format(compute_xs_norm(xs, ys, p)))
        SubTitle(master=self, text="Funcții spline")
        y = compute_spline(x, xs, ys, h, i)
        Text(master=self, text="rezultat = {}".format(y))
        Text(master=self, text="prima norma = {}".format(compute_x_norm(x, y, i)))
        plot(xs, ys, p, h, i)
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


class Example2(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Exemplul 2")
        i = 2
        x_0, x_n = x_0_x_n(i)
        x = np.random.uniform(x_0, x_n)
        Text(master=self, text="x = {}".format(x))
        xs = generate_xs(x_0, x_n)
        ys = compute_ys(xs, i)
        SubTitle(master=self, text="Aproximare polinomială")
        p = compute_polynomial(xs, ys)
        y = compute_y(x, p)
        Text(master=self, text="rezultat = {}".format(y))
        Text(master=self, text="prima norma = {}".format(compute_x_norm(x, y, i)))
        Text(master=self, text="a doua norma = {}".format(compute_xs_norm(xs, ys, p)))
        SubTitle(master=self, text="Funcții spline")
        y = compute_spline(x, xs, ys, h, i)
        Text(master=self, text="rezultat = {}".format(y))
        Text(master=self, text="prima norma = {}".format(compute_x_norm(x, y, i)))
        plot(xs, ys, p, h, i)
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


class Example3(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Exemplul 3")
        i = 3
        x_0, x_n = x_0_x_n(i)
        x = np.random.uniform(x_0, x_n)
        Text(master=self, text="x = {}".format(x))
        xs = generate_xs(x_0, x_n)
        ys = compute_ys(xs, i)
        SubTitle(master=self, text="Aproximare polinomială")
        p = compute_polynomial(xs, ys)
        y = compute_y(x, p)
        Text(master=self, text="rezultat = {}".format(y))
        Text(master=self, text="prima norma = {}".format(compute_x_norm(x, y, i)))
        Text(master=self, text="a doua norma = {}".format(compute_xs_norm(xs, ys, p)))
        SubTitle(master=self, text="Funcții spline")
        y = compute_spline(x, xs, ys, h, i)
        Text(master=self, text="rezultat = {}".format(y))
        Text(master=self, text="prima norma = {}".format(compute_x_norm(x, y, i)))
        plot(xs, ys, p, h, i)
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


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


def main():
    window = GUI()
    window.mainloop()


if __name__ == '__main__':
    main()
