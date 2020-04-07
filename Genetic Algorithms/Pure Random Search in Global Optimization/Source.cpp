#include <chrono>
#include <cmath>
#include <cstring>
#include <iostream>
#include <random>

#define SIZE        10000000

using namespace std;
using namespace std::chrono;

enum method
{
    deterministic,
    euristic
};

const double Pi = atan(1) * 4;

inline double Ackley(const double* arguments, unsigned char dimensions)
{
    double value = 20 + exp(1);

    double firstSum = 0;
    double secondSum = 0;

    for (unsigned char index = 0; index < dimensions; ++index)
    {
        firstSum += arguments[index] * arguments[index];
        secondSum += cos(2 * Pi * arguments[index]);
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
        value -= sin(arguments[index]) * pow(sin(arguments[index] * arguments[index] * (index + 1) / Pi), 20);
    }

    return value;
}

inline double Rastrigin(const double* arguments, unsigned char dimensions)
{
    double value = 10 * dimensions;

    for (unsigned char index = 0; index < dimensions; ++index)
    {
        value += arguments[index] * arguments[index] - 10 * cos(2 * Pi * arguments[index]);
    }

    return value;
}

inline double Rosenbrock(const double* arguments, unsigned char dimensions)
{
    double value = 0;

    for (unsigned char index = 0; index < dimensions - 1; ++index)
    {
        value += 100 * pow(arguments[index + 1] - arguments[index] * arguments[index], 2) + pow(arguments[index] - 1, 2);
    }

    return value;
}

class FunctionMinimum
{
private:  
    const char* functionName;
    double(*Function)(const double* argument, unsigned char dimensions);

    double leftEnd;
    double rightEnd;
    unsigned char precision;
    unsigned char dimensions;

    double increment;
    
    double* arguments;
    double value;
    
    double* minimumArguments;
    double minimumValue;

    method method_;
public:
    FILE* file;
    
    FunctionMinimum(const char* newFunctionName, double(*newFunction)(const double* argument, unsigned char dimensions), double newLeftEnd, double newRightEnd, unsigned char newDimensions, unsigned char newPrecision, method newMethod)
    {
        functionName = newFunctionName;
        Function = newFunction;

        leftEnd = newLeftEnd;
        rightEnd = newRightEnd;
        dimensions = newDimensions;
        precision = newPrecision;

        increment = pow(10, -precision);

        arguments = new double[dimensions];

        minimumArguments = new double[dimensions];
        
        method_ = newMethod;

        time_t rawTime = time(nullptr);
        struct tm *timeInformation = localtime(&rawTime);
        char timeBuffer[80];
        strftime(timeBuffer, 80, "%d-%m-%Y %H-%M-%S", timeInformation);

        char filenameBuffer[128];
        strcpy(filenameBuffer, functionName);
        switch (method_)
        {
            case 0:
                strcat(filenameBuffer, " Deterministic ");
                break;
            case 1:
                strcat(filenameBuffer, " Euristic ");
                break;
        }
        char dimensionsString[4];
        dimensionsString[0] = dimensionsString[2] = ' ';
        dimensionsString[1] = dimensions + 48;
        strcat(filenameBuffer, dimensionsString);     
        strcat(filenameBuffer, timeBuffer);

        file = fopen(filenameBuffer, "w");
    }

    ~FunctionMinimum()
    {
        delete[] arguments;
        delete[] minimumArguments;
        fclose(file);
    }

    void ChangeDimensions(unsigned char newDimensions)
    {
        dimensions = newDimensions;

        delete[] arguments;
        arguments = new double[dimensions];

        delete[] minimumArguments;
        minimumArguments = new double[dimensions];
    }

    void ChangePrecision(unsigned char newPrecision)
    {
        precision = newPrecision;

        increment = pow(10, -precision);
    }

    void SetFirst()
    {
        for (unsigned char index = 0; index < dimensions; ++index)
        {
            minimumArguments[index] = leftEnd;
        }
        minimumValue = Function(minimumArguments, dimensions);
    }

    void CheckMinimum()
    {
        value = Function(arguments, dimensions);

        if (value < minimumValue)
        {
            memcpy(minimumArguments, arguments, dimensions * sizeof(double));
            minimumValue = value;
        }
    }

    void Initialize(unsigned char index)
    {
        if (index == 0)
        {
            arguments[index] = leftEnd - increment;
        }
        else
        {
            arguments[index] = arguments[index - 1] - increment;
        }
    }

    bool HasNext(unsigned char index)
    {
        if (arguments[index] <= rightEnd - increment)
        {
            arguments[index] += increment;
            return true;
        }
        return false;
    }

    void Deterministic()
    {
        char index = 0;
        Initialize(index);

        while (index >= 0)
        {
            if (HasNext(index))
            {
                if (index == dimensions - 1)
                {
                    CheckMinimum();
                }
                else
                {
                    ++index;
                    Initialize(index);
                }
            }
            else
            {
                --index;
            }
        }
    }

    void Euristic()
    {
        const unsigned int seed = system_clock::now().time_since_epoch().count();
        default_random_engine generator(seed);
        uniform_real_distribution<double> distribution(leftEnd, rightEnd + 0.000001);

        for (unsigned int iterator = 1; iterator <= SIZE; ++iterator)
        {
            for (unsigned char index = 0; index < dimensions; ++index)
            {
                arguments[index] = distribution(generator);
            }

            CheckMinimum();
        }
    }

    void Run()
    {
        SetFirst();

        time_point<system_clock> startTime, endTime;

        startTime = system_clock::now();
        switch (method_)
        {
            case 0:
                Deterministic();
                break;
            case 1:
                Euristic();
                break;
        }
        endTime = system_clock::now();

        duration<double> elapsedSeconds = endTime - startTime;

        Print(elapsedSeconds.count());
    }

    void Print(double seconds)
    {
        switch (method_)
        {
            case 0:
                Deterministic();
                break;
            case 1:
                Euristic();
                break;
        }

        for (unsigned char index = 0; index < dimensions && index < 10; ++index)
        {
            fprintf(file, "argument %-5d", index + 1);
        }
        fprintf(file, "\n");
        for (unsigned char index = 0; index < dimensions && index < 10; ++index)
        {
            fprintf(file, "%-14f", minimumArguments[index]);
        } 
        if (dimensions > 10)
        {
            fprintf(file, "\n");
            for (unsigned char index = 10; index < dimensions; ++index)
            {
                fprintf(file, "argument %-5d", index + 1);
            }
            fprintf(file, "\n");
            for (unsigned char index = 10; index < dimensions; ++index)
            {
                fprintf(file, "%-14f", minimumArguments[index]);
            }
        }
        fprintf(file, "\nvalue = %f", minimumValue);

        fprintf(file, "\nelapsed seconds = %f seconds\n\n", seconds);
    }
};

void GetEuristicResults(const char* functionName, double(*Function)(const double* argument, unsigned char dimensions), double leftEnd, double rightEnd, unsigned char dimensions)
{
    FunctionMinimum functionObject(functionName, Function, leftEnd, rightEnd, dimensions, 6, euristic);

    for (unsigned char index = 1; index <= 32; ++index)
    {
        functionObject.Run();
    }
}

int main()
{
    GetEuristicResults("Ackley", Ackley, -15, 30, 2);
    GetEuristicResults("Ackley", Ackley, -15, 30, 5);
    GetEuristicResults("Ackley", Ackley, -15, 30, 20);

    GetEuristicResults("Michalewics", Michalewics, 0, Pi, 2);
    GetEuristicResults("Michalewics", Michalewics, 0, Pi, 5);
    GetEuristicResults("Michalewics", Michalewics, 0, Pi, 20);

    GetEuristicResults("Rastrigin", Rastrigin, -5.12, 5.12, 2);
    GetEuristicResults("Rastrigin", Rastrigin, -5.12, 5.12, 5);
    GetEuristicResults("Rastrigin", Rastrigin, -5.12, 5.12, 20);

    GetEuristicResults("Rosenbrock", Rosenbrock, -5, 10, 2);
    GetEuristicResults("Rosenbrock", Rosenbrock, -5, 10, 5);
    GetEuristicResults("Rosenbrock", Rosenbrock, -5, 10, 20);

    return 0;
}