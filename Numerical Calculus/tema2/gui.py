from exercises import *

import tkinter as tk

A, b, n, epsilon, bonus_A, copy_of_A, d = get_input()
A = Cholesky_decomposition(A, n, epsilon)
L = bonus_Cholesky_decomposition(bonus_A, n, epsilon)
x = system_solution(A, b, n, epsilon)


class GUI(tk.Tk):
    def __init__(self):
        super().__init__()

        self.title("Tema 2")
        self.grid_rowconfigure(index=0, weight=1)
        self.grid_columnconfigure(index=0, weight=1)

        container = tk.Frame(self)
        container.grid()
        container.grid_rowconfigure(index=0, weight=1)
        container.grid_columnconfigure(index=0, weight=1)

        self.frames = {}

        for name in (Menu, Exercise1, BonusExercise1, Exercise2, Exercise3, BonusExercise3, Exercise4, Exercise5,
                     Exercise6):
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
        Button(master=self, text="Exercițiul 1 (bonus)", command=lambda: window.show_frame(BonusExercise1))
        Button(master=self, text="Exercițiul 2", command=lambda: window.show_frame(Exercise2))
        Button(master=self, text="Exercițiul 3", command=lambda: window.show_frame(Exercise3))
        Button(master=self, text="Exercițiul 3 (bonus)", command=lambda: window.show_frame(BonusExercise3))
        Button(master=self, text="Exercițiul 4", command=lambda: window.show_frame(Exercise4))
        Button(master=self, text="Exercițiul 5", command=lambda: window.show_frame(Exercise5))
        Button(master=self, text="Exercițiul 6", command=lambda: window.show_frame(Exercise6))


class Exercise1(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Exercițiul 1")
        SubTitle(master=self, text="Descompunerea LLᵀ a matricii A")
        Text(master=self, text=get_formatted_matrix(A))
        Button(master=self, text="Salvează în fișier", command=lambda: print_matrix_to_file("exercise_1.txt", A))
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


class BonusExercise1(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Exercițiul 1 (bonus)")
        SubTitle(master=self, text="Partea inferior triunghiulară a matricii A")
        Text(master=self, text=get_formatted_array(bonus_A))
        Button(master=self, text="Salvează în fișier", command=lambda: print_array_to_file("exercise_1_bonus_a.txt",
                                                                                           bonus_A))
        SubTitle(master=self, text="Partea inferior triunghiulară a matricii L")
        Text(master=self, text=get_formatted_array(L))
        Button(master=self, text="Salvează în fișier", command=lambda: print_array_to_file("exercise_1_bonus_b.txt", L))
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


class Exercise2(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)

        det = determinant(A)

        Title(master=self, text="Exercițiul 2")
        SubTitle(master=self, text="Determinantul matricii A")
        Text(master=self, text="%.3f" % det)
        Button(master=self, text="Salvează în fișier", command=lambda: print_array_to_file("exercise_2.txt",
                                                                                           np.array([det])))
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


class Exercise3(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Exercițiul 3")
        SubTitle(master=self, text="Solutia sistemului Ax = b")
        Text(master=self, text=get_formatted_array(x))
        Button(master=self, text="Salvează în fișier", command=lambda: print_array_to_file("exercise_3.txt", x))
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


class BonusExercise3(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)

        _x = bonus_system_solution(L, b, n, epsilon)

        Title(master=self, text="Exercițiul 3 (bonus)")
        SubTitle(master=self, text="Solutia sistemului Ax = b\ncu A și L de la exercițiul 1 (bonus)")
        Text(master=self, text=get_formatted_array(_x))
        Button(master=self, text="Salvează în fișier", command=lambda: print_array_to_file("exercise_3_bonus.txt", _x))
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


class Exercise4(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)
        Title(master=self, text="Exercițiul 4")
        SubTitle(master=self, text="Verificarea solutiei sistemului Ax = b")
        Text(master=self, text=verify_system_solution(A, d, b, x, n, threshold=10 ** -9))
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


class Exercise5(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)

        _P, _L, _U, _x = LU_decomposition(copy_of_A, b)

        Title(master=self, text="Exercițiul 5")
        SubTitle(master=self, text="Matricea P")
        Text(master=self, text=get_formatted_matrix(_P))
        Button(master=self, text="Salvează în fișier", command=lambda: print_matrix_to_file("exercise_5_a.txt", _P))
        SubTitle(master=self, text="Matricea L")
        Text(master=self, text=get_formatted_matrix(_L))
        Button(master=self, text="Salvează în fișier", command=lambda: print_matrix_to_file("exercise_5_b.txt", _L))
        SubTitle(master=self, text="Matricea U")
        Text(master=self, text=get_formatted_matrix(_U))
        Button(master=self, text="Salvează în fișier", command=lambda: print_matrix_to_file("exercise_5_c.txt", _U))
        SubTitle(master=self, text="Solutia sistemului Ax = b")
        Text(master=self, text=get_formatted_array(_x))
        Button(master=self, text="Salvează în fișier", command=lambda: print_array_to_file("exercise_5_d.txt", _x))
        Button(master=self, text="Mergi inapoi", command=lambda: window.show_frame(Menu))


class Exercise6(tk.Frame):
    def __init__(self, frame, window):
        super().__init__(frame)

        inv = inverse(A, n, epsilon)
        np_inv = numpy_inverse(copy_of_A)

        Title(master=self, text="Exercițiul 6")
        SubTitle(master=self, text="Aproximarea inversei matricei A")
        Text(master=self, text=get_formatted_matrix(inv))
        Button(master=self, text="Salvează în fișier", command=lambda: print_matrix_to_file("exercise_6_a.txt", inv))
        SubTitle(master=self, text="Aproximarea inversei matricei A (NumPy)")
        Text(master=self, text=get_formatted_matrix(np_inv))
        Button(master=self, text="Salvează în fișier", command=lambda: print_matrix_to_file("exercise_6_b.txt",
                                                                                            np_inv))
        SubTitle(master=self, text="Verificarea aproximării inversei matricei A")
        Text(master=self, text=verify_inverse(inv, np_inv, threshold=10 ** -9))
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
