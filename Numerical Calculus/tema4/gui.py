from main import *
import tkinter as tk

epsilon = addition_precision()


class GUI(tk.Tk):
    def __init__(self):
        super().__init__()

        self.title("Tema 4")
        self.grid_rowconfigure(index=0, weight=1)
        self.grid_columnconfigure(index=0, weight=1)

        container = tk.Frame(self)
        container.grid()
        container.grid_rowconfigure(index=0, weight=1)
        container.grid_columnconfigure(index=0, weight=1)

        self.frames = {}

        for name in (Menu, Compulsory, Bonus):
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
        Button(master=self, text="Obligatoriu", command=lambda: window.show_frame(Compulsory))
        Button(master=self, text="Bonus", command=lambda: window.show_frame(Bonus))


class Compulsory(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Obligatoriu")
        SubTitle(master=self, text="Aproximarea și verificarea soluției sistemului liniar\n"
                                   "folosind metoda Gauss-Seidel (matricea coeficienților tridiagonală)")
        text = ""
        for i in range(1, 6):
            result = read_tridiagonal_matrix("a_{}.txt".format(i), epsilon)
            if result is None:
                continue
            n_a, p, q, a, b, c = result
            n_f, f = read_array("f_{}.txt".format(i))
            if n_a != n_f:
                print('n from "a_{}.txt" is not equal to n from "f_{}.txt"'.format(i, i))
                continue
            convergent, x = compute_solution(n_a, p, q, a, b, c, f, epsilon)
            norm = compute_norm(n_a, p, q, a, b, c, x, f)
            if convergent:
                text += "{}. convergence (norm = {})\n".format(i, norm)
            else:
                text += "{}. divergence (norm = {})\n".format(i, norm)
        text = text[:-1]
        Text(master=self, text=text)
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


class Bonus(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Bonus")
        result = read_tridiagonal_matrix("a_6.txt", epsilon)
        if result is not None:
            n_a, p, q, a, b, c = result
            n_f, f = read_array("f_6.txt")
            if n_a != n_f:
                print('n from "a_6.txt" is not equal to n from "f_6.txt"')
            else:
                convergent, x = compute_solution(n_a, p, q, a, b, c, f, epsilon)
                norm = compute_norm(n_a, p, q, a, b, c, x, f)
                if n_a <= 10:
                    SubTitle(master=self, text="Matricea coeficienților")
                    Text(master=self, text=get_formatted_matrix(n_a, p, q, a, b, c))
                    SubTitle(master=self, text="Vectorul termenilor liberi")
                    Text(master=self, text=get_formatted_array(f))
                    SubTitle(master=self, text="Soluția adevărată")
                    Text(master=self, text=get_formatted_array(get_solution(None, 6)))
                    SubTitle(master=self, text="Soluția aproximată")
                    Text(master=self, text=get_formatted_array(x))
                SubTitle(master=self, text="Aproximarea și verificarea soluției sistemului liniar\nfolosind "
                                           "metoda Gauss-Seidel (matricea coeficienților tridiagonală oarecare)")
                if convergent:
                    Text(master=self, text="convergence (norm = {})".format(norm))
                else:
                    Text(master=self, text="divergence (norm = {})".format(norm))
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
