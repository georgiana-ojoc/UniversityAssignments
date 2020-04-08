#include <dirent.h>
#include <errno.h>
#include <fcntl.h>
#include <grp.h>
#include <pwd.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <time.h>
#include <unistd.h>

#define MAXIMUM_USERNAME_LENGTH     512
#define MAXIMUM_COMMAND_LENGTH      512
#define MAXIMUM_ARGUMENTS_LENGTH    512
#define MAXIMUM_ANSWER_LENGTH       8192
#define STAT_BUFFER                 8192
#define FIND_BUFFER                 8192

#define REGISTER                "register:"
#define LOGIN                   "login:"
#define MYFIND                  "myfind"
#define MYSTAT                  "mystat"
#define QUIT                    "quit"

#define SOCKET                  "socket"
#define LINK                    "symbolic link"
#define REGULAR                 "regular file"
#define BLOCK                   "block file"
#define DIRECTORY               "directory file"
#define CHARACTER               "character file"
#define FIFO                    "fifo"
#define UNKNOWN                 "unknown file"

#define PATH                    "/home"
#define FIFO_NAME               "fifo"

#define SET_COMMAND_ARGUMENTS_AND_LENGTH \
    (*command).arguments = (char*)malloc((strlen(buffer) + 1) * sizeof(char)); \
    strcpy((*command).arguments, buffer); \
    (*command).bytes = strlen(buffer);
#define SET_COMMAND_NOT_FOUND \
    (*command).type = _UNKNOWN; \
    (*command).arguments = NULL; \
    (*command).bytes = 0;
#define PARSE_SPACES_COMMAND_LINE \
    if (parse_spaces(&buffer) != SUCCESS) \
    { \
    SET_COMMAND_NOT_FOUND \
    return COMMAND_NOT_FOUND; \
    }

#define PIPE_READ               _pipe[0]
#define PIPE_WRITE              _pipe[1]
#define FIFO_READ               _fifo[0]
#define FIFO_WRITE              _fifo[1]
#define SOCKET_PARENT           socket_pair[0]
#define SOCKET_CHILD            socket_pair[1]

#define SEND_MESSAGE(result, descriptor, message, bytes_error, message_error) \
    result = send_message(descriptor, message, bytes_error, message_error); \
    if (result != SUCCESS) \
    { \
        return result; \
    }
#define RECEIVE_MESSAGE(result, descriptor, message, bytes_error, message_error) \
    result = receive_message(descriptor, &message, bytes_error, message_error); \
    if (result != SUCCESS) \
    { \
        return result; \
    }
#define CLOSE_END(descriptor, error) \
    if (close(descriptor) != 0) \
    { \
        perror(error); \
        return CLOSE; \
    }

enum errors
{
    SUCCESS,
    OPEN,
    CLOSE,
    READ,
    WRITE,
    FOPEN,
    FCLOSE,
    FSEEK,
    FGETS,
    OPENDIR,
    CLOSEDIR,
    STAT,
    FORK,
    PIPE,
    FIFO_FILE,
    SOCKET_PAIR,
    END_OF_STRING,
    COMMAND_NOT_FOUND,
    USERNAME_EXISTS,
    USERNAME_NOT_FOUND
};

enum _type
{
    _REGISTER,
    _LOGIN,
    _FIND,
    _STAT,
    _QUIT,
    _UNKNOWN
};

struct _command
{
    enum _type          type;
    char*               arguments;
    unsigned int        bytes;
};

struct _stat
{
    char                type[15];
    long long           size;
    long long           blocks;
    long long           block_size;
    unsigned long long  device;
    unsigned long long  inode;
    unsigned long long  links;
    char                permissions[10];
    unsigned int        UID;
    char*               username;
    unsigned int        GID;
    char*               group_name;
    __time_t            access;
    __time_t            modify;
    __time_t            change;
    __time_t            create;
};

FILE* configuration_file;

void initialize_shell()
{
    printf("Commands:\n");
    printf("\tregister:\t<username>\n");
    printf("\tlogin:\t\t<username>\n");
    printf("\tmyfind\t\t<file>\n");
    printf("\tmystat\t\t<file>\n");
    printf("\tquit\n");
}

enum errors parse_spaces(char** string)
{
    while ((**string) == ' ')
    {
        ++(*string);
    }
    if ((**string) == '\0')
    {
        return END_OF_STRING;
    }
    return SUCCESS;
}

enum errors get_command(struct _command* command)
{
    char* buffer = (char*)malloc(MAXIMUM_COMMAND_LENGTH * sizeof(char));
    printf("myshell> ");
    if (fgets(buffer, MAXIMUM_COMMAND_LENGTH, stdin) == NULL)
    {
        perror("fgets command line");
        return FGETS;
    }
    unsigned short length = strlen(buffer) - 1;
    buffer[length] = '\0';
    PARSE_SPACES_COMMAND_LINE
    if (strstr(buffer, REGISTER) == buffer)
    {
        buffer += strlen(REGISTER);
        PARSE_SPACES_COMMAND_LINE
        (*command).type = _REGISTER;
        SET_COMMAND_ARGUMENTS_AND_LENGTH
    }
    else if (strstr(buffer, LOGIN) == buffer)
    {
        buffer += strlen(LOGIN);
        PARSE_SPACES_COMMAND_LINE
        (*command).type = _LOGIN;
        SET_COMMAND_ARGUMENTS_AND_LENGTH
    }
    else if (strstr(buffer, MYFIND) == buffer)
    {
        buffer += strlen(MYFIND);
        PARSE_SPACES_COMMAND_LINE
        (*command).type = _FIND;
        SET_COMMAND_ARGUMENTS_AND_LENGTH
    }
    else if (strstr(buffer, MYSTAT) == buffer)
    {
        buffer += strlen(MYSTAT);
        PARSE_SPACES_COMMAND_LINE
        (*command).type = _STAT;
        SET_COMMAND_ARGUMENTS_AND_LENGTH
    }
    else if (strstr(buffer, QUIT) == buffer)
    {
        buffer += strlen(QUIT);
        if (parse_spaces(&buffer) != END_OF_STRING)
        {
            SET_COMMAND_NOT_FOUND
            return COMMAND_NOT_FOUND;
        }
        (*command).type = _QUIT;
        (*command).arguments = NULL;
        (*command).bytes = 0;
    }
    else
    {
        SET_COMMAND_NOT_FOUND
        return COMMAND_NOT_FOUND;
    }
    return SUCCESS;
}

enum errors my_register(char* username)
{
    if (fseek(configuration_file, 0, SEEK_SET) != 0)
    {
        return FSEEK;
    }
    char* buffer = (char*)malloc(MAXIMUM_USERNAME_LENGTH * sizeof(char));
    while (fgets(buffer, MAXIMUM_USERNAME_LENGTH, configuration_file) != NULL)
    {
        buffer[strlen(buffer) - 1] = '\0';
        if (strcmp(username, buffer) == 0)
        {
            return USERNAME_EXISTS;
        }
    }
    fprintf(configuration_file, "%s\n", username);
    return SUCCESS;
}

enum errors my_login(char* username)
{
    if (fseek(configuration_file, 0, SEEK_SET) != 0)
    {
        return FSEEK;
    }
    char* buffer = (char*)malloc(MAXIMUM_USERNAME_LENGTH * sizeof(char));
    while (fgets(buffer, MAXIMUM_USERNAME_LENGTH, configuration_file) != NULL)
    {
        buffer[strlen(buffer) - 1] = '\0';
        if (strcmp(username, buffer) == 0)
        {
            return SUCCESS;
        }
    }
    return USERNAME_NOT_FOUND;
}

enum errors my_stat(char* file, struct _stat* information)
{
    struct stat status;
    if (stat(file, &status) != 0)
    {
        return STAT;
    }
    switch (status.st_mode & __S_IFMT)
    {
        case __S_IFSOCK:
            strcpy((*information).type, SOCKET);
            break;
        case __S_IFLNK:
            strcpy((*information).type, LINK);
            break;
        case __S_IFREG:
            strcpy((*information).type, REGULAR);
            break;
        case __S_IFBLK:
            strcpy((*information).type, BLOCK);
            break;
        case __S_IFDIR:
            strcpy((*information).type, DIRECTORY);
            break;
        case __S_IFCHR:
            strcpy((*information).type, CHARACTER);
            break;
        case __S_IFIFO:
            strcpy((*information).type, FIFO);
            break;
        default:
            strcpy((*information).type, UNKNOWN);
    }
    (*information).size = status.st_size;
    (*information).blocks = status.st_blocks;
    (*information).block_size = status.st_blksize;
    (*information).device = status.st_dev;
    (*information).inode = status.st_ino;
    (*information).links = status.st_nlink;
    strcpy((*information).permissions, "---------");
    if (status.st_mode & S_IRUSR)
    {
        (*information).permissions[0] = 'r';
    }
    if (status.st_mode & S_IWUSR)
    {
        (*information).permissions[1] = 'w';
    }
    if (status.st_mode & S_IXUSR)
    {
        (*information).permissions[2] = 'x';
    }
    if (status.st_mode & S_IRGRP)
    {
        (*information).permissions[3] = 'r';
    }
    if (status.st_mode & S_IWGRP)
    {
        (*information).permissions[4] = 'w';
    }
    if (status.st_mode & S_IXGRP)
    {
        (*information).permissions[5] = 'x';
    }
    if (status.st_mode & S_IROTH)
    {
        (*information).permissions[6] = 'r';
    }
    if (status.st_mode & S_IWOTH)
    {
        (*information).permissions[7] = 'w';
    }
    if (status.st_mode & S_IXOTH)
    {
        (*information).permissions[8] = 'x';
    }
    (*information).UID = status.st_uid;
    (*information).GID = status.st_gid;
    (*information).access = status.st_atim.tv_sec;
    (*information).modify = status.st_mtim.tv_sec;
    (*information).change = status.st_ctim.tv_sec;
    struct tm creation = *(gmtime(&(status.st_ctim.tv_sec)));
    (*information).create = mktime(&creation);
    struct passwd* password = getpwuid(status.st_uid);
    if (password == NULL)
    {
        (*information).username = NULL;
    }
    else
    {
        (*information).username = password->pw_name;
    }
    struct group* _group = getgrgid(status.st_gid);
    if (_group == NULL)
    {
        (*information).group_name = NULL;
    }
    else
    {
        (*information).group_name = _group->gr_name;
    }
    return SUCCESS;
}

char* get_my_stat_answer(char* file)
{
    struct _stat information;
    if (my_stat(file, &information) != SUCCESS)
    {
        return "File not found. Try again.\n";
    }
    char* buffer = (char*)malloc(STAT_BUFFER * sizeof(char));
    sprintf(buffer, "Type: %s\nSize: %lld\nBlocks: %lld\nIO Block: %lld\nDevice: %llu\nInode: %llu\nLinks: %llu\nPermissions: %s\nUID/username: %u/%s\nGID/group name: %u/%s\nAccess: %sModify: %sChange: %sCreate: %s", \
    information.type, information.size, information.blocks, information.block_size, information.device, information.inode, information.links, information.permissions, information.UID, information.username, information.GID, information.group_name, \
    ctime(&(information.access)), ctime(&(information.modify)), ctime(&(information.change)), ctime(&(information.create)));
    return buffer;
}

enum errors my_find(char* path, char* file, char** information)
{
    struct stat status;
    if (stat(path, &status) != 0)
    {
        return STAT;
    }
    DIR* directory;
    struct dirent* directory_entry;
    char* buffer = (char*)malloc(PATH_MAX * sizeof(char));
    if ((status.st_mode & __S_IFMT) == __S_IFDIR)
    {
        directory = opendir(path);
        if (directory == NULL)
        {
            return OPENDIR;
        }
        directory_entry = readdir(directory);
        while (directory_entry != NULL)
        {
            if (strcmp(directory_entry->d_name, ".") != 0 && strcmp(directory_entry->d_name, "..") != 0)
            {
                sprintf(buffer, "%s/%s", path, directory_entry->d_name);
                if (strcmp(file, directory_entry->d_name) == 0)
                {
                    sprintf((*information), "%sPath: %s\n%s", (*information), buffer, get_my_stat_answer(buffer));
                }
                my_find(buffer, file, information);
            }
            directory_entry = readdir(directory);
        }
        if (closedir(directory) != 0)
        {
            return CLOSEDIR;
        }
    }
    return SUCCESS;
}

char* get_my_find_answer(char* file)
{
    char* buffer = (char*)malloc(FIND_BUFFER * sizeof(char));
    strcpy(buffer, "");
    if (my_find(PATH, file, &buffer) != SUCCESS || strcmp(buffer, "") == 0)
    {
        strcpy(buffer, "File not found. Try again.\n");
    }
    return buffer;
}

enum errors send_message(int descriptor, char* message, char* bytes_error, char* message_error)
{
    unsigned int bytes = strlen(message);
    if (write(descriptor, &bytes, sizeof(bytes)) < 0)
    {
        perror(bytes_error);
        return WRITE;
    }
    if (write(descriptor, message, bytes) < 0)
    {
        perror(message_error);
        return WRITE;
    }
    return SUCCESS;
}

enum errors receive_message(int descriptor, char** message, char* bytes_error, char* message_error)
{
    unsigned bytes;
    if (read(descriptor, &bytes, sizeof(bytes)) < 0)
    {
        perror(bytes_error);
        return READ;
    }
    if (read(descriptor, *message, bytes) < 0)
    {
        perror(message_error);
        return READ;
    }
    return SUCCESS;
}

int main()
{
    configuration_file = fopen("configuration file", "a+");
    bool logged = false;
    if (configuration_file == NULL)
    {
        perror("fopen configuration file");
        return FOPEN;
    }
    initialize_shell();
    int socket_pair[2];
    if (socketpair(AF_UNIX, SOCK_STREAM, 0, socket_pair) < 0)
    {
        perror("socket pair parent - child");
        return SOCKET_PAIR;
    }
    int _pipe[2];
    if (pipe(_pipe) < 0)
    {
        perror("pipe child - register child");
        return PIPE;
    }
    int _fifo[2];
    if (mkfifo(FIFO_NAME, 0600) < 0 && errno != EEXIST)
    {
        perror("fifo child - login child");
        return FIFO_FILE;
    }
    switch (fork())
    {
        case -1:
            perror("fork parent - child");
            return FORK;
        case 0:
            CLOSE_END(SOCKET_PARENT, "[child] close socket parent")
            while (true)
            {
                struct _command command_child;
                if (read(SOCKET_CHILD, &command_child.type, sizeof(command_child.type)) < 0)
                {
                    perror("[child] read type socket child");
                    return READ;
                }
                if (read(SOCKET_CHILD, &command_child.bytes, sizeof(command_child.bytes)) < 0)
                {
                    perror("[child] read bytes socket child");
                    return READ;
                }
                command_child.arguments = (char*)malloc(MAXIMUM_ARGUMENTS_LENGTH * sizeof(char));
                if (read(SOCKET_CHILD, command_child.arguments, command_child.bytes) < 0)
                {
                    perror("[child] read arguments socket child");
                    return READ;
                }
                command_child.arguments[command_child.bytes] = '\0';
                enum errors result_child;
                enum errors result_register_child;
                enum errors result_login_child;
                char* username = (char*)malloc(MAXIMUM_USERNAME_LENGTH * sizeof(char));
                char* answer_register_child = (char*)malloc(MAXIMUM_ANSWER_LENGTH * sizeof(char));
                char* answer_login_child = (char*)malloc(MAXIMUM_ANSWER_LENGTH * sizeof(char));
                switch (command_child.type)
                {
                    case _QUIT:
                        CLOSE_END(PIPE_READ, "close pipe read")
                        CLOSE_END(PIPE_WRITE, "close pipe write")
                        SEND_MESSAGE(result_child, SOCKET_CHILD, QUIT, "[child] write bytes socket child", "[child] write answer socket child")
                        CLOSE_END(SOCKET_CHILD, "[child] close socket child")
                        return SUCCESS;
                    case _REGISTER:
                        switch (fork())
                        {
                            case -1:
                                perror("fork child - register child");
                                return FORK;
                            case 0:
                                RECEIVE_MESSAGE(result_register_child, PIPE_READ, username, "[register child] read bytes pipe read", "[register child] read username pipe read")
                                if (my_register(username) != SUCCESS)
                                {
                                    SEND_MESSAGE(result_register_child, PIPE_WRITE, "Username exists. Try again.\n", "[register child] write bytes pipe write", "[register child] write answer pipe write")
                                }
                                else
                                {
                                    SEND_MESSAGE(result_register_child, PIPE_WRITE, "Username is added successfully.\n", "[register child] write bytes pipe write", "[register child] write answer pipe write")
                                }
                                exit(SUCCESS);
                            default:
                                SEND_MESSAGE(result_child, PIPE_WRITE, command_child.arguments, "[child] write bytes pipe write", "[child] write username pipe write")
                                wait(NULL);
                                RECEIVE_MESSAGE(result_child, PIPE_READ, answer_register_child, "[child] read bytes pipe read", "[child] read answer pipe read")
                                SEND_MESSAGE(result_child, SOCKET_CHILD, answer_register_child, "[child] write bytes socket child", "[child] write answer socket child")
                        }
                        break;
                    case _LOGIN:
                        switch (fork())
                        {
                            case -1:
                                perror("fork child - login child");
                                return FORK;
                            case 0:
                                FIFO_READ = open(FIFO_NAME, O_RDONLY);
                                if (FIFO_READ < 0)
                                {
                                    perror("[login child] open fifo read");
                                    return OPEN;
                                }
                                RECEIVE_MESSAGE(result_login_child, FIFO_READ, username, "[login child] read bytes fifo read", "[login child] read username fifo read")
                                FIFO_WRITE = open(FIFO_NAME, O_WRONLY);
                                if (FIFO_WRITE < 0)
                                {
                                    perror("[login child] open fifo write");
                                    return OPEN;
                                }
                                if (my_login(username) != SUCCESS)
                                {
                                    SEND_MESSAGE(result_login_child, FIFO_WRITE, "Username is not found. Try again.\n", "[login child] write bytes fifo write", "[login child] write answer fifo write")
                                }
                                else
                                {
                                    SEND_MESSAGE(result_login_child, FIFO_WRITE, "Username is connected successfully.\n", "[login child] write bytes fifo write", "[login child] write answer fifo write")
                                }
                                exit(SUCCESS);
                            default:
                                FIFO_WRITE = open(FIFO_NAME, O_WRONLY);
                                if (FIFO_WRITE < 0)
                                {
                                    perror("[login child] open fifo write");
                                    return OPEN;
                                }
                                SEND_MESSAGE(result_child, FIFO_WRITE, command_child.arguments, "[child] write bytes fifo write", "[child] write username fifo read")
                                wait(NULL);
                                FIFO_READ = open(FIFO_NAME, O_RDONLY);
                                if (FIFO_READ < 0)
                                {
                                    perror("[child] open fifo read");
                                    return OPEN;
                                }
                                RECEIVE_MESSAGE(result_child, FIFO_READ, answer_login_child, "[child] read bytes fifo read", "[child] read answer fifo read")
                                if (strcmp(answer_login_child, "Username is connected successfully.\n") == 0)
                                {
                                    logged = true;
                                }
                                else
                                {
                                    logged = false;
                                }
                                CLOSE_END(FIFO_READ, "close fifo read")
                                CLOSE_END(FIFO_WRITE, "close fifo write")
                                SEND_MESSAGE(result_child, SOCKET_CHILD, answer_login_child, "[child] write bytes socket child", "[child] write answer socket child")
                        }
                        break;
                    case _STAT:
                        if (logged == false)
                        {
                            SEND_MESSAGE(result_child, SOCKET_CHILD, "No username is connected. Log in first.\n", "[child] write bytes socket child", "[child] write answer socket child")
                        }
                        else
                        {
                            SEND_MESSAGE(result_child, SOCKET_CHILD, get_my_stat_answer(command_child.arguments), "[child] write bytes socket child", "[child] write answer socket child")
                        }
                        break;
                    case _FIND:
                        if (logged == false)
                        {
                            SEND_MESSAGE(result_child, SOCKET_CHILD, "No username is connected. Log in first.\n", "[child] write bytes socket child", "[child] write answer socket child")
                        }
                        else
                        {
                            SEND_MESSAGE(result_child, SOCKET_CHILD, get_my_find_answer(command_child.arguments), "[child] write bytes socket child", "[child] write answer socket child")
                        }
                        break;
                    default:
                        SEND_MESSAGE(result_child, SOCKET_CHILD, "Command is not found. Try again.\n", "[child] write bytes socket child", "[child] write answer socket child")
                }
            }
        default:
            CLOSE_END(SOCKET_CHILD, "[parent] close socket child")
            while (true)
            {
                struct _command command_parent;
                get_command(&command_parent);
                if (write(SOCKET_PARENT, &command_parent.type, sizeof(command_parent.type)) < 0)
                {
                    perror("[parent] write type socket parent");
                    return WRITE;
                }
                if (write(SOCKET_PARENT, &command_parent.bytes, sizeof(command_parent.bytes)) < 0)
                {
                    perror("[parent] write bytes socket parent");
                    return WRITE;
                }
                if (write(SOCKET_PARENT, command_parent.arguments, command_parent.bytes) < 0)
                {
                    perror("[parent] write arguments socket parent");
                    return WRITE;
                }
                enum errors result_parent;
                char* answer_parent = (char*)malloc(MAXIMUM_ANSWER_LENGTH * sizeof(char));
                RECEIVE_MESSAGE(result_parent, SOCKET_PARENT, answer_parent, "[parent] read bytes socket parent", "[parent] read answer socket parent")
                if (strcmp(answer_parent, QUIT) == 0)
                {
                    CLOSE_END(SOCKET_PARENT, "[parent] close socket parent")
                    if (fclose(configuration_file) == EOF)
                    {
                        perror("fclose configuration file");
                        return FCLOSE;
                    }
                    return SUCCESS;
                }
                printf("%s", answer_parent);
            }
    }
}