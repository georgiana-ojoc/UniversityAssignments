from functions import *
import tkinter as tk

epsilon = addition_precision()


class GUI(tk.Tk):
    def __init__(self):
        super().__init__()

        self.title("Tema 5")
        self.grid_rowconfigure(index=0, weight=1)
        self.grid_columnconfigure(index=0, weight=1)

        container = tk.Frame(self)
        container.grid()
        container.grid_rowconfigure(index=0, weight=1)
        container.grid_columnconfigure(index=0, weight=1)

        self.frames = {}

        for name in (Menu, Exercise1, Exercise2):
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


class Exercise1(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Metoda Jacobi")
        n, a = get_jacobi_a(1)
        copy_of_a = get_matrix_a(n, a)
        SubTitle(master=self, text="Matricea A")
        Text(master=self, text=get_formatted_matrix(copy_of_a))
        lambdas, u = jacobi_approximate(n, a, epsilon)
        SubTitle(master=self, text="Valori proprii")
        Text(master=self, text=get_formatted_array(lambdas))
        SubTitle(master=self, text="Vectori proprii (coloane)")
        Text(master=self, text=get_formatted_matrix(u))
        SubTitle(master=self, text="Normă")
        Text(master=self, text=compute_norm(copy_of_a, lambdas, u))
        SubTitle(master=self, text="Matricea A⁽ᵏ⁾")
        Text(master=self, text=get_formatted_matrix(compute_series(copy_of_a, epsilon)))
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


class Exercise2(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Singular Value Decomposition")
        p, n, a = get_svd_a(2)
        SubTitle(master=self, text="Matricea A")
        Text(master=self, text=get_formatted_matrix(a))
        u, s, v_t = get_svd(a)
        SubTitle(master=self, text="Valori singulare")
        Text(master=self, text=get_formatted_array(s))
        SubTitle(master=self, text="Rang (fișier)")
        Text(master=self, text=compute_rank(s, epsilon))
        SubTitle(master=self, text="Rang (bibliotecă)")
        Text(master=self, text=get_rank(a))
        SubTitle(master=self, text="Număr de condiționare (fișier)")
        Text(master=self, text=compute_condition_number(s, epsilon))
        SubTitle(master=self, text="Număr de condiționare (bibliotecă)")
        Text(master=self, text=get_condition_number(a))
        a_i = compute_moore_penrose_pseudo_inverse(p, n, a, epsilon)
        SubTitle(master=self, text="Pseudoinversa Moore-Penrose")
        Text(master=self, text=get_formatted_matrix(a_i))
        a_j = compute_least_squares_pseudo_inverse(a)
        SubTitle(master=self, text="Pseudoinversa în sensul celor mai mici pătrate")
        Text(master=self, text=get_formatted_matrix(a_j))
        SubTitle(master=self, text="Normă")
        Text(master=self, text=get_norm(a_i - a_j))
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
