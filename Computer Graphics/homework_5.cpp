#include <algorithm>
#include <cmath>
#include <GL/glut.h>
#include <map>
#include <vector>

#define WINDOW      300
#define ROWS        30
#define COLUMNS     30

#define SCALE       0.9
#define CIRCLE      ((1.0 - SCALE) * 2.0)

#define CENTER_X    ((int) (COLUMNS / 2))
#define CENTER_Y    ((int) (ROWS / 2))

#define X_RADIUS    ((int) (min(ROWS, COLUMNS) / 4) - 1)
#define Y_RADIUS    ((int) (min(ROWS, COLUMNS) / 2) - 1)

#define CLICKS      10

using namespace std;

unsigned char previousKey = 27;

struct Click {
    int x;
    int y;
};

struct Pixel {
    int x = -1;
    int y = -1;
};

vector<Pixel> click_pixels;

struct Edge {
    Pixel start;
    Pixel end;
};

struct Intersection {
    int yMax;
    double xMin;
    double ratio;

    bool operator<(Intersection intersection) const {
        return xMin < intersection.xMin;
    }
};

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

    int getRows() const {
        return rows;
    }

    int getColumns() const {
        return columns;
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

    static void drawCircle(Point center, double radius, int lines) {
        glLineWidth(3);
        glColor3d(1.0, 0.0, 0.0);
        glBegin(GL_LINE_LOOP);
        for (int i = 0; i < lines; i++) {
            double theta = 8.0 * atan(1) * i / lines;
            double x = radius * cos(theta);
            double y = radius * sin(theta);
            glVertex2d(center.x + x, center.y + y);
        }
        glEnd();
    }

    static void drawEllipse(Point center, double xRadius, double yRadius, int lines) {
        glLineWidth(3);
        glColor3d(1.0, 0.0, 0.0);
        glBegin(GL_LINE_LOOP);
        for (int i = 0; i < lines; i++) {
            double theta = 8.0 * atan(1) * i / lines;
            double x = xRadius * cos(theta);
            double y = yRadius * sin(theta);
            glVertex2d(center.x + x, center.y + y);
        }
        glEnd();
    }

    void drawCircle(Pixel pixel, int radius) {
        if (isPixel(pixel)) {
            Point point = points[pixel.y][pixel.x];
            drawCircle(point, cell * radius, 100);
        }
    }

    void drawEllipse(Pixel pixel, int xRadius, int yRadius) {
        if (isPixel(pixel)) {
            Point point = points[pixel.y][pixel.x];
            drawEllipse(point, cell * xRadius, cell * yRadius, 100);
        }
    }

    void drawEdges() {
        glColor3d(1.0, 0.0, 0.0);
        glLineWidth(3);
        glBegin(GL_LINE_STRIP);
        for (Pixel pixel:click_pixels) {
            glVertex2d(points[pixel.y][pixel.x].x, points[pixel.y][pixel.x].y);
        }
        glEnd();
    }

    void drawPolygon(const vector<Pixel> &pixels) {
        glColor3d(1.0, 0.0, 0.0);
        glLineWidth(3);
        glBegin(GL_LINE_LOOP);
        for (Pixel pixel:pixels) {
            glVertex2d(points[pixel.y][pixel.x].x, points[pixel.y][pixel.x].y);
        }
        glEnd();
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
};

CartesianGrid cartesianGrid(ROWS, COLUMNS);

class Circle {
private:
    Pixel center;
    int radius;
    vector<Pixel> pixels;

    void addThickeningPixels(int x, int y) {
        int scaledX = x - center.x;
        int scaledY = y - center.y;

        if (abs(scaledX) >= abs(scaledY)) {
            pixels.push_back(Pixel{x - 1, y});
            pixels.push_back(Pixel{x, y});
            pixels.push_back(Pixel{x + 1, y});
        }
        if (abs(scaledX) <= abs(scaledY)) {
            pixels.push_back(Pixel{x, y - 1});
            pixels.push_back(Pixel{x, y});
            pixels.push_back(Pixel{x, y + 1});
        }
    }

    void addPixels(int x, int y) {
        int oppositeX = 2 * center.x - x;
        int oppositeY = 2 * center.y - y;

        addThickeningPixels(x, y); // N-E
        addThickeningPixels(x, oppositeY); // S-E
        addThickeningPixels(oppositeX, y); // N-W
        addThickeningPixels(oppositeX, oppositeY); // S-W
        if (x != y) {
            int difference = center.x - center.y;

            addThickeningPixels(y + difference, x - difference); // E-N
            addThickeningPixels(y + difference, oppositeX - difference); // E-S
            addThickeningPixels(oppositeY + difference, x - difference); // W-N
            addThickeningPixels(oppositeY + difference, oppositeX - difference); // W-S
        }
    }

    void addFirstOctanePixels() {
        int x = center.x + radius;
        int y = center.y;

        int d = 1 - radius; // F(R - 1/2, 0 + 1) - 1/4
        int dN = 3; // F(R - 1/2, 0 + 1 + 1) - 1/4 = d + 2 * y + 3
        int dNW = -2 * radius + 5; // F(R - 1/2 - 1/2, 0 + 1 + 1) - 1/4 = d - 2 * x + 2 * y + 5

        addPixels(x, y);

        while (x - center.x > y - center.y) {
            if (d < 0) {
                d += dN;
                dN += 2;
                dNW += 2;
            } else {
                d += dNW;
                dN += 2;
                dNW += 4;
                x--;
            }
            y++;
            addPixels(x, y);
        }
    }

    void addSecondOctanePixels() {
        int x = center.x;
        int y = center.y + radius;

        int d = 1 - radius; // F(0 + 1, R - 1/2) - 1/4
        int dE = 3; // F(0 + 1 + 1, R - 1/2) = d + 2 * x + 3
        int dSE = -2 * radius + 5; // F(0 + 1 + 1, R - 1/2 - 1) = d + 2 * x - 2 * y + 5

        addPixels(x, y);

        while (y - center.y > x - center.x) {
            if (d < 0) {
                d += dE;
                dE += 2;
                dSE += 2;
            } else {
                d += dSE;
                dE += 2;
                dSE += 4;
                y--;
            }
            x++;
            addPixels(x, y);
        }
    }

    void addSeventhOctanePixels() {
        int x = center.x;
        int y = center.y - radius;

        int d = 1 - radius;  // F(0 + 1, -R + 1/2) - 1/4
        int dE = 3; // F(0 + 1 + 1, -R + 1/2) = d + 2 * x + 3
        int dNE = -2 * radius + 5; // F(0 + 1 + 1, -R + 1/2 + 1) = d + 2 * x + 2 * y + 5

        addPixels(x, y);

        while (abs(y - center.y) > x - center.x) {
            if (d < 0) {
                d += dE;
                dE += 2;
                dNE += 2;
            } else {
                d += dNE;
                dE += 2;
                dNE += 4;
                y++;
            }
            x++;
            addPixels(x, y);
        }
    }

    void addEighthOctanePixels() {
        int x = center.x + radius;
        int y = center.y;

        int d = 1 - radius; // F(R - 1/2, 0 - 1) - 1/4
        int dS = 3; // F(R - 1/2, 0 - 1 - 1) = d - 2 * y + 3
        int dSW = -2 * radius + 5; // F(R - 1/2 - 1/2, 0 - 1 - 1) - 1/4 = d - 2 * x - 2 * y + 5

        addPixels(x, y);

        while (x - center.x > abs(y - center.y)) {
            if (d < 0) {
                d += dS;
                dS += 2;
                dSW += 2;
            } else {
                d += dSW;
                dS += 2;
                dSW += 4;
                x--;
            }
            y--;
            addPixels(x, y);
        }
    }

public:
    Circle(Pixel center, int radius) {
        this->center = center;
        this->radius = radius;
    }

    vector<Pixel> getPixels(int key) {
        switch (key) {
            case '1':
                addFirstOctanePixels();
                break;
            case '2':
                addSecondOctanePixels();
                break;
            case '3':
                addSeventhOctanePixels();
                break;
            case '4':
                addEighthOctanePixels();
            default:;
        }
        return pixels;
    }
};

class Ellipse {
private:
    Pixel center;
    int xRadius;
    int yRadius;
    int squaredXRadius;
    int squaredYRadius;
    vector<Pixel> pixels;

    void addPixels(int xEnd, int y) {
        for (int x = -xEnd; x <= xEnd; x++) {
            pixels.push_back(Pixel{x + center.x, y + center.y});
        }
        for (int x = -xEnd; x <= xEnd; x++) {
            pixels.push_back(Pixel{x + center.x, -y + center.y});
        }
    }

    void addFirstQuadrantPixels() {
        int x = 0;
        int y = yRadius;
        addPixels(x, y);

        double d = 0;

        while (squaredXRadius * (y - 0.5) > squaredYRadius * (x + 1)) {
            double dS = squaredXRadius * (1 - 2 * y);
            double dE = squaredYRadius * (2 * x + 1);
            double dSE = dS + dE;
            if (d + dE <= 0) {
                d += dE;
            } else {
                d += dSE;
                y--;
            }
            x++;
            addPixels(x, y);
        }

        while (y > 0) {
            double dS = squaredXRadius * (1 - 2 * y);
            double dE = squaredYRadius * (2 * x + 1);
            double dSE = dS + dE;
            if (d + dSE <= 0) {
                d += dSE;
                x++;
            } else {
                d += dS;
            }
            y--;
            addPixels(x, y);
        }
    }

    void addSecondQuadrantPixels() {
        int x = 0;
        int y = yRadius;
        addPixels(-x, y);

        double d = 0;

        while (squaredXRadius * (y - 0.5) > squaredYRadius * abs(x - 1)) {
            double dS = squaredXRadius * (1 - 2 * y);
            double dW = squaredYRadius * (1 - 2 * x);
            double dSW = dS + dW;
            if (d + dW <= 0) {
                d += dW;
            } else {
                d += dSW;
                y--;
            }
            x--;
            addPixels(-x, y);
        }

        while (y > 0) {
            double dS = squaredXRadius * (1 - 2 * y);
            double dW = squaredYRadius * (1 - 2 * x);
            double dSW = dS + dW;
            if (d + dSW <= 0) {
                d += dSW;
                x--;
            } else {
                d += dS;
            }
            y--;
            addPixels(-x, y);
        }
    }

    void addThirdQuadrantPixels() {
        int x = 0;
        int y = -yRadius;
        addPixels(-x, y);

        double d = 0;

        while (squaredXRadius * abs(y + 0.5) > squaredYRadius * abs(x - 1)) {
            double dN = squaredXRadius * (2 * y + 1);
            double dW = squaredYRadius * (1 - 2 * x);
            double dNW = dN + dW;
            if (d + dW <= 0) { // W in interior
                d += dW;
            } else {
                d += dNW;
                y++;
            }
            x--;
            addPixels(-x, y);
        }

        while (y < 0) {
            double dN = squaredXRadius * (2 * y + 1);
            double dW = squaredYRadius * (1 - 2 * x);
            double dNW = dN + dW;
            if (d + dNW <= 0) {
                d += dNW;
                x--;
            } else {
                d += dN;
            }
            y++;
            addPixels(-x, y);
        }
    }

    void addFourthQuadrantPixels() {
        int x = 0;
        int y = -yRadius;
        addPixels(x, y);

        double d = 0;

        while (squaredXRadius * abs(y + 0.5) > squaredYRadius * (x + 1)) {
            double dN = squaredXRadius * (2 * y + 1);
            double dE = squaredYRadius * (2 * x + 1);
            double dNE = dN + dE;
            if (d + dE <= 0) {
                d += dE;
            } else {
                d += dNE;
                y++;
            }
            x++;
            addPixels(x, y);
        }

        while (y < 0) {
            double dN = squaredXRadius * (2 * y + 1);
            double dE = squaredYRadius * (2 * x + 1);
            double dNE = dN + dE;
            if (d + dNE <= 0) {
                d += dNE;
                x++;
            } else {
                d += dN;
            }
            y++;
            addPixels(x, y);
        }
    }

public:
    Ellipse(Pixel center, int xRadius, int yRadius) {
        this->center = center;
        this->xRadius = xRadius;
        this->yRadius = yRadius;
        this->squaredXRadius = this->xRadius * this->xRadius;
        this->squaredYRadius = this->yRadius * this->yRadius;
    }

    vector<Pixel> getPixels(int key) {
        switch (key) {
            case '1':
                addFirstQuadrantPixels();
                break;
            case '2':
                addSecondQuadrantPixels();
                break;
            case '3':
                addThirdQuadrantPixels();
                break;
            case '4':
                addFourthQuadrantPixels();
            default:;
        }
        return pixels;
    }
};

class Polygon {
private:
    int ySize = cartesianGrid.getRows() + 1;
    vector<Edge> edges;
    map<int, vector<Intersection>> et;
    map<int, vector<Intersection>> ssms;
public:
    explicit Polygon(vector<Pixel> pixels) {
        for (int index = 0; index < pixels.size() - 1; index++) {
            edges.push_back(Edge{pixels[index], pixels[index + 1]});
        }
        edges.push_back(Edge{pixels.back(), pixels.front()});
    }

    void initialize() {
        int xMin;
        int yMin;
        int xMax;
        int yMax;

        for (int index = 0; index <= ySize; index++) {
            et[index] = vector<Intersection>();
        }

        // from the bottom up
        // ratio: negative (to left), positive (to right)
        for (Edge edge: edges) {
            if (edge.start.y != edge.end.y) {
                yMin = min(edge.start.y, edge.end.y);
                yMax = max(edge.start.y, edge.end.y);
                xMin = (yMin == edge.start.y) ? edge.start.x : edge.end.x;
                xMax = (yMax == edge.start.y) ? edge.start.x : edge.end.x;
                et[yMin].push_back(Intersection{yMax, (double) xMin, (double) (xMin - xMax) / (yMin - yMax)});
            }
        }

        for (int index = 0; index <= ySize; index++) {
            vector<Intersection> intersections = et[index];
            sort(intersections.begin(), intersections.end());
        }
    };

    void compute() {
        for (int index = 0; index < ySize; index++) {
            ssms[index] = vector<Intersection>();
        }

        int y = -1;
        for (int index = 0; index < ySize; index++) {
            if (!et[index].empty()) {
                y = index;
                break;
            }
        }

        if (y == -1) {
            return;
        }

        vector<Intersection> activeSSM;
        do {
            activeSSM.insert(activeSSM.end(), et[y].begin(), et[y].end());

            // keep new edge
            int index = 0;
            while (index < activeSSM.size()) {
                if (activeSSM[index].yMax == y) {
                    activeSSM.erase(activeSSM.begin() + index);
                } else {
                    index++;
                }
            }

            sort(activeSSM.begin(), activeSSM.end());

            ssms[y] = activeSSM;

            y++;

            for (auto &intersection : activeSSM) {
                intersection.xMin += intersection.ratio;
            }
        } while (y < ySize && !(et[y].empty() && activeSSM.empty()));
    }

    vector<Pixel> getPixels() {
        vector<Pixel> pixels;

        for (auto &ssm : ssms) {
            int y = ssm.first;
            vector<Intersection> intersections = ssm.second;
            if (intersections.empty()) {
                continue;
            }

            auto intersection = intersections.begin();
            int xLeft = (int) round(intersections.begin()->xMin);
            int xRight = (int) round(next(intersections.end(), -1)->xMin);
            bool parity = true;

            for (int index = xLeft; index < xRight + 1 && intersection != intersections.end(); index++) {
                if ((int) intersection->xMin != intersection->xMin) {
                    // in right or out left
                    if ((!parity && floor(intersection->xMin) == index - 1) ||
                        (parity && ceil((intersection->xMin)) == index)) {
                        while (intersection != intersections.end() && index >= intersection->xMin) {
                            parity = !parity;
                            intersection = next(intersection);
                        }
                    } else {
                        while (intersection != intersections.end() && index >= intersection->xMin) {
                            parity = !parity;
                            intersection = next(intersection);
                        }
                    }
                } else {
                    auto nextIntersection = next(intersection);
                    // convex vertex
                    if (nextIntersection != intersections.end() && intersection->xMin == nextIntersection->xMin &&
                        (y != intersection->yMax || y != nextIntersection->yMax) && index == intersection->xMin) {
                        pixels.push_back(Pixel{index, y});
                        while (intersection != intersections.end() && index >= intersection->xMin) {
                            parity = !parity;
                            intersection = next(intersection);
                        }
                        continue;
                    } else if (index == intersection->xMin || index == nextIntersection->xMin) {
                        // on edge
                        while (intersection != intersections.end() && index >= intersection->xMin) {
                            parity = !parity;
                            intersection = next(intersection);
                        }
                    } else {
                        while (intersection != intersections.end() && index >= intersection->xMin) {
                            parity = !parity;
                            intersection = next(intersection);
                        }
                    }
                }

                if (!parity) {
                    pixels.push_back(Pixel{index, y});
                }
            }
        }
        return pixels;
    }
};

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
    if (previousKey >= '1' && previousKey <= '8') {
        Pixel center{CENTER_X, CENTER_Y};
        if (previousKey <= '4') {
            cartesianGrid.drawCircle(center, X_RADIUS);
            Circle circle(center, X_RADIUS);
            cartesianGrid.drawPixels(circle.getPixels(previousKey));
        } else {
            cartesianGrid.drawEllipse(center, X_RADIUS, Y_RADIUS);
            class Ellipse ellipse(center, X_RADIUS, Y_RADIUS);
            cartesianGrid.drawPixels(ellipse.getPixels(previousKey - 4));
        }
    } else if (previousKey == '9') {
        FILE *file = fopen("polygon.txt", "r");
        vector<Pixel> pixels;
        if (file) {
            int n;
            fscanf(file, "%d", &n);
            int x;
            int y;
            for (int index = 0; index < n; index++) {
                fscanf(file, "%d %d", &x, &y);
                pixels.push_back(Pixel{x, y});
            }
            fclose(file);
        } else {
            pixels.push_back(Pixel{2, 3});
            pixels.push_back(Pixel{7, 1});
            pixels.push_back(Pixel{13, 5});
            pixels.push_back(Pixel{13, 11});
            pixels.push_back(Pixel{7, 7});
            pixels.push_back(Pixel{2, 9});
        }
        cartesianGrid.drawPolygon(pixels);
        class Polygon polygon(pixels);
        polygon.initialize();
        polygon.compute();
        cartesianGrid.drawPixels(polygon.getPixels());
    } else if (previousKey == '0') {
        cartesianGrid.drawEdges();
        if (click_pixels.size() > 3) {
            bool equal = click_pixels.front().x == click_pixels.back().x &&
                         click_pixels.front().y == click_pixels.back().y;
            if (equal || click_pixels.size() == CLICKS) {
                if (equal) {
                    click_pixels.pop_back();
                }
                class Polygon polygon(click_pixels);
                polygon.initialize();
                polygon.compute();
                cartesianGrid.drawPixels(polygon.getPixels());
                click_pixels.clear();
            }
        }
    }

    glPopMatrix();
    glFlush();
}

void Keyboard(unsigned char key, int x, int y) {
    previousKey = key;
    if (key == 27) {
        exit(0);
    }
    glutPostRedisplay();
}

void Mouse(int button, int state, int x, int y) {
    if (previousKey == '0') {
        if (button == GLUT_LEFT_BUTTON) {
            if (state == GLUT_DOWN) {
                click_pixels.push_back(cartesianGrid.getPixel(Click{x, y}));
            }
        }
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