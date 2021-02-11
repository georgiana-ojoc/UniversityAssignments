#include "helper.h"

int main(int argumentNumber, const char *arguments[]) {
    /**
     * Parses command line arguments.
     */
    if (argumentNumber < 3) {
        fprintf(stderr, "syntax: %s <port> <key>\n", arguments[0]);
        fflush(stderr);
        return -1;
    }
    if (strlen(arguments[2]) != KEY_LENGTH) {
        fprintf(stderr, "The key should have %d bytes\n.", KEY_LENGTH);
        fflush(stderr);
        return -1;
    }
    unsigned int port = strtol(arguments[1], NULL, 10);
    unsigned char initializationKey[KEY_LENGTH + 1];
    memcpy(initializationKey, arguments[2], KEY_LENGTH);
    initializationKey[KEY_LENGTH] = 0;

    /**
     * Creates server.
     */
    int keyManagerDescriptor;
    enum errors result = createServer("keyManager", port, &keyManagerDescriptor);
    CHECK_SUCCESS(result)

    int senderDescriptor;
    while (1) {
        /**
        * Accepts sender.
        */
        while (SUCCESS != acceptClient("keyManager", "A", keyManagerDescriptor, &senderDescriptor));

        unsigned char mode[MODE_LENGTH + 1];
        unsigned char fileKey[KEY_LENGTH + 1];
        while (1) {
            /**
             * Receives block mode from sender.
             * If receives empty string, finishes communication with sender.
             */
            result = receiveMode(senderDescriptor, initializationKey, mode, "A", "keyManager");
            if (result == FINISH) {
                printf("[keyManager] finished communication with A\n");
                FLUSH
                break;
            }
            CHECK_SUCCESS(result)

            /**
             * Generates random block mode key.
             * Sends block mode key to sender.
             * If does not receive CBC block mode, sends OFB key to sender.
             */
            generateString(fileKey);
            if (strcmp((char *) mode, CBC_MODE) == 0) {
                result = sendString(senderDescriptor, initializationKey, fileKey,
                                    "CBC key", "keyManager", "A");
                CHECK_SUCCESS(result)
            } else {
                result = sendString(senderDescriptor, initializationKey, fileKey,
                                    "OFB key", "keyManager", "A");
                CHECK_SUCCESS(result)
            }
        }

        CLOSE(senderDescriptor, "[keyManager] socket close")
    }
}
