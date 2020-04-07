#include <chrono>
#include <cmath>
#include <cstring>
#include <iostream>
#include <random>

using namespace std;
using namespace std::chrono;

constexpr unsigned int iterations = 1;
constexpr unssigned char bits = 5;
constexpr unsigned int runs = 32;

enum class method
{
    NAHC,
    SAHC,
    SA
};

const double pi = atan(1) * 4;

inline double Ackley(const double* arguments, unsigned char dimensions)
{
    double value = 20 + exp(1);

    double firstSum = 0;
    double secondSum = 0;

    for (unsigned char index = 0; index < dimensions; ++index)
    {
        firstSum += arguments[index] * arguments[index];
        secondSum += cos(2 * pi * arguments[index]);
    }

    firstSum = 20 * exp(-0.2 * sqrt(firstSum / dimensions));

    secondSum = exp(secondSum / dimensions);

    value -= firstSum + secondSum;

    return value;
}

inline double Michalewics(const double* arguments, unsigned char dimensions)
{
    double value = 0;

    for (unsigned char index = 0; index < dimensions; ++index)
    {
        value -= sin(arguments[index]) * pow(sin(arguments[index] * arguments[index] * (static_cast<double>(index) + 1) / pi), 20);
    }

    return value;
}

inline double Rastrigin(const double* arguments, unsigned char dimensions)
{
    double value = 10 * static_cast<double>(dimensions);

    for (unsigned char index = 0; index < dimensions; ++index)
    {
        value += arguments[index] * arguments[index] - 10 * cos(2 * pi * arguments[index]);
    }

    return value;
}

inline double Schwefel(const double* arguments, unsigned char dimensions)
{
    double value = 418.9829 * dimensions;

    for (unsigned char index = 0; index < dimensions; ++index)
    {
        value -= arguments[index] * sin(sqrt(abs(arguments[index])));
    }

    return value;
}

inline double Cubic(const double* arguments, unsigned char dimensions)
{
    double value = 100;

    for (unsigned char index = 0; index < dimensions; ++index)
    {
        value += pow(arguments[index], 3) - 60 * pow(arguments[index], 2) + 900 * arguments[index];
    }

    return value;
}

bool Minimum(const double first, const double second)
{
    return first < second;
}

bool Maximum(const double first, const double second)
{
    return first > second;
}

class FunctionOptimum
{
    bool (*Optimum)(const double first, const double second);
    char annealing;

    const char* functionName;
    double(*Function)(const double* argument, unsigned char dimensions);

    double leftEnd;
    double rightEnd;
    unsigned char dimensions;
    unsigned short totalBits;

    unsigned char* bitString;

    long long* candidateNumbers;
    double* candidateArguments;
    double candidateValue;

    long long* neighbourNumbers;
    double* neighbourArguments;
    double neighbourValue;

    double* optimumArguments;
    double optimumValue;

    method usedMethod;
public:
    FILE* file;
    FILE* neighbours;

    FunctionOptimum(bool(*newOptimum)(const double first, const double second), const char* newFunctionName, double(*newFunction)(const double* argument, unsigned char dimensions), double newLeftEnd, double newRightEnd, unsigned char newDimensions, method newMethod)
    {
        Optimum = newOptimum;
        if (Optimum == Minimum)
        {
            annealing = -1;
        }
        else
        {
            annealing = 1;
        }

        functionName = newFunctionName;
        Function = newFunction;

        leftEnd = newLeftEnd;
        rightEnd = newRightEnd;
        dimensions = newDimensions;
        totalBits = bits * dimensions;

        bitString = new unsigned char[totalBits];

        candidateNumbers = new long long[dimensions];
        candidateArguments = new double[dimensions];
        candidateValue = DBL_MIN;

        neighbourNumbers = new long long[dimensions];
        neighbourArguments = new double[dimensions];
        neighbourValue = DBL_MIN;

        optimumArguments = new double[dimensions];
        optimumValue = DBL_MIN;

        usedMethod = newMethod;

        time_t rawTime = time(nullptr);
        struct tm* timeInformation = localtime(&rawTime);
        char timeBuffer[80];
        strftime(timeBuffer, 80, "%d-%m-%Y %H-%M-%S", timeInformation);

        char filenameBuffer[128];
        strcpy(filenameBuffer, functionName);
        switch (usedMethod)
        {
        case method::NAHC:
            strcat(filenameBuffer, " NAHC ");
            break;
        case method::SAHC:
            strcat(filenameBuffer, " SAHC ");
            break;
        case method::SA:
            strcat(filenameBuffer, " SA ");
            break;
        }
        char dimensionsString[4];
        _itoa(dimensions, dimensionsString, 10);
        strcat(filenameBuffer, dimensionsString);
        //strcat(filenameBuffer, " ");
        //strcat(filenameBuffer, timeBuffer);
        char filenameNeighbours[128];
        strcpy(filenameNeighbours, filenameBuffer);
        strcat(filenameBuffer, ".csv");

        file = fopen(filenameBuffer, "w");
        fprintf(file, "First argument, ");
        if (Optimum == Minimum)
        {
            for (unsigned int index = 1; index < dimensions; ++index)
            {
                fprintf(file, "Minimum argument %d, ", index);
            }
            fprintf(file, "Minimum argument %d, ", dimensions);
            fprintf(file, "Minimum value, Elapsed seconds\n");
        }
        else
        {
            for (unsigned int index = 1; index < dimensions; ++index)
            {
                fprintf(file, "Maximum argument %d, ", index);
            }
            fprintf(file, "Maximum argument %d, ", dimensions);
            fprintf(file, "Maximum value, Elapsed seconds\n");
        }
        
        strcat(filenameNeighbours, " neighbours.csv");
        neighbours = fopen(filenameNeighbours, "w");
        fprintf(neighbours, "Neighbour argument, Neighbour value\n");
    }

    ~FunctionOptimum()
    {
        delete[] bitString;
        delete[] candidateNumbers;
        delete[] candidateArguments;
        delete[] neighbourNumbers;
        delete[] neighbourArguments;
        delete[] optimumArguments;
        fclose(file);
        fclose(neighbours);
    }

    void ChangeDimensions(unsigned char newDimensions)
    {
        dimensions = newDimensions;

        delete[] bitString;
        bitString = new unsigned char[totalBits];

        delete[] candidateNumbers;
        candidateNumbers = new long long[dimensions];
        delete[] candidateArguments;
        candidateArguments = new double[dimensions];

        delete[] neighbourNumbers;
        neighbourNumbers = new long long[dimensions];
        delete[] neighbourArguments;
        neighbourArguments = new double[dimensions];

        delete[] optimumArguments;
        optimumArguments = new double[dimensions];
    }

    void SetFirst()
    {
        for (unsigned char index = 0; index < dimensions; ++index)
        {
            optimumArguments[index] = leftEnd;
        }
        optimumValue = Function(optimumArguments, dimensions);
    }

    void EvaluateCandidate()
    {
        candidateValue = Function(candidateArguments, dimensions);

        if (Optimum(candidateValue, optimumValue))
        {
            memcpy(optimumArguments, candidateArguments, dimensions * sizeof(double));
            optimumValue = candidateValue;
        }
    }

    void Base10ToBase2(unsigned char index, unsigned char value)
    {
        for (unsigned char position = 0; position < bits; ++position)
        {
            bitString[bits * (index + 1) - position - 1] = value % 2;
            value /= 2;
        }
    }

    void GenerateCandidate(unsigned char value)
    {
        for (unsigned char index = 0; index < dimensions; ++index)
        {
            candidateArguments[index] = candidateNumbers[index] = value;

            Base10ToBase2(index, value);
        }
    }

    void GenerateRandomCandidate()
    {
        for (unsigned char index = 0; index < dimensions; ++index)
        {
            candidateNumbers[index] = 0;

            for (unsigned char position = 0; position < bits; ++position)
            {
                bitString[bits * index + position] = rand() % 2; // generate random bit string
                candidateNumbers[index] = candidateNumbers[index] * 2 + bitString[bits * index + position]; // convert base 2 to base 10
            }

            candidateArguments[index] = static_cast<double>(candidateNumbers[index]) / (pow(2, bits) - 1); // translate to closed interval [0, 1]
            candidateArguments[index] *= rightEnd - leftEnd; // translate to closed interval [0, rightEnd - leftEnd]
            candidateArguments[index] += leftEnd; // translate to closed interval [leftEnd, rightEnd]
        }
    }

    void ChangeBit(unsigned short position)
    {
        bitString[position] = 1 - bitString[position];
    }

    void GenerateNeighbour(unsigned short position)
    {
        const unsigned char changedArgument = position / bits;
        const unsigned char changedBit = position % bits;

        ChangeBit(position);

        if (bitString[position] == 1)
        {
            neighbourNumbers[changedArgument] += pow(2, bits - changedBit - 1);
        }
        else
        {
            neighbourNumbers[changedArgument] -= pow(2, bits - changedBit - 1);
        }

        neighbourArguments[changedArgument] = static_cast<double>(neighbourNumbers[changedArgument]) / (pow(2, bits) - 1); // translate to closed interval [0, 1]
        neighbourArguments[changedArgument] *= rightEnd - leftEnd; // translate to closed interval [0, rightEnd - leftEnd]
        neighbourArguments[changedArgument] += leftEnd; // translate to closed interval [leftEnd, rightEnd]
    }

    void RestoreNeighbour(unsigned short position)
    {
        const unsigned char changedArgument = position / bits;
        const unsigned char changedBit = position % bits;

        ChangeBit(position);

        if (bitString[position] == 1)
        {
            neighbourNumbers[changedArgument] += pow(2, bits - changedBit - 1);
        }
        else
        {
            neighbourNumbers[changedArgument] -= pow(2, bits - changedBit - 1);
        }
    }

    bool FirstImprovement()
    {
        memcpy(neighbourNumbers, candidateNumbers, dimensions * sizeof(double));
        memcpy(neighbourArguments, candidateArguments, dimensions * sizeof(double));
        
        for (unsigned char index = 0; index < dimensions; ++index)
        {
            Base10ToBase2(index, neighbourNumbers[index]);
        }

        for (unsigned short position = 0; position < totalBits; ++position)
        {
            GenerateNeighbour(position);
            neighbourValue = Function(neighbourArguments, dimensions);
            fprintf(neighbours, "%d, %d\n", (int)neighbourArguments[0], (int)neighbourValue);

            if (Optimum(neighbourValue, candidateValue))
            {
                memcpy(candidateNumbers, neighbourNumbers, dimensions * sizeof(double));
                memcpy(candidateArguments, neighbourArguments, dimensions * sizeof(double));
                candidateValue = neighbourValue;

                return false;
            }

            RestoreNeighbour(position);
        }

        return true;
    }

    bool BestImprovement()
    {
        bool isCandidateOptimum = true;

        memcpy(neighbourNumbers, candidateNumbers, dimensions * sizeof(double));
        memcpy(neighbourArguments, candidateArguments, dimensions * sizeof(double));

        for (unsigned char index = 0; index < dimensions; ++index)
        {
            Base10ToBase2(index, neighbourNumbers[index]);
        }

        for (unsigned short position = 0; position < totalBits; ++position)
        {
            GenerateNeighbour(position);
            neighbourValue = Function(neighbourArguments, dimensions);
            fprintf(neighbours, "%d, %d\n", (int)neighbourArguments[0], (int)neighbourValue);

            if (Optimum(neighbourValue, candidateValue))
            {
                memcpy(candidateNumbers, neighbourNumbers, dimensions * sizeof(double));
                memcpy(candidateArguments, neighbourArguments, dimensions * sizeof(double));
                candidateValue = neighbourValue;

                isCandidateOptimum = false;
            }

            RestoreNeighbour(position);
        }

        return isCandidateOptimum;
    }

    void Improvement(uniform_real_distribution<double> distribution, default_random_engine generator, double temperature)
    {
        memcpy(neighbourNumbers, candidateNumbers, dimensions * sizeof(double));
        memcpy(neighbourArguments, candidateArguments, dimensions * sizeof(double));

        for (unsigned char index = 0; index < dimensions; ++index)
        {
            Base10ToBase2(index, neighbourNumbers[index]);
        }

        for (unsigned short position = 0; position < totalBits; ++position)
        {
            GenerateNeighbour(position);
            neighbourValue = Function(neighbourArguments, dimensions);
            fprintf(neighbours, "%d, %d\n", (int)neighbourArguments[0], (int)neighbourValue);

            if (Optimum(neighbourValue, candidateValue))
            {
                memcpy(candidateNumbers, neighbourNumbers, dimensions * sizeof(double));
                memcpy(candidateArguments, neighbourArguments, dimensions * sizeof(double));
                candidateValue = neighbourValue;
            }
            else if (distribution(generator) < exp(annealing * (neighbourValue - candidateValue) / temperature))
            {
                memcpy(candidateNumbers, neighbourNumbers, dimensions * sizeof(double));
                memcpy(candidateArguments, neighbourArguments, dimensions * sizeof(double));
                candidateValue = neighbourValue;
            }
            else
            {
                RestoreNeighbour(position);
            }
        }
    }

    void NAHC(unsigned char index)
    {
        for (unsigned short iterator = 1; iterator <= iterations; ++iterator)
        {
            bool isCandidateOptimum = false;

            GenerateCandidate(index);
            EvaluateCandidate();
            fprintf(neighbours, "%d, %d\n", (int)candidateArguments[0], (int)candidateValue);

            while (!isCandidateOptimum)
            {
                isCandidateOptimum = FirstImprovement();
            }

            EvaluateCandidate();
        }
    }

    void SAHC(unsigned char index)
    {
        for (unsigned short iterator = 1; iterator <= iterations; ++iterator)
        {
            GenerateCandidate(index);
            EvaluateCandidate();
            fprintf(neighbours, "%d, %d\n", (int)candidateArguments[0], (int)candidateValue);
            
            bool isCandidateOptimum = false;
            while (!isCandidateOptimum)
            {
                isCandidateOptimum = BestImprovement();
            }

            EvaluateCandidate();
        }
    }

    void SA(unsigned char index)
    {
        const unsigned int seed = system_clock::now().time_since_epoch().count();
        const default_random_engine generator(seed);
        const uniform_real_distribution<double> distribution(0, 1);

        constexpr double coolingRate = 0.1;

        for (unsigned short iterator = 1; iterator <= iterations; ++iterator)
        {
            GenerateCandidate(index);
            EvaluateCandidate();
            fprintf(neighbours, "%d, %d\n", (int)candidateArguments[0], (int)candidateValue);

            for (double temperature = 1000; temperature > 0.01; temperature *= 1 - coolingRate)
            {
                Improvement(distribution, generator, temperature);
            }

            EvaluateCandidate();
        }
    }

    void Run(unsigned char index)
    {
        SetFirst();

        time_point<system_clock> startTime, endTime;

        startTime = system_clock::now();
        switch (usedMethod)
        {
        case method::NAHC:
            NAHC(index);
            break;
        case method::SAHC:
            SAHC(index);
            break;
        case method::SA:
            SA(index);
            break;
        }
        endTime = system_clock::now();

        const duration<double> elapsedSeconds = endTime - startTime;

        Print(elapsedSeconds.count(), index);
    }

    void Print(double seconds, unsigned char index)
    {
        fprintf(file, "%d, ", index);
        for (unsigned char index = 0; index < dimensions - 1; ++index)
        {
            fprintf(file, "%f, ", optimumArguments[index]);
        }
        fprintf(file, "%f, ", optimumArguments[dimensions - 1]);
        fprintf(file, "%f, %f\n", optimumValue, seconds);
    }
};

void GetResults(bool(*Optimum)(const double first, const double second), const char* functionName, double(*Function)(const double* argument, unsigned char dimensions), double leftEnd, double rightEnd, unsigned char dimensions, method usedMethod)
{
    FunctionOptimum functionObject(Optimum, functionName, Function, leftEnd, rightEnd, dimensions, usedMethod);

    for (unsigned char index = 0; index < runs; ++index)
    {
        functionObject.Run(index);
    }
}

void ApplyMethods(bool(*Optimum)(const double first, const double second), const char* functionName, double(*Function)(const double* argument, unsigned char dimensions), double leftEnd, double rightEnd)
{
    GetResults(Optimum, functionName, Function, leftEnd, rightEnd, 5, method::NAHC);
    GetResults(Optimum, functionName, Function, leftEnd, rightEnd, 10, method::NAHC);
    GetResults(Optimum, functionName, Function, leftEnd, rightEnd, 30, method::NAHC);

    GetResults(Optimum, functionName, Function, leftEnd, rightEnd, 5, method::SAHC);
    GetResults(Optimum, functionName, Function, leftEnd, rightEnd, 10, method::SAHC);
    GetResults(Optimum, functionName, Function, leftEnd, rightEnd, 30, method::SAHC);

    GetResults(Optimum, functionName, Function, leftEnd, rightEnd, 5, method::SA);
    GetResults(Optimum, functionName, Function, leftEnd, rightEnd, 10, method::SA);
    GetResults(Optimum, functionName, Function, leftEnd, rightEnd, 30, method::SA);
}

int main()
{
    srand(static_cast<unsigned int>(time(nullptr) * clock()));

    //ApplyMethods(Minimum, "Ackley", Ackley, -15, 30);
    //ApplyMethods(Minimum, "Michalewics", Michalewics, 0, pi);
    //ApplyMethods(Minimum, "Rastrigin", Rastrigin, -5.12, 5.12);
    //ApplyMethods(Minimum, "Schwefel", Schwefel, -500, 500);

    GetResults(Maximum, "Cubic", Cubic, 0, 31, 1, method::NAHC);
    GetResults(Maximum, "Cubic", Cubic, 0, 31, 1, method::SAHC);
    GetResults(Maximum, "Cubic", Cubic, 0, 31, 1, method::SA);

    return 0;
}