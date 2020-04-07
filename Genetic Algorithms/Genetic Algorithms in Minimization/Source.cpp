#include <chrono>
#include <cmath>
#include <cstring>
#include <iostream>
#include <random>
#include <utility>
#include <vector>

using namespace std;
using namespace std::chrono;

constexpr double crossover = 0.3;
constexpr double mutation = 0.001;
const unsigned char tournament = 10;

constexpr unsigned char bits = 32;
constexpr unsigned short generations = 1000;
constexpr unsigned short individuals = 100;
constexpr unsigned short runs = 32;

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

    const char* functionName;
    double(*Function)(const double* argument, unsigned char dimensions);

    double leftEnd;
    double rightEnd;
    unsigned char dimensions;
    unsigned short chromosomeSize;
    unsigned int populationSize;

    unsigned char* chromosome;
    unsigned char* population;
    unsigned char* newPopulation;

    long long* generationNumbers;
    double* generationArguments;
    double* generationValues;

    double* fitness;

    double* optimumArguments;
    double optimumValue;
public:
    FILE* file;

    FunctionOptimum(bool(*newOptimum)(const double first, const double second), const char* newFunctionName, double(*newFunction)(const double* argument, unsigned char dimensions), double newLeftEnd, double newRightEnd, unsigned char newDimensions)
    {
        Optimum = newOptimum;
        if (Optimum == Minimum)
        {
            optimumValue = DBL_MAX;
        }
        else
        {
            optimumValue = DBL_MIN;
        }

        functionName = newFunctionName;
        Function = newFunction;

        leftEnd = newLeftEnd;
        rightEnd = newRightEnd;
        dimensions = newDimensions;
        chromosomeSize = bits * dimensions;
        populationSize = individuals * chromosomeSize;

        chromosome = new unsigned char[chromosomeSize];
        population = new unsigned char[unsigned long long(populationSize)];
        newPopulation = new unsigned char[unsigned long long(populationSize)];

        generationNumbers = new long long[unsigned long long(individuals) * dimensions];
        generationArguments = new double[unsigned long long(individuals) * dimensions];
        generationValues = new double[unsigned long long(individuals)];

        fitness = new double[individuals + 1];

        optimumArguments = new double[dimensions];

        char filenameBuffer[128];
        strcpy(filenameBuffer, functionName);
        strcat(filenameBuffer, " GA ");
        char dimensionsString[4];
        _itoa(dimensions, dimensionsString, 10);
        strcat(filenameBuffer, dimensionsString);
        strcat(filenameBuffer, ".csv");
        file = fopen(filenameBuffer, "w");

        fprintf(file, "Index, ");

        for (unsigned short index = 50; index <= 950; index += 50)
        {
            fprintf(file, "%u, ", index);
        }
        fprintf(file, "1000, ");

        fprintf(file, "Elapsed seconds\n");
    }

    ~FunctionOptimum()
    {
        delete[] chromosome;
        delete[] population;

        delete[] generationNumbers;
        delete[] generationArguments;
        delete[] generationValues;
        delete[] fitness;

        delete[] optimumArguments;

        fclose(file);
    }

    void ChangeDimensions(unsigned char newDimensions)
    {
        dimensions = newDimensions;
        chromosomeSize = bits * dimensions;
        populationSize = individuals * chromosomeSize;

        delete[] chromosome;
        chromosome = new unsigned char[chromosomeSize];
        delete[] population;
        population = new unsigned char[populationSize];

        delete[] generationNumbers;
        generationNumbers = new long long[unsigned long long(individuals) * dimensions];
        delete[] generationArguments;
        generationArguments = new double[unsigned long long(individuals) * dimensions];

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

    void GenerateRandomPopulation()
    {
        for (unsigned int index = 0; index < populationSize; ++index)
        {
            population[index] = rand() % 2;
        }
    }

    void Mutation()
    {
        const uniform_real_distribution<double> distribution(0, 1);
        const unsigned int seed = system_clock::now().time_since_epoch().count();
        default_random_engine generator(seed);

        for (unsigned int index = 0; index < populationSize; ++index)
        {
            if (distribution(generator) < mutation)
            {
                population[index] = 1 - population[index];
            }
        }
    }

    void SinglePointCrossover(unsigned short point, unsigned short first, unsigned short second)
    {
        for (unsigned short index = point; index < chromosomeSize; ++index)
        {
            swap(population[chromosomeSize * first + index], population[chromosomeSize * second + index]);
        }
    }

    void Crossover()
    {
        vector<pair<double, unsigned short>> probabilities;
        probabilities.reserve(individuals);

        const uniform_real_distribution<double> probabilityDistribution(0, 1);
        const unsigned int seed = system_clock::now().time_since_epoch().count();
        default_random_engine generator(seed);

        for (unsigned short iterator = 0; iterator < individuals; ++iterator)
        {
            probabilities.emplace_back(make_pair(probabilityDistribution(generator), iterator));
        }

        sort(probabilities.begin(), probabilities.end());

        const uniform_int_distribution<int> pointDistribution(2, chromosomeSize - 2);

        auto individual = probabilities.begin();
        ++individual;
        for (; individual < probabilities.end() && individual->first < crossover; individual += 2)
        {
            const unsigned short point = pointDistribution(generator);
            SinglePointCrossover(point, (individual - 1)->second, individual->second);
        }

        if (individual < probabilities.end())
        {
            if ((individual - 1)->first < crossover)
            {
                if (probabilityDistribution(generator) < 0.5)
                {
                    const unsigned short point = pointDistribution(generator);
                    SinglePointCrossover(point, (individual - 1)->second, individual->second);
                }
            }
        }
        else
        {
            if (individual == probabilities.end())
            {
                if ((individual - 1)->first < crossover)
                {
                    if (probabilityDistribution(generator) < 0.5)
                    {
                        const unsigned short point = pointDistribution(generator);
                        SinglePointCrossover(point, (individual - 2)->second, (individual - 1)->second);
                    }
                }
            }
        }
    }

    void CalculateArguments(const unsigned short chromosomeStart, const unsigned int geneStart)
    {
        for (unsigned char index = 0; index < dimensions; ++index)
        {
            generationNumbers[chromosomeStart + index] = 0;

            const unsigned short argument = bits * index;

            for (unsigned char position = 0; position < bits; ++position)
            {
                generationNumbers[chromosomeStart + index] = generationNumbers[chromosomeStart + index] * 2 + population[geneStart + argument + position]; // convert base 2 to base 10
            }

            generationArguments[chromosomeStart + index] = static_cast<double>(generationNumbers[chromosomeStart + index]) / (pow(2, bits) - 1); // translate to closed interval [0, 1]
            generationArguments[chromosomeStart + index] *= rightEnd - leftEnd; // translate to closed interval [0, rightEnd - leftEnd]
            generationArguments[chromosomeStart + index] += leftEnd; // translate to closed interval [leftEnd, rightEnd]
        }
    }

    void EvaluateValue(const unsigned short iterator, const unsigned short chromosomeStart)
    {
        generationValues[iterator] = Function(generationArguments + chromosomeStart, dimensions);

        if (Optimum(generationValues[iterator], optimumValue))
        {
            memcpy(optimumArguments, generationArguments + chromosomeStart, dimensions * sizeof(double));
            optimumValue = generationValues[iterator];
        }
    }

    void CalculateFitness()
    {
        if (Optimum == Minimum)
        {
            double maximumValue = DBL_MIN;

            for (unsigned short iterator = 0; iterator < individuals; ++iterator)
            {
                const unsigned short chromosomeStart = dimensions * iterator;
                const unsigned int geneStart = chromosomeSize * iterator;

                CalculateArguments(chromosomeStart, geneStart);
                generationValues[iterator] = Function(generationArguments + chromosomeStart, dimensions);

                if (generationValues[iterator] > maximumValue)
                {
                    maximumValue = generationValues[iterator];
                }
            }

            fitness[0] = 0;
            for (unsigned short iterator = 0; iterator < individuals; ++iterator)
            {
                fitness[iterator + 1] = 1.1 * maximumValue - generationValues[iterator];
                fitness[iterator + 1] += fitness[iterator];
            }
        }
        else
        {
            double minimumValue = DBL_MAX;

            for (unsigned short iterator = 0; iterator < individuals; ++iterator)
            {
                const unsigned short chromosomeStart = dimensions * iterator;
                const unsigned int geneStart = chromosomeSize * iterator;

                CalculateArguments(chromosomeStart, geneStart);
                generationValues[iterator] = Function(generationArguments + chromosomeStart, dimensions);

                if (generationValues[iterator] < minimumValue)
                {
                    minimumValue = generationValues[iterator];
                }
            }

            fitness[0] = 0;
            for (unsigned short iterator = 0; iterator < individuals; ++iterator)
            {
                fitness[iterator + 1] = generationValues[iterator] - 0.9 * minimumValue;
                fitness[iterator + 1] += fitness[iterator];
            }
        }
    }

    unsigned short RouletteWheel()
    {
        const uniform_real_distribution<double> distribution(0, 1);
        const unsigned int seed = system_clock::now().time_since_epoch().count();
        default_random_engine generator(seed);
        const double position = distribution(generator) * fitness[individuals];

        for (unsigned short iterator = 0; iterator < individuals; ++iterator)
        {
            if (fitness[iterator] <= position && position <= fitness[iterator + 1])
            {
                return iterator;
            }
        }
    }

    unsigned short Tournament()
    {
        unsigned char* participants = new unsigned char[tournament];

        for (unsigned char index = 0; index < tournament; ++index)
        {
            participants[index] = rand() % individuals;
        }

        double bestValue;
        if (Optimum == Minimum)
        {
            bestValue = DBL_MAX;
        }
        else
        {
            bestValue = DBL_MIN;
        }
        unsigned char bestIndex = 0;

        for (unsigned char index = 0; index < tournament; ++index)
        {
            if (Optimum(generationValues[participants[index]], bestValue))
            {
                bestValue = generationValues[participants[index]];
                bestIndex = index;
            }
        }

        delete[] participants;

        return bestIndex;
    }

    void Select()
    {
        CalculateFitness();

        for (unsigned short iterator = 0; iterator < individuals; ++iterator)
        {
            memcpy(newPopulation + unsigned long long(chromosomeSize) * iterator, population + unsigned long long(chromosomeSize) * RouletteWheel(), chromosomeSize * sizeof(unsigned char));
        }
        for (unsigned short iterator = 0; iterator < individuals; ++iterator)
        {
            memcpy(population + unsigned long long(chromosomeSize) * iterator, newPopulation + unsigned long long(chromosomeSize) * RouletteWheel(), chromosomeSize * sizeof(unsigned char));
        }
    }

    void SelectOptimum()
    {
        for (unsigned short iterator = 0; iterator < individuals; ++iterator)
        {
            const unsigned short chromosomeStart = dimensions * iterator;
            const unsigned int geneStart = chromosomeSize * iterator;

            CalculateArguments(chromosomeStart, geneStart);
            EvaluateValue(iterator, chromosomeStart);
        }
    }

    void GA()
    {
        GenerateRandomPopulation();
        SelectOptimum();

        for (unsigned short iterator = 1; iterator <= generations; ++iterator)
        {
            Select();
            Crossover();
            Mutation();
            SelectOptimum();

            if (iterator % 50 == 0)
            {
                fprintf(file, "%f, ", optimumValue);
            }
        }
    }

    void Run(unsigned char index)
    {
        fprintf(file, "%d, ", index);

        SetFirst();

        time_point<system_clock> startTime, endTime;

        startTime = system_clock::now();
        GA();
        endTime = system_clock::now();

        const duration<double> elapsedSeconds = endTime - startTime;

        fprintf(file, "%f\n", elapsedSeconds.count());
    }
};

void GetResults(bool(*Optimum)(const double first, const double second), const char* functionName, double(*Function)(const double* argument, unsigned char dimensions), double leftEnd, double rightEnd, unsigned char dimensions)
{
    FunctionOptimum functionObject(Optimum, functionName, Function, leftEnd, rightEnd, dimensions);

    for (unsigned char index = 1; index <= runs; ++index)
    {
        functionObject.Run(index);
    }
}

void ApplyMethods(bool(*Optimum)(const double first, const double second), const char* functionName, double(*Function)(const double* argument, unsigned char dimensions), double leftEnd, double rightEnd)
{
    GetResults(Optimum, functionName, Function, leftEnd, rightEnd, 5);
    GetResults(Optimum, functionName, Function, leftEnd, rightEnd, 10);
    GetResults(Optimum, functionName, Function, leftEnd, rightEnd, 30);
}

int main()
{
    srand(static_cast<unsigned int>(time(nullptr) * clock()));

    ApplyMethods(Minimum, "Ackley", Ackley, -15, 30);
    ApplyMethods(Minimum, "Michalewics", Michalewics, 0, pi);
    ApplyMethods(Minimum, "Rastrigin", Rastrigin, -5.12, 5.12);
    ApplyMethods(Minimum, "Schwefel", Schwefel, -500, 500);

    return 0;
}