#include <cmath>
#include <GL/glut.h>
#include <vector>

#define WINDOW  300
#define SCALE   0.9
#define CIRCLE  ((1.0 - SCALE) * 2.0)

using namespace std;

struct Click {
    int x = -1;
    int y = -1;
} click_1, click_2;

struct Pixel {
    int x = -1;
    int y = -1;
} global_pixel_1, global_pixel_2;

struct Point {
    double x;
    double y;
};

class CartesianGrid {
private:
    int rows;
    int columns;
    double cell;
    double leftUpX;
    double leftUpY;
    Point **points;
public:
    CartesianGrid(int newRows, int newColumns) {
        rows = newRows;
        columns = newColumns;

        points = new Point *[rows + 1];
        for (int i = 0; i < rows + 1; i++) {
            points[i] = new Point[columns + 1];
        }

        cell = 2.0 / (rows > columns ? rows : columns);
        leftUpX = -cell * columns / 2.0;
        leftUpY = cell * rows / 2.0;

        setPoints();
    }

    void setPoints() {
        for (int i = 0; i < rows + 1; i++) {
            for (int j = 0; j < columns + 1; j++) {
                Point point{};
                point.x = leftUpX + cell * j;
                point.y = leftUpY - cell * i;
                points[rows - i][j] = point;
            }
        }
    }

    Pixel getPixel(Click click) const {
        int width = glutGet(GLUT_WINDOW_WIDTH);
        int height = glutGet(GLUT_WINDOW_HEIGHT);

        int length = (int) (height < width ? height : width);
        int horizontalMargin = (width - length) / 2;
        if (click.x < horizontalMargin) {
            click.x = 0;
        } else if (click.x > width - horizontalMargin) {
            click.x = length;
        } else {
            click.x -= horizontalMargin;
        }
        click.y = height - click.y;
        int verticalMargin = (height - length) / 2;
        if (click.y < verticalMargin) {
            click.y = 0;
        } else if (click.y > height - verticalMargin) {
            click.y = length;
        } else {
            click.y -= verticalMargin;
        }

        double x = (2.0 * click.x / length - 1.0) / SCALE;
        if (x < leftUpX) {
            x = leftUpX;
        }
        if (x > -leftUpX) {
            x = -leftUpX;
        }
        double y = (2.0 * click.y / length - 1.0) / SCALE;
        if (y < -leftUpY) {
            y = -leftUpY;
        }
        if (y > leftUpY) {
            y = leftUpY;
        }

        Pixel pixel{};
        pixel.x = (int) round((x - leftUpX) / cell);
        pixel.y = (int) round(rows - (leftUpY - y) / cell);
        return pixel;
    }

    bool isPixel(Pixel pixel) const {
        if (pixel.x < 0) {
            return false;
        }
        if (pixel.x > columns) {
            return false;
        }
        if (pixel.y < 0) {
            return false;
        }
        if (pixel.y > rows) {
            return false;
        }
        return true;
    }

    void drawGrid() const {
        glColor3d(0.0, 0.0, 0.0);
        glLineWidth(1);
        for (int i = 0; i < rows + 1; i++) {
            glBegin(GL_LINE_STRIP);
            glVertex2d(points[i][0].x, points[i][0].y);
            glVertex2d(points[i][columns].x, points[i][columns].y);
            glEnd();
        }
        for (int i = 0; i < columns + 1; i++) {
            glBegin(GL_LINE_STRIP);
            glVertex2d(points[0][i].x, points[0][i].y);
            glVertex2d(points[rows][i].x, points[rows][i].y);
            glEnd();
        }
    }

    static void drawDisk(Point center, double radius, int lines) {
        glColor3d(0.25, 0.25, 0.25);
        glBegin(GL_POLYGON);
        for (int i = 0; i < lines; i++) {
            double theta = 8.0 * atan(1) * i / lines;
            double x = radius * cos(theta);
            double y = radius * sin(theta);
            glVertex2d(center.x + x, center.y + y);
        }
        glEnd();
    }

    void drawPixel(Pixel pixel) {
        if (isPixel(pixel)) {
            Point point = points[pixel.y][pixel.x];
            drawDisk(point, cell * CIRCLE, 100);
        }
    }

    void drawPixels(const vector<Pixel> &pixels) {
        for (Pixel pixel:pixels) {
            drawPixel(pixel);
        }
    }

    void drawLine(Pixel pixel_1, Pixel pixel_2) {
        if (isPixel(pixel_1) && isPixel(pixel_2)) {
            glColor3d(1.0, .0, .0);
            glLineWidth(3);
            glBegin(GL_LINE_STRIP);
            glVertex2d(points[pixel_1.y][pixel_1.x].x, points[pixel_1.y][pixel_1.x].y);
            glVertex2d(points[pixel_2.y][pixel_2.x].x, points[pixel_2.y][pixel_2.x].y);
            glEnd();
        }
    }
};

CartesianGrid cartesianGrid(10, 15);

double computeSlope(Pixel pixel_1, Pixel pixel_2) {
    return (double) (pixel_2.y - pixel_1.y) / (pixel_2.x - pixel_1.x);
}

vector<Pixel> getFirstOctanePixels(Pixel pixel_1, Pixel pixel_2) {
    vector<Pixel> pixels;

    int d_x = pixel_2.x - pixel_1.x; // x_1 <= x_2
    int d_y = pixel_2.y - pixel_1.y; // y_1 <= y_2

    int d = 2 * d_y - d_x; // 2 * (a + b / 2)
    int dE = 2 * d_y; // 2 * a
    int dNE = 2 * (d_y - d_x); // 2 * (a + b)

    int x = pixel_1.x;
    int y = pixel_1.y;
    pixels.push_back(Pixel{x, y - 1});
    pixels.push_back(Pixel{x, y});
    pixels.push_back(Pixel{x, y + 1});

    while (x < pixel_2.x) {
        if (d <= 0) { // M deasupra dreptei (b negativ, y mai mare)
            d += dE;
        } else {
            d += dNE;
            y++;
        }
        x++;
        pixels.push_back(Pixel{x, y - 1});
        pixels.push_back(Pixel{x, y});
        pixels.push_back(Pixel{x, y + 1});
    }

    return pixels;
}

vector<Pixel> getSecondOctanePixels(Pixel pixel_1, Pixel pixel_2) {
    vector<Pixel> pixels;

    int d_x = pixel_2.x - pixel_1.x; // x_1 <= x_2
    int d_y = pixel_2.y - pixel_1.y; // y_1 <= y_2

    int d = d_y - 2 * d_x; // 2 * (a / 2 + b)
    int dN = -2 * d_x; // 2 * b
    int dNE = 2 * (d_y - d_x); // 2 * (a + b)

    int x = pixel_1.x;
    int y = pixel_1.y;
    pixels.push_back(Pixel{x - 1, y});
    pixels.push_back(Pixel{x, y});
    pixels.push_back(Pixel{x + 1, y});

    while (y < pixel_2.y) {
        if (d < 0) { // M in stanga dreptei (a pozitiv, x mai mic)
            d += dNE;
            x++;
        } else {
            d += dN;
        }
        y++;
        pixels.push_back(Pixel{x - 1, y});
        pixels.push_back(Pixel{x, y});
        pixels.push_back(Pixel{x + 1, y});
    }

    return pixels;
}

vector<Pixel> getSeventhOctanePixels(Pixel pixel_1, Pixel pixel_2) {
    vector<Pixel> pixels;

    int d_x = pixel_2.x - pixel_1.x; // x_1 <= x_2
    int d_y = pixel_2.y - pixel_1.y; // y_1 > y_2

    int d = 2 * d_x + d_y; // 2 * (a / 2 - b)
    int dS = 2 * d_x; // 2 * (-b)
    int dSE = 2 * (d_x + d_y); // 2 * (a - b)

    int x = pixel_1.x;
    int y = pixel_1.y;
    pixels.push_back(Pixel{x - 1, y});
    pixels.push_back(Pixel{x, y});
    pixels.push_back(Pixel{x + 1, y});

    while (y > pixel_2.y) {
        if (d <= 0) { // M in dreapta dreptei (a negativ, x mai mare)
            d += dS;
        } else {
            d += dSE;
            x++;
        }
        y--;
        pixels.push_back(Pixel{x - 1, y});
        pixels.push_back(Pixel{x, y});
        pixels.push_back(Pixel{x + 1, y});
    }

    return pixels;
}

vector<Pixel> getEighthOctanePixels(Pixel pixel_1, Pixel pixel_2) {
    vector<Pixel> pixels;
    int d_x = pixel_2.x - pixel_1.x; // x_1 <= x_2
    int d_y = pixel_2.y - pixel_1.y; // y_1 > y_2

    int d = 2 * d_y + d_x; // 2 * (a - b / 2)
    int dE = 2 * d_y; // 2 * a
    int dSE = 2 * (d_x + d_y); // 2 * (a - b)

    int x = pixel_1.x;
    int y = pixel_1.y;
    pixels.push_back(Pixel{x, y - 1});
    pixels.push_back(Pixel{x, y});
    pixels.push_back(Pixel{x, y + 1});

    while (x < pixel_2.x) {
        if (d < 0) { // M deasupra dreptei (b negativ, y mai mare)
            d += dSE;
            y--;
        } else {
            d += dE;
        }
        x++;
        pixels.push_back(Pixel{x, y - 1});
        pixels.push_back(Pixel{x, y});
        pixels.push_back(Pixel{x, y + 1});
    }

    return pixels;
}

vector<Pixel> getPixels(Pixel pixel_1, Pixel pixel_2) {
    if (pixel_1.x > pixel_2.x) {
        swap(pixel_1.x, pixel_2.x);
        swap(pixel_1.y, pixel_2.y);
    }
    double slope = computeSlope(pixel_1, pixel_2);
    if (slope >= 0) {
        if (slope <= 1) {
            return getFirstOctanePixels(pixel_1, pixel_2);
        }
        return getSecondOctanePixels(pixel_1, pixel_2);
    } else {
        if (slope >= -1) {
            return getEighthOctanePixels(pixel_1, pixel_2);
        }
        return getSeventhOctanePixels(pixel_1, pixel_2);
    }
}

void Initialization() {
    glClearColor(1.0, 1.0, 1.0, 1.0);
    glPolygonMode(GL_FRONT, GL_FILL);
}

void Display() {
    int width = glutGet(GLUT_WINDOW_WIDTH);
    int height = glutGet(GLUT_WINDOW_HEIGHT);
    int length = height < width ? height : width;

    glClear(GL_COLOR_BUFFER_BIT);
    glPushMatrix();
    glLoadIdentity();
    glScaled((double) length / width * SCALE, (double) length / height * SCALE, 1);

    cartesianGrid.drawGrid();
    if (global_pixel_1.x != -1 && global_pixel_1.y != -1 && global_pixel_2.x != -1 && global_pixel_2.y != -1) {
        cartesianGrid.drawPixels(getPixels(global_pixel_1, global_pixel_2));
        cartesianGrid.drawLine(global_pixel_1, global_pixel_2);
    }

    glPopMatrix();
    glFlush();
}

void Keyboard(unsigned char key, int x, int y) {
    if (key == 27) {
        exit(0);
    }
    glutPostRedisplay();
}

void Mouse(int button, int state, int x, int y) {
    if (button == GLUT_LEFT_BUTTON) {
        if (state == GLUT_DOWN) {
            click_1.x = x;
            click_1.y = y;
        } else {
            click_2.x = x;
            click_2.y = y;
        }
    }
    if (click_1.x != -1 && click_1.y != -1 && click_2.x != -1 && click_2.y != -1) {
        global_pixel_1 = cartesianGrid.getPixel(click_1);
        global_pixel_2 = cartesianGrid.getPixel(click_2);
    }
}

void Reshape(int width, int height) {
    glViewport((GLint) 0, (GLint) 0, (GLsizei) width, (GLsizei) height);
}

int main(int argc, char **argv) {
    glutInit(&argc, argv);
    glutInitWindowSize(WINDOW, WINDOW);
    glutInitWindowPosition(100, 100);
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
    glutCreateWindow(argv[0]);
    Initialization();
    glutDisplayFunc(Display);
    glutKeyboardFunc(Keyboard);
    glutMouseFunc(Mouse);
    glutReshapeFunc(Reshape);
    glutMainLoop();
    return 0;
}