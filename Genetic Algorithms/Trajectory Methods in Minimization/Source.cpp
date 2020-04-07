#include <chrono>
#include <cmath>
#include <cstring>
#include <iostream>
#include <random>

using namespace std;
using namespace std::chrono;

constexpr unsigned int iterations = 1000;
constexpr unsigned char bits = 32;
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

class FunctionMinimum
{
    const char* functionName;
    double(*Function)(const double* argument, unsigned char dimensions);

    double leftEnd;
    double rightEnd;
    unsigned char dimensions;
    unsigned short totalBits;

    unsigned char* randomBitString;

    unsigned long long* candidateNumbers;
    double* candidateArguments;
    double candidateValue;

    unsigned long long* neighbourNumbers;
    double* neighbourArguments;
    double neighbourValue;

    double* minimumArguments;
    double minimumValue;

    method usedMethod;
public:
    FILE* file;

    FunctionMinimum(const char* newFunctionName, double(*newFunction)(const double* argument, unsigned char dimensions), double newLeftEnd, double newRightEnd, unsigned char newDimensions, method newMethod)
    {
        functionName = newFunctionName;
        Function = newFunction;

        leftEnd = newLeftEnd;
        rightEnd = newRightEnd;
        dimensions = newDimensions;
        totalBits = bits * dimensions;

        randomBitString = new unsigned char[totalBits];

        candidateNumbers = new unsigned long long[dimensions];
        candidateArguments = new double[dimensions];
        candidateValue = DBL_MIN;

        neighbourNumbers = new unsigned long long[dimensions];
        neighbourArguments = new double[dimensions];
        neighbourValue = DBL_MIN;

        minimumArguments = new double[dimensions];
        minimumValue = DBL_MIN;

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
        strcat(filenameBuffer, ".csv");
        
        file = fopen(filenameBuffer, "w");
        //fprintf(file, "Index, Elapsed seconds, Minimum value, ");
        //for (unsigned int index = 1; index < dimensions; ++index)
        //{
        //    fprintf(file, "%d, ", index);
        //}
        //fprintf(file, "%d\n", dimensions);
        fprintf(file, "Index, ");
        for (unsigned short index = 50; index <= 950; index += 50)
        {
            fprintf(file, "%u, ", index);
        }
        fprintf(file, "1000, ");
        fprintf(file, "Elapsed seconds\n");
    }

    ~FunctionMinimum()
    {
        delete[] randomBitString;
        delete[] candidateNumbers;
        delete[] candidateArguments;
        delete[] neighbourNumbers;
        delete[] neighbourArguments;
        delete[] minimumArguments;
        fclose(file);
    }

    void ChangeDimensions(unsigned char newDimensions)
    {
        dimensions = newDimensions;

        delete[] randomBitString;
        randomBitString = new unsigned char[totalBits];

        delete[] candidateNumbers;
        candidateNumbers = new unsigned long long[dimensions];
        delete[] candidateArguments;
        candidateArguments = new double[dimensions];

        delete[] neighbourNumbers;
        neighbourNumbers = new unsigned long long[dimensions];
        delete[] neighbourArguments;
        neighbourArguments = new double[dimensions];

        delete[] minimumArguments;
        minimumArguments = new double[dimensions];
    }

    void SetFirst()
    {
        for (unsigned char index = 0; index < dimensions; ++index)
        {
            minimumArguments[index] = leftEnd;
        }
        minimumValue = Function(minimumArguments, dimensions);
    }

    void EvaluateCandidate()
    {
        candidateValue = Function(candidateArguments, dimensions);

        if (candidateValue < minimumValue)
        {
            memcpy(minimumArguments, candidateArguments, dimensions * sizeof(double));
            minimumValue = candidateValue;
        }
    }

    void GenerateCandidate()
    {
        for (unsigned char index = 0; index < dimensions; ++index)
        {
            candidateNumbers[index] = 0;

            for (unsigned char position = 0; position < bits; ++position)
            {
                randomBitString[bits * index + position] = rand() % 2; // generate random bit string
                candidateNumbers[index] = candidateNumbers[index] * 2 + randomBitString[bits * index + position]; // convert base 2 to base 10
            }

            candidateArguments[index] = static_cast<double>(candidateNumbers[index]) / (pow(2, bits) - 1); // translate to closed interval [0, 1]
            candidateArguments[index] *= rightEnd - leftEnd; // translate to closed interval [0, rightEnd - leftEnd]
            candidateArguments[index] += leftEnd; // translate to closed interval [leftEnd, rightEnd]
        }
    }

    void ChangeBit(unsigned short position)
    {
        randomBitString[position] = 1 - randomBitString[position];
    }

    void GenerateNeighbour(unsigned short position)
    {
        const unsigned char changedArgument = position / bits;
        const unsigned char changedBit = position % bits;

        ChangeBit(position);

        if (randomBitString[position] == 1)
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

        ChangeBit(position);

        neighbourNumbers[changedArgument] = candidateNumbers[changedArgument];
        neighbourArguments[changedArgument] = candidateArguments[changedArgument];
    }

    bool FirstImprovement()
    {
        memcpy(neighbourNumbers, candidateNumbers, dimensions * sizeof(double));
        memcpy(neighbourArguments, candidateArguments, dimensions * sizeof(double));

        for (unsigned short position = 0; position < totalBits; ++position)
        {
            GenerateNeighbour(position);
            neighbourValue = Function(neighbourArguments, dimensions);

            if (neighbourValue < candidateValue)
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
        bool isCandidateMinimum = true;

        memcpy(neighbourNumbers, candidateNumbers, dimensions * sizeof(double));
        memcpy(neighbourArguments, candidateArguments, dimensions * sizeof(double));

        for (unsigned short position = 0; position < totalBits; ++position)
        {
            GenerateNeighbour(position);
            neighbourValue = Function(neighbourArguments, dimensions);

            if (neighbourValue < candidateValue)
            {
                memcpy(candidateNumbers, neighbourNumbers, dimensions * sizeof(double));
                memcpy(candidateArguments, neighbourArguments, dimensions * sizeof(double));
                candidateValue = neighbourValue;

                isCandidateMinimum = false;
            }

            RestoreNeighbour(position);
        }

        return isCandidateMinimum;
    }

    void Improvement(uniform_real_distribution<double> distribution, default_random_engine generator, double temperature)
    {
        memcpy(neighbourNumbers, candidateNumbers, dimensions * sizeof(double));
        memcpy(neighbourArguments, candidateArguments, dimensions * sizeof(double));

        for (unsigned short position = 0; position < totalBits; ++position)
        {
            GenerateNeighbour(position);
            neighbourValue = Function(neighbourArguments, dimensions);

            if (neighbourValue < candidateValue)
            {
                memcpy(candidateNumbers, neighbourNumbers, dimensions * sizeof(double));
                memcpy(candidateArguments, neighbourArguments, dimensions * sizeof(double));
                candidateValue = neighbourValue;
            }
            else if (distribution(generator) < exp(-(neighbourValue - candidateValue) / temperature))
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

    void NAHC()
    {
        for (unsigned short iterator = 1; iterator <= iterations; ++iterator)
        {
            bool isCandidateMinimum = false;

            GenerateCandidate();
            EvaluateCandidate();

            while (!isCandidateMinimum)
            {
                isCandidateMinimum = FirstImprovement();
            }

            EvaluateCandidate();

            if (iterator % 50 == 0)
            {
                fprintf(file, "%f, ", minimumValue);
            }
        }
    }

    void SAHC()
    {
        for (unsigned short iterator = 1; iterator <= iterations; ++iterator)
        {
            GenerateCandidate();
            EvaluateCandidate();

            bool isCandidateMinimum = false;
            while (!isCandidateMinimum)
            {
                isCandidateMinimum = BestImprovement();
            }

            EvaluateCandidate();

            if (iterator % 5 == 0)
            {
                fprintf(file, "%f, ", minimumValue);
            }
        }
    }

    void SA()
    {
        const unsigned int seed = system_clock::now().time_since_epoch().count();
        const default_random_engine generator(seed);
        const uniform_real_distribution<double> distribution(0, 1);

        constexpr double coolingRate = 0.1;

        for (unsigned short iterator = 1; iterator <= iterations; ++iterator)
        {
            GenerateCandidate();
            EvaluateCandidate();

            for (double temperature = 1000; temperature > 0.01; temperature *= 1 - coolingRate)
            {
                Improvement(distribution, generator, temperature);
            }

            EvaluateCandidate();

            if (iterator % 5 == 0)
            {
                fprintf(file, "%f, ", minimumValue);
            }
        }
    }

    void Run(unsigned char index)
    {
        fprintf(file, "%d, ", index);

        SetFirst();

        time_point<system_clock> startTime, endTime;

        startTime = system_clock::now();
        switch (usedMethod)
        {
        case method::NAHC:
            NAHC();
            break;
        case method::SAHC:
            SAHC();
            break;
        case method::SA:
            SA();
            break;
        }
        endTime = system_clock::now();

        const duration<double> elapsedSeconds = endTime - startTime;

        fprintf(file, "%f\n", elapsedSeconds.count());

        //Print(elapsedSeconds.count(), index);
    }

    //void Print(double seconds, unsigned char index)
    //{
    //    fprintf(file, "%d, %f, %f, ", index, seconds, minimumValue);
    //    for (unsigned char index = 0; index < dimensions - 1; ++index)
    //    {
    //        fprintf(file, "%f, ", minimumArguments[index]);
    //    }
    //    fprintf(file, "%f\n", minimumArguments[dimensions - 1]);
    //}
};

void GetResults(const char* functionName, double(*Function)(const double* argument, unsigned char dimensions), double leftEnd, double rightEnd, unsigned char dimensions, method usedMethod)
{
    FunctionMinimum functionObject(functionName, Function, leftEnd, rightEnd, dimensions, usedMethod);

    for (unsigned char index = 1; index <= runs; ++index)
    {
        functionObject.Run(index);
    }
}

void ApplyMethods(const char* functionName, double(*function)(const double* argument, unsigned char dimensions), double leftEnd, double rightEnd)
{
    GetResults(functionName, function, leftEnd, rightEnd, 5, method::NAHC);
    GetResults(functionName, function, leftEnd, rightEnd, 10, method::NAHC);
    GetResults(functionName, function, leftEnd, rightEnd, 30, method::NAHC);

    GetResults(functionName, function, leftEnd, rightEnd, 5, method::SAHC);
    GetResults(functionName, function, leftEnd, rightEnd, 10, method::SAHC);
    GetResults(functionName, function, leftEnd, rightEnd, 30, method::SAHC);

    GetResults(functionName, function, leftEnd, rightEnd, 5, method::SA);
    GetResults(functionName, function, leftEnd, rightEnd, 10, method::SA);
    GetResults(functionName, function, leftEnd, rightEnd, 30, method::SA);
}

int main()
{
    srand(static_cast<unsigned int>(time(nullptr) * clock()));

    ApplyMethods("Ackley", Ackley, -15, 30);
    ApplyMethods("Michalewics", Michalewics, 0, pi);
    ApplyMethods("Rastrigin", Rastrigin, -5.12, 5.12);
    ApplyMethods("Schwefel", Schwefel, -500, 500);

    return 0;
}