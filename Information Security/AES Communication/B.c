#include "helper.h"

int main(int argumentNumber, const char *arguments[]) {
    /**
    * Parses command line arguments.
    */
    if (argumentNumber < 6) {
        fprintf(stderr, "syntax: %s <address> <port> <blocks> <key> <file name>\n",
                arguments[0]);
        fflush(stderr);
        return -1;
    }
    if (strlen(arguments[4]) != KEY_LENGTH) {
        fprintf(stderr, "The key should have %d bytes\n.", KEY_LENGTH);
        fflush(stderr);
        return -1;
    }
    const char *address = arguments[1];
    unsigned int port = strtol(arguments[2], NULL, 10);
    unsigned int blocks = strtol(arguments[3], NULL, 10);
    if (blocks == 0) {
        blocks = 1;
    }
    unsigned char initializationKey[KEY_LENGTH + 1];
    memcpy(initializationKey, arguments[4], KEY_LENGTH);
    initializationKey[KEY_LENGTH] = 0;
    const char *fileName = arguments[5];

    /**
     * Connects to sender
     */
    int senderDescriptor;
    enum errors result = connectToServer("A", "B", address, port, &senderDescriptor);
    CHECK_SUCCESS(result)

    /**
    * Opens file for writing binary.
    */
    FILE *file = fopen(fileName, "wb");
    if (file == NULL) {
        fprintf(stderr, "Unable to open \"%s\" for writing binary.\n", fileName);
        fflush(stderr);
        return -1;
    }

    unsigned int receivedBlocks = blocks;
    unsigned char mode[MODE_LENGTH + 1];
    unsigned char initializationVector[KEY_LENGTH + 1];
    unsigned char fileKey[KEY_LENGTH + 1];
    unsigned char block[KEY_LENGTH + 1];
    while (1) {
        if (receivedBlocks == blocks) {
            /**
             * Receives block mode from sender.
             * If receives empty string, finishes communication with sender.
             */
            result = receiveMode(senderDescriptor, initializationKey, mode, "A", "B");
            if (result == FINISH) {
                printf("[B] finished communication with A\n");
                FLUSH
                break;
            }
            CHECK_SUCCESS(result)

            /**
             * Receives initialization vector from sender.
             */
            result = receiveString(senderDescriptor, initializationKey, initializationVector,
                    "initialization vector", "A", "B");
            CHECK_SUCCESS(result)

            /**
             * Receives block mode key from sender.
             */
            result = receiveString(senderDescriptor, initializationKey, fileKey, "key", "A", "B");
            CHECK_SUCCESS(result)
            receivedBlocks = 0;
        }

        /**
         * Receives block from sender.
         * If receives empty string, finishes communication with sender.
         */
        result = receiveBlock(senderDescriptor, initializationVector, fileKey, mode, block, "A", "B");
        if (result == FINISH) {
            printf("[B] finished communication with A\n");
            FLUSH
            break;
        }
        CHECK_SUCCESS(result)

        /**
         * Writes block to file.
         */
        if (0 == fwrite(block, 1, strlen((char*)block), file)) {
            perror("[B] file write");
            return WRITE;
        }
        receivedBlocks++;
    }

    CHECK_ZERO(fclose(file), "[A] file close", CLOSE)

    CLOSE(senderDescriptor, "[B] socket close")
    return 0;
}
