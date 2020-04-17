package music_albums.bonus;

import music_albums.database.ConnectionSource;

import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ConcurrentTaskExecutor {
    private ThreadPoolExecutor threadPoolExecutor;
    private ConnectionSource connectionSource;
    private Connection connection;

    public ConcurrentTaskExecutor(ConnectionSource connectionSource) {
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
        this.connectionSource = connectionSource;
        connection = connectionSource.getConnection();
    }

    public void executeTasks(int taskNumber) {
        for (int index = 1; index <= taskNumber; ++index) {
            ConcurrentTask concurrentTask =
                    new ConcurrentTask(connectionSource.getType() + " " + index, connection);
            threadPoolExecutor.execute(concurrentTask);
        }
        threadPoolExecutor.shutdown();
    }
}
