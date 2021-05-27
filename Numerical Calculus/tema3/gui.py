from helper import *

import dictionary_of_keys as dok
import list_of_lists as lil
import tkinter as tk

epsilon = addition_precision()

b = read_tridiagonal_matrix("b.txt")
p = b[0]
q = b[1]
b = b[2:]

b_1 = read_tridiagonal_matrix("b_1.txt")
p_1 = b_1[0]
q_1 = b_1[1]
b_1 = b_1[2:]

b_2 = read_tridiagonal_matrix("b_2.txt")
p_2 = b_2[0]
q_2 = b_2[1]
b_2 = b_2[2:]

option = get_option()
if option == 1:
    library = lil
else:
    library = dok

a = library.read_sparse_matrix("a.txt")

read_a_plus_b = library.read_sparse_matrix("a_plus_b.txt")
read_a_ori_b = library.read_sparse_matrix("a_ori_b.txt")

computed_a_plus_b = library.compute_a_plus_b(p, q, a, b)
computed_a_ori_b = library.compute_a_ori_b(p, q, a, b)
computed_bonus_a_ori_b = library.compute_bonus_a_ori_b(p_1, q_1, b_1, p_2, q_2, b_2)


class GUI(tk.Tk):
    def __init__(self):
        super().__init__()

        self.title("Tema 3")
        self.grid_rowconfigure(index=0, weight=1)
        self.grid_columnconfigure(index=0, weight=1)

        container = tk.Frame(self)
        container.grid()
        container.grid_rowconfigure(index=0, weight=1)
        container.grid_columnconfigure(index=0, weight=1)

        self.frames = {}

        for name in (Menu, Exercise1, Exercise2, Bonus):
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
        Button(master=self, text="Bonus", command=lambda: window.show_frame(Bonus))


class Exercise1(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Exercițiul 1")
        SubTitle(master=self, text="Verificarea A + B (B matrice tridiagonală)")
        if library.are_equal_matrices(read_a_plus_b, computed_a_plus_b, epsilon):
            Text(master=self, text="Adevărat")
        else:
            Text(master=self, text="Fals")
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


class Exercise2(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Exercițiul 1")
        SubTitle(master=self, text="Verificarea A · B (B matrice tridiagonală)")
        if library.are_equal_matrices(read_a_ori_b, computed_a_ori_b, epsilon):
            Text(master=self, text="Adevărat")
        else:
            Text(master=self, text="Fals")
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


class Bonus(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Bonus")
        SubTitle(master=self, text="Produsul a două matrice tridiagonale oarecare")
        matrix = library.get_formatted_matrix(computed_bonus_a_ori_b)
        if len(b_1[0]) <= 10:
            Text(master=self, text=matrix)
        else:
            Text(master=self, text="Este afișat în consolă.")
            print(matrix)

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
