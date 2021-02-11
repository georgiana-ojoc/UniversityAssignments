#ifndef AES_HELPER_H
#define AES_HELPER_H

#include "aes.h"
#include <arpa/inet.h>
#include <ctype.h>
#include <string.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <time.h>
#include <unistd.h>

#define BACKLOG         5
#define BITS            128
#define KEY_LENGTH      BITS / 8
#define MODE_LENGTH     3
#define STDIN           0

#define CBC_MODE        "CBC"
#define OFB_MODE        "OFB"

#define FLUSH           fflush(stdout);

#define HEXADECIMAL(string) \
    for (unsigned int index = 0; index < KEY_LENGTH; index++) { \
        printf("%02X", string[index]); \
    }

#define CHECK_ZERO(result, errorMessage, errorCode) \
    if ((result) < 0) { \
        perror(errorMessage); \
        return errorCode; \
    }
#define CHECK_SUCCESS(result) \
    if ((result) != SUCCESS) { \
        return result; \
    }

#define RECEIVE_MESSAGE(descriptor, message, bytesError, messageError) \
    bytes = 0; \
    CHECK_ZERO(read(descriptor, &bytes, sizeof(bytes)), bytesError, READ); \
    CHECK_ZERO(read(descriptor, message, bytes), messageError, READ);
#define SEND_MESSAGE(descriptor, message, messageLength, bytesError, messageError) \
    bytes = messageLength; \
    CHECK_ZERO(write(descriptor, &bytes, sizeof(bytes)), bytesError, WRITE); \
    CHECK_ZERO(write(descriptor, message, bytes), messageError, WRITE);

#define CLOSE(descriptor, error_message) \
    if (close(descriptor) != 0) \
    { \
        perror(error_message); \
        return CLOSE; \
    }

enum errors {
    SUCCESS,
    SOCKET,
    SOCKET_OPTION,
    BIND,
    LISTEN,
    CONNECT,
    ACCEPT,
    READ,
    WRITE,
    FINISH,
    CLOSE
};


void generateString(unsigned char *string);

void upperCase(unsigned char *string);

void XOR(unsigned char *firstString, const unsigned char *secondString);

void AES_ECB_blockEncryption(unsigned char *key, unsigned char *plainText);

void AES_ECB_blockDecryption(unsigned char *key, unsigned char *cipherText);

void AES_CBC_blockEncryption(unsigned char *initializationVector, unsigned char *key, unsigned char *plainText);

void AES_CBC_blockDecryption(unsigned char *initializationVector, unsigned char *key, unsigned char *cipherText);

void AES_OFB_blockEncryption(unsigned char *initializationVector, unsigned char *key, unsigned char *plainText);

void AES_OFB_blockDecryption(unsigned char *initializationVector, unsigned char *key, unsigned char *cipherText);

enum errors createSocket(const char *serverName, int *socketDescriptor);

enum errors createServer(const char *serverName, unsigned int port, int *serverDescriptor);

enum errors acceptClient(const char *serverName, const char *clientName, int serverDescriptor, int *clientDescriptor);

enum errors connectToServer(const char *serverName, const char *clientName, const char *address, unsigned int port,
                            int *serverDescriptor);

enum errors sendMode(int descriptor, unsigned char *key, unsigned char *mode, const char *sender, const char *receiver);

enum errors receiveMode(int descriptor, unsigned char *key, unsigned char *mode,
                        const char *sender, const char *receiver);

enum errors sendString(int descriptor, unsigned char *encryptionKey, unsigned char *string,
                       const char *stringName, const char *sender, const char *receiver);

enum errors receiveString(int descriptor, unsigned char *encryptionKey, unsigned char *receivedKey,
                          const char *stringName, const char *sender, const char *receiver);

enum errors sendBlock(int descriptor, unsigned char *initializationVector, unsigned char *key, unsigned char *mode,
                      unsigned char *block, const char *sender, const char *receiver);

enum errors receiveBlock(int descriptor, unsigned char *initializationVector, unsigned char *key, unsigned char *mode,
                         unsigned char *block, const char *sender, const char *receiver);

enum errors sendFinish(int descriptor, unsigned char *encryptionKey, const char *sender, const char *receiver);

#endif
