#include "helper.h"

enum errors readModeFromConsole(unsigned char *mode) {
    /**
    * Reads block mode from console.
    * If block mode is not "CBC" or empty string, will be "OFB".
    */
    printf("block mode: ");
    FLUSH
    unsigned char readMode[MODE_LENGTH + 2];
    memset(readMode, 0, MODE_LENGTH + 2);
    CHECK_ZERO(read(STDIN, readMode, MODE_LENGTH + 1), "[sender] read mode", READ)
    readMode[MODE_LENGTH] = 0;
    upperCase(readMode);
    if (strcmp((char *) readMode, CBC_MODE) == 0 || strcmp((char *) readMode, "") == 0) {
        memcpy(mode, readMode, MODE_LENGTH);
    } else {
        memcpy(mode, OFB_MODE, MODE_LENGTH);
    }
    mode[MODE_LENGTH] = 0;
    return SUCCESS;
}

int main(int argumentNumber, const char *arguments[]) {
    /**
    * Parses command line arguments.
    */
    if (argumentNumber < 7) {
        fprintf(stderr, "syntax: %s <key manager address> <key manager port> <port> "
                        "<blocks> <key> <file name>\n", arguments[0]);
        fflush(stderr);
        return -1;
    }
    if (strlen(arguments[5]) != KEY_LENGTH) {
        fprintf(stderr, "The key should have %d bytes\n.", KEY_LENGTH);
        fflush(stderr);
        return -1;
    }
    const char *keyManagerAddress = arguments[1];
    unsigned int keyManagerPort = strtol(arguments[2], NULL, 10);
    unsigned int port = strtol(arguments[3], NULL, 10);
    unsigned int blocks = strtol(arguments[4], NULL, 10);
    if (blocks == 0) {
        blocks = 1;
    }
    unsigned char initializationKey[KEY_LENGTH + 1];
    memcpy(initializationKey, arguments[5], KEY_LENGTH);
    initializationKey[KEY_LENGTH] = 0;
    const char *fileName = arguments[6];

    /**
     * Connects to key manager.
     */
    int keyManagerDescriptor;
    enum errors result = connectToServer("keyManager", "A",
                                         keyManagerAddress, keyManagerPort, &keyManagerDescriptor);
    CHECK_SUCCESS(result)

    /**
     * Creates server.
     */
    int senderDescriptor;
    result = createServer("A", port, &senderDescriptor);
    CHECK_SUCCESS(result)

    int receiverDescriptor;
    while (1) {
        /**
        * Accepts receiver.
        */
        while (SUCCESS != acceptClient("A", "B", senderDescriptor, &receiverDescriptor));

        /**
         * Opens file for reading binary.
         */
        FILE *file = fopen(fileName, "rb");
        if (file == NULL) {
            fprintf(stderr, "Unable to open \"%s\" for reading binary.\n", fileName);
            fflush(stderr);
            return -1;
        }

        unsigned int sentBlocks = blocks;
        unsigned char mode[MODE_LENGTH + 1];
        unsigned char initializationVector[KEY_LENGTH + 1];
        unsigned char fileKey[KEY_LENGTH + 1];
        unsigned char block[KEY_LENGTH + 1];
        /**
         * Reads block from file while end-of-file is not reached.
         */
        while (!feof(file)) {
            memset(block, 0, KEY_LENGTH + 1);
            if (0 == fread(block, 1, KEY_LENGTH, file)) {
                return READ;
            }

            if (sentBlocks == blocks) {
                /**
                * Reads block mode from console.
                * If reads empty string, finishes communication with receiver.
                */
                result = readModeFromConsole(mode);
                CHECK_SUCCESS(result)
                if (strcmp((char *) mode, "") == 0) {
                    break;
                }

                /**
                 * Sends block mode to key manager.
                 */
                result = sendMode(keyManagerDescriptor, initializationKey, mode, "A", "keyManager");
                CHECK_SUCCESS(result)

                /**
                 * Receives block mode key from key manager.
                 */
                result = receiveString(keyManagerDescriptor, initializationKey, fileKey,
                                       "key", "keyManager", "A");
                CHECK_SUCCESS(result)

                /**
                 * Sends block mode to receiver.
                 */
                result = sendMode(receiverDescriptor, initializationKey, mode, "A", "B");
                CHECK_SUCCESS(result)

                /**
                 * Generates random block mode key.
                 * Sends initialization vector to receiver.
                 */
                generateString(initializationVector);
                result = sendString(receiverDescriptor, initializationKey, initializationVector,
                                    "initialization vector", "A", "B");
                CHECK_SUCCESS(result)

                /**
                 * Sends block mode key to receiver.
                 */
                result = sendString(receiverDescriptor, initializationKey, fileKey, "key", "A", "B");
                CHECK_SUCCESS(result)

                sentBlocks = 0;
            }

            /**
             * Sends block to receiver.
             */
            result = sendBlock(receiverDescriptor, initializationVector, fileKey, mode, block, "A", "B");
            CHECK_SUCCESS(result)
            sentBlocks++;
        }

        CHECK_ZERO(fclose(file), "[A] file close", CLOSE)

        /**
         * Finish communication with receiver.
         */
        result = sendFinish(receiverDescriptor, initializationKey, "A", "B");
        CHECK_SUCCESS(result)
        CLOSE(receiverDescriptor, "[A] socket close")
    }
}
