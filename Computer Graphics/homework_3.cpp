#include <cassert>
#include <cmath>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <GL/glut.h>

#define WINDOW 350

#define MAX_ITERATIONS  5000
#define MAX_MODULUS     10000000
#define RATIO           0.01

unsigned char oldKey = 27; // escape
unsigned char key;
int startLevel = 0;

void ChangeViewPort() {
    int currentWidth = glutGet(GLUT_WINDOW_WIDTH);
    int currentHeight = glutGet(GLUT_WINDOW_HEIGHT);

    int width = WINDOW - 50;
    int height = WINDOW - 50;

    int centerX = currentWidth / 2;
    int centerY = currentHeight / 2;

    int leftDownX = centerX - width / 2;
    int leftDownY = centerY - height / 2;

    glViewport((GLint) leftDownX, (GLint) leftDownY, (GLsizei) width, (GLsizei) height);
}

class Complex {
private:
    double real, imaginary;
public:
    Complex() : real(0.0), imaginary(0.0) {}

    Complex(double newReal, double newImaginary) : real(newReal * 1.0), imaginary(newImaginary * 1.0) {}

    Complex(const Complex &complex) = default;

    ~Complex() = default;

    double getReal() const { return real; }

    double getImaginary() const { return imaginary; }

    void setReal(double newRe) { real = newRe; }

    void setImaginary(double newIm) { imaginary = newIm; }

    double getModulus() const { return sqrt(real * real + imaginary * imaginary); }

    Complex &operator=(Complex const &complex) = default;

    int operator==(Complex &complex) const {
        return real == complex.real && imaginary == complex.imaginary;
    }

    friend Complex operator+(Complex const &complex_1, Complex const &complex_2);

    friend Complex operator*(Complex const &complex_1, Complex const &complex_2);

    Complex pow_2() const {
        Complex result;
        result.real = pow(real * 1.0, 2) - pow(imaginary * 1.0, 2);
        result.imaginary = 2.0 * real * imaginary;
        return result;
    }

    void print(FILE *file) const {
        fprintf(file, "%.20f%+.20fi", real, imaginary);
    }
};

Complex operator+(Complex const &complex_1, Complex const &complex_2) {
    Complex result(complex_1.real + complex_2.real, complex_1.imaginary + complex_2.imaginary);
    return result;
}

Complex operator*(Complex const &complex_1, Complex const &complex_2) {
    Complex result(complex_1.real * complex_2.real - complex_1.imaginary * complex_2.imaginary,
                   complex_1.real * complex_2.imaginary + complex_1.imaginary * complex_2.real);
    return result;
}

class Coordinates {
protected:
    double x, y;
public:
    Coordinates() : x(0.0), y(0.0) {}

    Coordinates(double newX, double newY) : x(newX), y(newY) {}

    Coordinates(const Coordinates &p) = default;

    double getX() const { return x; }

    double getY() const { return y; }

    Coordinates &operator=(Coordinates const &p) = default;

    int operator==(Coordinates &p) const {
        return x == p.x && y == p.y;
    }

    void print(FILE *f) const {
        fprintf(f, "(%+f, %+f)", x, y);
    }
};

class Point : public Coordinates {
public:
    Point() : Coordinates(0.0, 0.0) {}

    Point(double x, double y) : Coordinates(x, y) {}

    void draw() {
        glBegin(GL_POINTS);
        glVertex2d(x, y);
        glEnd();
    }

    Point &operator=(const Point &p) = default;

    int operator==(Point &p) {
        return x == p.x && y == p.y;
    }
};

class Vector : public Coordinates {
private:
    void normalize() {
        double d = sqrt(x * x + y * y);
        if (d != 0.0) {
            x = x * 1.0 / d;
            y = y * 1.0 / d;
        }
    }

public:
    Vector() : Coordinates(0.0, 0.0) {
        normalize();
    }

    Vector(double x, double y) : Coordinates(x, y) {
        normalize();
    }

    Point getDestination(Point &origin, double length) {
        Point destination(origin.getX() + x * length, origin.getY() + y * length);
        return destination;
    }

    void rotate(double grade) {
        double copyX = getX();
        double copyY = getY();
        double t = (4.0 * atan(1.0)) * grade / 180.0;
        x = copyX * cos(t) - copyY * sin(t);
        y = copyX * sin(t) + copyY * cos(t);
        normalize();
    }

    void draw(Point origin, double length) {
        Point destination = getDestination(origin, length);
        glColor3f(1.0, 0.1, 0.1);
        glBegin(GL_LINE_STRIP);
        glVertex2d(origin.getX(), origin.getY());
        glVertex2d(destination.getX(), destination.getY());
        glEnd();
    }

    Vector &operator=(Vector const &vector) = default;

    int operator==(Vector &vector) {
        return x == vector.x && y == vector.y;
    }
};

class JuliaFatou {
protected:
    Complex complex;
    int maxIterations = MAX_ITERATIONS;;
    double maxModulus = MAX_MODULUS;
public:
    JuliaFatou() = default;

    explicit JuliaFatou(Complex &newComplex) : complex(newComplex) {}

    ~JuliaFatou() = default;

    int getMaxIterations() const { return maxIterations; }

    double getMaxModulus() const { return maxModulus; }

    void setMaxModulus(double newMaxModulus) {
        assert(newMaxModulus <= MAX_MODULUS);
        maxModulus = newMaxModulus;
    }

    void setMaxIterations(int newMaxIterations) {
        assert(newMaxIterations <= MAX_ITERATIONS);
        maxIterations = newMaxIterations;
    }

    // testeaza daca x apartine multimii Julia-Fatou
    // returneaza 0 daca apartine, -1 daca converge finit, +1 daca converge infinit
    virtual int isIn(Complex &x) {
        Complex z_0 = x, z_1;
        int result = 0;

        for (int i = 1; i < maxIterations; i++) {
            z_1 = (z_0 * z_0) + complex;
            if (z_1 == z_0) {
                // procesul iterativ converge finit
                result = -1;
                break;
            } else if (z_1.getModulus() > maxModulus) {
                // procesul iterativ converge la infinit
                result = 1;
                break;
            }
            z_0 = z_1;
        }

        return result;
    }

    void display(double x_min, double y_min, double x_max, double y_max) {
        // glPushMatrix();
        // glLoadIdentity();

        // glTranslated((x_min + x_max) * 1.0 / (x_min - x_max), (y_min + y_max) * 1.0 / (y_min - y_max), 0);
        // glScaled(1.0 / (x_max - x_min), 1.0 / (y_max - y_min), 1);

        glBegin(GL_POINTS);
        for (double x = x_min; x <= x_max; x += RATIO)
            for (double y = y_min; y <= y_max; y += RATIO) {
                Complex z(x, y);
                if (isIn(z) == 0) {
                    glVertex2d(x, y);
                }
            }
        printf("STOP\n");
        glEnd();

        // glPopMatrix();
    }
};

class Mandelbrot : public JuliaFatou {
public:
    Mandelbrot() : JuliaFatou() {}

    explicit Mandelbrot(Complex &c) : JuliaFatou(c) {}

    // testeaza daca x apartine multimii Mandelbrot
    // returneaza 0 daca apartine, +i daca |z_i| depaseste 2
    int isIn(Complex &x) override {
        Complex z_0 = x, z_1;
        int result = 0;

        for (int i = 1; i < maxIterations; i++) {
            z_1 = (z_0 * z_0) + x;
            if (z_1.getModulus() > maxModulus) {
                // procesul iterativ depaseste 2
                result = i;
                break;
            }
            z_0 = z_1;
        }

        return result;
    }

    void displayA(double x_min, double y_min, double x_max, double y_max) {
        glColor3d(1.0, 0.1, 0.1);
        glBegin(GL_POINTS);
        for (double x = x_min; x <= x_max; x += RATIO)
            for (double y = y_min; y <= y_max; y += RATIO) {
                Complex z(x, y);
                if (isIn(z) == 0) {
                    glVertex2d(x / 2.0, y / 2.0);
                }
            }
        printf("STOP\n");
        glEnd();
    }

    void displayB(double x_min, double y_min, double x_max, double y_max) {
        glBegin(GL_POINTS);
        for (double x = x_min; x <= x_max; x += RATIO)
            for (double y = y_min; y <= y_max; y += RATIO) {
                Complex z(x, y);
                int result = isIn(z);

                if (result == 0) {
                    glColor3d(1.0, 1.0, 1.0);
                    glVertex2d(x / 2.0, y / 2.0);
                } else if (result > 0) {
                    glColor3d(result / 7.5, 0.0, result / 12.5);
                    glVertex2d(x / 2.0, y / 2.0);
                }
            }
        printf("STOP\n");
        glEnd();
    }
};

class KochSnowflake {
public:
    void draw(double length, double factor, int level, Point &origin, Vector vector) {
        if (level == 0) {
            vector.draw(origin, length);
        } else {
            Point destination;
            length *= factor;

            draw(length, factor, level - 1, origin, vector);

            destination = vector.getDestination(origin, length);
            vector.rotate(60);
            draw(length, factor, level - 1, destination, vector);

            destination = vector.getDestination(destination, length);
            vector.rotate(-120);
            draw(length, factor, level - 1, destination, vector);

            destination = vector.getDestination(destination, length);
            vector.rotate(60);
            draw(length, factor, level - 1, destination, vector);
        }
    }

    void show(double length, double factor) {
        Vector vector_1(sqrt(3.0) / 2.0, 0.5);
        Point point_1(-1.0, 0.0);

        Vector vector_2(0.0, -1.0);
        Point point_2(0.5, sqrt(3.0) / 2.0);

        Vector vector_3(-sqrt(3.0) / 2.0, 0.5);
        Point point_3(0.5, -sqrt(3.0) / 2.0);

        draw(length, factor, startLevel, point_1, vector_1);
        draw(length, factor, startLevel, point_2, vector_2);
        draw(length, factor, startLevel, point_3, vector_3);
    }
};

class BinaryTree {
public:
    void draw(double length, int level, Point &origin, Vector vector) {
        if (level == 0) {
            vector.draw(origin, length);
        } else {
            Point destination;

            draw(length, level - 1, origin, vector);

            destination = vector.getDestination(origin, length);
            vector.rotate(-45);
            draw(length / 2.0, level - 1, destination, vector);

            vector.rotate(90);
            draw(length / 2.0, level - 1, destination, vector);
        }
    }

    void show(double length) {
        Point point(0.0, 1.0);
        Vector vector(0.0, -1.0);

        draw(length, startLevel, point, vector);
    }
};

class PerronTree {
public:
    void draw(double length, double factor, int level, Point point, Vector vector) {
        if (level > 0) {
            vector.rotate(30);
            vector.draw(point, length);

            Point point_1, point_2;

            point_1 = vector.getDestination(point, length);
            draw(length * factor, factor, level - 1, point_1, vector);

            vector.rotate(-90);
            vector.draw(point, length);

            point_1 = point_2 = vector.getDestination(point, length);
            vector.rotate(-30);
            vector.draw(point_1, length);

            point_1 = vector.getDestination(point_1, length);
            draw(length * factor, factor, level - 1, point_1, vector);

            point_1 = point_2;
            vector.rotate(90);
            vector.draw(point_1, length);

            point_1 = point_2 = vector.getDestination(point_1, length);
            vector.rotate(30);
            vector.draw(point_1, length);

            point_1 = vector.getDestination(point_1, length);
            draw(length * factor, factor, level - 1, point_1, vector);

            point_1 = point_2;
            vector.rotate(-90);
            vector.draw(point_1, length);

            point_1 = vector.getDestination(point_1, length);
            draw(length * factor, factor, level - 1, point_1, vector);
        }
    }

    void show(double length) {
        Vector vector(0.0, 1.0);
        Point point(0.0, -1.0);

        vector.draw(point, 0.25);
        point = vector.getDestination(point, 0.25);

        draw(length, 0.4, startLevel, point, vector);
    }
};

class HilbertCurve {
public:
    void draw(double length, int level, Point &point, Vector &vector, int direction) {
        if (level > 0) {
            vector.rotate(direction * 90);
            draw(length, level - 1, point, vector, -direction);

            vector.draw(point, length);

            point = vector.getDestination(point, length);
            vector.rotate(-direction * 90);
            draw(length, level - 1, point, vector, direction);

            vector.draw(point, length);

            point = vector.getDestination(point, length);
            draw(length, level - 1, point, vector, direction);

            vector.rotate(-direction * 90);
            vector.draw(point, length);

            point = vector.getDestination(point, length);
            draw(length, level - 1, point, vector, -direction);

            vector.rotate(direction * 90);
        }
    }

    void show(double length) {
        Vector vector(0.0, 1.0);
        Point point(0.0, 0.0);

        draw(length, startLevel, point, vector, 1);
    }
};

class SierpinskiCarpet {
public:
    void draw(double length, double factor, int level, Point &origin) {
        Vector vector(1.0, 0.0);
        double bigSquareLength = length * factor;

        if (level == 0) {
            Point destination;
            double bigSquareCornerDistance = length * factor * sqrt(2) / 2.0;

            vector.rotate(135);
            destination = vector.getDestination(origin, bigSquareCornerDistance);
            vector.rotate(-135);

            for (int i = 0; i < 4; i++) {
                vector.draw(destination, bigSquareLength);

                destination = vector.getDestination(destination, bigSquareLength);
                vector.rotate(-90);
            }
        } else {
            draw(length, factor, 0, origin);

            Point center;
            double diagonalSquareCenterDistance = length * factor * sqrt(2);
            double perpendicularSquareCenterDistance = length * factor;

            for (int i = 0; i < 8; i++) {
                if (i % 2 == 0) {
                    center = vector.getDestination(origin, perpendicularSquareCenterDistance);
                } else {
                    center = vector.getDestination(origin, diagonalSquareCenterDistance);
                }
                draw(bigSquareLength, factor, level - 1, center);

                vector.rotate(-45);
            }
        }
    }

    void show(double length) {
        Vector vector(1.0, 0.0);
        Point point(0.0, 0.0);

        Point destination;
        double bigSquareCornerDistance = length * sqrt(2) / 2.0;

        vector.rotate(135);
        destination = vector.getDestination(point, bigSquareCornerDistance);
        vector.rotate(-135);

        for (int i = 0; i < 4; i++) {
            vector.draw(destination, length);

            destination = vector.getDestination(destination, length);
            vector.rotate(-90);
        }

        draw(length, 1.0 / 3.0, startLevel, point);
    }
};

class IrregularTree {
public:
    void draw(double length, double factor, int level, Point &point, Vector vector) {
        if (level > 0) {
            Point point_1, point_2;

            vector.rotate(-45);
            vector.draw(point, length);

            point_1 = vector.getDestination(point, length);
            draw(length * factor, factor, level - 1, point_1, vector);

            vector.rotate(90);
            vector.draw(point, length);

            point_1 = vector.getDestination(point, length);
            vector.rotate(15);
            vector.draw(point_1, length);

            point_2 = vector.getDestination(point_1, length);
            draw(length * factor, factor, level - 1, point_2, vector);

            vector.rotate(-60);
            vector.draw(point_1, length);

            point_2 = vector.getDestination(point_1, length);
            vector.rotate(-90);
            vector.draw(point_2, length / 2.0);

            point_1 = vector.getDestination(point_2, length / 2.0);
            draw(length * factor, factor, level - 1, point_1, vector);

            vector.rotate(120);
            vector.draw(point_2, length / 2.0);

            point_1 = vector.getDestination(point_2, length / 2.0);
            draw(length * factor, factor, level - 1, point_1, vector);
        }
    }

    void show(double length) {
        Vector vector(0.0, -1.0);
        Point point(0.0, 1.0);

        vector.draw(point, 0.2);
        point = vector.getDestination(point, 0.2);

        draw(length, 0.4, startLevel, point, vector);
    }
};

class SierpinskiArrowheadCurve {
public:
    void draw(double length, int level, Point &point, Vector &vector, int direction) {
        if (level == 0) {
            vector.draw(point, length);
            point = vector.getDestination(point, length);
        } else {
            vector.rotate(direction * 60);
            draw(length / 2.0, level - 1, point, vector, -direction);

            vector.rotate(-direction * 60);
            draw(length / 2.0, level - 1, point, vector, direction);

            vector.rotate(-direction * 60);
            draw(length / 2.0, level - 1, point, vector, -direction);

            vector.rotate(direction * 60);
        }
    }

    void show(double length) {
        Vector vector(0.0, -1.0);
        Point point(-0.75, 0.75);

        draw(length, startLevel, point, vector, 1);
    }
};

void DisplayName(const char *name) {
    glColor3d(1.0, 0.1, 1.0);
    glRasterPos2d(-0.9, 1.0);
    for (unsigned int index = 0; index < strlen(name); index++) {
        glutBitmapCharacter(GLUT_BITMAP_9_BY_15, name[index]);
    }
}

void DisplayLevel() {
    char message[11];
    if (startLevel < 10) {
        sprintf(message, "Nivelul%2d", startLevel);
    } else {
        sprintf(message, "Nivelul %2d", startLevel);
    }

    glColor3d(1.0, 0.1, 1.0);
    glRasterPos2d(-0.9, -1.0);
    for (unsigned int index = 0; index < strlen(message); index++) {
        glutBitmapCharacter(GLUT_BITMAP_9_BY_15, message[index]);
    }
}

// multimea Julia-Fatou pentru z_0 = 0 si complex = -0.12375+0.056805i
void DisplayJuliaFatou_1() {
    Complex complex(-0.12375, 0.056805);
    JuliaFatou juliaFatou(complex);
    juliaFatou.setMaxIterations(30);

    glColor3d(1.0, 0.1, 0.1);
    juliaFatou.display(-0.8, -0.4, 0.8, 0.4);
}

// multimea Julia-Fatou pentru z_0 = 0 si complex = -0.012+0.74i
void DisplayJuliaFatou_2() {
    Complex complex(-0.012, 0.74);
    JuliaFatou juliaFatou(complex);
    juliaFatou.setMaxIterations(30);

    glColor3d(1.0, 0.1, 0.1);
    juliaFatou.display(-1.0, -1.0, 1.0, 1.0);
}

void DisplayMandelbrotA() {
    Complex complex(0.0, 0.0);
    Mandelbrot mandelbrot(complex);
    mandelbrot.setMaxIterations(10);
    mandelbrot.setMaxModulus(2);

    mandelbrot.displayA(-2.0, -2.0, 2.0, 2.0);
}

void DisplayMandelbrotB() {
    Complex complex(0.0, 0.0);
    Mandelbrot mandelbrot(complex);
    mandelbrot.setMaxIterations(10);
    mandelbrot.setMaxModulus(2);

    mandelbrot.displayB(-2.0, -2.0, 2.0, 2.0);
}

void DisplayKochSnowflake() {
    DisplayName("Koch Snowflake");

    KochSnowflake kochSnowflake;
    kochSnowflake.show(sqrt(3.0), 1.0 / 3.0);

    DisplayLevel();
    
    startLevel++;
}

void DisplayBinaryTree() {
    DisplayName("Binary Tree");
    
    BinaryTree binaryTree;
    binaryTree.show(1);

    DisplayLevel();

    startLevel++;
}

void DisplayPerronTree() {
    DisplayName("Perron Tree");

    PerronTree perronTree;
    glPushMatrix();
    glLoadIdentity();
    glScaled(0.35, 0.35, 0.0);
    glTranslated(-0.5, -0.5, 0.0);
    perronTree.show(1);
    glPopMatrix();

    DisplayLevel();

    startLevel++;
}

void DisplayHilbertCurve() {
    DisplayName("Hilbert Curve");

    HilbertCurve hilbertCurve;
    glPushMatrix();
    glLoadIdentity();
    glScaled(0.85, 0.85, 0.0);
    glTranslated(1.05, -1.05, 0.0);
    hilbertCurve.show(0.05);
    glPopMatrix();

    DisplayLevel();

    startLevel++;
}

void DisplaySierpinskiCarpet() {
    DisplayName("Sierpinski Carpet");

    SierpinskiCarpet sierpinskiCarpet;
    sierpinskiCarpet.show(1.5);

    DisplayLevel();

    startLevel++;
}

void DisplayIrregularTree() {
    DisplayName("Irregular Tree");

    IrregularTree irregularTree;
    glPushMatrix();
    glLoadIdentity();
    glScaled(0.4, 0.4, 1);
    glTranslated(-0.5, 1.4, 0.0);
    irregularTree.show(1);
    glPopMatrix();

    DisplayLevel();

    startLevel++;
}

void DisplaySierpinskiArrowheadCurve() {
    DisplayName("Sierpinski Arrowhead Curve");

    SierpinskiArrowheadCurve imagine3;
    imagine3.show(1.5);

    DisplayLevel();

    startLevel++;
}

void Init() {
    glClearColor(1.0, 1.0, 1.0, 1.0);

    glLineWidth(1);

    glPolygonMode(GL_FRONT, GL_LINE);
}

void Display() {
    if (key < '0' || key > '4') {
        if (oldKey != key) {
            startLevel = 0;
        }
    }

    switch (key) {
        case '0':
            glClear(GL_COLOR_BUFFER_BIT);
            startLevel = 0;
            break;
        case '1':
            glClear(GL_COLOR_BUFFER_BIT);
            DisplayJuliaFatou_1();
            break;
        case '2':
            glClear(GL_COLOR_BUFFER_BIT);
            DisplayJuliaFatou_2();
            break;
        case '3':
            glClear(GL_COLOR_BUFFER_BIT);
            DisplayMandelbrotA();
            break;
        case '4':
            glClear(GL_COLOR_BUFFER_BIT);
            DisplayMandelbrotB();
            break;
        case '5':
            glClear(GL_COLOR_BUFFER_BIT);
            DisplayKochSnowflake();
            break;
        case '6':
            glClear(GL_COLOR_BUFFER_BIT);
            DisplayBinaryTree();
            break;
        case '7':
            glClear(GL_COLOR_BUFFER_BIT);
            DisplayPerronTree();
            break;
        case '8':
            glClear(GL_COLOR_BUFFER_BIT);
            DisplayHilbertCurve();
            break;
        case '9':
            glClear(GL_COLOR_BUFFER_BIT);
            DisplaySierpinskiCarpet();
            break;
        case '+':
            glClear(GL_COLOR_BUFFER_BIT);
            DisplayIrregularTree();
            break;
        case '-':
            glClear(GL_COLOR_BUFFER_BIT);
            DisplaySierpinskiArrowheadCurve();
            break;
        default:
            break;
    }

    glFlush();
}

void Reshape(int width, int height) {
    ChangeViewPort();
}

void KeyboardFunc(unsigned char newKey, int x, int y) {
    oldKey = key;
    key = newKey;
    if (key == 27) { // escape
        exit(0);
    }
    glutPostRedisplay();
}

void MouseFunc(int button, int state, int x, int y) {
}

int main(int argc, char **argv) {
    glutInit(&argc, argv);

    glutInitWindowSize(WINDOW, WINDOW);

    glutInitWindowPosition(100, 100);

    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);

    glutCreateWindow(argv[0]);

    Init();

    glutReshapeFunc(Reshape);

    glutKeyboardFunc(KeyboardFunc);

    glutMouseFunc(MouseFunc);

    glutDisplayFunc(Display);

    glutMainLoop();

    return 0;
}