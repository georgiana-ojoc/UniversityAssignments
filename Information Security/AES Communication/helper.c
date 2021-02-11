#include "helper.h"

void generateString(unsigned char *string) {
    /**
     * Generates random 256-bit length string.
     */
    srandom(time(NULL) * getpid());
    for (unsigned int index = 0; index < KEY_LENGTH; index++) {
        string[index] = (unsigned char)random();
    }
    string[KEY_LENGTH] = 0;
}

void upperCase(unsigned char *string) {
    /**
     * Converts specified string to uppercase.
     */
    unsigned char *character = string;
    while (*character) {
        *character = toupper(*character);
        character++;
    }
}

void XOR(unsigned char *firstString, const unsigned char *secondString) {
    /**
     * Computes XOR between two 256-bit strings.
     */
    for (unsigned int index = 0; index < KEY_LENGTH; index++) {
        firstString[index] ^= secondString[index];
    }
}

void AES_ECB_blockEncryption(unsigned char *key, unsigned char *plainText) {
    /**
     * Encrypts 256-bit length plain text with AES using ECB block mode.
     */
    struct AES_ctx context;
    AES_init_ctx(&context, key);
    AES_ECB_encrypt(&context, plainText);
    plainText[KEY_LENGTH] = 0;
}

void AES_ECB_blockDecryption(unsigned char *key, unsigned char *cipherText) {
    /**
    * Decrypts 256-bit length cipher text with AES using ECB block mode.
    */
    struct AES_ctx context;
    AES_init_ctx(&context, key);
    AES_ECB_decrypt(&context, cipherText);
    cipherText[KEY_LENGTH] = 0;
}

void AES_CBC_blockEncryption(unsigned char *initializationVector, unsigned char *key, unsigned char *plainText) {
    /**
    * Encrypts plain text with AES using CBC block mode.
    */
    XOR(plainText, initializationVector);
    AES_ECB_blockEncryption(key, plainText);
    plainText[KEY_LENGTH] = 0;
    memcpy(initializationVector, plainText, KEY_LENGTH + 1);
}

void AES_CBC_blockDecryption(unsigned char *initializationVector, unsigned char *key, unsigned char *cipherText) {
    /**
    * Decrypts cipher text with AES using CBC block mode.
    */
    unsigned char cipherTextCopy[KEY_LENGTH + 1];
    memcpy(cipherTextCopy, cipherText, KEY_LENGTH);
    cipherTextCopy[KEY_LENGTH] = 0;
    AES_ECB_blockDecryption(key, cipherText);
    XOR(cipherText, initializationVector);
    memcpy(initializationVector, cipherTextCopy, KEY_LENGTH + 1);
}

void AES_OFB_blockEncryption(unsigned char *initializationVector, unsigned char *key, unsigned char *plainText) {
    /**
    * Encrypts plain text with AES using CBC block mode.
    */
    AES_ECB_blockEncryption(key, initializationVector);
    XOR(initializationVector, plainText);
}

void AES_OFB_blockDecryption(unsigned char *initializationVector, unsigned char *key, unsigned char *cipherText) {
    /**
    * Decrypts cipher text with AES using CBC block mode.
    */
    AES_ECB_blockEncryption(key, initializationVector);
    XOR(initializationVector, cipherText);
}

enum errors createSocket(const char *socketName, int *socketDescriptor) {
    /**
    * Creates socket with IPv4, bidirectional bytes streaming and TCP/IP model.
    */
    *socketDescriptor = socket(AF_INET, SOCK_STREAM, 0);
    char *errorMessage = (char *) malloc((strlen(socketName) + strlen("socket create") + 4) * sizeof(char));
    strcpy(errorMessage, "[");
    strcat(errorMessage, socketName);
    strcat(errorMessage, "] socket create");
    CHECK_ZERO(*socketDescriptor, errorMessage, SOCKET)
    free(errorMessage);
    return SUCCESS;
}

enum errors createServer(const char *serverName, unsigned int port, int *serverDescriptor) {
    /**
     * Creates socket.
     */
    enum errors errorCode = createSocket(serverName, serverDescriptor);
    if (errorCode != SUCCESS) {
        return errorCode;
    }

    /**
     * Sets socket option to reuse address.
     */
    int option = 1;
    char *errorMessage = (char *) malloc((strlen(serverName) + strlen("socket set option") + 4) * sizeof(char));
    strcpy(errorMessage, "[");
    strcat(errorMessage, serverName);
    strcat(errorMessage, "] socket set option");
    CHECK_ZERO(setsockopt(*serverDescriptor, SOL_SOCKET, SO_REUSEADDR, &option, sizeof(option)), errorMessage,
               SOCKET_OPTION)
    free(errorMessage);

    /**
     * Creates socket structure with IPv4, at localhost and specified port.
     */
    struct sockaddr_in serverStructure;
    memset(&serverStructure, 0, sizeof(serverStructure));
    serverStructure.sin_family = AF_INET;
    serverStructure.sin_addr.s_addr = htonl(INADDR_ANY);
    serverStructure.sin_port = htons(port);

    /**
     * Binds structure to socket.
     */
    errorMessage = (char *) malloc((strlen(serverName) + strlen("socket bind") + 4) * sizeof(char));
    strcpy(errorMessage, "[");
    strcat(errorMessage, serverName);
    strcat(errorMessage, "] socket bind");
    CHECK_ZERO(bind(*serverDescriptor, (struct sockaddr *) &serverStructure, sizeof(struct sockaddr)),
               "[keyManager] socket bind", BIND)
    free(errorMessage);

    /**
     * Start listening to socket.
     */
    errorMessage = (char *) malloc((strlen(serverName) + strlen("socket listen") + 4) * sizeof(char));
    strcpy(errorMessage, "[");
    strcat(errorMessage, serverName);
    strcat(errorMessage, "] socket listen");
    CHECK_ZERO(listen(*serverDescriptor, BACKLOG), "[keyManager] socket listen", LISTEN)
    free(errorMessage);

    return SUCCESS;
}

enum errors acceptClient(const char *serverName, const char *clientName, int serverDescriptor, int *clientDescriptor) {
    printf("[%s] waiting %s\n", serverName, clientName);
    FLUSH

    /**
     * Creates client structure.
     */
    struct sockaddr_in clientStructure;
    memset(&clientStructure, 0, sizeof(clientStructure));
    socklen_t clientLength = sizeof(clientStructure);

    /**
     * Accepts client.
     */
    *clientDescriptor = accept(serverDescriptor, (struct sockaddr *) &clientStructure, &clientLength);
    char *errorMessage = (char *) malloc((strlen(serverName) + strlen("socket accept") + 4) * sizeof(char));
    strcpy(errorMessage, "[");
    strcat(errorMessage, serverName);
    strcat(errorMessage, "] socket accept");
    CHECK_ZERO(*clientDescriptor, errorMessage, ACCEPT)
    free(errorMessage);

    printf("[%s] accepted %s\n", serverName, clientName);
    FLUSH

    return SUCCESS;
}

enum errors connectToServer(const char *serverName, const char *clientName, const char *address, unsigned int port,
                            int *serverDescriptor) {
    /**
     * Creates socket.
     */
    enum errors errorCode = createSocket(clientName, serverDescriptor);
    if (errorCode != SUCCESS) {
        return errorCode;
    }

    /**
    * Creates socket structure with IPv4, at specified address and port.
    */
    struct sockaddr_in sender;
    memset(&sender, 0, sizeof(sender));
    sender.sin_family = AF_INET;
    sender.sin_addr.s_addr = inet_addr(address);
    sender.sin_port = htons(port);

    /**
     * Connects to socket.
     */
    char *errorMessage = (char *) malloc((strlen(clientName) + strlen("socket connect") + 4) * sizeof(char));
    strcpy(errorMessage, "[");
    strcat(errorMessage, clientName);
    strcat(errorMessage, "] socket connect");
    CHECK_ZERO(connect(*serverDescriptor, (struct sockaddr *) &sender, sizeof(struct sockaddr)), errorMessage,
               CONNECT)
    free(errorMessage);

    printf("[%s] connected to %s\n", clientName, serverName);
    FLUSH

    return SUCCESS;
}

enum errors sendMode(int descriptor, unsigned char *key, unsigned char *mode, const char *sender,
                     const char *receiver) {
    /**
     * Sends encrypted block mode.
     */
    unsigned char encryptedMode[KEY_LENGTH + 1];
    memset(encryptedMode, 0, KEY_LENGTH + 1);
    memcpy(encryptedMode, mode, MODE_LENGTH);
    AES_ECB_blockEncryption(key, encryptedMode);
    unsigned int bytes;
    char *bytesError = (char *) malloc((strlen(sender) + strlen("write mode bytes") + 4) * sizeof(char));
    strcpy(bytesError, "[");
    strcat(bytesError, sender);
    strcat(bytesError, "] write mode bytes");
    char *messageError = (char *) malloc((strlen(sender) + strlen("write mode message") + 4) * sizeof(char));
    strcpy(messageError, "[");
    strcat(messageError, sender);
    strcat(messageError, "] write mode message");
    SEND_MESSAGE(descriptor, encryptedMode, KEY_LENGTH, bytesError, messageError)
    free(messageError);
    free(bytesError);
    printf("[%s] sent block mode to %s: %s\n", sender, receiver, mode);
    FLUSH
    return SUCCESS;
}

enum errors receiveMode(int descriptor, unsigned char *key, unsigned char *mode,
                        const char *sender, const char *receiver) {
    /**
     * Receives encrypted block mode.
     * If receives empty string, finishes communication with sender.
     */
    unsigned int bytes;
    char *bytesError = (char *) malloc((strlen(receiver) + strlen("read mode bytes") + 4) * sizeof(char));
    strcpy(bytesError, "[");
    strcat(bytesError, receiver);
    strcat(bytesError, "] read mode bytes");
    char *messageError = (char *) malloc((strlen(receiver) + strlen("read mode message") + 4) * sizeof(char));
    strcpy(messageError, "[");
    strcat(messageError, receiver);
    strcat(messageError, "] read mode message");
    unsigned char encryptedMode[KEY_LENGTH + 1];
    memset(encryptedMode, 0, KEY_LENGTH + 1);
    RECEIVE_MESSAGE(descriptor, encryptedMode, bytesError, messageError)
    encryptedMode[KEY_LENGTH] = 0;
    free(messageError);
    free(bytesError);
    if (strcmp((char *) encryptedMode, "") == 0) {
        return FINISH;
    }
    AES_ECB_blockDecryption(key, encryptedMode);
    upperCase(encryptedMode);
    memcpy(mode, encryptedMode, MODE_LENGTH);
    mode[MODE_LENGTH] = 0;
    printf("[%s] received block mode from %s: %s\n", receiver, sender, mode);
    FLUSH
    return SUCCESS;
}

enum errors sendString(int descriptor, unsigned char *encryptionKey, unsigned char *string,
                       const char *stringName, const char *sender, const char *receiver) {
    /**
     * Sends encrypted initialization vector or block mode key.
     */
    unsigned char encryptedString[KEY_LENGTH + 1];
    memset(encryptedString, 0, KEY_LENGTH + 1);
    memcpy(encryptedString, string, KEY_LENGTH);
    AES_ECB_blockEncryption(encryptionKey, encryptedString);
    unsigned int bytes;
    char *bytesError = (char *) malloc((strlen(sender) + strlen(stringName) + strlen("[] write  bytes") + 1)
                                       * sizeof(char));
    strcpy(bytesError, "[");
    strcat(bytesError, sender);
    strcat(bytesError, "] write ");
    strcat(bytesError, stringName);
    strcat(bytesError, " bytes");
    char *messageError = (char *) malloc((strlen(sender) + strlen(stringName) + strlen("[] write  message") + 1)
                                         * sizeof(char));
    strcpy(messageError, "[");
    strcat(messageError, sender);
    strcat(messageError, "] write ");
    strcat(messageError, stringName);
    strcat(messageError, " message");
    SEND_MESSAGE(descriptor, encryptedString, KEY_LENGTH, bytesError, messageError)
    free(messageError);
    free(bytesError);
    printf("[%s] sent %s to %s: ", sender, stringName, receiver);
    /**
     * Prints initialization vector or block mode key in hexadecimal.
     */
    HEXADECIMAL(string)
    FLUSH
    return SUCCESS;
}

enum errors receiveString(int descriptor, unsigned char *encryptionKey, unsigned char *string,
                          const char *stringName, const char *sender, const char *receiver) {
    /**
     * Receives encrypted initialization vector or block mode key.
     */
    unsigned int bytes;
    char *bytesError = (char *) malloc((strlen(sender) + strlen(stringName) + strlen("[] read  bytes") + 1)
                                       * sizeof(char));
    strcpy(bytesError, "[");
    strcat(bytesError, sender);
    strcat(bytesError, "] read ");
    strcat(bytesError, stringName);
    strcat(bytesError, " bytes");
    char *messageError = (char *) malloc((strlen(sender) + strlen(stringName) + strlen("[] read  message") + 1)
                                         * sizeof(char));
    strcpy(messageError, "[");
    strcat(messageError, sender);
    strcat(messageError, "] read ");
    strcat(messageError, stringName);
    strcat(messageError, " message");
    memset(string, 0, KEY_LENGTH + 1);
    RECEIVE_MESSAGE(descriptor, string, bytesError, messageError)
    string[KEY_LENGTH] = 0;
    free(messageError);
    free(bytesError);
    AES_ECB_blockDecryption(encryptionKey, string);
    printf("[%s] received %s from %s: ", receiver, stringName, sender);
    /**
    * Prints initialization vector or block mode key in hexadecimal.
    */
    HEXADECIMAL(string)
    FLUSH
    return SUCCESS;
}

enum errors sendBlock(int descriptor, unsigned char *initializationVector, unsigned char *key, unsigned char *mode,
                      unsigned char *block, const char *sender, const char *receiver) {
    /**
     * Sends encrypted block.
     */
    unsigned char encryptedBlock[KEY_LENGTH + 1];
    memset(encryptedBlock, 0, KEY_LENGTH + 1);
    memcpy(encryptedBlock, block, KEY_LENGTH);

    /**
     * Adds padding, if block size is not 256 bits.
     * The value of each added byte is the number of bytes that are added.
     */
    unsigned char padding = 0;
    for (unsigned int index = KEY_LENGTH - 1; encryptedBlock[index] == 0; index--) {
        padding++;
    }
    for (unsigned int index = KEY_LENGTH - 1; encryptedBlock[index] == 0; index--) {
        encryptedBlock[index] = padding;
    }

    if (strcmp((char *) mode, CBC_MODE) == 0) {
        AES_CBC_blockEncryption(initializationVector, key, encryptedBlock);
    } else {
        AES_OFB_blockEncryption(initializationVector, key, encryptedBlock);
    }
    unsigned int bytes;
    char *bytesError = (char *) malloc((strlen(sender) + strlen("write block bytes") + 4) * sizeof(char));
    strcpy(bytesError, "[");
    strcat(bytesError, sender);
    strcat(bytesError, "] write block bytes");
    char *messageError = (char *) malloc((strlen(sender) + strlen("write block message") + 4) * sizeof(char));
    strcpy(messageError, "[");
    strcat(messageError, sender);
    strcat(messageError, "] write block message");
    SEND_MESSAGE(descriptor, encryptedBlock, KEY_LENGTH, bytesError, messageError)
    free(messageError);
    free(bytesError);
    printf("[%s] sent block to %s: %s\n", sender, receiver, block);
    FLUSH
    return SUCCESS;
}

enum errors receiveBlock(int descriptor, unsigned char *initializationVector, unsigned char *key, unsigned char *mode,
                         unsigned char *block, const char *sender, const char *receiver) {
    /**
     * Receives encrypted block.
     * If receives empty string, finishes communication with sender.
     */
    unsigned int bytes;
    char *bytesError = (char *) malloc((strlen(receiver) + strlen("read block bytes") + 4) * sizeof(char));
    strcpy(bytesError, "[");
    strcat(bytesError, receiver);
    strcat(bytesError, "] read block bytes");
    char *messageError = (char *) malloc((strlen(receiver) + strlen("read block message") + 4) * sizeof(char));
    strcpy(messageError, "[");
    strcat(messageError, receiver);
    strcat(messageError, "] read block message");
    memset(block, 0, KEY_LENGTH + 1);
    RECEIVE_MESSAGE(descriptor, block, bytesError, messageError)
    free(messageError);
    free(bytesError);
    if (strcmp((char *) block, "") == 0) {
        return FINISH;
    }
    if (strcmp((char *) mode, CBC_MODE) == 0) {
        AES_CBC_blockDecryption(initializationVector, key, block);
    } else {
        AES_OFB_blockDecryption(initializationVector, key, block);
    }

    /**
     * Remove padding, if exists.
     */
    int padding = block[KEY_LENGTH - 1];
    if (padding <= KEY_LENGTH) {
        unsigned char isPadded = 1;
        for (unsigned int index = KEY_LENGTH - 1; isPadded && index > KEY_LENGTH - padding - 1; index--) {
            if (block[index] != padding) {
                isPadded = 0;
            }
        }
        if (isPadded) {
            unsigned int index = KEY_LENGTH - 2;
            while (block[KEY_LENGTH - 1] > 1) {
                block[index] = 0;
                index--;
                block[KEY_LENGTH - 1]--;
            }
            block[KEY_LENGTH - 1] = 0;
        }
    }

    printf("[%s] received block from %s: %s\n", receiver, sender, block);
    FLUSH
    return SUCCESS;
}

enum errors sendFinish(int descriptor, unsigned char *encryptionKey, const char *sender, const char *receiver) {
    /**
     * Sends empty string to finish communication.
     */
    unsigned int bytes;
    char *bytesError = (char *) malloc((strlen(sender) + strlen("write empty string bytes") + 4) * sizeof(char));
    strcpy(bytesError, "[");
    strcat(bytesError, sender);
    strcat(bytesError, "] write empty string bytes");
    char *messageError = (char *) malloc((strlen(sender) + strlen("write empty string message") + 4) * sizeof(char));
    strcpy(messageError, "[");
    strcat(messageError, sender);
    strcat(messageError, "] write empty string message");
    SEND_MESSAGE(descriptor, "", strlen(""), bytesError, messageError)
    free(messageError);
    free(bytesError);
    printf("[%s] finished communication with %s\n", sender, receiver);
    FLUSH
    return SUCCESS;
}
